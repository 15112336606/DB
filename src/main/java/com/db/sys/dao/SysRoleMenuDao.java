package com.db.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 此DAO对象对应sys_role_menus表中数据操作.
 * 此表中存储的是角色和菜单的关系数据
 * @param menuId
 * @return
 */
public interface SysRoleMenuDao {
	/**
	 * 基于角色id查找菜单id信息
	 */
	List<Integer> findMenuIdsByRoleIds(@Param("roleIds")List<Integer> roleIds);
	/**
	 * 基于菜单id删除角色和菜单的关系数据
	 * @param menuId 菜单id
	 * @return 删除的行数
	 */
	int deleteObjectsByMenuId(@Param("menuId")Integer menuId);
	int insertObject(@Param("roleId")Integer roleId,@Param("menuIds")Integer[] menuIds);
	/**
	 * 基于角色id删除菜单信息
	 * @param roleId
	 * @return
	 */
	int deleteObjectsByRoleId(Integer roleId);
}
