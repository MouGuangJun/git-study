package java8.lambda;

/**
 * �����빹�캯������ ͨ����̬������ʾ#+{@link FuncInterface} �е�����
 */
public class FuncAndConstructor {
	public static void main(String[] args) {
		// ����Integer�ľ�̬valueOf����
		FuncInterface<String, Integer> converter = Integer::valueOf;
		System.out.println(converter.convert("15"));

		constructor();
	}

	/**
	 * ���캯��ʹ��::�ؼ���������
	 */
	private static void constructor() {
		//ʹ��Person::new����ȡPerson�๹�캯�������ã�Java���Զ�����PersonFactory.create������ǩ����ѡ����ʵĹ��캯��
		PersonFactory<Person> personFactory = Person::new;
		Person person = personFactory.create("Peter", "Parker");
		System.out.println(person);
	}

	/**
	 * ָ��һ����������Person����Ķ��󹤳��ӿ�
	 */
	@FunctionalInterface
	interface PersonFactory<P extends Person> {
		P create(String firstName, String lastName);
	}
}
