package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Test {
	public static void main(String[] args) {
		/*GamePlayer gamePlayer = new GamePlayer("张三");
		GamePlayProxy playProxy = new GamePlayProxy(gamePlayer);
		IGamePlayer gamePlayProxy = (IGamePlayer) playProxy.getProxy();
		
		System.out.println(gamePlayProxy.login());*/
		
		IGamePlayer iGamePlayer = new GamePlayer();
		IGamePlayer gamePlayer = (IGamePlayer) Proxy.newProxyInstance(iGamePlayer.getClass().getClassLoader(), iGamePlayer.getClass().getInterfaces(), new InvocationHandler() {
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				System.out.println(args[0]);
				Object result = 1111;
				System.out.println("速战速决！");
				method.invoke(iGamePlayer, "李四");
				System.out.println("改日再战！");
				return result;
			}
		});
		
		
		gamePlayer.login("张三");
	}
}
