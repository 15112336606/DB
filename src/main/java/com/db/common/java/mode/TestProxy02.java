package com.db.common.java.mode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 由于接口有实现类,在传参的时候我们可以直接传接口的实现类,然后在处理器内直接调用目标实现类.
 * 重写实现类,完成扩展功能.
 * @author UID
 *
 */
interface doSearch{
	String search();
}
class doSearchImpl implements doSearch{
	@Override
	public String search() {
		System.out.println("1111");
		return "search result Tedu";
	}
}
class ProxyFactory{
	static class searchHandler implements InvocationHandler{
		private Object target;
		public searchHandler(Object target){
			this.target=target;
		}
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			long t1=System.nanoTime();
			Object result = method.invoke(target, args);
			long t2=System.nanoTime();
			System.out.println("aaa:"+(t2-t1));
			return result;
		}
		
	}
	public static Object newProxySearch(Object target){
		return Proxy.newProxyInstance(
				target.getClass().getClassLoader(), 
				target.getClass().getInterfaces(), 
				new searchHandler(target));
	}
	/**
	 * 为目标创建代理对象
	 * @param target 目标对象
	 * @return 代理对象
	 */
	public static Object newProxy(Object target){
		return Proxy.newProxyInstance(
				target.getClass().getClassLoader(),//loader 为类加载器对象
				target.getClass().getInterfaces(),//要实现接口对象
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						long t1=System.nanoTime();
						//通过反射执行目标对象的目标方法
						Object result=method.invoke(target, args);
						long t2=System.nanoTime();
						System.out.println("time:"+(t2-t1));
						return result;
					}
				});//处理器(扩展业务及方法调用)
	}
}

public class TestProxy02 {
	public static void main(String[] args) {
		//1.创建目标对象
		doMethod01();
		doMethod02();
		}
	//alt+shift+m 可以快速将一段代码提出
	private static void doMethod01() {
		MessageServiceImpl target = new MessageServiceImpl();
		//2.为目标对象创建代理对象(代理对象会与目标对象实现共同的接口)
		Object proxy = ProxyFactory.newProxy(target);
		//3.执行代理对象方法
		MessageService ms=(MessageService)proxy;
		System.out.println(ms.getClass().getName());
		ms.sendMsg("hello");
	}
	public static void doMethod02(){
		doSearchImpl target = new doSearchImpl();
		Object result = ProxyFactory.newProxySearch(target);
		doSearch ds=(doSearch)result;
		String s = ds.search();
		System.out.println(s);
	}
	}

