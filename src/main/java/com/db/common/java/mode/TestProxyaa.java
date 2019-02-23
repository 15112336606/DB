package com.db.common.java.mode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface sayHello{
	String doSayHello();
}

class doSayHelloImpl implements sayHello{
	@Override
	public String doSayHello() {
		System.out.println("Hello");
		return "return Hello";
	}
}
class invocationProxy implements InvocationHandler{
	private Object target;
	public invocationProxy(Object target){
		this.target=target;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		long t1 = System.nanoTime();
		Object result = method.invoke(target, args);
		long t2 = System.nanoTime();
		System.out.println("time:"+(t2-t1));
		return result;
	}
	
}
class ProxySayHelloFactory{
	public static Object proxyDoSayHello(Object target){
		return Proxy.newProxyInstance(
				target.getClass().getClassLoader(), 
				target.getClass().getInterfaces(),
				new invocationProxy(target));
	}
} 
public class TestProxyaa {
	public static void main(String[] args) {
		doSayHelloImpl dshi = new doSayHelloImpl();
		Object proxy = ProxySayHelloFactory.proxyDoSayHello(dshi);
		sayHello sh=(sayHello)proxy;
		String hello = sh.doSayHello();
		System.out.println(hello);
	}
}
