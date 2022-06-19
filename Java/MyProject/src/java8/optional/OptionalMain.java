package java8.optional;

import java.util.Optional;

public class OptionalMain {
	public static void main(String[] args) {
		Integer val1 = null;
		Integer val2 = 10;
		// Optional.of - ������Ϊnull�Ĳ���
		Optional<Integer> a = Optional.ofNullable(val1);
		// Optional.of - ������ݵĲ�����null���׳��쳣NullPointerException
		Optional<Integer> b = Optional.of(val2);
		// Optional.isPresent - �ж�ֵ�Ƿ����
		System.out.println("��һ���������ڣ�" + a.isPresent());
		System.out.println("�ڶ����������ڣ�" + b.isPresent());
		// Optional.orElse - ���ֵ���ڣ������������򷵻�Ĭ��ֵ
		Integer value1 = a.orElse(0);
		//System.out.println(a.get());//java.util.NoSuchElementException: No value present
		//Optional.get() - ��ȡֵ��ֵ��Ҫ����
		Integer value2 = b.get();
		
		System.out.println(value1 + value2);
	}
}
