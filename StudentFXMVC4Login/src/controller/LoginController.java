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
		//1. ��ưȮ�� �̺�Ʈ ó��
		btnLogin.setOnAction( e-> {	handlerBtnLoginAction(e); });
		//2. ��ư��� �̺�Ʈó��
		btnCancel.setOnAction( e-> {	handlerBtnCancelAction(e); });
		//�׽�Ʈ������ �۾��ϱ�
		txtId.setText("admin");
		txtPassword.setText("1234");

	}
	//1. ��ưȮ�� �̺�Ʈ ó��
	public void handlerBtnLoginAction(ActionEvent event) {
		//1. ���̵�� �н����尡 �Է¾ȵǾ����� ���â�� �ش�.
		if( txtId.getText().equals("") || txtPassword.getText().equals("")) {
			alertDisplay(1,"�α��� ����","���̵�, �н����� ���Է�","�ٽ� ����� �Է��Ͻÿ�");
		//2. ���̵�� �н����尡 �ùٸ��� �Է������� ���â�� �ش�.
		}else if(txtId.getText().equals("admin") && txtPassword.getText().equals("1234") ) {
			//�α����� �Ϸ�Ǿ����� ���� ����â���� �̵��Ѵ�. 
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
				//������ ���������� �ݰ� ���ο�â�� ����. 
				((Stage) btnLogin.getScene().getWindow()).close();
				mainStage.show();
			} catch (Exception e) {
				alertDisplay(1,"����â �ݽ���","����â �θ��� ����", e.toString()+e.getMessage());
			}
			
		//3. ���̵�� �н����尡 ��ġ���� �ʾ����� ���â�� �ش�. 
		}else {
			alertDisplay(1,"�α��� ����","���̵�, �н����� ����ġ","�ٽ� ����� �Է��Ͻÿ�");
		}
		
	}
	//2. ��ư��� �̺�Ʈó��
	public void handlerBtnCancelAction(ActionEvent e) {
		((Stage) btnLogin.getScene().getWindow()).close();
	}
	//3. ���â ó���ϴ� �Լ�
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
