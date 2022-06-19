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
 * 1.系统主界面及工作台
 * 2.信贷政策管理
 * 3.客户管理
 * 4.授信额度
 * 5.授信方案
 * 6.授信执行
 * 7.信贷核算
 * 8.担保管理
 * 9.押品管理
 * 10.贷后管理
 * 11.统计查询
 * 12.风险资产管理
 * 13.系统管理
 * 14.信贷大数据应用
 * 
 * a.技术类
 * b.业务类
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
			System.out.println("请输入操作类型：\r\n1.系统主界面及工作台、2.信贷政策管理、3.客户管理、4.授信额度、5.授信方案、6.授信执行、7.信贷核算\r\n"
					+ "8.担保管理、9.押品管理、10.贷后管理、11.统计查询、12.风险资产管理、13.系统管理、14.信贷大数据应用\r\n"
					+ "a.技术类、b.业务类");
			String msg = sc.nextLine();
			ArrayList<Integer> arrList = new ArrayList<Integer>(arr.length);
			Collections.addAll(arrList, arr);
			ArrayList<Character> charList = new ArrayList<Character>();
			Collections.addAll(charList, chars);
			
			char[] cs = msg.toCharArray();
			int len = cs.length;
			if(len < 2) {
				System.err.println("输入格式不正确！");
				continue;
			}
			
			String pre = "";
			String mid = "";
			char op = cs[cs.length - 1];
			if(!charList.contains(op)) {
				System.err.println("输入格式不正确！");
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
				System.err.println("输入格式不正确！");
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
			System.out.println("编号结果：" + value);
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
		System.out.println("获得编号：" + value);
	}

	private static void getTime() {
		System.out.println("当前时间：" + new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
	}
}
