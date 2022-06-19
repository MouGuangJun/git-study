package java8.lambda;

/**
 * lambda�ڲ�����ʵ�����ֶ��뾲̬�����ɶ��ֿ�д��������������һ�µ�
 */
public class StaticVariable {
	static int outerStaticNum;
	int outerNum;

	void scopes() {
		FuncInterface<Integer, String> strConvert1 = (from) -> {
			outerNum = 23;// д�ֲ�����
			return String.valueOf(from);
		};

		System.out.println(strConvert1.convert(17));

		FuncInterface<Integer, String> strConvert2 = (from) -> {
			outerStaticNum = 72;// д��̬����
			return String.valueOf(from);
		};

		System.out.println(strConvert2.convert(28));

		System.out.println("outerStaticNum=" + outerStaticNum + ",outerNum=" + outerNum);
	}

	// ���ʶ����ֶ��뾲̬����
	public static void main(String[] args) {
		StaticVariable sVariable = new StaticVariable();
		sVariable.scopes();
	}
}
