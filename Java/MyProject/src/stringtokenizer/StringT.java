package stringtokenizer;

import java.util.StringTokenizer;

public class StringT {
	public static void main(String[] args) {
		//sToken();
		//sFunction();
		for(StringTokenizer st = new StringTokenizer("www.baidu.com", "."); st.hasMoreElements();) {
			System.out.println(st.nextElement());
		}
	}
	
	
	/**
	 * StringTokenizer(String str) str：被分割的对象，分隔符采取默认分割，Java默认分割符是“空格”、“制表符\t”、换行符“\n”、回车符“\r”。默认所有的分隔符都有效
	 * StringTokenizer(String str, String delim)：分隔符不采用默认，提供一个指定的分隔符号
	 * StringTokenizer(String str, String delim, boolean returnDelims)：指定一个特定的分隔符，同时，指定是否返回分割符。如果是true，分隔符将被作为一个token返回
	 */
	private static void sToken() {
		// 1.StringTokenizer(String str)
		StringTokenizer st = new StringTokenizer("www baidu\r\ncom");
		while(st.hasMoreElements()) {
			System.out.println("1.Token:" + st.nextToken());
		}
		
		// 2.StringTokenizer(String str, String delim)
		st = new StringTokenizer("www.baidu.com", ".");
		while(st.hasMoreElements()) {
			System.out.println("2.Token:" + st.nextToken());
		}
		
		// 3.StringTokenizer(String str, String delim, boolean returnDelims)
		st = new StringTokenizer("www.baidu.com", ".", true);
		while(st.hasMoreElements()) {
			System.out.println("3.Token:" + st.nextToken());
			System.out.println(st.countTokens());
		}
	}
	
	/**
	 * int countTokens()：还有几个元素可以被遍历。返回的是当前可以被访问的元素的个数
	 * boolean hasMoreElements()：用来获取当前元素的token，一个返回String类型，一个返回Object类型
	 * Object nextElement()：除返回结果类型外，其返回与nextToken方法相同的值
	 * String nextToken()：StringTokenizer对象中的下一个token
	 * Sting nextToken(String delim)：返回当前索引开始，指定分隔符的下一个token。实际上返回的是索引当前位置到下一个delim出现为止的所有字符
	 */
	private static void sFunction() {
		//Element和Token输出的结果都一样，只是Token返回String，Element返回Object
		StringTokenizer st = new StringTokenizer("J-PHONE,Vodafone,SoftBank", ",");
		while(st.hasMoreTokens()) {
			System.out.println("count0=" + st.countTokens());
			System.out.println("carry:" + st.nextToken());
			System.out.println("count1=" + st.countTokens());
		}
		
		st = new StringTokenizer("J-PHONE,Vodafone,SoftBank", ",");
		while(st.hasMoreElements()) {
			System.out.println("count0=" + st.countTokens());
			System.out.println("carry:" + st.nextElement());
			System.out.println("count1=" + st.countTokens());
		}
	}
}
