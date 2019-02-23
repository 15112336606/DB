package com.test.service;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.db.sys.dao.SysMenuDao;
import com.test.TestBase;

public class TestMenuDao extends TestBase{
	@Test
	public void testFindObjects(){
		SysMenuDao dao=ctx.getBean("sysMenuDao",SysMenuDao.class);
		List<Map<String, Object>> findObjects = dao.findObjects();
		for(Map<String, Object> map:findObjects){
			System.out.println(map);
		}
	}
}
