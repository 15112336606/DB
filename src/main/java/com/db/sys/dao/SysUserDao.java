package com.db.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.sys.entity.SysUser;
import com.db.sys.vo.SysUserDeptResult;

public interface SysUserDao {
	
	/**
	 * 基于用户名查找用户信息
	 * @param username
	 * @return
	 */
	SysUser findUserByUserName(String username);
	int getRowCount(@Param("username")String username);
	List<SysUserDeptResult> findPageObjects(
			@Param("username")String username,
			@Param("startIndex")Integer startIndex,
			@Param("pageSize")Integer pageSize);
	int validById(@Param("id")Integer id,@Param("valid")Integer valid,@Param("modifiedUser")String modifiedUser);
	int insertObject(SysUser entity);
	SysUserDeptResult findObjectById(Integer Id);
	int updateObject(SysUser user);
	int updatePassword(@Param("username")String username,@Param("salt")String salt,@Param("password")String password);
}
