package com.db.sys.service;

import java.util.List;

import com.db.common.vo.CheckBox;
import com.db.common.vo.PageObject;
import com.db.common.vo.SysRoleMenuVo;
import com.db.sys.entity.SysRole;

public interface SysRoleService {
	PageObject<SysRole> findPageObjects(String name,Integer pageCurrent);
	int saveObject(SysRole entity,Integer[] menuIds);
	/**
	 * 基于角色id查询角色以及角色对应的信息
	 */
	SysRoleMenuVo findObjectById(Integer id);
	
	int updateObject(SysRole entity,Integer[] menuIds);
	
	List<CheckBox> findObjects();
}
