package java8.lambda;

/**
 * 方法与构造函数引用 通过静态方法表示#+{@link FuncInterface} 中的内容
 */
public class FuncAndConstructor {
	public static void main(String[] args) {
		// 调用Integer的静态valueOf方法
		FuncInterface<String, Integer> converter = Integer::valueOf;
		System.out.println(converter.convert("15"));

		constructor();
	}

	/**
	 * 构造函数使用::关键字来引用
	 */
	private static void constructor() {
		//使用Person::new来获取Person类构造函数的引用，Java会自动根据PersonFactory.create方法的签名来选择合适的构造函数
		PersonFactory<Person> personFactory = Person::new;
		Person person = personFactory.create("Peter", "Parker");
		System.out.println(person);
	}

	/**
	 * 指定一个用来创建Person对象的对象工厂接口
	 */
	@FunctionalInterface
	interface PersonFactory<P extends Person> {
		P create(String firstName, String lastName);
	}
}
