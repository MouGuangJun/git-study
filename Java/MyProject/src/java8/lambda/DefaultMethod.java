package java8.lambda;

import java8.default_interface.Formula;

/**
 * Lambda表达式无法访问到接口的默认方法
 */
public class DefaultMethod {
	public static void main(String[] args) {
		//编译报错
		//Formula formula = (a) -> sqrt(a * 100);
	}
}
