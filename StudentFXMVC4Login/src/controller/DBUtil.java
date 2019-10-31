package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;



//드라이버를 적재, 아이디 패스워드 mysql 데이타베이스 연결요청->객체참조변수
public class DBUtil {
	//1. 드라이버명 저장
	private static String driver = "com.mysql.jdbc.Driver";
	//2. 데이타베이스 url 저장
	private static String url ="jdbc:mysql://localhost/studentDB";
	
	//2. 드라이버를 적재하고, 데이타베이스를 연결하는 함수
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		//1.드라이버를 적재
		Class.forName(driver);
		//2. 데이타베이스 연결
		Connection con=DriverManager.getConnection(url, "root", "123456");
		return con;
	}
	
	public static void alertDisplay(int type, String title, String headerText, String contentText) {
		Alert alert = null;
		switch (type) {
		case 1:
			alert = new Alert(AlertType.WARNING);
			break;
		case 2:
			alert = new Alert(AlertType.CONFIRMATION);
			break;
		case 3:
			alert = new Alert(AlertType.ERROR);
			break;
		case 4:
			alert = new Alert(AlertType.NONE);
			break;
		case 5:
			alert = new Alert(AlertType.INFORMATION);
			break;
		}
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(headerText + "\n" + contentText);
		alert.setResizable(false);
		alert.show();
	}

}







