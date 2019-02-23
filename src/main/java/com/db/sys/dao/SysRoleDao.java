package com.db.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.common.vo.CheckBox;
import com.db.common.vo.SysRoleMenuVo;
import com.db.sys.entity.SysRole;

public interface SysRoleDao {
	/**
	 * 基于记录名字获取记录总数
	 * @param name
	 * @return
	 */
	int getRowCount(@Param("name")String name);
	/**
	 * 查询当前页要呈现的角色信息
	 * @param name	查询条件
	 * @param startIndex	查询起始编号
	 * @param pageSize	页面大小
	 * @return
	 */
	List<SysRole> findPageObjects(@Param("name")String name,
								@Param("startIndex")Integer startIndex,
								@Param("pageSize")Integer pageSize);
	int insertObject(SysRole entity);
	/**
	 * 基于角色id查询角色以及对应的菜单信息
	 */
	SysRoleMenuVo findObjectById(Integer id);
	/**
	 * 更新角色自身信息
	 */
	int updateObject(SysRole entity);
	
	List<CheckBox> findObjects();
}
