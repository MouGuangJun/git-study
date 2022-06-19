import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;

public class MakeSerialNo {
	public static final String ANY_KEY = "A";
	public static final String ANY_VALUE = "Any";
	public static final String STR_KEY = "Str";
	public static final String PATH = "serialno.properties";
	public static final String NUMBER_ZERO = "0";
	public static final String NUMBER_ONE = "1";

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		while (true) {
			String chose = "";
			Map<String, String> loadMap = null;
			String tableName = "";
			String prefix = "";
			BigDecimal value = null;
			do {
				System.out.println("请选择[输入A进行任意输入]：");
				loadMap = loadFile();
				System.out.println(loadMap.get(STR_KEY));
				chose = sc.nextLine().trim();
				boolean isContinue = true;
				if (ANY_KEY.equalsIgnoreCase(chose)) {
					System.out.println("请输入表名：");
					tableName = sc.nextLine().trim();
					if (!loadMap.values().contains(tableName)) {
						System.out.println("请输入前缀：");
						prefix = sc.nextLine().trim();
						value = new BigDecimal(NUMBER_ONE);
						isContinue = false;
					} else {
						Iterator<Entry<String, String>> iter = loadMap.entrySet().iterator();
						while (iter.hasNext()) {
							Entry<String, String> entry = iter.next();
							if (entry.getValue().equals(tableName)) {
								chose = entry.getKey();
								break;
							}
						}
					}
				}

				if (isContinue) {
					tableName = loadMap.get(chose);
					if (null != tableName) {
						char[] cs = PropUtil.getValue(tableName, PATH).toCharArray();
						int len = cs.length;
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < len - 16; i++) {
							sb.append(cs[i]);
						}
						prefix = sb.toString();

						sb.delete(0, sb.length());

						for (int i = len - 8; i < cs.length; i++) {
							sb.append(cs[i]);
						}

						value = new BigDecimal(sb.toString()).add(new BigDecimal(NUMBER_ONE));
					}
				}
			} while (loadMap.get(chose) == null && !ANY_KEY.equalsIgnoreCase(chose));

			String result = executeUpdate(tableName, prefix, value);
			System.out.println("获取主键流水号成功：" + result);
		}
	}

	private static Map<String, String> loadFile() throws Exception {
		HashMap<String, String> retMap = new HashMap<String, String>();
		FileInputStream fis = new FileInputStream(new File(PATH));
		Properties prop = new Properties();
		prop.load(fis);
		fis.close();
		StringBuilder sb = new StringBuilder();
		StringBuilder sbt = new StringBuilder();
		Iterator<Entry<Object, Object>> iter = prop.entrySet().iterator();
		int count = 1;
		while (iter.hasNext()) {
			Entry<Object, Object> entry = iter.next();
			sb.append(count).append(".");
			sb.append(entry.getKey());
			sbt.append(count).append(".");
			sbt.append(entry.getKey());
			retMap.put(String.valueOf(count), (String) entry.getKey());

			sb.append("    ");
			sbt.append("    ");

			if (sb.length() >= 100) {
				sbt.append('\n');
				sb.delete(0, sb.length());
			}

			count++;
		}

		retMap.put(ANY_KEY, ANY_VALUE);
		retMap.put(STR_KEY, sbt.toString());

		return retMap;
	}

	private static String executeUpdate(String tableName, String prefix, BigDecimal value) {
		StringBuilder sb = new StringBuilder();
		sb.append(prefix);
		sb.append(new SimpleDateFormat("yyyyMMdd").format(new Date()));
		int len = value.toString().length();
		for (int i = 0; i < 8 - len; i++) {
			sb.append(NUMBER_ZERO);
		}

		sb.append(value);

		PropUtil.setValue(tableName, sb.toString(), PATH);

		return sb.toString();
	}
}
