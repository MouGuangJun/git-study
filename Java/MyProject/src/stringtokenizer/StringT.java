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
	 * StringTokenizer(String str) str�����ָ�Ķ��󣬷ָ�����ȡĬ�ϷָJavaĬ�Ϸָ���ǡ��ո񡱡����Ʊ��\t�������з���\n�����س�����\r����Ĭ�����еķָ�������Ч
	 * StringTokenizer(String str, String delim)���ָ���������Ĭ�ϣ��ṩһ��ָ���ķָ�����
	 * StringTokenizer(String str, String delim, boolean returnDelims)��ָ��һ���ض��ķָ�����ͬʱ��ָ���Ƿ񷵻طָ���������true���ָ���������Ϊһ��token����
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
	 * int countTokens()�����м���Ԫ�ؿ��Ա����������ص��ǵ�ǰ���Ա����ʵ�Ԫ�صĸ���
	 * boolean hasMoreElements()��������ȡ��ǰԪ�ص�token��һ������String���ͣ�һ������Object����
	 * Object nextElement()�������ؽ�������⣬�䷵����nextToken������ͬ��ֵ
	 * String nextToken()��StringTokenizer�����е���һ��token
	 * Sting nextToken(String delim)�����ص�ǰ������ʼ��ָ���ָ�������һ��token��ʵ���Ϸ��ص���������ǰλ�õ���һ��delim����Ϊֹ�������ַ�
	 */
	private static void sFunction() {
		//Element��Token����Ľ����һ����ֻ��Token����String��Element����Object
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
