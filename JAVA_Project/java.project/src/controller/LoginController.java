package controller;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.DBUtilDAO;
import model.DBVO;

public class LoginController implements Initializable {
	@FXML
	private TextField txtField; // ���̵� �Է�â
	@FXML
	private PasswordField passwordField; // ��й�ȣ �Է�â
	@FXML
	private Button btnOk;
	@FXML
	private Button btnExit;
	@FXML
	private Button btnSignUP;
	@FXML
	private Button btnMaster;
	@FXML
	private Button btnExp1;

	private DBUtilDAO dbUtilDAO;
	ArrayList<DBVO> data = new ArrayList<DBVO>();
	private boolean flagCheckID = false;
	private DBVO dbVO;
	private DBVO saveDBVO;
	private boolean flagLogin = false;

	@FXML
	private Button btMaster;

	public static String clientId = null;

	String str;
	
	/******************************
	 * 
	 *  �α��� â ȭ��
	 * 
	 *  ������
	 *  
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// �׸� �������� ������ ���
		btnMaster.setOnAction((event) -> {
			Parent adminView = null;
			Stage adminStage = null;

			try {
				adminView = FXMLLoader.load(getClass().getResource("/view/adminlogin.fxml"));
				Scene scene = new Scene(adminView);
				adminStage = new Stage();
				adminStage.setTitle("Admin");
				adminStage.setScene(scene);
				adminStage.setResizable(true);
				adminStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
		//������ ��ư �������� 
	      btnExp1.setOnAction(event -> {
	         try {
	            Parent dialogView = FXMLLoader.load(getClass().getResource("/view/explain.fxml"));
	            Stage explainStage = new Stage(StageStyle.UTILITY);
	            explainStage.initModality(Modality.WINDOW_MODAL);
	            explainStage.initOwner(btnOk.getScene().getWindow());
	            explainStage.setTitle("���� â");
	            Scene scene = new Scene(dialogView);
	            
	            TextArea textArea = (TextArea)dialogView.lookup("#textArea");
	            
	            textArea.setEditable(false);
	            
	            explainStage.setScene(scene);
	            explainStage.setResizable(false);
	            explainStage.show();
	            
	         } catch (IOException e1) {
	            // TODO Auto-generated catch block
	            e1.printStackTrace();
	         }
	         
	      });

		// �α��� ��ư ��������
		btnOk.setOnAction(e -> {
			totalList(); // ��� ������ �����´�.

			// ���̵� ��й�ȣ ���Է½�
			if (txtField.getText().equals("") || passwordField.getText().equals("")) {
				alertDisplay(1, "�α��ν���", "���̵�,�н����� ���Է�", "�Է����ּ���");
				return;
			}

			boolean nametest = false;

			for (int i = 0; i < data.size(); i++) {

				nametest = txtField.getText().equals(data.get(i).getTxtId());

				if (txtField.getText().equals(data.get(i).getTxtId())) { // DB���ִ� ���̵�� �α��� ���̵�â�� ��ġ�ϸ� ����
					if (!(passwordField.getText().equals(data.get(i).getTxtFiled()))) {
						alertDisplay(1, "�����", "���̵� Ȥ�� ��й�ȣ ����ġ", "�ٽ� �Է¹ٶ��ϴ�.");
						return;
					}

					Parent mainView = null;
					Stage mainStage = null;

					try {
						clientId = txtField.getText();
						mainView = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
						Scene scene = new Scene(mainView);
						mainStage = new Stage();
						mainStage.setTitle("new");
						mainStage.setScene(scene);
						mainStage.setResizable(true);
						mainStage.show();
						break;

					} catch (Exception e1) {

						alertDisplay(1, "����â �ݽ���", "����â �θ��� ����", e.toString() + e1.getMessage());
					}

				}
			} // end of for
			if (!nametest) {
				alertDisplay(1, "�����", "��ϵ��� ���� ���̵��Դϴ�", "ȸ�������� ���ּ���");
			}
		});// end of login Button

		// ��� ��ư
		btnExit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Platform.exit();
			}
		});

		// ȸ������ ��ư ������ ���
		btnSignUP.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				try {
					Parent dialogView = FXMLLoader.load(getClass().getResource("/view/signup.fxml"));
					Stage mainStage2 = new Stage(StageStyle.UTILITY);
					mainStage2.initModality(Modality.WINDOW_MODAL);
					mainStage2.initOwner(btnOk.getScene().getWindow());
					mainStage2.setTitle("ȸ������");
					TextField txtId = (TextField) dialogView.lookup("#txtId");
					PasswordField txtField = (PasswordField) dialogView.lookup("#txtField");
					PasswordField txtField2 = (PasswordField) dialogView.lookup("#txtField2");
					TextField txtName = (TextField) dialogView.lookup("#txtName");
					TextField frontN = (TextField) dialogView.lookup("#frontN");
					TextField backN = (TextField) dialogView.lookup("#backN");
					ComboBox comboFM = (ComboBox) dialogView.lookup("#comboFM");
					TextField phoneNum = (TextField) dialogView.lookup("#phoneNum");
					TextField enterpriseName = (TextField) dialogView.lookup("#tfEnterpriseName");

					Button overlap = (Button) dialogView.lookup("#overlap");
					Button btnUP = (Button) dialogView.lookup("#btnUP");
					Button btnClose = (Button) dialogView.lookup("#btnClose");

					btnUP.setDisable(true);

					// �ߺ�Ȯ�ι�ư
					overlap.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {

							// �ƹ��͵� �Է��� ��������
							if (txtId.getText().equals("") != false) {
								alertDisplay(1, "�����", "���̵� �Է����ּ���", "���̵� �Է��� �ȉ���ϴ�.");
							}
							DBUtilDAO tt = new DBUtilDAO();
							ArrayList<DBVO> test = tt.getUserIdtest(txtId.getText());
							if (test.size() == 1) {
								alertDisplay(1, "�ߺ��ߺ�", "�̹� ������� ���̵� �Դϴ�", "�ٸ����̵� �Է¹ٶ�");
							} else {
								alertDisplay(2, "���ɰ���", "��밡���� ���̵��Դϴ�.", "��밡�� ��밡��");
								btnUP.setDisable(false);
							}

						}

					});

					DecimalFormat format = new DecimalFormat("######");
					// �ֹ� ���ڸ� ����
					frontN.setTextFormatter(new TextFormatter<>(e2 -> {
						if (e2.getControlNewText().isEmpty()) {
							return e2;
						}
						ParsePosition parsePosition = new ParsePosition(0);
						Object object = format.parse(e2.getControlNewText(), parsePosition);
						if (object == null || parsePosition.getIndex() < e2.getControlNewText().length()
								|| e2.getControlNewText().length() == 7) {
							return null;
						} else {
							return e2;
						}
					}));
					// �ֹ� ���ڸ� ����
					backN.setTextFormatter(new TextFormatter<>(e3 -> {
						if (e3.getControlNewText().isEmpty()) {
							return e3;
						}
						ParsePosition parsePosition = new ParsePosition(0);
						Object object = format.parse(e3.getControlNewText(), parsePosition);
						if (object == null || parsePosition.getIndex() < e3.getControlNewText().length()
								|| e3.getControlNewText().length() == 8) {
							return null;
						} else {
							return e3;
						}
					}));
					// ����ȣ ���ڸ�
					phoneNum.setTextFormatter(new TextFormatter<>(e3 -> {
						if (e3.getControlNewText().isEmpty()) {
							return e3;
						}
						ParsePosition parsePosition = new ParsePosition(0);
						Object object = format.parse(e3.getControlNewText(), parsePosition);
						if (object == null || parsePosition.getIndex() < e3.getControlNewText().length()
								|| e3.getControlNewText().length() == 12) {
							return null;
						} else {
							return e3;
						}
					}));

					// �޺��ڽ� ���ڿ��� �׸� �߰�
					ObservableList MF = FXCollections.observableArrayList();
					MF.add(0, "����");
					MF.add(1, "����");
					comboFM.setValue("����");
					comboFM.setItems(MF);

					// ȸ������â���� ��Ϲ�ư ��������
					btnUP.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							if (!(txtId.getText().equals("") || txtField.getText().equals("")
									|| txtField2.getText().equals("") || txtName.getText().equals("")
									|| frontN.getText().equals("") || backN.getText().equals("")
									|| phoneNum.getText().equals("") || enterpriseName.getText().equals(""))) {

								// || midN.getText().equals("") || lastN.getText().equals("")

								if (checkbackN(Integer.parseInt(backN.getText()))) {
									alertDisplay(1, "�����", "�̹� ������ �ֹι�ȣ�Դϴ�", "�ٽ� �Է����ּ���.");
									return;
								} else if (txtField.getText().equals(txtField2.getText())) {

									DBVO dvo = new DBVO(txtId.getText(), txtField.getText(), txtField2.getText(),
											txtName.getText(), Integer.parseInt(frontN.getText()),
											Integer.parseInt(backN.getText()),
											comboFM.getSelectionModel().getSelectedItem().toString(),
											phoneNum.getText(), enterpriseName.getText());
									// , Integer.parseInt(midN.getText()),Integer.parseInt(lastN.getText())

									dbUtilDAO = new DBUtilDAO();
									dbUtilDAO.insertClient(dvo); // ���
									alertDisplay(1, "���ԿϷ�", "������ �Ϸ�Ǿ����ϴ�", "�����մϴ�");
									mainStage2.close();
								} else {
									alertDisplay(1, "�����", "��й�ȣ�� Ȯ�����ּ���", "��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
								}

							} else {
								alertDisplay(1, "�����", "��� �Է����ּ���", "��� �Է� �ٶ��ϴ�.");
								return;

							}
						}
					});
					// ȸ������â���� ��� ��������
					btnClose.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							mainStage2.close();
						}
					});

					Scene scene = new Scene(dialogView);
					mainStage2.setScene(scene);
					mainStage2.setResizable(false);
					mainStage2.show();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	// ���â
	protected void alertDisplay(int type, String title, String headerText, String contentText) {
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

	// ȸ������DB���ִ� ���� ��������
	public void totalList() {

		DBUtilDAO dbUtilDAO = new DBUtilDAO();

		data = dbUtilDAO.getCustomerTotal();
		System.out.println(data.size());
	}

	// ���̵� �ߺ�Ȯ��
	public boolean checkID(String id) {
		totalList();
		System.out.println(data.size());
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getTxtId().equals(id)) {
				flagCheckID = false;
				System.out.println(data.get(i).getTxtId());
				return flagCheckID;
			} else {
				flagCheckID = true;
			}
		}
		System.out.println(data);
		return flagCheckID;

	}

	// �ֹ� ���ڸ� �ߺ�Ȯ��
	public boolean checkbackN(int backN) {
		totalList();
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getBackN() == backN) {
				flagCheckID = true;
				return flagCheckID;
			} else {
				flagCheckID = false;
			}
		}
		return flagCheckID;
	}

}