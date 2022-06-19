package java8.lambda;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * lambda���ʽ�����ַ���
 */
public class Compare {
	public static void main(String[] args) {
		// 1.�����ڲ��ࣺ��Ҫ����̬����Collections.sort����һ��List�����Լ�һ���Ƚ�������ָ����˳�����С�ͨ���������Ǵ���һ�������ıȽ�������Ȼ���䴫�ݸ�sort����
		List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");
		Collections.sort(names, new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s1.compareTo(s2);
			}
		});
		
		System.out.println(names);
		
		//lambda���ʽ-1
		Collections.sort(names, (String a, String b) -> {
			return b.compareTo(a);
		});
		System.out.println(names);
		
		//lambda���ʽ-2
		Collections.sort(names, (String a, String b) -> a.compareTo(b));
		System.out.println(names);
		
		//lambda���ʽ-3
		//java�����������Զ��Ƶ�����������
		Collections.sort(names, (a, b) -> b.compareTo(a));
		System.out.println(names);
		
	}
}
