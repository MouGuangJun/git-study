package java8.lambda;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * lambda表达式排列字符串
 */
public class Compare {
	public static void main(String[] args) {
		// 1.匿名内部类：需要给静态方法Collections.sort传入一个List对象以及一个比较器来按指定的顺序排列。通常做法都是创建一个匿名的比较器对象然后将其传递给sort方法
		List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");
		Collections.sort(names, new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s1.compareTo(s2);
			}
		});
		
		System.out.println(names);
		
		//lambda表达式-1
		Collections.sort(names, (String a, String b) -> {
			return b.compareTo(a);
		});
		System.out.println(names);
		
		//lambda表达式-2
		Collections.sort(names, (String a, String b) -> a.compareTo(b));
		System.out.println(names);
		
		//lambda表达式-3
		//java编译器可以自动推导出参数类型
		Collections.sort(names, (a, b) -> b.compareTo(a));
		System.out.println(names);
		
	}
}
