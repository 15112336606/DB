package com.db.common.java.mode;
/**
 * 消息业务接口
 * @author UID
 *
 */
interface MessageService{
	void sendMsg(String msg);
}
class MessageServiceImpl implements MessageService{
	@Override
	public void sendMsg(String msg) {
		System.out.println(msg);
	}
}
class SubMessageServiceImpl extends MessageServiceImpl{
	@Override
	public void sendMsg(String msg) {
		long t1=System.nanoTime();
		super.sendMsg(msg);
		long t2=System.nanoTime();
		System.out.println("time is:"+(t2-t1));
	}
}
class ProxyMessageService implements MessageService{
	private MessageService messageService;
	public ProxyMessageService(MessageService messageService){
		this.messageService=messageService;
	}
	@Override
	public void sendMsg(String msg) {
		long t1=System.nanoTime();
		messageService.sendMsg(msg);
		long t2=System.nanoTime();
		System.out.println("time is:"+(t2-t1));
		
	}
	
}
//1.通过继承方式实现扩展的应用分析
//1.优势:可读性好,扩展简单
//2.劣势:
//1)耦合性比较强(子类和父类)
//2)假如有多个目标对象,需要进行功能扩展,类的个数会增加,代码冗余会增大

//=========================================================================
//2.通过创建兄弟类的方法实现扩展
//2.兄弟类方式扩展应用分析
//1)优势:耦合性比较低,扩展类的个数会减少(一个接口写一个扩展类就可以了);
//2)劣势:方法中代码冗余还是比较高,每个接口都要自己写实现类.
public class TestProxy01 {
	static void Proxy01(){
		MessageServiceImpl ms = new MessageServiceImpl();
		ProxyMessageService proxy = new ProxyMessageService(ms);
		proxy.sendMsg("你好");
	}
	static void Proxy02(){
		SubMessageServiceImpl ms = new SubMessageServiceImpl();
		ms.sendMsg("你好");
	}
	public static void main(String[] args) {
		Proxy01();
		Proxy02();
	}
}
