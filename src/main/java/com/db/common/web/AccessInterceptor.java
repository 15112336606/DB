package com.db.common.web;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.db.common.exception.ServiceException;
/**
 * Spring MVC中的拦截器对象:
 * 要实现的功能:时间访问限制
 */
@Component
public class AccessInterceptor extends HandlerInterceptorAdapter {
	/**此方法会在控制层方法执行之前执行,
	 * 返回值决定了是否要继续执行控制层方法*/
@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {
	System.out.println("preHandle");
	long time=System.currentTimeMillis();
	//日历对象
	Calendar c=Calendar.getInstance();
	c.set(Calendar.HOUR_OF_DAY, 0);
	c.set(Calendar.MINUTE, 59);
	c.set(Calendar.SECOND, 59);
	long start=c.getTimeInMillis();
	c.set(Calendar.HOUR_OF_DAY, 23);
	long end=c.getTimeInMillis();
	System.out.println(start);
	System.out.println(time);
	if(time<start||time>end)throw new ServiceException("不在访问时间内");
	return true;//true表示放行,false表示不放行
}
}
