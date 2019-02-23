package com.db.common.java.mode;
/**
 * 产生一个代理对象,通过jdk中的return Proxy.newProxyinstance创建一个代理工厂,调用工厂方法即创立代理对象,
 * 代理工厂中传参包括类加载器,类接口,处理器
 * 对于实现接口的代理,处理器需要实现invocationHandler接口,在处理器中书写扩展业务
 * 由于mybatis接口中没有实现类,当调用接口时候,底层会创建一个代理工厂,传入所调用接口,通过传入接口的名字和方法名,
 * 组成namespace.methodId,通过mybatis其他功能(封装JDBC)进行查询/
 */
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
//模拟mybatis中如何直接为接口产生代理对象
//定义dao接口
interface SearchDao{
	String search(String key);
}
//建立代理
class DaoProxyHandler implements InvocationHandler{
	private Class<?> targetCls;
	public DaoProxyHandler(Class<?> targetCls){
		this.targetCls=targetCls;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("invoke");
		String namespace=targetCls.getName();
		String methodId=method.getName();
		System.out.println(namespace+"."+methodId);
		return null;
	}
}
class DaoProxyFactory{
	/**
	 * <T>应用在方法的返回值左侧时表示这个方法是泛型方法
	 * @param targetCls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T>T getProxy(Class<T> targetCls){
		return (T)Proxy.newProxyInstance(
				targetCls.getClassLoader(),
				new Class[]{targetCls} ,//interfaces
				new DaoProxyHandler(targetCls));
	}
}


public class TestProxy03 {
	public static void main(String[] args) {
		SearchDao dao = new DaoProxyFactory().getProxy(SearchDao.class);
		System.out.println(dao.getClass().getName());
		dao.search("aaa");
	}
}
