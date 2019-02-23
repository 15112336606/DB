package com.db.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface SysUserRoleDao {
	int insertObject(@Param("userId")Integer userId,@Param("roleIds")Integer[] roleIds);
	List<Integer> findRoleIdsByUserId(Integer userId);
	int deleteObjectsByUserId(Integer userId);
}
