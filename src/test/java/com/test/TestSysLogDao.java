package com.test;

import java.util.List;

import org.junit.Test;

import com.db.sys.dao.SysLogDao;
import com.db.sys.dao.SysUserDao;
import com.db.sys.entity.SysLog;
import com.db.sys.vo.SysUserDeptResult;

public class TestSysLogDao extends TestBase{
	@Test
	public void testGetRowCount(){
		SysLogDao dao = ctx.getBean("sysLogDao",SysLogDao.class);
		int rowCount = dao.getRowCount("admin");
		System.out.println(rowCount);
	}
	
	@Test
	public void testFindPageObjects(){
		SysLogDao dao = ctx.getBean("sysLogDao",SysLogDao.class);
		List<SysLog> obj = dao.findPageObjects("admin", 5, 5);
		for(SysLog sl:obj){
			System.out.println(sl.toString());
		}
	}

	
	
}
