import java.lang.reflect.Method;

public class Sub extends Supper {
	@Override
	public String run() {
		//super.run();
		System.out.println("this is sub class!");
		return "bbb";
	}
	
	public static void main(String[] args) throws Exception {
		Class<?> clazz = Class.forName("Sub");
		Object obj = clazz.newInstance();
		Method method = clazz.getDeclaredMethod("run");
		Object result = method.invoke(obj);
		System.out.println(result);
		
		System.out.println("=================================");
		
		Supper supper = (Supper) clazz.newInstance();
		System.out.println(supper.run());
	}
}
