package com.db.common.java.mode;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
/**
 * 只有实现类,可以通过拦截器拦截目标方法实现功能的扩展
 * @author UID
 *
 */
class ProductServiceImpl{
	void saveObject(String msg){
		System.out.println("save product");
		System.out.println(msg);
		System.out.println("22222222222");
	}
}

class ProductServiceCglibProxy implements MethodInterceptor{
	@Override				//代理对象                 被拦截方法                        方法参数                                代理方法
	public Object intercept(Object Object, Method Method, Object[] Objects, MethodProxy MethodProxy) throws Throwable {
		System.out.println("ProductServiceCglibProxy implements MethodInterceptor");
		System.out.println("1111111111111");//通过代理类调用父类方法
		Object invote = MethodProxy.invokeSuper(Object, Objects);
		System.out.println(Object.getClass().getName());
		System.out.println(Method.getName());
		System.out.println(Arrays.toString(Objects));
		System.out.println(MethodProxy.getClass().getName());
		System.out.println("333333333333");
		return invote;
	}
	
}




//不实现接口
//基于CGLIB如何为目标对象(ProductServiceImpl)产生代理对象
public class TestProxy04 {
	public static void main(String[] args) {
		ProductServiceCglibProxy CglibProxy = new ProductServiceCglibProxy();//拦截器
		Enhancer enhancer = new Enhancer();//增强器
		enhancer.setSuperclass(ProductServiceImpl.class);//注入被代理对象
		enhancer.setCallback(CglibProxy);//设置回调对象(当执行代理对象的方法时执行此方法)
		ProductServiceImpl Impl=(ProductServiceImpl)enhancer.create();//产生代理实例
		Impl.saveObject("参数121212");
		System.out.println(Impl.getClass().getName());
		
	}
}
