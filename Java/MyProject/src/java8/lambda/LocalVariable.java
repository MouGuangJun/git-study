package java8.lambda;

/**
 * ���ʾֲ�����
 */
public class LocalVariable {
	public static void main(String[] args) {
		/*final*/ int num = 1;//�˴���final��ʡ�ԣ�����num���ܱ�����Ĵ����޸ģ��������뱨�������ڲ�������ⲿ������ʱ�򣬱���Ҫ����final�ؼ��֣�
		//���ǿ���ֱ����lambda���ʽ�з������ľֲ�����
		FuncInterface<Integer, String> converter = (from) -> String.valueOf(from + num);
		System.out.println(converter.convert(3));
		
		//num = 3;//���뱨��
	}
}
