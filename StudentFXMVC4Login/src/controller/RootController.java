package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.time.LocalDate;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.StudentVO;

public class RootController implements Initializable {
	@FXML
	private TextField txtName;
	@FXML
	private ComboBox<String> cbYear;
//   @FXML private TextField txtLevel;
	@FXML
	private ToggleGroup genderGroup;
	@FXML
	private RadioButton rbMale;
	@FXML
	private RadioButton rbFemale;
	@FXML
	private TextField txtBan;
	@FXML
	private TextField txtKo;
	@FXML
	private TextField txtEng;
	@FXML
	private TextField txtMath;
	@FXML
	private TextField txtSic;
	@FXML
	private TextField txtSoc;
	@FXML
	private TextField txtMusic;
	@FXML
	private TextField txtTotal;
	@FXML
	private TextField txtAvg;
	@FXML
	private Button btnTotal;
	@FXML
	private Button btnAvg;
	@FXML
	private Button btnInit;
	@FXML
	private Button btnOk;
	@FXML
	private Button btnExit;
	@FXML
	private Button btnTotalList;
	// �˻�����߰�
	@FXML
	private TextField txtSearch;
	@FXML
	private Button btnSearch;
	// ����,������ư�߰�
	@FXML
	private Button btnEdit;
	@FXML
	private Button btnDelete;
	// ��íƮ ��ư�����߰�
	@FXML
	private Button btnBarChart;
	@FXML
	private DatePicker dpDate;
	@FXML
	private TextField txtDay;
	@FXML
	private HBox imageBox;
	@FXML
	private Button btnImageFile;
	@FXML
	private ImageView imageView;
	// �����Ҷ��� ��ư�������� ����
	private boolean editDelete = false;
	// ���̺�並 ���������� ��ġ���� ��ü���� �����Ҽ��ִ� ���� ����
	private int selectedIndex;
	private ObservableList<StudentVO> selectStudent;

	@FXML
	private TableView<StudentVO> tableView;
	ObservableList<StudentVO> data; // ���̺�信 �����ֱ����ؼ� ����� ����Ÿ
	private StudentDAO studentDAO;
	private String selectFileName = ""; // �̹��� ���ϸ�
	private String localUrl = ""; // �̹��� ���� ���
	private Image localImage;
	private File selectedFile = null;
	// �̹��� ó��
	// �̹��� ������ ������ �Ű������� ���� ��ü ����
	private File dirSave = new File("C:/images");
	// �̹��� �ҷ��� ������ ������ ���� ��ü ����
	private File file = null;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// 1. ��ư�ʱ�ȭ(����, ���x,�ʱ�ȭ,���x,����,����x,����x)
		buttonInitSetting(false, true, false, true, false, true, true);
		// 1.2 �޺��ڽ� �ʱ�ȭ
		cbYear.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5", "6"));
		// 2. ���̺������(���������˼�������)
		tableViewSetting();
		// 3. ������ư�� �������� ��չ�ưȰ��ȭ �� 6���� ������ ���ؼ� �����ʵ�������´�.
		btnTotal.setOnAction(e -> {
			handlerBtnTotalAction(e);
		});
		// 4. ��չ�ư�� �������� ��հ��, ������ư��Ȱ��,��չ�ư��Ȱ��,���Ȱ��ȭ,����ؽ�Ʈ�ʵ��Ȱ��ȭ
		btnAvg.setOnAction(e -> {
			handlerBtnAvgAction(e);
		});
		// 5. �ʱ�ȭ��ư�� �������� 1�� ��ư�ʱ�ȭ, �ؽ�Ʈ �ʵ� ���Ȱ��ȭ
		btnInit.setOnAction(e -> {
			handlerBtnInitAction(e);
		});
		// 6. ��Ϲ�ư�� �������� ���̺� ����ϰ�, ��簪�� �ʱ�ȭ�Ѵ�.
		btnOk.setOnAction(e -> {
			handlerBtnOkAction(e);
		});
		// 7. �˻���ư�� �������� ���̺�信�� ã����
		btnSearch.setOnAction(e -> {
			handlerBtnSearchAction(e);
		});
		// 8. ������ư�� �������� ���̺�信�� �������
		btnDelete.setOnAction(e -> {
			handlerBtnDeleteAction(e);
		});
		// 9. ���̺�信�� �������� �̺�Ʈ ó�����
		tableView.setOnMousePressed(e -> {
			handlerTableViewPressedAction(e);
		});
		// 10. ������ư�� �������� �̺�Ʈ ó�����
		btnEdit.setOnAction(e -> {
			handlerBtnEditAction(e);
		});
		// 11. �����ư�� �������� �̺�Ʈ ó�����
		btnExit.setOnAction(e -> {
			Platform.exit();
		});
		// 12.���̺�� ���콺�� ����Ŭ�������� ���õ� ������ �߽����� ����íƮ ���̱�
		tableView.setOnMouseClicked(e -> {
			handlerPieChartAction(e);
		});
		// 13.��íƮ�� �������� �̺�Ʈ ó�����
		btnBarChart.setOnAction(e -> {
			handlerBtnBarChartAction(e);
		});
		// 14. ��ó���� ���̺�信 ����Ÿ���̽����� �о ���̺�信 �����´�.
		totalList();
		// 15. ��ü�� ����Ʈ�� ������ ����Ÿ���̽����� ���� ��������.
		btnTotalList.setOnAction(e -> {
			handlerBtnTotalListAction(e);
		});
		// 16. �⺻�̹��� ����
		imageViewInit();
		// 17. ��¥���õǸ� ��¥����ϴ� �ؽ�Ʈ�� �ٷ� ������ִ� �̺�Ʈó��
		dpDate.setOnAction(e -> {
			handlerDatePickerAction(e);
		});
		// 18. �̹��� ��ư �����̺�Ʈ �̹�������â
		btnImageFile.setOnAction(e -> handlerBtnImageFileAction(e));

