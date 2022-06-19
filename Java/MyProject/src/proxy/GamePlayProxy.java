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
		System.out.println("速战速决！");
		method.invoke(obj, args);
		System.out.println("改日再战！");
		return result;
	}
	
	public Object getProxy() {
		return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), obj.getClass().getInterfaces(), this);
	}
}
