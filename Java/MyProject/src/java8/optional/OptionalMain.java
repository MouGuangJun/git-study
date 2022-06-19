package java8.optional;

import java.util.Optional;

public class OptionalMain {
	public static void main(String[] args) {
		Integer val1 = null;
		Integer val2 = 10;
		// Optional.of - 允许传递为null的参数
		Optional<Integer> a = Optional.ofNullable(val1);
		// Optional.of - 如果传递的参数是null，抛出异常NullPointerException
		Optional<Integer> b = Optional.of(val2);
		// Optional.isPresent - 判断值是否存在
		System.out.println("第一个参数存在：" + a.isPresent());
		System.out.println("第二个参数存在：" + b.isPresent());
		// Optional.orElse - 如果值存在，返回它，否则返回默认值
		Integer value1 = a.orElse(0);
		//System.out.println(a.get());//java.util.NoSuchElementException: No value present
		//Optional.get() - 获取值，值需要存在
		Integer value2 = b.get();
		
		System.out.println(value1 + value2);
	}
}
