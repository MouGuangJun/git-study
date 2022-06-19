package thread;

public class MultiOpTest {
	static public class MultiOp {
		static String a = "H";
		
		static void modify(String v) {
			a = v;
		}
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				synchronized (MultiOp.class) {
					MultiOp.modify(Thread.currentThread().getName());
					System.out.println(Thread.currentThread().getName() + ":" + MultiOp.a);
				}
			}).start();
		}
	}
}
