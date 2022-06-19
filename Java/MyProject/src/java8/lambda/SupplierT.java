package java8.lambda;

import java.util.function.Supplier;

/**
 * Supplier接口返回一个任意泛型的值，和Function接口不同的是该接口没有任何参数
 */
public class SupplierT {
	public static void main(String[] args) {
		Supplier<Person> supplier = Person :: new;
		Person person = supplier.get();
		System.out.println(person);
	}
}
