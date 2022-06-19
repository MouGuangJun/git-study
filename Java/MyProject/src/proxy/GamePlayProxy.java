package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class GamePlayProxy implements InvocationHandler{
	private Object obj;
	
	public GamePlayProxy(Object obj) {
		this.obj = obj;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = 1111;
		System.out.println("��ս�پ���");
		method.invoke(obj, args);
		System.out.println("������ս��");
		return result;
	}
	
	public Object getProxy() {
		return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), obj.getClass().getInterfaces(), this);
	}
}
