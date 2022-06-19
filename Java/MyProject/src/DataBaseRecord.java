import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DataBaseRecord {
	static HashMap<String, String> jbdbMap1 = new HashMap<String, String>();
	static HashMap<String, String> jbdbMap2 = new HashMap<String, String>();
	static HashMap<String, String> jbdbMap3 = new HashMap<String, String>();
	static {
		jbdbMap1.put("url", "jdbc:oracle:thin:@192.168.30.49:1521/pdb1");
		jbdbMap1.put("username", "xdcredit");
		jbdbMap1.put("password", "xdcredit");
		
		jbdbMap2.put("url", "jdbc:oracle:thin:@192.168.30.49:1521/pdb1");
		jbdbMap2.put("username", "clcredit");
		jbdbMap2.put("password", "clcredit");
		
		jbdbMap3.put("url", "jdbc:oracle:thin:@192.168.30.49:1521/pdb1");
		jbdbMap3.put("username", "collcredit");
		jbdbMap3.put("password", "collcredit");
	}
	
	public static void main(String[] args) throws Exception {
		getDBResult(jbdbMap1);//信贷数据库
		//getDBResult(jbdbMap2);//额度数据库
		//getDBResult(jbdbMap3);//押品数据库
	}

	private static void getDBResult(HashMap<String, String> jbdbMap) throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		String url = jbdbMap.get("url");
		String username = jbdbMap.get("username");
		String password = jbdbMap.get("password");
		Connection conn = DriverManager.getConnection(url, username, password);
		
		String fileName = "db.properties";
		File file = new File(fileName);
		if(!file.exists()) throw new Exception("没有读到配置文件！");
		LinkedHashMap<String, ArrayList<String>> groupMap = new LinkedHashMap<String, ArrayList<String>>();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		String s = null;
		while((s = br.readLine()) != null) {
			if(s.startsWith("--")) continue;
			if("".equals(s.trim())) continue;
			String[] cs = s.split("=");
			String colName = cs[0].trim();
			String tableName = cs[1].trim();
			String exContent = "";//额外内容
			if(cs.length == 3) exContent = cs[2]; 
			if(groupMap.get(tableName) != null) {
				groupMap.get(tableName).add(colName + "~" + exContent);
			}else {
				ArrayList<String> colList = new ArrayList<String>();
				colList.add(colName + "~" + exContent);
				groupMap.put(tableName, colList);
			}
		}
		
		if(br != null) br.close();
		
		StringBuffer sb = getResultMap(groupMap, conn);
		File outFile = new File("result.properties");
		if(!outFile.exists()) outFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(outFile);
		fos.write(sb.toString().getBytes("UTF-8"));
		if(null != fos) fos.flush();
		if(null != fos) fos.close();
		
		if(null != conn) conn.close();
		System.out.println("操作成功，请查看result.properties文件！");
	}

	private static StringBuffer getResultMap(LinkedHashMap<String, ArrayList<String>> groupMap, Connection conn) throws Exception {
		Iterator<Entry<String, ArrayList<String>>> iter = groupMap.entrySet().iterator();
		StringBuffer sb = new StringBuffer();
		while(iter.hasNext()) {
			HashMap<String, Integer> resultMap = new HashMap<String, Integer>();
			Entry<String, ArrayList<String>> entry = iter.next();
			String tableName = entry.getKey();
			ArrayList<String> colList = entry.getValue();
			PreparedStatement pp = conn.prepareStatement("select * from " + tableName + " where 1 = 2");
			ResultSet rs = pp.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int colum = metaData.getColumnCount();
			for (String colDetail : colList) {
				String[] cs = colDetail.split("~");
				String colName = cs[0];//字段真实名称
				String exContent = "";
				if(cs.length == 2) exContent = cs[1];//额外的内容
				String[] es = exContent.split("@");
				StringBuilder sf = new StringBuilder();
				for (String ex : es) {
					sf.append(ex.trim()).append("====================");
				}
				A:for (int i = 1; i <= colum; i++) {
					String columName = metaData.getColumnName(i);
					if(colName.equalsIgnoreCase(columName)) {
						resultMap.put(colName + "====================" + sf.toString(), i);
						break A;
					}
					
					if(i == colum && !colName.equalsIgnoreCase(columName)) {
						//throw new Exception("字段[" + colName + "]在表[" + tableName + "]中不存在，请确认！");
						System.err.println("字段[" + colName + "]在表[" + tableName + "]中不存在，请确认！");
					}
				}
			}
			
			List<Map.Entry<String, Integer>> listEntry = new ArrayList<>();
			listEntry.addAll(resultMap.entrySet());
			
			Collections.sort(listEntry, new Comparator<Entry<String, Integer>>() {
				@Override
				public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
					return o1.getValue().compareTo(o2.getValue());
				}
			});
			
			for (Entry<String, Integer> ele : listEntry) {
				sb.append(tableName.toUpperCase()).append("====================").append(ele.getKey().toUpperCase()).append("\r\n");
			}
			
			if(rs != null) rs.close();
			if(pp != null) pp.close();
		}
		
		return sb;
	}
}
