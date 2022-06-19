package java8.lambda;

/**
 * 访问局部变量
 */
public class LocalVariable {
	public static void main(String[] args) {
		/*final*/ int num = 1;//此处的final可省略，但是num不能被后面的代码修改，否则会编译报错（匿名内部类访问外部变量的时候，必须要加上final关键字）
		//我们可以直接在lambda表达式中访问外层的局部变量
		FuncInterface<Integer, String> converter = (from) -> String.valueOf(from + num);
		System.out.println(converter.convert(3));
		
		//num = 3;//编译报错
	}
}
