package com.db.sys.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.db.common.annotation.RequiredCache;
import com.db.common.annotation.RequiredLog;
import com.db.common.exception.ServiceException;
import com.db.common.util.PageUtils;
import com.db.common.vo.PageObject;
import com.db.sys.dao.SysUserDao;
import com.db.sys.dao.SysUserRoleDao;
import com.db.sys.entity.SysUser;
import com.db.sys.service.SysUserService;
import com.db.sys.vo.SysUserDeptResult;
//timeout 超时时间回滚
@Transactional(readOnly=false,propagation=Propagation.REQUIRED,rollbackFor=Throwable.class,timeout=30,isolation=Isolation.READ_COMMITTED)
@Service
public class SysUserServiceImpl implements SysUserService{
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	@RequiredLog("查找用户")
	@RequiredCache
	@Override
	public PageObject<SysUserDeptResult> findPageObjects(String username, Integer pageCurrent) {
		System.out.println("query from database");
		//数据合法性验证
		if(pageCurrent==null||pageCurrent<=0)
			throw new ServiceException("参数不合法");
		int rowCount=sysUserDao.getRowCount(username);
		if(rowCount==0)throw new ServiceException("记录不存在");
		int pageSize=3;
		int startIndex=(pageCurrent-1)*pageSize;
		List<SysUserDeptResult> records = sysUserDao.findPageObjects(username, startIndex, pageSize);
		PageObject<SysUserDeptResult> po = PageUtils.newPageObject(rowCount, records, pageSize, pageCurrent);
		return po;
	}
	@RequiredLog("禁用启用")
	@RequiresPermissions("sys:user:valid")
	@Override
	public int validById(Integer id, Integer valid, String modifiedUser) {
		if(id==null||id<=0)
			throw new ServiceException("参数不合法,id="+id);
			if(valid!=1&&valid!=0)
			throw new ServiceException("参数不合法,valie="+valid);
			if(StringUtils.isEmpty(modifiedUser))
			throw new ServiceException("修改用户不能为空");
			//2.执行禁用或启用操作
			int rows=0;
			try{
		    rows=sysUserDao.validById(id, valid, modifiedUser);
			}catch(Throwable e){
			e.printStackTrace();
			//报警,给维护人员发短信
			throw new ServiceException("底层正在维护");
			}
			//3.判定结果,并返回
			if(rows==0)
			throw new ServiceException("此记录可能已经不存在");
			return rows;
		}
	
	
	@Transactional(readOnly=true)
	@Override
	@RequiredLog("保存用户")
	public int savaObject(SysUser entity, Integer[] roleIds) {
		//1.验证数据合理性
		if(entity==null)
			throw new ServiceException("保存对象不能为空");
		if(entity.getDeptId()==null)
			throw new ServiceException("请选择对应部门");
		if(StringUtils.isEmpty(entity.getUsername()))
				throw new ServiceException("用户名不能为空");
		if(StringUtils.isEmpty(entity.getPassword()))
			throw new ServiceException("密码不能为空");
		if(roleIds==null||roleIds.length==0)
			throw new ServiceException("至少要为用户分配角色");
		
		//2.将数据写入数据库
		String salt = UUID.randomUUID().toString();
		entity.setSalt(salt);
		//加密(先了解)
		SimpleHash sHash = new SimpleHash("MD5",entity.getPassword(),salt);
		entity.setPassword(sHash.toString());
		int rows = sysUserDao.insertObject(entity);
		sysUserRoleDao.insertObject(entity.getId(), roleIds);
		//if(rows>0)throw new ServiceException("炸了");
		//返回结果
		return rows;
	}
    @Transactional(readOnly=true)
	@Override
	public Map<String, Object> findObjectById(Integer userId) {
		//合法性验证
		if(userId==null||userId<=0)
			throw new ServiceException("参数数据不合法,userId="+userId);
		//业务查询
		SysUserDeptResult user = sysUserDao.findObjectById(userId);
		if(user==null)
			throw new ServiceException("此用户已经不存在");
		List<Integer> roleIds = sysUserRoleDao.findRoleIdsByUserId(userId);
		//封装数据
		HashMap<String, Object> map = new HashMap<>();
		map.put("user", user);
		map.put("roleIds", roleIds);
		return map;
	}

	@Override
	public int updateObject(SysUser entity, Integer[] roleIds) {
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getUsername()))
			throw new IllegalArgumentException("用户名不能为空");
		if(roleIds==null||roleIds.length==0)
			throw new IllegalArgumentException("必须为其指定角色");
		//其他验证自己实现,例如用户名已存在,密码长度...
		//2.更新用户与角色关系数据
		int rows = sysUserDao.updateObject(entity);
		//3.保存用户与角色关系数据
		sysUserRoleDao.deleteObjectsByUserId(entity.getId());
		sysUserRoleDao.insertObject(entity.getId(), roleIds);
		return rows;
	}
	@Override
	public int updatePassword(String password, String newPassword, String cfgPassword) {
		SysUser user=(SysUser)SecurityUtils.getSubject().getPrincipal();
		SimpleHash sh = new SimpleHash("MD5", password, user.getSalt(), 1);
		if(!user.getPassword().equals(sh.toHex()))
			throw new ServiceException("原密码不正确");
		if(password.equals(newPassword))
			throw new ServiceException("新密码不能与原密码一致");
		if(!newPassword.equals(cfgPassword))
			throw new IllegalArgumentException("两次输入的新密码不一致");
		String salt = UUID.randomUUID().toString();
		sh=new SimpleHash("MD5",newPassword,salt,1);
		int rows = sysUserDao.updatePassword(user.getUsername(), salt, sh.toHex());
		return rows;
	}
	
}
