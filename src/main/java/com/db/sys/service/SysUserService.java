package com.db.sys.service;


import java.util.Map;

import com.db.common.vo.PageObject;
import com.db.sys.entity.SysUser;
import com.db.sys.vo.SysUserDeptResult;

public interface SysUserService {
	PageObject<SysUserDeptResult> findPageObjects(String username,Integer pageCurrent);
	int validById(Integer id,Integer valid,String modifiedUser);
	int savaObject(SysUser entity,Integer[] roleIds);
	Map<String,Object> findObjectById(Integer userId);
	int updateObject(SysUser entity,Integer[] roleIds);
	int updatePassword(String password,String newPassword,String cfgPassword);
}
