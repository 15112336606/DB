package com.test;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;



public class TestBase {
	protected ClassPathXmlApplicationContext ctx;
	@Before
	public void init(){
		ctx = new ClassPathXmlApplicationContext("spring-configs.xml");

	}
	@Test
	public void basetext() throws SQLException{
		DataSource bean = ctx.getBean("dataSource",DataSource.class);
		System.out.println(bean.getConnection());
	}
	@After
	public void destroy(){
		ctx.close();
	}


}
