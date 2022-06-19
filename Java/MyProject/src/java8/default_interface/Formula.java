package java8.default_interface;

/**
 * Java 8�������Ǹ��ӿ����һ���ǳ���ķ���ʵ�֣�ֻ��Ҫʹ��default�ؼ��ּ��ɣ���������ֽ�����չ����
 */
public interface Formula {
	double calculate(int a);
	
	/**
	 * ��չ�������÷������ñ���д�����Ա�ֱ��ʹ��
	 */
	default double sqrt(int a) {
		return Math.sqrt(a);
	}
	
	public static void main(String[] args) {
		Formula formula = (a) -> Math.sqrt(a * 100);
		System.out.println(formula.calculate(100));
		System.out.println(formula.sqrt(100));
	}
}
