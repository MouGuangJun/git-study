package java8.lambda;

/**
 * lambda内部对于实例的字段与静态变量可读又可写。和匿名对象是一致的
 */
public class StaticVariable {
	static int outerStaticNum;
	int outerNum;

	void scopes() {
		FuncInterface<Integer, String> strConvert1 = (from) -> {
			outerNum = 23;// 写局部变量
			return String.valueOf(from);
		};

		System.out.println(strConvert1.convert(17));

		FuncInterface<Integer, String> strConvert2 = (from) -> {
			outerStaticNum = 72;// 写静态变量
			return String.valueOf(from);
		};

		System.out.println(strConvert2.convert(28));

		System.out.println("outerStaticNum=" + outerStaticNum + ",outerNum=" + outerNum);
	}

	// 访问对象字段与静态变量
	public static void main(String[] args) {
		StaticVariable sVariable = new StaticVariable();
		sVariable.scopes();
	}
}
