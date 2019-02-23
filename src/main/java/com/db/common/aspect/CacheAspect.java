package com.db.common.aspect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;

import com.db.common.cache.CacheKey;
@Aspect
@Service
public class CacheAspect {
	Map<CacheKey,Object> cache=new ConcurrentHashMap<>();
	@Around("@annotation(com.db.common.annotation.RequiredCache)")
	public Object cacheAspect(ProceedingJoinPoint jp) throws Throwable{
		CacheKey key=new CacheKey();
		key.setArgs(jp.getArgs());
		Class<?> targetClass = jp.getTarget().getClass();
		key.setTargetClass(targetClass);
		MethodSignature ms=(MethodSignature) jp.getSignature();
		key.setTargetMethod(targetClass.getMethod(ms.getName(), ms.getParameterTypes()));
		Object result=cache.get(key);
		if(result==null){
			result = jp.proceed();
			cache.put(key, result);
		}
		return result;
	}
}
