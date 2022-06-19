import java.util.Scanner;

/**
 * @describe 字符制造者
 * @author gjmou
 * @date 2021.11.11
 * @history gjmou 2021.11.11 创建文件
 * */
public class CreateText {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println("请输入需要生成的字符:");
			String chars = sc.nextLine();
			while(chars.length() != 1) {
				System.err.println("输入的字符长度只能为1，请重新输入！");
				chars = sc.nextLine();
			}
			
			boolean flag = true;
			int len = 0;
			while(flag) {
				System.out.println("请输入需要生成的长度：");
				String lenStr = sc.nextLine();
				if(lenStr.contains(".")) {
					System.err.println("字符长度必须为整数，请确认！");
					continue;
				}
				try {
					len = Integer.valueOf(lenStr);
					flag = false;
				} catch (NumberFormatException e) {
					System.err.println("字符长度必须是数字，请确认！");
					continue;
				}
				
				if(len < 0) {
					System.err.println("字符长度必须为正数，请确认！");
					flag = true;
					continue;
				}
			}
			
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < len; i++) {
				sb.append(chars);
			}
			
			System.out.println(sb.toString());
		}
	}
}
