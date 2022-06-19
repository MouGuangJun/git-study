import java.util.Scanner;

/**
 * @describe �ַ�������
 * @author gjmou
 * @date 2021.11.11
 * @history gjmou 2021.11.11 �����ļ�
 * */
public class CreateText {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println("��������Ҫ���ɵ��ַ�:");
			String chars = sc.nextLine();
			while(chars.length() != 1) {
				System.err.println("������ַ�����ֻ��Ϊ1�����������룡");
				chars = sc.nextLine();
			}
			
			boolean flag = true;
			int len = 0;
			while(flag) {
				System.out.println("��������Ҫ���ɵĳ��ȣ�");
				String lenStr = sc.nextLine();
				if(lenStr.contains(".")) {
					System.err.println("�ַ����ȱ���Ϊ��������ȷ�ϣ�");
					continue;
				}
				try {
					len = Integer.valueOf(lenStr);
					flag = false;
				} catch (NumberFormatException e) {
					System.err.println("�ַ����ȱ��������֣���ȷ�ϣ�");
					continue;
				}
				
				if(len < 0) {
					System.err.println("�ַ����ȱ���Ϊ��������ȷ�ϣ�");
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
