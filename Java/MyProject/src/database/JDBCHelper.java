package database;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class JDBCHelper {
	
	/**
	 * ��ȡ����
	 */
	public static Connection getConnection() {
		Properties p = null;
		Connection conn = null;
		try {
			p = loadConfig();
			//Class.forName(p.getProperty("driver"));
			conn = DriverManager.getConnection(p.getProperty("url"), p.getProperty("user"), p.getProperty("password"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return conn;
	}

	/**
	 * ����������Ϣ��˽�з�����
	 */
	private static Properties loadConfig() {
		Properties p = null;
		try {
			FileInputStream fis = new FileInputStream(new File("database.properties"));
			p = new Properties();
			p.load(fis);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return p;
	}
	
	/**
	 * �ر�����
	 */
	public static void clear(Connection conn) {
		try {
			if(null != conn && !conn.isClosed()) {
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �ͷ�Ԥ�������
	 */
	public static void clear(PreparedStatement prep) {
		try {
			if(null != prep && !prep.isClosed()) {
				prep.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �رս����
	 */
	public static void clear(ResultSet rs) {
		try {
			if(null != rs && !rs.isClosed()) {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �رս�������ͷ�Ԥ������䡢�ر�����
	 */
	public static void close(ResultSet rs, PreparedStatement prep, Connection conn) {
		clear(rs);
		clear(prep);
		clear(conn);
	} 
}
