package java8.lambda;

import java.util.function.Supplier;

/**
 * Supplier�ӿڷ���һ�����ⷺ�͵�ֵ����Function�ӿڲ�ͬ���Ǹýӿ�û���κβ���
 */
public class SupplierT {
	public static void main(String[] args) {
		Supplier<Person> supplier = Person :: new;
		Person person = supplier.get();
		System.out.println(person);
	}
}