		// �׽����� ���� �����
		txtName.setText("aaa");
		// txtLevel.setText("2");
		cbYear.setValue("2");
		rbFemale.setSelected(true);
		txtBan.setText("2");
		txtKo.setText("23");
		txtEng.setText("23");
		txtMath.setText("23");
		txtSic.setText("23");
		txtSoc.setText("23");
		txtMusic.setText("23");
	}

	// 1. ��ư�ʱ�ȭ(����,���,�ʱ�ȭ,���,����,����,����)���� �� ����, ��� �ؽ�Ʈ�ʵ� ��Ȱ��ȭ
	public void buttonInitSetting(boolean b, boolean c, boolean d, boolean e, boolean f, boolean g, boolean h) {
		btnTotal.setDisable(b);
		btnAvg.setDisable(c);
		btnInit.setDisable(d);
		btnOk.setDisable(e);
		btnExit.setDisable(f);
		btnEdit.setDisable(g);
		btnDelete.setDisable(h);

		btnImageFile.setDisable(true);

		txtTotal.setDisable(true);
		txtAvg.setDisable(true);
		dpDate.setValue(LocalDate.now());

	}

	// 2. ���̺������(���������˼�������)
	public void tableViewSetting() {
		// 2. ���̺��� ���̸���Ʈ����
		data = FXCollections.observableArrayList();
		// 2. ���̺��� ���̺�並 �������ϰ� ����
		tableView.setEditable(false);
		// 3.�ؽ�Ʈ�� ���ڸ� �Է�
		DecimalFormat format = new DecimalFormat("###");
		// ���� �Է½� ���� ���� �̺�Ʈ ó��
		txtKo.setTextFormatter(new TextFormatter<>(event -> {
			if (event.getControlNewText().isEmpty()) {
				return event;
			}
			ParsePosition parsePosition = new ParsePosition(0);
			Object object = format.parse(event.getControlNewText(), parsePosition);
			if (object == null || parsePosition.getIndex() < event.getControlNewText().length()
					|| event.getControlNewText().length() == 4) {
				return null;
			} else {
				return event;
			}
		}));

		txtEng.setTextFormatter(new TextFormatter<>(event -> {
			if (event.getControlNewText().isEmpty()) {
				return event;
			}
			ParsePosition parsePosition = new ParsePosition(0);
			Object object = format.parse(event.getControlNewText(), parsePosition);
			if (object == null || parsePosition.getIndex() < event.getControlNewText().length()
					|| event.getControlNewText().length() == 4) {
				return null;
			} else {
				return event;

			}
		}));

		txtMath.setTextFormatter(new TextFormatter<>(event -> {
			if (event.getControlNewText().isEmpty()) {
				return event;
			}
			ParsePosition parsePosition = new ParsePosition(0);
			Object object = format.parse(event.getControlNewText(), parsePosition);
			if (object == null || parsePosition.getIndex() < event.getControlNewText().length()
					|| event.getControlNewText().length() == 4) {
				return null;
			} else {
				return event;
			}
		}));

		txtSic.setTextFormatter(new TextFormatter<>(event -> {
			if (event.getControlNewText().isEmpty()) {
				return event;
			}
			ParsePosition parsePosition = new ParsePosition(0);
			Object object = format.parse(event.getControlNewText(), parsePosition);
			if (object == null || parsePosition.getIndex() < event.getControlNewText().length()
					|| event.getControlNewText().length() == 4) {
				return null;
			} else {
				return event;
			}
		}));

		txtSoc.setTextFormatter(new TextFormatter<>(event -> {
			if (event.getControlNewText().isEmpty()) {
				return event;
			}
			ParsePosition parsePosition = new ParsePosition(0);
			Object object = format.parse(event.getControlNewText(), parsePosition);

			if (object == null || parsePosition.getIndex() < event.getControlNewText().length()
					|| event.getControlNewText().length() == 4) {
				return null;
			} else {
				return event;
			}
		}));

		txtMusic.setTextFormatter(new TextFormatter<>(event -> {
			if (event.getControlNewText().isEmpty()) {
				return event;
			}
			ParsePosition parsePosition = new ParsePosition(0);
			Object object = format.parse(event.getControlNewText(), parsePosition);
			if (object == null || parsePosition.getIndex() < event.getControlNewText().length()
					|| event.getControlNewText().length() == 4) {
				return null;
			} else {
				return event;
			}
		}));

		// 2. ���̺��� �÷�����
		TableColumn colNo = new TableColumn("NO");
		colNo.setMaxWidth(30);
		colNo.setStyle("-fx-alignment: CENTER;");
		colNo.setCellValueFactory(new PropertyValueFactory("no"));

		TableColumn colName = new TableColumn("����");
		colName.setMaxWidth(50);
		colName.setStyle("-fx-alignment: CENTER;");
		colName.setCellValueFactory(new PropertyValueFactory("name"));

		TableColumn colLevel = new TableColumn("�г�");
		colLevel.setMaxWidth(35);
		colLevel.setStyle("-fx-alignment: CENTER;");
		colLevel.setCellValueFactory(new PropertyValueFactory("level"));

		TableColumn colBan = new TableColumn("��");
		colBan.setMaxWidth(40);
		colBan.setStyle("-fx-alignment: CENTER;");
		colBan.setCellValueFactory(new PropertyValueFactory("ban"));

		TableColumn colGender = new TableColumn("����");
		colGender.setMaxWidth(40);
		colGender.setStyle("-fx-alignment: CENTER;");
		colGender.setCellValueFactory(new PropertyValueFactory("gender"));

		TableColumn colKorean = new TableColumn("����");
		colKorean.setMaxWidth(40);
		colKorean.setCellValueFactory(new PropertyValueFactory<>("korean"));

		TableColumn colEnglish = new TableColumn("����");
		colEnglish.setMaxWidth(40);
		colEnglish.setCellValueFactory(new PropertyValueFactory<>("english"));

		TableColumn colMath = new TableColumn("����");
		colMath.setMaxWidth(40);
		colMath.setCellValueFactory(new PropertyValueFactory<>("math"));

		TableColumn colSic = new TableColumn("����");
		colSic.setMaxWidth(40);
		colSic.setCellValueFactory(new PropertyValueFactory<>("sic"));

		TableColumn colSoc = new TableColumn("��ȸ");
		colSoc.setMaxWidth(40);
		colSoc.setCellValueFactory(new PropertyValueFactory<>("soc"));

		TableColumn colMusic = new TableColumn("����");
		colMusic.setMaxWidth(40);
		colMusic.setCellValueFactory(new PropertyValueFactory<>("music"));

		TableColumn colTotal = new TableColumn("����");
		colTotal.setMaxWidth(50);
		colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

		TableColumn colAvg = new TableColumn("���");
		colAvg.setMaxWidth(70);
		colAvg.setCellValueFactory(new PropertyValueFactory<>("avg"));

		TableColumn colRegister = new TableColumn("�����");
		colRegister.setMinWidth(90);
		colRegister.setCellValueFactory(new PropertyValueFactory<>("register"));

		TableColumn colImageFileName = new TableColumn("�̹���");
		colImageFileName.setMinWidth(260);
		colImageFileName.setCellValueFactory(new PropertyValueFactory<>("filename"));

		// 2. ���̺��� �÷��� ��ü�� ���̺�信 ����Ʈ�߰� �� �׸��߰�
		tableView.setItems(data);
		tableView.getColumns().addAll(colNo, colName, colLevel, colBan, colGender, colKorean, colEnglish, colMath,
				colSic, colSoc, colMusic, colTotal, colAvg, colRegister, colImageFileName);

	}

	// 3. ������ư�� �������� ��չ�ưȰ��ȭ �� 6���� ������ ���ؼ� �����ʵ�������´�.
	public void handlerBtnTotalAction(ActionEvent e) {
		try {
			int korean = Integer.parseInt(txtKo.getText());
			int english = Integer.parseInt(txtEng.getText());
			int math = Integer.parseInt(txtMath.getText());
			int sic = Integer.parseInt(txtSic.getText());
			int soc = Integer.parseInt(txtSoc.getText());
			int music = Integer.parseInt(txtMusic.getText());
			if ((korean <= 100) && (english <= 100) && (math <= 100) && (sic <= 100) && (soc <= 100)
					&& (music <= 100)) {
				int total = korean + english + math + sic + soc + music;
				txtTotal.setText(String.valueOf(total));
				btnAvg.setDisable(false);
			} else {
				throw new Exception("��ȿ�Ѱ� ����");
			}
		} catch (Exception e2) {
			alertDisplay(1, "�հ����", "������(��ȿ�Ѱ� ���˿��) ", e2.toString());
		}
	}

	// 4. ��չ�ư�� �������� ��հ��, ������ư��Ȱ��,��չ�ư��Ȱ��,���Ȱ��ȭ,����ؽ�Ʈ�ʵ��Ȱ��ȭ
	public void handlerBtnAvgAction(ActionEvent e) {
		txtAvg.setText(String.valueOf(Integer.parseInt(txtTotal.getText()) / 6.0));
		// 1. ��ư�ʱ�ȭ(����x, ���x,�ʱ�ȭ,���,����,����x,����x)
		buttonInitSetting(true, true, false, false, false, true, true);
		// �ؽ�Ʈ�ʵ� ��Ȱ��ȭ(�̸�x,�г�x,��x,����x,����x,����x,����x,����x,��ȸx,����x)
		textFieldInitSetting(true, true, true, true, true, true, true, true, true, true);
		// �̹�����ư Ȱ��ȭ
		btnImageFile.setDisable(false);
	}

	// 5. �ʱ�ȭ��ư�� �������� 1�� ��ư�ʱ�ȭ, �ؽ�Ʈ �ʵ� ���Ȱ��ȭ
	public void handlerBtnInitAction(ActionEvent e) {
		// 1. ��ư�ʱ�ȭ(����, ���x,�ʱ�ȭ,���x,����,����x,����x)
		buttonInitSetting(false, true, false, true, false, true, true);
		// �ؽ�Ʈ�ʵ� Ȱ��ȭ(�̸�,�г�,��,����,����,����,����,����,��ȸ,����)
		textFieldInitSetting(false, false, false, false, false, false, false, false, false, false);
		// �ؽ�Ʈ�ʵ� �ʱ�ȭ
//      txtName.clear();
//      txtLevel.clear();
//      txtBan.clear();
//      txtKo.clear();
//      txtEng.clear();
//      txtMath.clear();
//      txtSic.clear();
//      txtSoc.clear();
//      txtMusic.clear();
		txtTotal.clear();
		txtAvg.clear();
	}

	// 6. ��Ϲ�ư�� �������� ���̺� ����ϰ�, ��簪�� �ʱ�ȭ�Ѵ�.
	public void handlerBtnOkAction(ActionEvent e) {
		// ������ ����� �ִ��� Ȯ���Ѵ�.
		try {
			File dirMake = new File(dirSave.getAbsolutePath());
			// �̹��� ���� ���� ����
			if (!dirMake.exists()) {
				dirMake.mkdir();
			}
			// �̹��� ���� ����
			String fileName = imageSave(selectedFile);

			if (txtTotal.getText().equals("") || txtAvg.getText().equals("")) {
				throw new Exception();
			} else {
				StudentVO svo = new StudentVO(txtName.getText(), cbYear.getSelectionModel().getSelectedItem(),
						txtBan.getText(), genderGroup.getSelectedToggle().getUserData().toString(),
						Integer.parseInt(txtKo.getText().trim()), Integer.parseInt(txtEng.getText().trim()),
						Integer.parseInt(txtMath.getText().trim()), Integer.parseInt(txtSic.getText().trim()),
						Integer.parseInt(txtSoc.getText().trim()), Integer.parseInt(txtMusic.getText().trim()),
						Integer.parseInt(txtTotal.getText().trim()), Double.parseDouble(txtAvg.getText().trim()), null,
						fileName);

				// ���̺�信 ������ ����
				if (editDelete == true) {
					data.remove(selectedIndex);
					data.add(selectedIndex, svo);
					editDelete = false;
				} else {
					studentDAO = new StudentDAO();
					// ����Ÿ���̽� ���̺� �Է°��� �Է��ϴ� �Լ�.
					int count = studentDAO.getStudentregiste(svo);
					if (count != 0) {
						data.removeAll(data);
						totalList();
						// �̹����並 �ʱ�ȭ����
						imageViewInit();
					} else {
						throw new Exception("����Ÿ���̽� ��Ͻ���");
					}
				}
				alertDisplay(1, "��ϼ���", "���̺��ϼ���", "��������..");
			}
		} catch (Exception e2) {
			alertDisplay(1, "��Ͻ���", "�հ�,���������Է¿��", e2.toString());
			return;
		}
		// 5. �ʱ�ȭ��ư�� �������� 1�� ��ư�ʱ�ȭ, �ؽ�Ʈ �ʵ� ���Ȱ��ȭ
		handlerBtnInitAction(e);
	}

	// 7. �˻���ư�� �������� ���̺�信�� ã����
	public void handlerBtnSearchAction(ActionEvent e) {
		try {
			ArrayList<StudentVO> list = new ArrayList<StudentVO>();
			StudentDAO studentDAO = new StudentDAO();
			list = studentDAO.getStudentCheck(txtSearch.getText());

			if (list == null) {
				throw new Exception("�˻�����");
			}

			data.removeAll(data);
			for (StudentVO svo : list) {
				data.add(svo);
			}
		} catch (Exception e1) {
			alertDisplay(1, "�˻����", "�̸��˻�����", e1.toString());
		}
	}

	// 8. ������ư�� �������� ���̺�信�� �������
	public void handlerBtnDeleteAction(ActionEvent e) {
		try {
			StudentDAO studentDAO = new StudentDAO();
			studentDAO.getStudentDelete(selectStudent.get(0).getNo());
			data.removeAll(data);
			totalList();
		} catch (Exception e1) {
			DBUtil.alertDisplay(1, "��������", "8 ��������", e1.toString());
		}
		// 1. ��ư�ʱ�ȭ(����,���x,�ʱ�ȭ,���x,����,����x,����x)
		buttonInitSetting(false, true, false, true, false, true, true);
		// �ؽ�Ʈ�ʵ� ��Ȱ��ȭ(�̸�,�г�,��,����,����,����,����,����,��ȸ,����)
		textFieldInitSetting(false, false, false, false, false, false, false, false, false, false);
		editDelete = false;
	}

	// 9. ���̺�信�� �������� �̺�Ʈ ó�����
	public void handlerTableViewPressedAction(MouseEvent e) {
		try {
			// �������� ��ġ�� �ش�� ��ü�� �����´�.
			editDelete = true;
			// 1. ��ư�ʱ�ȭ(����X, ���x,�ʱ�ȭ,���x,����,����,����)
			buttonInitSetting(true, true, true, true, false, false, false);

			selectedIndex = tableView.getSelectionModel().getSelectedIndex();
			selectStudent = tableView.getSelectionModel().getSelectedItems();
			// �������� ���̺� �ִ� ���� �����ͼ� �������ʵ忡 ����ִ´�.
			txtName.setText(selectStudent.get(0).getName());
			cbYear.setValue(selectStudent.get(0).getLevel());
			txtBan.setText(selectStudent.get(0).getBan());
			if (selectStudent.get(0).getGender().equals("����")) {
				rbMale.setSelected(true);
				rbFemale.setSelected(false);
			} else {
				rbMale.setSelected(false);
				rbFemale.setSelected(true);
			}
			txtKo.setText(String.valueOf(selectStudent.get(0).getKorean()));
			txtEng.setText(String.valueOf(selectStudent.get(0).getEnglish()));
			txtMath.setText(String.valueOf(selectStudent.get(0).getMath()));
			txtSic.setText(String.valueOf(selectStudent.get(0).getSic()));
			txtSoc.setText(String.valueOf(selectStudent.get(0).getSoc()));
			txtMusic.setText(String.valueOf(selectStudent.get(0).getMusic()));
			txtTotal.setText(String.valueOf(selectStudent.get(0).getTotal()));
			txtAvg.setText(String.valueOf(selectStudent.get(0).getAvg()));

			String fileName = selectStudent.get(0).getFilename();
			selectedFile = new File("C:/images/" + fileName);
			if (selectedFile != null) {
				// �̹��� ���� ���
				localUrl = selectedFile.toURI().toURL().toString();
				localImage = new Image(localUrl, false);
				imageView.setImage(localImage);
				imageView.setFitHeight(250);
				imageView.setFitWidth(230);
			}

			// �ؽ�Ʈ�ʵ� ��Ȱ��ȭ(�̸�x,�г�x,��x,����x,����x,����x,����x,����x,��ȸx,����x)
			textFieldInitSetting(true, true, true, true, true, true, true, true, true, true);
		} catch (Exception e2) {

			// 1. ��ư�ʱ�ȭ(����, ���x,�ʱ�ȭ,���x,����,����x,����x)
			buttonInitSetting(false, true, false, true, false, true, true);
			editDelete = false;
		}
	}

	// 10. ������ư�� �������� �̺�Ʈ ó�����
	public void handlerBtnEditAction(ActionEvent e) {
		try {
			// 1. ��ư�ʱ�ȭ(����,���x,�ʱ�ȭ,���x,����,����x,����x)
			buttonInitSetting(false, true, false, true, false, true, true);
			// �ؽ�Ʈ�ʵ� ��Ȱ��ȭ(�̸�x,�г�x,��x,����x,����x,����x,����x,����x,��ȸx,����x)
			textFieldInitSetting(false, false, false, false, false, false, false, false, false, false);
			// ����ȭ���� �θ���
			Parent editRoot = FXMLLoader.load(getClass().getResource("/view/formedit.fxml"));
			Stage stageDialog = new Stage(StageStyle.UTILITY);
			stageDialog.initModality(Modality.WINDOW_MODAL);
			stageDialog.initOwner(btnOk.getScene().getWindow());
			stageDialog.setTitle("����â");
			// =====================
			TextField editName = (TextField) editRoot.lookup("#txtName");
			TextField editYear = (TextField) editRoot.lookup("#txtYear");
			TextField editBan = (TextField) editRoot.lookup("#txtBan");
			TextField editGender = (TextField) editRoot.lookup("#txtGender");
			TextField editKorean = (TextField) editRoot.lookup("#txtKorean");
			TextField editEnglish = (TextField) editRoot.lookup("#txtEnglish");
			TextField editMath = (TextField) editRoot.lookup("#txtMath");
			TextField editSic = (TextField) editRoot.lookup("#txtSic");
			TextField editSoc = (TextField) editRoot.lookup("#txtSoc");
			TextField editMusic = (TextField) editRoot.lookup("#txtMusic");
			TextField editTotal = (TextField) editRoot.lookup("#txtTotal");
			TextField editAvg = (TextField) editRoot.lookup("#txtAvg");
			Button btnCal = (Button) editRoot.lookup("#btnCal");
			Button btnFormAdd = (Button) editRoot.lookup("#btnFormAdd");
			Button btnFormCancel = (Button) editRoot.lookup("#btnFormCancel");

			// ������ ����� ������ ���� ���ϰ� �Ѵ�.
			editTotal.setDisable(true);
			editAvg.setDisable(true);
			// �ؽ�Ʈ ����Ʈ ���� �ִ´�.
			editName.setText(selectStudent.get(0).getName());
			editYear.setText(selectStudent.get(0).getLevel());
			editBan.setText(selectStudent.get(0).getBan());
			editGender.setText(selectStudent.get(0).getGender());
			editKorean.setText(String.valueOf(selectStudent.get(0).getKorean()));
			editEnglish.setText(String.valueOf(selectStudent.get(0).getEnglish()));
			editMath.setText(String.valueOf(selectStudent.get(0).getMath()));
			editSic.setText(String.valueOf(selectStudent.get(0).getSic()));
			editSoc.setText(String.valueOf(selectStudent.get(0).getSoc()));
			editMusic.setText(String.valueOf(selectStudent.get(0).getMusic()));
			editTotal.setText(String.valueOf(selectStudent.get(0).getTotal()));
			editAvg.setText(String.valueOf(selectStudent.get(0).getAvg()));
			// =====================
			// ��ư��� �̺�Ʈó��
			btnCal.setOnAction((e1) -> {
				try {
					int korean = Integer.parseInt(editKorean.getText());
					int english = Integer.parseInt(editEnglish.getText());
					int math = Integer.parseInt(editMath.getText());
					int sic = Integer.parseInt(editSic.getText());
					int soc = Integer.parseInt(editSoc.getText());
					int music = Integer.parseInt(editMusic.getText());
					if ((korean <= 100) && (english <= 100) && (math <= 100) && (sic <= 100) && (soc <= 100)
							&& (music <= 100)) {
						int total = korean + english + math + sic + soc + music;
						editTotal.setText(String.valueOf(total));
						editAvg.setText(String.valueOf(total / 6.0));
					} else {
						throw new Exception("��ȿ�Ѱ� ����");
					}
				} catch (Exception e2) {
					alertDisplay(1, "�հ����", "������(��ȿ�Ѱ� ���˿��) ", e2.toString());
				}
			});
			// �����ư�� ��������
			btnFormAdd.setOnAction(e4 -> {
				try {
					if (editTotal.getText().equals("") || editAvg.getText().equals("")) {
						throw new Exception();
					} else {
						StudentVO svo = new StudentVO(selectStudent.get(0).getNo(), editName.getText(),
								editYear.getText(), editBan.getText(), editGender.getText(),
								Integer.parseInt(editKorean.getText().trim()),
								Integer.parseInt(editEnglish.getText().trim()),
								Integer.parseInt(editMath.getText().trim()), Integer.parseInt(editSic.getText().trim()),
								Integer.parseInt(editSoc.getText().trim()),
								Integer.parseInt(editMusic.getText().trim()),
								Integer.parseInt(editTotal.getText().trim()),
								Double.parseDouble(editAvg.getText().trim()));

						StudentDAO studentDAO = new StudentDAO();
						StudentVO studentVO = studentDAO.getStudentUpdate(svo, selectStudent.get(0).getNo());

						// ���̺�信 ������ ����
						if (editDelete == true && studentVO != null) {
							data.remove(selectedIndex);
							data.add(selectedIndex, svo);
							editDelete = false;
						} else {
							throw new Exception("������Ͽ���");
						}

					}
				} catch (Exception e2) {
					alertDisplay(1, "������Ͻ���", "������Ͻ���", "��������" + e2.getMessage());
				}
				stageDialog.close();
			});
			// ��ҹ�ư�� ��������
			btnFormCancel.setOnAction(e3 -> {
				stageDialog.close();
			});

			Scene scene = new Scene(editRoot);
			stageDialog.setScene(scene);
			stageDialog.setResizable(false);
			stageDialog.show();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	// 12.���̺�� ���콺�� ����Ŭ�������� ���õ� ������ �߽����� ����íƮ ���̱�
	public void handlerPieChartAction(MouseEvent e) {
		try {
			if (e.getClickCount() != 2) {
				return;
			}
			// 1. ��ư�ʱ�ȭ(����,���x,�ʱ�ȭ,���x,����,����x,����x)
			buttonInitSetting(false, true, false, true, false, true, true);
			// �ؽ�Ʈ�ʵ� ��Ȱ��ȭ(�̸�x,�г�x,��x,����x,����x,����x,����x,����x,��ȸx,����x)
			textFieldInitSetting(false, false, false, false, false, false, false, false, false, false);
			// �ι�Ŭ���� �ߴ����� �����Ѵ�.

			// �ι�Ŭ���� �Ǿ�����
			Parent pieChartRoot = FXMLLoader.load(getClass().getResource("/view/piechart.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(btnOk.getScene().getWindow());
			stage.setTitle(selectStudent.get(0).getName() + " �հ� ��� ����íƮ");

			PieChart pieChart = (PieChart) pieChartRoot.lookup("#pieChart");
			Button btnClose = (Button) pieChartRoot.lookup("#btnClose");
			// �׷��� �׸���
			pieChart.setData(FXCollections.observableArrayList(
					new PieChart.Data("����", (double) (selectStudent.get(0).getTotal())),
					new PieChart.Data("���", selectStudent.get(0).getAvg())));
			// â �ݱ���
			btnClose.setOnAction(e2 -> {
				editDelete = false;
				stage.close();
			});

			Scene scene = new Scene(pieChartRoot);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	// 13.��íƮ�� �������� �̺�Ʈ ó�����
	public void handlerBtnBarChartAction(ActionEvent e) {
		try {
			Parent barChartRoot = FXMLLoader.load(getClass().getResource("/view/barchart.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(btnOk.getScene().getWindow());
			stage.setTitle(" ����׷��� ����íƮ");

			BarChart barChart = (BarChart) barChartRoot.lookup("#barChart");
			Button btnClose = (Button) barChartRoot.lookup("#btnClose");

			// ��� �������� ��íƮ�� �Է��Ѵ�.
			XYChart.Series seriesKorean = new XYChart.Series();
			seriesKorean.setName("����");
			ObservableList koreanList = FXCollections.observableArrayList();
			for (int i = 0; i < data.size(); i++) {
				koreanList.add(new XYChart.Data(data.get(i).getName(), data.get(i).getKorean()));
			}
			seriesKorean.setData(koreanList);
			barChart.getData().add(seriesKorean);
			// ��� ��������
			XYChart.Series seriesMath = new XYChart.Series();
			seriesMath.setName("����");
			ObservableList mathList = FXCollections.observableArrayList();
			for (int i = 0; i < data.size(); i++) {
				mathList.add(new XYChart.Data(data.get(i).getName(), data.get(i).getMath()));
			}
			seriesMath.setData(mathList);
			barChart.getData().add(seriesMath);
			// ��� ��������
			XYChart.Series seriesEnglish = new XYChart.Series();
			seriesEnglish.setName("����");
			ObservableList englishList = FXCollections.observableArrayList();
			for (int i = 0; i < data.size(); i++) {
				englishList.add(new XYChart.Data(data.get(i).getName(), data.get(i).getEnglish()));
			}
			seriesEnglish.setData(englishList);
			barChart.getData().add(seriesEnglish);
			// ��� ��������
			XYChart.Series seriesSic = new XYChart.Series();
			seriesSic.setName("����");
			ObservableList sicList = FXCollections.observableArrayList();
			for (int i = 0; i < data.size(); i++) {
				sicList.add(new XYChart.Data(data.get(i).getName(), data.get(i).getSic()));
			}
			seriesSic.setData(sicList);
			barChart.getData().add(seriesSic);
			// ��� ��ȸ����
			XYChart.Series seriesSoc = new XYChart.Series();
			seriesSoc.setName("��ȸ");
			ObservableList socList = FXCollections.observableArrayList();
			for (int i = 0; i < data.size(); i++) {
				socList.add(new XYChart.Data(data.get(i).getName(), data.get(i).getSoc()));
			}
			seriesSoc.setData(socList);
			barChart.getData().add(seriesSoc);
			// ��� ��������
			XYChart.Series seriesMusic = new XYChart.Series();
			seriesMusic.setName("����");
			ObservableList musicList = FXCollections.observableArrayList();
			for (int i = 0; i < data.size(); i++) {
				musicList.add(new XYChart.Data(data.get(i).getName(), data.get(i).getMusic()));
			}
			seriesMusic.setData(musicList);
			barChart.getData().add(seriesMusic);

			// ��íƮ ��ưclose �̺�Ʈ
			btnClose.setOnAction(e1 -> {
				// 1. ��ư�ʱ�ȭ(����,���x,�ʱ�ȭ,���x,����,����x,����x)
				buttonInitSetting(false, true, false, true, false, true, true);
				// �ؽ�Ʈ�ʵ� ��Ȱ��ȭ(�̸�x,�г�x,��x,����x,����x,����x,����x,����x,��ȸx,����x)
				textFieldInitSetting(false, false, false, false, false, false, false, false, false, false);
				editDelete = false;
				stage.close();
			});

			Scene scene = new Scene(barChartRoot);
			stage.setScene(scene);
			stage.show();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// 14. ��ó���� ���̺�信 ����Ÿ���̽����� �о ���̺�信 �����´�.
	public void totalList() {
		ArrayList<StudentVO> list = null;
		StudentDAO studentDAO = new StudentDAO();
		StudentVO studentVO = null;
		list = studentDAO.getStudentTotal();
		if (list == null) {
			DBUtil.alertDisplay(1, "���", "DB �����������", "���˿��");
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			studentVO = list.get(i);
			data.add(studentVO);
		}
	}

	// 15. ��ü�� ����Ʈ�� ������ ����Ÿ���̽����� ���� ��������.
	public void handlerBtnTotalListAction(ActionEvent e) {
		try {
			data.removeAll(data);
			totalList();
		} catch (Exception e1) {
			DBUtil.alertDisplay(1, "��ü����Ʈ����", "15�� ��ü����Ʈ�����߻�", e1.toString());
		}
	}

	// 16. �⺻�̹��� ����
	public void imageViewInit() {
		localUrl = "/images/profile.png";
		localImage = new Image(localUrl, false);
		imageView.setImage(localImage);
	}

	// 17. ��¥���õǸ� ��¥����ϴ� �ؽ�Ʈ�� �ٷ� ������ִ� �̺�Ʈó��
	public void handlerDatePickerAction(ActionEvent e) {
		LocalDate date = dpDate.getValue();
		txtDay.setText(date.toString());
	}

	// 18. �̹��� ��ư �����̺�Ʈ �̹�������â
	public void handlerBtnImageFileAction(ActionEvent e) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));
		try {
			selectedFile = fileChooser.showOpenDialog(btnOk.getScene().getWindow());
			if (selectedFile != null) {
				// �̹��� ���� ���
				localUrl = selectedFile.toURI().toURL().toString();
			}
		} catch (MalformedURLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		localImage = new Image(localUrl, false);
		imageView.setImage(localImage);
		imageView.setFitHeight(250);
		imageView.setFitWidth(230);
		btnOk.setDisable(false);

		if (selectedFile != null) {
			selectFileName = selectedFile.getName();
		}

	}

	/***********************************************************
	 * imageSave() �̹��� ���� �޼ҵ�. ������ �����ͼ� ���ϸ� ���� �ۼ��� ���� inputStream, outputStream ��
	 * ���ؼ� �ش������� �����Ѵ�.
	 * 
	 * @param (������ ���ϸ�)
	 * @return ���� ���ϸ�
	 ***********************************************************/
	public String imageSave(File file) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		int data = -1;
		String fileName = null;
		try {
			// �̹��� ���ϸ� ����
			fileName = "student" + System.currentTimeMillis() + "_" + file.getName();
			bis = new BufferedInputStream(new FileInputStream(file));
			bos = new BufferedOutputStream(new FileOutputStream(dirSave.getAbsolutePath() + "\\" + fileName));

			// ������ �̹��� ���� InputStream�� �������� �̸����� ���� -1
			while ((data = bis.read()) != -1) {
				bos.write(data);
				bos.flush();
			}
		} catch (Exception e) {
			e.getMessage();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				e.getMessage();
			}
		}
		return fileName;
	}

	/***********************************************************
	 * imageDelete() �̹��� ���� �޼ҵ�.
	 * 
	 * @param (������ ���ϸ�)
	 * @return �������θ� ����
	 ***********************************************************/
	public boolean imageDelete(String fileName) {
		boolean result = false;
		try {
			File fileDelete = new File(dirSave.getAbsolutePath() + "\\" + fileName); // �����̹�������
			if (fileDelete.exists() && fileDelete.isFile()) {
				result = fileDelete.delete();
				imageViewInit();
			}
		} catch (Exception ie) {
			System.out.println("ie = [ " + ie.getMessage() + "]");
			result = false;
		}
		return result;
	}

	// ���â���÷���
	public void alertDisplay(int type, String title, String headerText, String contentText) {
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

	// �ؽ�Ʈ�ʵ� ��Ȱ��ȭ(�̸�,�г�,��,����,����,����,����,����,��ȸ,����)
	private void textFieldInitSetting(boolean b, boolean c, boolean d, boolean k, boolean e, boolean f, boolean g,
			boolean h, boolean i, boolean j) {
		txtName.setDisable(b);
		cbYear.setDisable(c);
//      txtLevel.setDisable(c);
		txtBan.setDisable(d);
		rbMale.setDisable(k);
		rbFemale.setDisable(k);
		txtKo.setDisable(e);
		txtEng.setDisable(f);
		txtMath.setDisable(g);
		txtSic.setDisable(h);
		txtSoc.setDisable(i);
		txtMusic.setDisable(j);
	}
}