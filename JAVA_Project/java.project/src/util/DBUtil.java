package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	
	private static String url = "jdbc:mysql://localhost/management";

	public static Connection getConnection() throws SQLException {
		Connection connection = DriverManager.getConnection(url, "root", "123456");
		return connection;
		
	}
	
}
