package com.db.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.db.common.exception.ServiceException;
import com.db.common.util.PageUtils;
import com.db.common.vo.CheckBox;
import com.db.common.vo.PageObject;
import com.db.common.vo.SysRoleMenuVo;
import com.db.sys.dao.SysRoleDao;
import com.db.sys.dao.SysRoleMenuDao;
import com.db.sys.entity.SysRole;
import com.db.sys.service.SysRoleService;

@Service
public class SysRoleServiceImpl implements SysRoleService{
	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@Override
	public PageObject<SysRole> findPageObjects(String name, Integer pageCurrent) {
		//1.验证合法性
		if(pageCurrent==null||pageCurrent<1){
			throw new IllegalArgumentException("页码不正确");
		}
		int rowCount = sysRoleDao.getRowCount(name);
		if(rowCount==0)throw new ServiceException("查不到数据");
		int pageSize=3;
		int startIndex=(pageCurrent-1)*pageSize;
		List<SysRole> records = sysRoleDao.findPageObjects(name, startIndex, pageSize);
		return PageUtils.newPageObject(rowCount, records, pageSize, pageCurrent);
	}
	
	@Override
	public int saveObject(SysRole entity, Integer[] menuIds) {
		//1.对参数进行校验
		if(entity==null)
			throw new IllegalArgumentException("保存对象不存在");
		if(StringUtils.isEmpty(entity.getName()))
			throw new IllegalArgumentException("角色名不存在");
		if(menuIds==null||menuIds.length==0)
			throw new ServiceException("必须为角色制定权限");
		//2.保存角色自身信息
		int rows=sysRoleDao.insertObject(entity);
		//3.保存角色菜单关系数据
		sysRoleMenuDao.insertObject(entity.getId(), menuIds);
		//4.返回结果
		return rows;
	}

	@Override
	public SysRoleMenuVo findObjectById(Integer id) {
		if(id==null||id<1)
			throw new IllegalArgumentException("输入id无效");
		SysRoleMenuVo vo = sysRoleDao.findObjectById(id);
		if(vo==null)throw new ServiceException("此记录已经不存在");
		return vo;
	}

	@Override
	public int updateObject(SysRole entity, Integer[] menuIds) {
		//1.对参数进行校验
				if(entity==null)
					throw new IllegalArgumentException("保存对象不存在");
				if(StringUtils.isEmpty(entity.getName()))
					throw new IllegalArgumentException("角色名不存在");
				if(menuIds==null||menuIds.length==0)
					throw new ServiceException("必须为角色制定权限");
				//2.保存角色自身信息
				int rows=sysRoleDao.updateObject(entity);
				if(rows==0)throw new ServiceException("记录可能已经不存在");
				//3.保存角色菜单关系数据
				//3.1删除原先关系数据
				sysRoleMenuDao.deleteObjectsByRoleId(entity.getId());
				//3.2添加新的关系输出
				sysRoleMenuDao.insertObject(entity.getId(), menuIds);
				//4.返回结果
				return rows;
		
	}

	@Override
	public List<CheckBox> findObjects() {
		return sysRoleDao.findObjects();
	}

	
}
