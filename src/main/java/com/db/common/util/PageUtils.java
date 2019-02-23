package com.db.common.util;

import java.util.List;

import com.db.common.vo.PageObject;

public class PageUtils {
	<T>T add(T t){return null;} 
	/**
	 * 当一个泛型参数应用在了方法的返回值类型
	 * 左侧时这样的方法称之为泛型方法
	 * @param rowCount
	 * @param records
	 * @param pageSize
	 * @param pageCurrent
	 * @return
	 */
	public static <T>PageObject<T> newPageObject(Integer rowCount,List<T> records,Integer pageSize,Integer pageCurrent){
		PageObject<T> po = new PageObject<>();
		po.setPageCount((rowCount-1)/pageSize+1);
		po.setPageCurrent(pageCurrent);
		po.setPageSize(pageSize);
		po.setRecords(records);
		po.setRowCount(rowCount);
		return po;	
	}
}
