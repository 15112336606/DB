package com.db.common.java.mode;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
/**
 * 产生代理对象继承父类,代理对象获得从父类继承来的方法
 * @author UID
 *
 */
class PersonService{
	public PersonService(){
		System.out.println("PersonService构造方法");
	}
	//该方法不能被子类覆盖,加上final不被代理
	final public Object getPerson(String code){
		System.out.println("(final)方法1:"+code);
		return null;
	}
	public void setPerson(){
		System.out.println("方法2");
	}
}
class CglibProxyIntercepter implements MethodInterceptor{
	//Object:cglib生成的代理对象,Method:被代理对象方法,
	//Object[]:方法入参,methodProxy:代理方法
	@Override
	public Object intercept(Object arg0, Method arg1,
			Object[] arg2, MethodProxy arg3) throws Throwable {
		System.out.println("执行前...");
		Object object = arg3.invokeSuper(arg0, arg2);
		System.out.println("执行后...");
		return object;
	}

}
public class TestProxycc {
	public static void main(String[] args) {
		CglibProxyIntercepter proxy = new CglibProxyIntercepter();
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(PersonService.class);
		enhancer.setCallback(proxy);
		PersonService create = (PersonService)enhancer.create();
		create.getPerson("1111");
		create.setPerson();
		
	}
	
}
