package java8.function;

import java.util.Arrays;
import java.util.function.Function;

/**
 * Function接口有一个参数并且返回一个结果，并附带了一些可以和其他函数组合的默认方法(compose,andThen)
 */
public class FunctionT {
	public static void main(String[] args) {
		Function<String, Integer> toInteger = Integer::valueOf;
		Function<Integer, String> toString = String :: valueOf;
		Function<String, char[]> andThen = toInteger.andThen(toString).andThen(String :: toCharArray);
		System.out.println(Arrays.toString(andThen.apply("789")));
		
		Function<String, String> backToInteger = toInteger.andThen(String::valueOf);
		System.out.println(backToInteger.apply("123"));

		Function<String, Integer> doubleVal = toInteger.compose(String::trim);
		System.out.println(doubleVal.apply("  456  "));
	}
}
