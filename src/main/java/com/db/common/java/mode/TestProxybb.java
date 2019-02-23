package com.db.common.java.mode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface BuySomething{
	String buyApple();
}
class BuySomethingProxyInvocation implements InvocationHandler{
	Class<?> target;
	public BuySomethingProxyInvocation(Class<?> target){
		this.target=target;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String a = target.getName();
		System.out.println("aaa");
		String b = method.getName();
		System.out.println(a+"."+b);
		System.out.println("bbb");
		return null;
	}

}


class BuySomethingProxyFactory{
	@SuppressWarnings("unchecked")
	public <T>T BuySomethingProxy(Class<T> target){
		return (T)Proxy.newProxyInstance(
				target.getClassLoader(),
				new Class[]{target}, 
				new BuySomethingProxyInvocation(target));
	}
}
public class TestProxybb{
	public static void main(String[] args) {
		doMethod01();
	}

	private static void doMethod01() {
		System.out.println("1aaaa");
		BuySomething dao = new BuySomethingProxyFactory().BuySomethingProxy(BuySomething.class);
		dao.buyApple();
		System.out.println("2bbbb");
	}
}
