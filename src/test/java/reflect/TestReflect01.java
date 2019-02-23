package reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


class Point{
	private int x;
	private int y;
	public Point(){}
	public Point(int x,int y){
		this.x=x;
		this.y=y;
	}
	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}
	public double distance(Point point){
		double a=(this.x-point.x)*(this.x-point.x);
		double b=(this.y-point.y)*(this.y-point.y);
		return Math.sqrt(a+b);
	}
	
}
public class TestReflect01 {
	/**类对象(字节码对象)的获取:每个类都有一个类对象*/
	static void doClass() throws Exception{
		Class<?> c1=Point.class;
		Class<?> c2=Class.forName("reflect.Point");
		Point p1=new Point();
		Class<?> c3=p1.getClass();
		System.out.println(c1==c2);
		System.out.println(c2==c3);
	}
	static Object doConstructor() throws Exception{
		//1.获取类对象
		Class<?> c1=Class.forName("reflect.Point");
		//2.基于类对象获取构造方法对象(无参构造)
		Constructor<?> con = c1.getDeclaredConstructor();
		//3.设置构造方法是否私有化可访问
		con.setAccessible(true);
		//4.基于构造方法新建实例
		Object p = con.newInstance();
		System.out.println(p);
		return p;
	}
	static Object doConstructor(Object[] obj,Class<?>... parameterTypes) throws Exception{
		//1.获取类对象
		Class<?> c1=Class.forName("reflect.Point");
		//2.基于类对象获取构造方法对象(无参构造)
		Constructor<?> con = c1.getDeclaredConstructor(parameterTypes);
		//3.设置构造方法是否私有化可访问
		con.setAccessible(true);
		//4.基于构造方法新建实例
		Object p = con.newInstance(obj);
		System.out.println(p);
		return p;
	}
	static void doField()throws Exception{
		//1.获取类对象
		Class<?> c1=Class.forName("reflect.Point");
		//基于类对象获取属性对象
		Field fs = c1.getDeclaredField("x");
		fs.setAccessible(true);
		Object obj=doConstructor();
		fs.set(obj, 100);
		System.out.println("x="+fs.get(obj));
	}
	static void doMethod()throws Exception{
		//1.获取类对象
		Class<?> c1=Class.forName("reflect.Point");
		//获取方法对象
		Method method = c1.getDeclaredMethod("distance", Point.class);
		//构建两个点对象
		Object o1 = doConstructor();
		Object o2 = doConstructor(new Object[]{1,2},int.class,int.class);
		//求两点距离
		Double d = (Double)method.invoke(o1, o2);
		System.out.println(d);
	}
	public static void main(String[] args) throws Exception {
		doMethod();
	}
}
