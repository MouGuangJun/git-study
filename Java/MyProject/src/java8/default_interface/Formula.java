package java8.default_interface;

/**
 * Java 8允许我们给接口添加一个非抽象的方法实现，只需要使用default关键字即可，这个特征又叫做扩展方法
 */
public interface Formula {
	double calculate(int a);
	
	/**
	 * 扩展方法：该方法不用被重写，可以被直接使用
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
