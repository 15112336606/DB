package com.db.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.db.common.vo.Node;
import com.db.sys.entity.SysMenu;

public interface SysMenuDao {
	List<Map<String,Object>> findObjects();
	/**
	 * 基于菜单id查找权限标识信息
	 */
	List<String> findPermisssions(@Param("menuIds")List<Integer> menuIds);
	/**
	 * 基于菜单id统计子菜单数
	 * @param id
	 * @return
	 */
	int getChildCount(Integer id);
	/**
	 * 基于菜单id执行菜单自身信息删除操作
	 * @param id
	 * @return
	 */
	int deleteObject(Integer id);
	List<Node> findZtreeMenuNodes();
	/**
	 * 将菜单信息写入数据库
	 */
	int insertObject(SysMenu entity);
	int updateObject(SysMenu entity);
	
}
