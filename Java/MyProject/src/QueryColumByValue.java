import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.FileHelper;
import database.JDBCHelper;

public class QueryColumByValue {
	public static void main(String[] args) {
		String tableName = "";
		String columnName = "";
		try {
			String queryValue = "BusinessDueBill";
			Connection conn = JDBCHelper.getConnection();
			PreparedStatement pStatement = conn.prepareStatement(
					"select Table_Name, Column_Name from USER_TAB_COLUMNS where length(Column_Name)=lengthb(Column_Name)");
			ResultSet rs = pStatement.executeQuery();
			StringBuilder sbSql = new StringBuilder();
			StringBuilder sbOut = new StringBuilder();
			while (rs.next()) {
				try {
					tableName = rs.getString("Table_Name");
					columnName = rs.getString("Column_Name");
					sbSql.delete(0, sbSql.length());
					sbSql.append("select count(1) as Result from ").append(tableName).append(" where upper(")
							.append(columnName).append(")=").append("'").append(queryValue.toUpperCase()).append("'");
					PreparedStatement pp = conn.prepareStatement(sbSql.toString());
					ResultSet rsi = pp.executeQuery();
					int count = 0;
					if (rsi.next()) {
						count = rsi.getInt("Result");
					}

					if (null != rsi && !rsi.isClosed())
						rsi.close();
					if (null != pp && !pp.isClosed())
						pp.close();

					if (count > 0) {
						sbOut.delete(0, sbOut.length());
						sbOut.append(tableName).append("<==>").append("columnName").append("<==>").append(count);
						FileHelper.appendWithEnter("AllColumn.txt", sbOut.toString());
					}
				} catch (Exception e) {
					System.out.println(tableName + "===============>" + columnName + "#[Error]#" + e.getMessage());
				}
			}

			if (null != rs && !rs.isClosed())
				rs.close();
			if (null != pStatement && !pStatement.isClosed())
				pStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
