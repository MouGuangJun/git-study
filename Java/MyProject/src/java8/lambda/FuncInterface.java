package java8.lambda;

/**
 * 函数式接口
 * @FunctionalInterface 注解，编译器如果发现标注了这个注解的接口有多于一个抽象方法的时候是会报错的
 */
@FunctionalInterface
public interface FuncInterface<F, T> {
	T convert(F from);
	
	public static void main(String[] args) {
		FuncInterface<String, Integer> convert = (a) -> Integer.valueOf(a);
		System.out.println(convert.convert("123"));
	}
}
