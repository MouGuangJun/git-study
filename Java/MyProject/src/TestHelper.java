import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

/**
 * 1.ϵͳ�����漰����̨
 * 2.�Ŵ����߹���
 * 3.�ͻ�����
 * 4.���Ŷ��
 * 5.���ŷ���
 * 6.����ִ��
 * 7.�Ŵ�����
 * 8.��������
 * 9.ѺƷ����
 * 10.�������
 * 11.ͳ�Ʋ�ѯ
 * 12.�����ʲ�����
 * 13.ϵͳ����
 * 14.�Ŵ�������Ӧ��
 * 
 * a.������
 * b.ҵ����
 * @author W4421017
 *
 */
public class TestHelper {
	public static Integer[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
	public static String[] strs = {"GZT", "XDZC", "KHGL", "SXED", "SXFA", "SXZX", "XDHS", "DBGL", "YPGL", "DHGL", "TJCX", "FXZC", "XTGL", "DSJ"};
	public static Character[] chars = {'a', 'b', 'A', 'B'};
	
	public static void main(String[] args) throws Exception {
		while(true) {
			Scanner sc = new Scanner(System.in);
			System.out.println("������������ͣ�\r\n1.ϵͳ�����漰����̨��2.�Ŵ����߹���3.�ͻ�����4.���Ŷ�ȡ�5.���ŷ�����6.����ִ�С�7.�Ŵ�����\r\n"
					+ "8.��������9.ѺƷ����10.�������11.ͳ�Ʋ�ѯ��12.�����ʲ�����13.ϵͳ����14.�Ŵ�������Ӧ��\r\n"
					+ "a.�����ࡢb.ҵ����");
			String msg = sc.nextLine();
			ArrayList<Integer> arrList = new ArrayList<Integer>(arr.length);
			Collections.addAll(arrList, arr);
			ArrayList<Character> charList = new ArrayList<Character>();
			Collections.addAll(charList, chars);
			
			char[] cs = msg.toCharArray();
			int len = cs.length;
			if(len < 2) {
				System.err.println("�����ʽ����ȷ��");
				continue;
			}
			
			String pre = "";
			String mid = "";
			char op = cs[cs.length - 1];
			if(!charList.contains(op)) {
				System.err.println("�����ʽ����ȷ��");
				continue;
			}
			
			mid = ('a' == op || 'A' == op) ? "X" : "Y";
			
			String numStr = "";
			
			for (int i = 0; i < cs.length; i++) {
				if(i != cs.length - 1) {
					numStr += cs[i];
				}
			}
			
			int num = 0;
			try {
				num = Integer.valueOf(numStr);
			}catch (Exception e) {
				System.err.println("�����ʽ����ȷ��");
				continue;
			}
			
			pre = strs[num - 1];
			
			getCard(pre, mid);
		}
	}

	private static void getCard(String pre, String mid) throws Exception {
		String fileName = "config.properties";
		File file = new File(fileName);
		if(!file.exists()) file.createNewFile();
		Properties pp = new Properties();
		FileInputStream in = new FileInputStream(fileName);
		pp.load(in);
		
		in.close();
		
		FileOutputStream out = new FileOutputStream(fileName);
		if(null == pp.getProperty(pre)) {
			String value = pre + "_" + mid + "10001";
			pp.setProperty(pre, value);
			pp.store(out, "getCard");
			out.close();
			getTime();
			System.out.println("��Ž����" + value);
			return;
		}
		
		String card = (String) pp.get(pre);
		Integer cardNum = Integer.valueOf(card.substring(card.length() - 5));
		cardNum++;
		
		String value = pre + "_" + mid + cardNum;
		pp.setProperty(pre, value);
		pp.store(out, "getCard");
		out.close();
		getTime();
		System.out.println("��ñ�ţ�" + value);
	}

	private static void getTime() {
		System.out.println("��ǰʱ�䣺" + new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
	}
}
