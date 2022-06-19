package java8.lambda;

/**
 * ����ʽ�ӿ�
 * @FunctionalInterface ע�⣬������������ֱ�ע�����ע��Ľӿ��ж���һ�����󷽷���ʱ���ǻᱨ���
 */
@FunctionalInterface
public interface FuncInterface<F, T> {
	T convert(F from);
	
	public static void main(String[] args) {
		FuncInterface<String, Integer> convert = (a) -> Integer.valueOf(a);
		System.out.println(convert.convert("123"));
	}
}
