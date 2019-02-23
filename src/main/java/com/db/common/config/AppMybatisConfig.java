package com.db.common.config;


import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan("com.db.**.dao")
public class AppMybatisConfig {
	@Bean("sqlSessionFactory")
	public SqlSessionFactoryBean newSqlSessionFactoryBean(DataSource ds) throws Exception{
		SqlSessionFactoryBean ssf = new SqlSessionFactoryBean();
		ssf.setDataSource(ds);
		ssf.setMapperLocations(
				new PathMatchingResourcePatternResolver().
				getResources("classpath*:mapper/sys/*Mapper.xml"));
		return ssf;
	}
}
