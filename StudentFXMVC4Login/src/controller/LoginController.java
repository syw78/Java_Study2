package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable {
	@FXML	private TextField txtId;
	@FXML	private PasswordField txtPassword;
	@FXML	private Button btnLogin;
	@FXML	private Button btnCancel;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//1. 버튼확인 이벤트 처리
		btnLogin.setOnAction( e-> {	handlerBtnLoginAction(e); });
		//2. 버튼취소 이벤트처리
		btnCancel.setOnAction( e-> {	handlerBtnCancelAction(e); });
		//테스트용으로 작업하기
		txtId.setText("admin");
		txtPassword.setText("1234");

	}
	//1. 버튼확인 이벤트 처리
	public void handlerBtnLoginAction(ActionEvent event) {
		//1. 아이디와 패스워드가 입력안되었을때 경고창을 준다.
		if( txtId.getText().equals("") || txtPassword.getText().equals("")) {
			alertDisplay(1,"로그인 실패","아이디, 패스워드 미입력","다시 제대로 입력하시오");
		//2. 아이디와 패스워드가 올바르게 입력했을때 경고창을 준다.
		}else if(txtId.getText().equals("admin") && txtPassword.getText().equals("1234") ) {
			//로그인이 완료되었으면 다음 메인창으로 이동한다. 
			Parent mainView=null;
			Parent mainView1=null;
			Stage mainStage=null;
			
			try {
//				mainView=FXMLLoader.load(getClass().getResource("/view/main.fxml"));
				mainView=FXMLLoader.load(getClass().getResource("/view/view.fxml"));
				Scene scene = new Scene(mainView);
				mainStage = new Stage();
				mainStage.setTitle("Main Window");
				mainStage.setScene(scene);
				mainStage.setResizable(true);
				//현재의 스테이지를 닫고 새로운창을 연다. 
				((Stage) btnLogin.getScene().getWindow()).close();
				mainStage.show();
			} catch (Exception e) {
				alertDisplay(1,"메인창 콜실패","메인창 부르기 실패", e.toString()+e.getMessage());
			}
			
		//3. 아이디와 패스워드가 일치하지 않았을때 경고창을 준다. 
		}else {
			alertDisplay(1,"로그인 실패","아이디, 패스워드 불일치","다시 제대로 입력하시오");
		}
		
	}
	//2. 버튼취소 이벤트처리
	public void handlerBtnCancelAction(ActionEvent e) {
		((Stage) btnLogin.getScene().getWindow()).close();
	}
	//3. 경고창 처리하는 함수
	private void alertDisplay(int type, String title, String headerText, String contentText) {
		Alert alert = null;
		switch(type){
		case  1:   alert = new Alert(AlertType.WARNING); break;
		case  2:   alert = new Alert(AlertType.CONFIRMATION);break;
		case  3:   alert = new Alert(AlertType.ERROR);break;
		case  4:   alert = new Alert(AlertType.NONE);break;
		case  5:   alert = new Alert(AlertType.INFORMATION);break;
		}
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(headerText+"\n"+contentText);
		alert.setResizable(false);
		alert.show();
	}
}
