package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.DBUtilDAO;
import model.DBVO;
import util.AlertManager;
import util.AlertManager.AlertInfo;
import util.DBUtil;

public class AdminMainController implements Initializable {

	@FXML
	private TableView<DBVO> tableView;
	@FXML
	private ImageView imageView;
	@FXML
	private Button btnRevise;
	@FXML
	private Button btnSearch;
	@FXML
	private Button btnDelete1;
	@FXML
	private TextField adminTF;
	@FXML
	private Button btnTotal;
	@FXML
	private Button btnExit;
	
	private int selectedIndex;
	private ObservableList<DBVO> selectClient;
	private String selectFileName = ""; // �̹��� ���ϸ�
	private String localUrl = ""; // �̹��� ���� ���
	private Image localImage;
	private File selectedFile = null;
	// �̹��� ó��
	// �̹��� ������ ������ �Ű������� ���� ��ü ����
	private File dirSave = new File("C:\\Users\\user\\Desktop\\image\\");
	// �̹��� �ҷ��� ������ ������ ���� ��ü ����
	private File file = null;

	ObservableList<DBVO> data = FXCollections.observableArrayList();
	DBUtilDAO dbUtilDAO = new DBUtilDAO();
	// private boolean delete = false;

	// tableView select ����
	private ObservableList<DBVO> selectDBVO = FXCollections.observableArrayList();
	private int selectDBVOIndex = 0;
	/***********************
	 * 
	 * ������ â
	 * 
	 * ������
	 * 
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tableViewSetting();

		// ���̺��� �׸� ��������
		tableView.setOnMousePressed(event -> {
			handlerTableViewMousePressedAction(event);
		});
		// ������ư ��������
		btnRevise.setOnAction(event -> {
			handlerButtonReviseAction(event);

		});
		// �˻���ư ��������
		btnSearch.setOnAction(event -> {
			try {
				ArrayList<DBVO> list = new ArrayList<DBVO>();
				DBUtilDAO dbUtilDAO = new DBUtilDAO();
				list = dbUtilDAO.getCustomerCheck(adminTF.getText().trim());

				if (list == null) {
					throw new Exception("�˻�����");
				}

				data.removeAll(data);
				for (DBVO svo : list) {
					data.add(svo);
				}
			} catch (Exception e1) {
				alertDisplay(1, "�˻����", "�̸��˻�����", e1.toString());
			}
		});

		// ��ü ��ư ��������
		btnTotal.setOnAction(event -> {
			dbUtilDAO = new DBUtilDAO();
			System.out.println("������");
			data.removeAll(data);

			totalList();

		});
		
		btnExit.setOnAction(e -> {
			Platform.exit();
		});
		
		
	      
		// ���� ��ư ��������
		btnDelete1.setOnAction(event -> {
			try {
				DBUtilDAO dbUtilDAO = new DBUtilDAO();
				dbUtilDAO.getCustomerDelete(selectDBVO.get(0).getTxtId());
				data.removeAll(data);
				totalList();
			} catch (Exception e1) {
				AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DELETE, null);
			}
		});

	}

	// adminMain���� ������ư ��������
	private void handlerButtonReviseAction(ActionEvent event) {
		try {

			Parent dialogReviseroot = FXMLLoader.load(getClass().getResource("/view/adminrevise.fxml"));
			Stage reviseStage = new Stage(StageStyle.UTILITY);
			reviseStage.initModality(Modality.WINDOW_MODAL);
			reviseStage.initOwner(btnRevise.getScene().getWindow());
			reviseStage.setTitle("��������");

			TextField reviseId = (TextField) dialogReviseroot.lookup("#reviseId");
			TextField reviseName = (TextField) dialogReviseroot.lookup("#reviseName");
			TextField reviseNum = (TextField) dialogReviseroot.lookup("#reviseNum");
			TextField reviseEnterprise = (TextField) dialogReviseroot.lookup("#reviseEnterprise");

			Button btnReviseAdd = (Button) dialogReviseroot.lookup("#btnReviseAdd");
			Button btnReviseExit = (Button) dialogReviseroot.lookup("#btnReviseExit");
			Button btnImageAdd = (Button) dialogReviseroot.lookup("#btnImageAdd");

			ImageView reviseImage = (ImageView) dialogReviseroot.lookup("#reviseImage");

			// ���� â���̵�
			reviseId.setPromptText(selectDBVO.get(0).getTxtId()); // ������� �ʵ� ����
			reviseId.setEditable(false); // �۾� ������
			reviseId.setDisable(true); // â�� Ȱ��ȭ�� �Ƚ�Ŵ

			reviseName.setText(selectDBVO.get(0).getTxtName());
			reviseNum.setText(selectDBVO.get(0).getPhoneNum());
			reviseEnterprise.setText(selectDBVO.get(0).getEnterpriseName());

			// ���� â �̹����߰� ��ư ��������
			btnImageAdd.setOnAction(event2 -> {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("�̹����߰�");
				fileChooser.setInitialDirectory(new File("C:\\Users\\user\\Desktop\\image\\")); // ���丮 ��ġ�� ����
				ExtensionFilter imageType = new ExtensionFilter("image file", "*.jpg", "*.png", "*.gif"); // Ÿ���� ����
				fileChooser.getExtensionFilters().add(imageType); // Ÿ���� �־��ִ°�

				try {
					selectedFile = fileChooser.showOpenDialog(btnRevise.getScene().getWindow()); // �����༭ �� ���� ����

					if (selectedFile != null) {
						// �̹��� ���� ���
						localUrl = selectedFile.toURI().toURL().toString();
					}
				} catch (MalformedURLException e2) {
					AlertManager.getInstance().show(AlertInfo.FAIL_LOAD_IMAGE, null);
					e2.printStackTrace();
				}
				localImage = new Image(localUrl, false);
				imageView.setImage(localImage);
				imageView.setFitHeight(250);
				imageView.setFitWidth(230);
				reviseImage.setImage(localImage);
				if (selectedFile != null) {
					selectFileName = selectedFile.getName();
				}
			});

			// ���� â ��Ϲ�ư ������ ��
			btnReviseAdd.setOnAction(event3 -> {
				try {
					if (reviseName.getText().equals("") || reviseNum.getText().equals("")
							|| reviseEnterprise.getText().equals("")) {
						alertDisplay(1, "�Է��Է�", "���� ���� �Է��Ͻÿ�", "�Է¿��.");
						return;
					}

					File dirMake = new File(dirSave.getAbsolutePath());
					// �̹��� ���� ���� ����
					if (!dirMake.exists()) {
						dirMake.mkdir();
					}
					// �̹��� ���� ����
					String fileName = imageSave(selectedFile);
					DBVO dvo = new DBVO(selectDBVO.get(0).getTxtId(), reviseName.getText().trim(),
							reviseNum.getText().trim(), reviseEnterprise.getText().trim(), fileName);
					data.remove(selectDBVOIndex);
					data.add(selectDBVOIndex, dvo);

					DBUtilDAO dbUtilDAO = new DBUtilDAO();
					DBVO dbVO = dbUtilDAO.getCustomerUpdate(dvo);
					reviseStage.close();

				} catch (Exception e2) {
					e2.printStackTrace();
					AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);
					
				}
			});
			// ��ҹ�ư�� ��������
			btnReviseExit.setOnAction(e3 -> {
				reviseStage.close();
			});

			Scene scene = new Scene(dialogReviseroot);
			reviseStage.setScene(scene);
			reviseStage.show();

		} catch (IOException e) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);
			e.printStackTrace();
		}

	}
	//�̹��� ����
	public String imageSave(File file) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		int data = -1;
		String fileName = null;
		try {
			// �̹��� ���ϸ� ����
			fileName = "cusotomer" + System.currentTimeMillis() + "_" + file.getName();
			bis = new BufferedInputStream(new FileInputStream(file));
			bos = new BufferedOutputStream(new FileOutputStream(dirSave.getAbsolutePath() + "//" + fileName));

			// ������ �̹��� ���� InputStream�� �������� �̸����� ���� -1
			while ((data = bis.read()) != -1) {
				bos.write(data);
				bos.flush();
			}
		} catch (Exception e) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);
				e.getMessage();
			}
		}
		return fileName;
	}

	// ���̺� �׸� ��������
	private void handlerTableViewMousePressedAction(MouseEvent event) {

		selectDBVOIndex = tableView.getSelectionModel().getSelectedIndex();
		selectDBVO = tableView.getSelectionModel().getSelectedItems();

		String fileName = selectDBVO.get(0).getFileName();
		selectedFile = new File("C:\\Users\\user\\Desktop\\image\\" + fileName);
		if (selectedFile != null) {
			// �̹��� ���� ���
			try {
				localUrl = selectedFile.toURI().toURL().toString();
			} catch (MalformedURLException e) {
				AlertManager.getInstance().show(AlertInfo.FAIL_LOAD_IMAGE, null);
				e.printStackTrace();
			}
			localImage = new Image(localUrl, false);
			imageView.setImage(localImage);

		}

	}

	// �÷�����
	private void tableViewSetting() {

		// �÷�����-------------------------------------------------------------------------------------

		TableColumn colId = new TableColumn("���̵�");
		colId.setMaxWidth(70);
		colId.setStyle("-fx-alignment: CENTER;");
		colId.setCellValueFactory(new PropertyValueFactory("txtId"));

		TableColumn colName = new TableColumn("ȸ����");
		colName.setMaxWidth(70);
		colName.setStyle("-fx-alignment: CENTER;");
		colName.setCellValueFactory(new PropertyValueFactory("txtName"));

		TableColumn colNum = new TableColumn("��ȭ��ȣ");
		colNum.setMaxWidth(150);
		colNum.setStyle("-fx-alignment: CENTER;");
		colNum.setCellValueFactory(new PropertyValueFactory("phoneNum"));

		TableColumn colCom = new TableColumn("��ü��");
		colCom.setMaxWidth(150);
		colCom.setStyle("-fx-alignment: CENTER;");
		colCom.setCellValueFactory(new PropertyValueFactory("enterpriseName"));

		TableColumn colFile = new TableColumn("�̹���");
		colFile.setMaxWidth(150);
		colFile.setStyle("-fx-alignment: CENTER;");
		colFile.setCellValueFactory(new PropertyValueFactory("fileName"));

		// 2. ���̺����� �÷��� ��ü�� ���̺��信 ����Ʈ�߰� �� �׸��߰�

		totalList();
		tableView.setItems(data);

		tableView.getColumns().addAll(colId, colName, colNum, colCom, colFile);
		// ------------------------------------------------------------------------------------------------------
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

	public void totalList() {
		ArrayList<DBVO> list = null;
		DBUtilDAO dbUtilDAO = new DBUtilDAO();
		DBVO dbVO = null;
		list = dbUtilDAO.getCusTotal();
		if (list == null) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			dbVO = list.get(i);
			data.add(dbVO);
		}
	}
}
