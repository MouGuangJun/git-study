package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Test {
	public static void main(String[] args) throws Exception {
		Connection conn = JDBCHelper.getConnection();
		PreparedStatement prep = conn.prepareStatement("select SortNo from CODE_LIBRARY where CodeNo='AreaCode' and length(SortNo)=4");
		ResultSet rs = prep.executeQuery();
		while(rs.next()) {
			String sortNo = rs.getString("SortNo");
			PreparedStatement preps = conn.prepareStatement("select ItemNo from CODE_LIBRARY where CodeNo='AreaCode' and SortNo like ?");
			preps.setString(1, sortNo + "%");
			ResultSet rss = preps.executeQuery();
			int count = 0;
			String itemNo = "";
			while (rss.next()) {
				count++;
				itemNo = rss.getString("ItemNo");
			}
			
			if (count < 2) {
				System.out.println(itemNo);
			}
			
			JDBCHelper.clear(rss);
			JDBCHelper.clear(preps);
		}
		
		JDBCHelper.close(rs, prep, conn);
	}
}
