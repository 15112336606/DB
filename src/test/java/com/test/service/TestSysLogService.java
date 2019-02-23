package com.test.service;

import java.util.List;

import org.junit.Test;

import com.db.common.vo.PageObject;
import com.db.sys.entity.SysLog;
import com.db.sys.service.SysLogService;
import com.test.TestBase;

public class TestSysLogService extends TestBase{
	@Test
	public void TestFindPageObject(){
		SysLogService service = ctx.getBean("sysLogServiceImpl",SysLogService.class);
		PageObject<SysLog> fpo = service.findPageObjects("admin", 1);
		Integer pageCount = fpo.getPageCount();
		Integer pageCurrent = fpo.getPageCurrent();
		Integer pageSize = fpo.getPageSize();
		List<SysLog> records = fpo.getRecords();
		Integer rowCount = fpo.getRowCount();
		System.out.println("总页数:"+pageCount+",当前页数:"+pageCurrent+",页面大小:"+pageSize+"总条数:"+rowCount);
		for(SysLog sl:records){
			System.out.println(sl);
		}
	}
}