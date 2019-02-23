package com.db.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.db.common.exception.ServiceException;
import com.db.common.vo.Node;
import com.db.sys.dao.SysMenuDao;
import com.db.sys.dao.SysRoleMenuDao;
import com.db.sys.entity.SysMenu;
import com.db.sys.service.SysMenuService;

@Service
public class SysMenuServiceImpl implements SysMenuService{
	@Autowired
	private SysMenuDao sysMenuDao;

	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@Override
	public List<Map<String, Object>> findObjects() {
		List<Map<String, Object>> list = sysMenuDao.findObjects();
		if(list==null||list.size()==0)
			throw new ServiceException("没有对应的菜单");
		return list;
	}

	@Override
	public int deleteObject(Integer id) {
		//1.验收数据的合法性
		if(id==null||id<=0)throw new ServiceException("请先选择");
		//2.基于id进行子元素查询
		int count=sysMenuDao.getChildCount(id);
		if(count>0){
			throw new ServiceException("请先删除子菜单");
		}
		//3.删除菜单元素
		int rows=sysMenuDao.deleteObject(id);
		if(rows==0)throw new ServiceException("此菜单可能已经不存在");
		//4.删除角色,菜单关系数据
		sysRoleMenuDao.deleteObjectsByMenuId(id);
		//5.返回结果
		return rows;
	}

	@Override
	public List<Node> findZtreeMenuNodes() {
		List<Node> nodes=sysMenuDao.findZtreeMenuNodes();
		if(nodes==null||nodes.size()==0)
			throw new ServiceException("没有上级菜单");
		return nodes;
	}

	@Override
	public int saveObject(SysMenu entity) {
		//1.合法验证
		if(entity==null)throw new ServiceException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))
			throw new ServiceException("菜单名不能为空");
		int rows=-1;
		//保存数据
		try {
			rows=sysMenuDao.insertObject(entity);
		} catch (Error e) {//Error系统级错误
			e.printStackTrace();//给运维人员发短信
		}catch(RuntimeException e){
			e.printStackTrace();//给运维人员发短信
			throw new ServiceException("系统维护中...");
		}
		return rows;
	}

	@Override
	public int updateObject(SysMenu entity) {
		//1.合法验证
		if(entity==null)
			throw new ServiceException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))
			throw new ServiceException("菜单名不能为空");
		//2.更新数据
		int rows=sysMenuDao.updateObject(entity);
		if(rows==0)
			throw new ServiceException("记录可能已经不存在");
		//3.返回数据
		return rows;
	}


}
