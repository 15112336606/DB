package com.db.sys.service.realm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.db.sys.dao.SysMenuDao;
import com.db.sys.dao.SysRoleMenuDao;
import com.db.sys.dao.SysUserDao;
import com.db.sys.dao.SysUserRoleDao;
import com.db.sys.entity.SysUser;
/**
 * Reaml
 */
@Service
public class ShiroUserRealm extends AuthorizingRealm {
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@Autowired
	private SysMenuDao sysMenuDao;
	/**
	 * 通过此方法指定凭证匹配器(加密算法)
	 */
	@Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		//构建凭证匹配对象
				HashedCredentialsMatcher cMatcher=
				new HashedCredentialsMatcher();
				//设置加密算法
				cMatcher.setHashAlgorithmName("MD5");
				//设置加密次数
				cMatcher.setHashIterations(1);
				super.setCredentialsMatcher(cMatcher);
	}
	/**
	 * 基于此方法完成认证信息的获取以及封装
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//1.获取客户端输入的用户信息
		UsernamePasswordToken uToken=(UsernamePasswordToken)token;
		String username=uToken.getUsername();
		//2.基于用户名查找用户信息
		SysUser user=sysUserDao.findUserByUserName(username);
		//3.判定用户是否存在
		if(user==null)
			throw new UnknownAccountException();
		//4.判定用户是否已被禁用
		if(user.getValid()==0)
			throw new LockedAccountException();
		//5.封装用户信息并返回(传递给认证管理器完成认证)
		ByteSource credentialsSalt = ByteSource.Util.bytes(user.getSalt());
		//记住:构建什么对象要看方法的返回值
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
				user,//principal(身份)
				user.getPassword(),//hashedCredentials(已加密的密码)
				credentialsSalt,//credentialsSalt(凭证盐值)
				getName());//realmName realm的名字
		return info;//返回值会传递给认证管理器(后续认证管理器会通过此信息完成认证操作)
	}
	/**
	 * 基于此方法完成用户权限信息的获取及封装
	 */
	//Map<String,AuthorizationInfo> cacheMap=new ConcurrentHashMap<>();
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//1.获取登陆用户信息,例如用户id
		SysUser user=(SysUser) principals.getPrimaryPrincipal();
		Integer userId = user.getId();
		//2.基于用户id获取用户拥有的角色(sys_user_roles)
	//	AuthorizationInfo result = cacheMap.get(user.getUsername());
	//	if(result!=null)return result;
		System.out.println("aaaaa");
		List<Integer> roleIds = sysUserRoleDao.findRoleIdsByUserId(userId);
		if(roleIds==null||roleIds.size()==0)
			throw new AuthorizationException();
		//3.基于角色id获取菜单id(sys_role_menus)
		List<Integer> menuIds = sysRoleMenuDao.findMenuIdsByRoleIds(roleIds);
		if(menuIds==null||menuIds.size()==0)
			throw new AuthorizationException();
		//4.基于菜单id获取权限标识(sys_menus)
		List<String> permissions = sysMenuDao.findPermisssions(menuIds);
		if(permissions==null||permissions.size()==0)
			throw new AuthorizationException();
		//5.对权限标识信息进行封装并返回
		Set<String> set = new HashSet<>();
		for(String per:permissions){
			if(!StringUtils.isEmpty(per)){
				System.out.println(per);
				set.add(per);
			}
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(set);
		//cacheMap.put(user.getUsername(), info);
		return info;
	}
}
