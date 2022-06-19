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
	 * 获取连接
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
	 * 加载配置信息（私有方法）
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
	 * 关闭连接
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
	 * 释放预处理语句
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
	 * 关闭结果集
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
	 * 关闭结果集、释放预处理语句、关闭连接
	 */
	public static void close(ResultSet rs, PreparedStatement prep, Connection conn) {
		clear(rs);
		clear(prep);
		clear(conn);
	} 
}
