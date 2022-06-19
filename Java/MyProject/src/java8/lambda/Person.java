package java8.lambda;

import java.util.function.Supplier;

/**
 * ����һ������������캯���ļ���
 */
public class Person {
	String firstName;
	String lastName;

	Person() {
	}

	Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "Person [firstName=" + firstName + ", lastName=" + lastName + "]";
	}
}
