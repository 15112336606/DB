	package com.db.common.aspect;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.db.common.annotation.RequiredLog;
import com.db.common.utils.IPUtils;
import com.db.sys.dao.SysLogDao;
import com.db.sys.entity.SysLog;
import com.db.sys.entity.SysUser;
/**
 * @Aspect 修饰的类表示一个切面对象,此对象中可以封装
 * 我们要织入的扩展功能.
 *@Order 用于指定切面的优先级,数字越小优先级越高
 */
@Aspect
@Service
public class SysLogAspect {
	@Autowired
	SysLogDao sysLogDao;
	/**
	 * @Around 注解描述的方法为一个环绕通知(
	 * 用户封装和织入扩展功能)
	 * @param jp 表示连接点对象(封装了具体目标方法信息)
	 * @return 返回目标方法的执行结果
	 * @throws Throwable
	 */
	//@Around("bean(*ServiceImpl)")
	@Around("@annotation(com.db.common.annotation.RequiredLog)")
	public Object around(ProceedingJoinPoint jp) throws Throwable{
		long t1=System.currentTimeMillis();
		Object result=jp.proceed();//执行目标方法
		long t2=System.currentTimeMillis();
		//System.out.println("方法执行时长:"+(t2-t1)+"ms");
		saveLog(jp,(t2-t1));
		return result;
	}

	private void saveLog(ProceedingJoinPoint jp, long time) throws Exception {
		//1.获取用户的操作日志
		//1.1获取登陆用户
		SysUser user=(SysUser)SecurityUtils.getSubject().getPrincipal();
		String username=user.getUsername();
		//1.2获取目标方法的方法名
		Class<?> targetClass = jp.getTarget().getClass();
		String pkgClassName = targetClass.getName();
		MethodSignature me=(MethodSignature)jp.getSignature();//方法签名
		String method = pkgClassName+"."+me.getName();
		//1.3获取执行目标方法时传入的实际参数
		Object[] args = jp.getArgs();
		String params=Arrays.toString(args);
		//1.4获取操作名(这个名字定义在注解中)
		//1.4.1获取目标方法对象(业务实现类中的方法)
		Method targetMethod = targetClass.getDeclaredMethod(me.getName(), me.getParameterTypes());
		//1.4.2获取方法对象上的注解
		RequiredLog requiredLog=targetMethod.getDeclaredAnnotation(RequiredLog.class);
		String operation=requiredLog.value();
		//1.5获取ip地址
		String ip = IPUtils.getIpAddr();
		//2.封装操作日志
		SysLog log=new SysLog();
		log.setUsername(username);
		log.setTime(time);
		log.setCreateDate(new Date());
		log.setOperation(operation);
		log.setIp(ip);
		log.setParams(params);
		//log.setId(id);
		log.setMethod(method);
		//3.保存操作日志(写入到数据库)
		sysLogDao.insertObject(log);
	}
}
