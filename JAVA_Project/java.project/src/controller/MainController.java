package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.GoodsDAO;
import model.GoodsVO;
import model.SaleDAO;
import model.SaleVO;
import model.iMTableDAO;
import model.iMTableVO;
import util.AlertManager;
import util.AlertManager.AlertInfo;
import util.SceneLoader;

public class MainController implements Initializable {

	/*****************
	 * 
	 * ��� ������ �ʿ��� â�� ����
	 * 
	 * ���¼�
	 * 
	 */
	@FXML
	private Button buttonIM; // 1. ��� ������ư
	@FXML
	private Button buttonIMClose; // 2. ������ â �ݱ�
	@FXML
	private TextField textFieldIMSearch; // �˻��� �Է� TextField
	@FXML
	private Button buttonIMSearch; // 3. �˻� ��ư
	@FXML
	private Button buttonIMSearchAll; // 4. ��ü �˻� ��ư
	@FXML
	private BarChart<String, Integer> stackedBarChart; // chart
	@FXML
	private TableView<iMTableVO> iMtableView; // 5. ��Ʈ
	@FXML
	private DatePicker datePickerIMSelect;
	@FXML
	private Button btDelete;
	
	ObservableList<iMTableVO> data; // ���̺�信 �����ֱ����ؼ� ����� ����Ÿ
	public boolean flagIMCheck = false;
	private LocalDate local = null;
	private String localString = null;

	// ���̺� �信�� ���õ� ���� ����
	private ObservableList<iMTableVO> selectTableViewVO = FXCollections.observableArrayList();
	private int selectTableViewIndex = 0;
	private iMTableVO selectTableView = null;

	/********************
	 * ��� : �ʿ��� �Ǹ� ���� â�� ����
	 * 
	 * �ۼ��� : ������
	 * 
	 */
	@FXML
	private TextField tfCount;
	@FXML
	private TextField tfTotalSalePrice;
	@FXML
	private TextField tfComents;
	@FXML
	private ComboBox<String> comboBox;
	@FXML
	private Button btCheck;
	@FXML
	private Button btSaleAdd;
	@FXML
	private Button btSaleDelete;
	@FXML
	private Button btSelectRowDelete;
	@FXML
	private Button btMenuCheck;
	@FXML
	private Button btSaleTotalPrice;
	@FXML
	private Button btRemoveTable;
	@FXML
	private DatePicker datePicker;
	@FXML
	private TableView<SaleVO> tableView;
	@FXML
	private BarChart<String, Integer> barChart;
	
	private ArrayList<SaleVO> searchDateSaleVO = new ArrayList<SaleVO>();
	private String saveGoodsString = null;

	// ���� �� ���̺� �信 ǥ�� �� ����Ʈ
	private ObservableList<SaleVO> afterDeleteSaleVOList = FXCollections.observableArrayList();
	// ��񿡼� �ҷ��� ����Ʈ (��¥�� ������)
	private ObservableList<SaleVO> loadSaleVOList = FXCollections.observableArrayList();
	// �߰��� �׸���� ����ִ� ����Ʈ
	private ObservableList<SaleVO> addSaleVOList = FXCollections.observableArrayList();
	// ���̺�� ���� �� �������� ��
	private ObservableList<SaleVO> tableSelectSaleVOList = FXCollections.observableArrayList();
	private SaleVO tableSelectSaleVO = null;
	private int tableSelectIndex = 0;
	// Ȯ�� ��ư�� �������� �ߺ��� ������ false �ߺ��� ������ true
	private boolean flagCheck = false;
	// ���� ���� �� �Ѿ��� Ȯ���ϱ� ���� �÷��� ���� �� �Ķ�� true
	private boolean flagDeleteCheck = false;

	/*******************************
	 * 
	 * ��� : �ʿ��� goodsVO�� ������ 2019 10 �� 18 ��
	 * 
	 * �ۼ��� : ������
	 * 
	 */
	private ObservableList<GoodsVO> goodsVOList = FXCollections.observableArrayList();
	private ObservableList<String> goodsNameList = FXCollections.observableArrayList();;
	private String editGoods = null;
	private ObservableList<GoodsVO> selectMenuEditGoodsVOList = FXCollections.observableArrayList();
	private GoodsVO selectMenuEditGoodsVO = null;

	/**********************************
	 * 
	 * ��� : �ʿ��� �� ���� ������ 2019 10 �� 18 ��
	 * 
	 * �ۼ��� : ������
	 * 
	 */
	private LocalDate localDate = null;
	private String localDateStr = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//��ư ����
		buttonInitSetting(false, true, true, true, false, true);
		//���̺� ����
		tableViewSetting();
		//��ǰ ������ ����
		loadTotalGoodsDB();
		//�޺� �ڽ� ����
		comboBoxSettion();

		datePicker.setOnAction(e -> handlerDatePicker(e));

		comboBox.setOnAction(e -> {

			saveGoodsString = comboBox.getSelectionModel().getSelectedItem();

		}); // end of cbGoodsList

		/******************************
		 * 
		 * ��� : ���͸� ������ �׸� �߰��Ѵ�.
		 * 
		 * ������ 
		 * 
		 */
		tfCount.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent keyEvent) {
				KeyCode keyCode = keyEvent.getCode();
				if (keyCode.equals(KeyCode.ENTER)) {
					if (localDate == null || saveGoodsString.equals(null) || tfCount.getText().equals(null)) {
						AlertManager.getInstance().show(AlertInfo.ERROR_TASK_EMPTY, null);
						buttonInitSetting(false, true, true, true, false, true);
						return;
					}

					try {
						GoodsDAO goodsDAO = new GoodsDAO();
						GoodsVO addGoodsVO = goodsDAO.getGoodsInfomation(saveGoodsString, LoginController.clientId);
						int count = Integer.parseInt(tfCount.getText());
						int total = count * addGoodsVO.getPrice();

						SaleVO addSaleVO = new SaleVO(localDateStr, addGoodsVO.getGoodsName(), addGoodsVO.getPrice(),
								count, total, tfComents.getText());
						
						addSaleVOList.add(addSaleVO);
						loadSaleVOList.add(addSaleVO);
						tableView.setItems(loadSaleVOList);
						barChart.getData().clear();
						tfTotalSalePrice.setText(null);
						buttonInitSetting(false, true, true, false, false, true);

					} catch (SQLException e) {
						AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
						e.printStackTrace();
					}
				}

			}
		});
		
		btSelectRowDelete.setOnAction(e -> handlerButtonRowDeleteAction(e));

		btCheck.setOnAction(e -> handlerButtonCheckAction(e));

		btSaleAdd.setOnAction(e -> handlerButtonSaleAddAction(e));

		btSaleDelete.setOnAction(e -> handlerButtonSaleDelete(e));

		btMenuCheck.setOnAction(e -> handlerButtonMenuCheck(e));

		btSaleTotalPrice.setOnAction(e -> handlerButtonSaleTotalPrice(e));

		btRemoveTable.setOnAction(e -> handlerButtonRemoveTableAction(e));

		tableView.setOnMousePressed(e1 -> {

			tableSelectSaleVOList = tableView.getSelectionModel().getSelectedItems();
			tableSelectSaleVO = tableView.getSelectionModel().getSelectedItem();
			tableSelectIndex = tableView.getSelectionModel().getSelectedIndex();

		}); // end of tableView select
		
		handlerTableView();

		// 1. ��� Ȯ�� ��ư
		buttonIM.setOnAction(e -> {
			handlerButtonIM(e);
		});

		// 2. �ݱ� ��ư
		buttonIMClose.setOnAction(e -> {
			handlerButtonIMClose(e);
		});

		// 3. �˻� ��ư
		buttonIMSearch.setOnAction(e -> {
			handlerButtonIMSearch(e);
		});

		// 4. ��ü �˻� ��ư
		buttonIMSearchAll.setOnAction(e -> {
			handlerButtonIMSearchAll(e);
		});

		// 5. ��Ʈ
		iMtableView.setOnMousePressed(e -> {
			selectTableViewVO = iMtableView.getSelectionModel().getSelectedItems();
			selectTableViewIndex = iMtableView.getSelectionModel().getSelectedIndex();
			selectTableView = iMtableView.getSelectionModel().getSelectedItem();
		});

		// 6. datepicker
		datePickerIMSelect.setOnAction(e -> handlerDatePickerAction(e));

		// 7. ���� ��ư
		btDelete.setOnAction(e -> handlerButtonDeleteAction(e));

	} // end of initialize
	
	/******************************
	 * 
	 * ��� : ������ ���̽��� �߰� ���� ���� �׸�鸸 �����Ѵ�.
	 * 
	 * ������
	 * 
	 */
	private void handlerButtonRowDeleteAction(ActionEvent e) {
		
		if(addSaleVOList.contains(tableSelectSaleVO)) {
			AlertManager.getInstance().show(AlertType.CONFIRMATION, "�׸� ����", "������ �׸��� �����Ͻðڽ��ϱ�?", buttonType -> {
				if (buttonType != ButtonType.OK) {
					return;
				}

				loadSaleVOList.remove(tableSelectIndex);
				
				tableView.setItems(loadSaleVOList);

			});
		} else {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DELETE, null);
			
		}

	} // end of handlerButtonRowDeleteAction
	
	/*********************
	 * 
	 * ��� : ��ư�� ������ ���̺� ���� ������� Ȯ���Ѵ�.
	 * 
	 * ������
	 * 
	 */
	private void handlerButtonCheckAction(ActionEvent e) {

		if (loadSaleVOList.isEmpty()) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_EMPTY, null);
			return;
		}

		flagCheck = true;
		AlertManager.getInstance().show(AlertInfo.SUCCESS_CHECK, null);
		buttonInitSetting(true, false, true, false, false, false);
		
	} // end of handlerButtonCheckAction
	
	/**********************
	 * 
	 * ��� : ������ ���̽��� �ִ� ��ǰ���� �ҷ��ͼ� �޺��ڽ��� �ִ´�.
	 * 
	 * ������
	 * 
	 */
	private void comboBoxSettion() {

		try {

			GoodsDAO goodsDAO = new GoodsDAO();
			ArrayList<GoodsVO> goodsList = goodsDAO.getGoodsTotal();

			ArrayList<String> saveGoodsName = new ArrayList<String>();

			for (int i = 0; i < goodsList.size(); i++) {

				saveGoodsName.add(goodsList.get(i).getGoodsName());

			}

			goodsNameList = FXCollections.observableArrayList(saveGoodsName);

			comboBox.setItems(goodsNameList);

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

	} // end of comboBoxSettion

	/*************************
	 * 
	 * ��� : datePicker�� ��¥�� �����ϸ� ������ ��¥�� SaleVO�� �����ͼ� ���̺�信 ǥ���Ѵ�. 2019 10 �� 18 ��
	 * 
	 * �ۼ��� : ������
	 * 
	 */
	private void handlerDatePicker(ActionEvent e) {

		try {

			localDate = datePicker.getValue();

			if (localDate.getDayOfMonth() < 10) {
				localDateStr = localDate.getYear() + "-" + localDate.getMonthValue() + "-" + "0"
						+ localDate.getDayOfMonth();
			} else {
				localDateStr = localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth();
			}

			SaleDAO saleDAO = new SaleDAO();

			loadSaleVOList = FXCollections.observableArrayList(saleDAO.getListToDate(localDateStr));

			if (!loadSaleVOList.isEmpty()) {
				buttonInitSetting(true, true, false, true, false, false);
			}

			tableView.setItems(loadSaleVOList);
			System.out.println(loadSaleVOList.toString());
			barChart.getData().clear();
			barChartSetting(loadSaleVOList);

		} catch (SQLException sqle) {
			sqle.printStackTrace();
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);

		}

	} // end of handlerDatePicker

	/***********************
	 * 
	 * ��� : Ȯ�� ��ư�� ���� �� ������ ���̽��� ������� ���� �׸���� �����Ѵ�.
	 * 
	 * ������
	 * 
	 */
	private void handlerButtonSaleAddAction(ActionEvent e) {

		if (!flagCheck) {
			AlertManager.getInstance().show(AlertInfo.FAIL_SALE_ADD_UNCHECK, null);
			return;
		} else {
			AlertManager.getInstance().show(AlertType.CONFIRMATION, "���� ���", "������ ����Ͻðڽ��ϱ�?", buttonType -> {
				if (buttonType != ButtonType.OK) {
					return;
				}
				try {
					SaleDAO saleDAO = new SaleDAO();
					for (int i = 0; i < addSaleVOList.size(); i++) {
						saleDAO.insertSaleDB(addSaleVOList.get(i));
					}
					addSaleVOList.removeAll(addSaleVOList);
					AlertManager.getInstance().show(AlertInfo.SUCCESS_TASK, null);
					buttonInitSetting(true, true, false, true, false, false);
					saleDAO.getListToDate(localDateStr);
					barChartSetting(loadSaleVOList);
				} catch (SQLException sqle) {
					AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
				}
			});
		}

	} // end of handlerButtonSaleAddAction

	/************************************
	 * 
	 * ��� : ��ư�� ������ �׸��� �����. 
	 * 
	 * �ۼ��� : ������
	 * 
	 */
	private void handlerButtonSaleDelete(ActionEvent e) {

		// System.out.println(tableSelectSaleVO.toString());

		if (tableSelectSaleVO == null) {
			AlertManager.getInstance().show(AlertType.ERROR, "����", "�׸��� �������ּ���.", null);
			return;
		}

		SaleDAO saleDAO = new SaleDAO();

		AlertManager.getInstance().show(AlertType.CONFIRMATION, "�׸� ����", "������ �׸��� �����Ͻðڽ��ϱ�?", buttonType -> {
			if (buttonType != ButtonType.OK) {
				return;
			}

			try {

				boolean deleted = saleDAO.deleteSale(tableSelectSaleVO);

				if (deleted) {

					AlertManager.getInstance().show(AlertInfo.SUCCESS_TASK, null);
					tableSelectSaleVO = null;

					// ������ ��¥ ���� �����ߴ� �� �״�θ� ���� �ؾ� �ϹǷ� ������ ���� ���� �ٽ� ���̺�並 �����Ѵ�.

					searchDateSaleVO = saleDAO.getListToDate(localDateStr);
					loadSaleVOList = FXCollections.observableArrayList(searchDateSaleVO);
					tableView.setItems(loadSaleVOList);
					barChartSetting(loadSaleVOList);
					System.out.println(loadSaleVOList.toString());

				}

			} catch (SQLException sqle) {
				sqle.printStackTrace();
				AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
			}

		});

	} // end of handlerButtonSaleDelete

	/***************************
	 * 
	 * ��� : ��ư�� ������ �޴� Ȯ�� ���̾�α� â�� ����.
	 * 
	 * �ۼ��� : ������
	 * 
	 */
	private void handlerButtonMenuCheck(ActionEvent e) {
		try {
			Scene scene = SceneLoader.getInstance().makeMenuCheckScene();
			Parent menuCheckRoot = scene.getRoot();
			Stage dialogStage = new Stage(StageStyle.UTILITY);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(btMenuCheck.getScene().getWindow());
			dialogStage.setTitle("����");

			TableView<GoodsVO> tableView = (TableView<GoodsVO>) menuCheckRoot.lookup("#tableView");
			TextField tfGoods = (TextField) menuCheckRoot.lookup("#tfGoods");
			Button btSearch = (Button) menuCheckRoot.lookup("#btSearch");
			Button btMenuRefresh = (Button) menuCheckRoot.lookup("#btMenuRefresh");
			Button btAdd = (Button) menuCheckRoot.lookup("#btAdd");
			Button btEdit = (Button) menuCheckRoot.lookup("#btEdit");
			Button btEditDelete = (Button) menuCheckRoot.lookup("#btEditDelete");
			Button btBack = (Button) menuCheckRoot.lookup("#btBack");

			tableView.setEditable(false);

			TableColumn columnGoods = new TableColumn("��ǰ");
			columnGoods.setMaxWidth(100);
			columnGoods.setStyle("-fx-alignment: CENTER;");
			columnGoods.setCellValueFactory(new PropertyValueFactory("goodsName"));

			TableColumn columnPrice = new TableColumn("����");
			columnPrice.setMaxWidth(100);
			columnPrice.setStyle("-fx-alignment: CENTER;");
			columnPrice.setCellValueFactory(new PropertyValueFactory("price"));

			goodsVOList.removeAll(goodsVOList);
			loadTotalGoodsDB();

			tableView.setItems(goodsVOList);
			tableView.getColumns().addAll(columnGoods, columnPrice);

			/*******************
			 * 
			 * 2019 10 �� 18 �� �ۼ��� : ������
			 * 
			 * ��� : �˻���ư
			 * 
			 */
			btSearch.setOnAction(e1 -> {

				ObservableList<GoodsVO> list = FXCollections.observableArrayList();
				GoodsDAO goodsDVO = new GoodsDAO();

				try {

					list = goodsDVO.getCheckGoods(tfGoods.getText(), LoginController.clientId);
					tableView.setItems(list);

				} catch (SQLException sqle) {
					sqle.printStackTrace();
					AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
				}

			}); // end of btSearch

			/**********************
			 * 
			 * 2019 10 �� 18 �� �ۼ��� : ������
			 * 
			 * ��� : ���̺�� ���� ��ħ ��ư
			 * 
			 */
			btMenuRefresh.setOnAction(e1 -> {

				goodsVOList.removeAll(goodsVOList);
				loadTotalGoodsDB();

				tableView.setItems(goodsVOList);

			});

			/******************************
			 * 
			 * 2019 10 �� 18 �� �ۼ��� : ������
			 * 
			 * ��� : �޴��� ����Ѵ�.
			 * 
			 */
			btAdd.setOnAction(e1 -> {

				try {

					Scene addMenuScene = SceneLoader.getInstance().makeMenuAddScene();
					Parent menuAddRoot = addMenuScene.getRoot();
					Stage dialMenuAddStage = new Stage(StageStyle.UTILITY);
					dialMenuAddStage.initModality(Modality.WINDOW_MODAL);
					dialMenuAddStage.initOwner(btMenuCheck.getScene().getWindow());
					dialMenuAddStage.setTitle("�޴� ���");

					TextField tfAddGoods = (TextField) menuAddRoot.lookup("#tfAddGoods");
					TextField tfAddPrice = (TextField) menuAddRoot.lookup("#tfAddPrice");
					Button btAddGoods = (Button) menuAddRoot.lookup("#btAddGoods");
					Button btAddCancle = (Button) menuAddRoot.lookup("#btAddCancle");

					btAddGoods.setOnAction(e3 -> {

						GoodsDAO goodsDVO = new GoodsDAO();

						GoodsVO insertGoods = new GoodsVO(tfAddGoods.getText(), Integer.parseInt(tfAddPrice.getText()));
						try {
							goodsDVO.insertGoodsDB(LoginController.clientId, insertGoods.getGoodsName(),
									insertGoods.getPrice());
							AlertManager.getInstance().show(AlertInfo.SUCCESS_TASK, null);
						} catch (SQLException sqle) {
							sqle.printStackTrace();
							AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
						}
					});

					btAddCancle.setOnAction(e3 -> {

						dialMenuAddStage.close();

					});

					dialMenuAddStage.setScene(addMenuScene);
					dialMenuAddStage.setResizable(false);
					dialMenuAddStage.show();

				} catch (IOException ioe) {
					ioe.printStackTrace();
					AlertManager.getInstance().show(AlertInfo.ERROR_LOAD_SCENE, null);
				}

			}); // end of btAdd

			/**************************
			 * 
			 * 2019 10 �� 18 �� �ۼ��� : ������
			 * 
			 * ��� : ���̺�� ���� �� ������ �����Ѵ�.
			 * 
			 */
			tableView.setOnMousePressed(e1 -> {

				selectMenuEditGoodsVO = tableView.getSelectionModel().getSelectedItem();
				selectMenuEditGoodsVOList = tableView.getSelectionModel().getSelectedItems();

			}); // end of tableView select

			/************************************
			 * 
			 * 2019 10 �� 18�� �ۼ��� : ������
			 * 
			 * ��� : ���̺� �信�� ���õ� �׸��� �����ϴ� â�� �����ش�.
			 * 
			 */
			btEdit.setOnAction(e1 -> {

				try {
					Scene editMenuScene = SceneLoader.getInstance().makeMenuEditScene();
					Parent menuEditRoot = editMenuScene.getRoot();
					Stage dialogEditStage = new Stage(StageStyle.UTILITY);
					dialogEditStage.initModality(Modality.WINDOW_MODAL);
					dialogEditStage.initOwner(btMenuCheck.getScene().getWindow());
					dialogEditStage.setTitle("�޴� ����");

					TextField tfEditGoods = (TextField) menuEditRoot.lookup("#tfEditGoods");
					TextField tfEditPrice = (TextField) menuEditRoot.lookup("#tfEditPrice");
					Button btEditOk = (Button) menuEditRoot.lookup("#btEditOk");
					Button btEditBack = (Button) menuEditRoot.lookup("#btEditBack");

					editGoods = selectMenuEditGoodsVOList.get(0).getGoodsName();

					tfEditGoods.setPromptText(editGoods);
					tfEditPrice.setPromptText(String.valueOf(selectMenuEditGoodsVOList.get(0).getPrice()));

					/********************************
					 * 
					 * 2019 10 �� 18 �� �ۼ��� : ������
					 * 
					 * ��� : ������ ������ ������Ʈ �Ѵ�.
					 * 
					 */
					btEditOk.setOnAction(e2 -> {

						GoodsDAO goodsDAO = new GoodsDAO();
						boolean updateCheck = false;

						try {
							if(!(tfEditGoods.getText().equals("")) && !(tfEditPrice.getText().equals(""))) {
								updateCheck = goodsDAO.updateGoods(tfEditGoods.getText(), 
										Integer.parseInt(tfEditPrice.getText()), selectMenuEditGoodsVO.getNo());
							} else if ((tfEditGoods.getText().equals("")) && !(tfEditPrice.getText().equals(""))) {
								updateCheck = goodsDAO.updateGoods(tfEditGoods.getPromptText(),
										Integer.parseInt(tfEditPrice.getText()), selectMenuEditGoodsVO.getNo());
							} else if (!(tfEditGoods.getText().equals("")) && (tfEditPrice.getText().equals(""))) {
								updateCheck = goodsDAO.updateGoods(tfEditGoods.getText(),
										Integer.parseInt(tfEditPrice.getPromptText()), selectMenuEditGoodsVO.getNo());
							} else {
								AlertManager.getInstance().show(AlertInfo.ERROR_TASK_EMPTY, null);
							}

							if (updateCheck) {
								AlertManager.getInstance().show(AlertInfo.SUCCESS_TASK, null);
							} else {
								AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);
							}
							updateCheck = false;

						} catch (NumberFormatException | SQLException e3) {
							// TODO Auto-generated catch block
							e3.printStackTrace();
						}

					}); // end of btEditOk

					/********************************************
					 * 
					 * 2019 10 �� 18 �� �ۼ��� : ������
					 * 
					 * ��� : ���� â�� �ݴ´�.
					 * 
					 */
					btEditBack.setOnAction(e2 -> {
						goodsVOList.removeAll(goodsVOList);
						loadTotalGoodsDB();

						tableView.setItems(goodsVOList);

						dialogEditStage.close();
					}); // end of btEditBack

					dialogEditStage.setScene(editMenuScene);
					dialogEditStage.setResizable(false);
					dialogEditStage.show();

				} catch (IOException ioe) {
					ioe.printStackTrace();
					AlertManager.getInstance().show(AlertInfo.ERROR_LOAD_SCENE, null);
				}

			}); // end of btEdit

			/***********************
			 * 
			 * 2019 10 �� 18 �� �ۼ��� : ������
			 * 
			 * ��� : ���̺� �信�� ���� �� �׸��� ���̺� �信�� �����Ѵ�.
			 * 
			 */
			btEditDelete.setOnAction(e1 -> {

				GoodsDAO goodsDVO = new GoodsDAO();

				AlertManager.getInstance().show(AlertType.CONFIRMATION, "�׸� ����", "������ �׸��� �����Ͻðڽ��ϱ�?", buttonType -> {
					if (buttonType != ButtonType.OK) {
						return;
					}

					try {
						boolean deleted = goodsDVO.deleteGoods(selectMenuEditGoodsVO.getNo());
						if (deleted) {
							AlertManager.getInstance().show(AlertInfo.SUCCESS_TASK, null);

						} else {
							AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);
						}

					} catch (SQLException sqle) {
						sqle.printStackTrace();
						AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
					}
				});
			}); // end of btEditDelete
			
			/*****************
			 * 
			 * 2019 10 �� 18 �� �ۼ��� : ������
			 * 
			 * ��� : �޴� Ȯ�� â�� �ݴ´�.
			 * 
			 */
			btBack.setOnAction(e1 -> {
				comboBoxSettion();
				dialogStage.close();

			}); // end of btBack

			dialogStage.setScene(scene);
			dialogStage.setResizable(false);
			dialogStage.show();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			AlertManager.getInstance().show(AlertInfo.ERROR_LOAD_SCENE, null);
		}
	} // end of handlerButtonMenuCheck

	/****************************
	 * 
	 * ��� : ���̺� ���� ǥ�õ� �ݾ��� ���� ���ؼ� �����ش�. 2019 10 �� 19
	 * 
	 * �ۼ��� : ������
	 * 
	 */
	private void handlerButtonSaleTotalPrice(ActionEvent e) {
		
		int saleTotalPrice = 0;
		
		for(int i = 0; i < loadSaleVOList.size(); i++) {
			saleTotalPrice = loadSaleVOList.get(i).getTotal() + saleTotalPrice;
		}
		
		tfTotalSalePrice.setText(String.valueOf(saleTotalPrice));

	} // end of handlerButtonSaleTotalPrice
	
	/******************************
	 * 
	 * ��� : ȭ���� ���� ��ħ �Ѵ�.
	 * 
	 * ������ 
	 * 
	 */
	private void handlerButtonRemoveTableAction(ActionEvent e) {

		ObservableList<SaleVO> removeList = FXCollections.observableArrayList();
		ObservableList<String> removeComboList = FXCollections.observableArrayList();
		tableView.setItems(removeList);

		loadSaleVOList.removeAll(loadSaleVOList);
		addSaleVOList.removeAll(addSaleVOList);
		afterDeleteSaleVOList.removeAll(afterDeleteSaleVOList);

		tfCount.setText(null);
		tfComents.setText(null);
		tfComents.setPromptText("��� (10���� �̳�)");
		tfCount.setPromptText("����");
		comboBox.setItems(removeComboList);
		datePicker.setValue(null);
		comboBoxSettion();
		barChart.getData().clear();

		buttonInitSetting(false, true, true, true, false, true);

	} // end of handlerButtonRemoveTableAction
	
	/********************
	 * 
	 * ��� : ��ư���� disable ���� �����Ѵ�.
	 * 
	 * ������
	 * 
	 */
	private void buttonInitSetting(boolean btCheck, boolean btSaleAdd, boolean btSaleDelete, boolean btSelectRowDelete,
			boolean btMenuCheck, boolean btSaleTotalPrice) {

		this.btCheck.setDisable(btCheck);
		this.btSaleAdd.setDisable(btSaleAdd);
		this.btSaleDelete.setDisable(btSaleDelete);
		this.btSelectRowDelete.setDisable(btSelectRowDelete);
		this.btMenuCheck.setDisable(btMenuCheck);
		this.btSaleTotalPrice.setDisable(btSaleTotalPrice);

	} // end of buttonInitSetting
	
	/**************************
	 * 
	 * ��� : ��ǰ�� �׸���� ������ ���̽����� �ҷ��´�.
	 * 
	 * ������
	 * 
	 */
	private void loadTotalGoodsDB() {
		try {
			GoodsDAO goodsDVO = new GoodsDAO();
			ArrayList<GoodsVO> fetchedList = goodsDVO.getGoodsTotal();
			ArrayList<GoodsVO> goodsList = new ArrayList<GoodsVO>();
			for (int i = 0; i < fetchedList.size(); i++) {
				GoodsVO goods = fetchedList.get(i);
				goodsVOList.add(goods);
				goodsList.add(goods);
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
		}

	} // end of loadTotalGoodsDB

	/************************
	 * 
	 * ��� : ����â�� ���̺� �並 �����Ѵ�. 2019 10 �� 18 �� �ۼ��� : ������
	 * 
	 * ������ 
	 * 
	 */
	private void tableViewSetting() {

		tableView.setEditable(false);

		TableColumn columnDate = new TableColumn("��¥");
		columnDate.setMaxWidth(150);
		columnDate.setStyle("-fx-alignment: CENTER;");
		columnDate.setCellValueFactory(new PropertyValueFactory("date"));

		TableColumn columnGoods = new TableColumn("��ǰ");
		columnGoods.setMaxWidth(160);
		columnGoods.setStyle("-fx-alignment: CENTER;");
		columnGoods.setCellValueFactory(new PropertyValueFactory("goods"));

		TableColumn columnPrice = new TableColumn("����");
		columnPrice.setMaxWidth(80);
		columnPrice.setStyle("-fx-alignment: CENTER;");
		columnPrice.setCellValueFactory(new PropertyValueFactory("price"));

		TableColumn columnCount = new TableColumn("����");
		columnCount.setMaxWidth(60);
		columnCount.setStyle("-fx-alignment: CENTER;");
		columnCount.setCellValueFactory(new PropertyValueFactory("count"));

		TableColumn columnTotal = new TableColumn("�Ǹ� �ݾ�");
		columnTotal.setMaxWidth(100);
		columnTotal.setStyle("-fx-alignment: CENTER;");
		columnTotal.setCellValueFactory(new PropertyValueFactory("total"));

		TableColumn columnComents = new TableColumn("���");
		columnComents.setMaxWidth(200);
		columnComents.setStyle("-fx-alignment: CENTER;");
		columnComents.setCellValueFactory(new PropertyValueFactory("coments"));

		tableView.getColumns().addAll(columnDate, columnGoods, columnPrice, columnCount, columnTotal, columnComents);

	} // end of tableViewSetting

	/************************
	 * 
	 * ��� : �� ��Ʈ�� ���� ǥ�� 2019 10 �� 19 �� ���� : 2019 10 �� 20 �� ������ ���� ���� : �ش�
	 * ObservableList �� �޾Ƽ� ��Ʈ�� �����ش�.
	 * 
	 * �ۼ��� : ������
	 * 
	 * @param dateSelectList2
	 * 
	 */
	private void barChartSetting(ObservableList<SaleVO> observableList) {

		try {

			ObservableList<XYChart.Data<String, Integer>> barChartList = FXCollections.observableArrayList();
			XYChart.Series<String, Integer> series = new XYChart.Series<>();

			barChart.setTitle(localDateStr + "����");

			if (!(barChartList.equals(null))) {

				barChartList.removeAll(barChartList);

			}

			observableList = observableList.sorted();

			SaleDAO saleDAO = new SaleDAO();
			ObservableList<SaleVO> barChartData = saleDAO.barChartData(observableList.get(0).getDate());
			
			for (int i = 0; i < barChartData.size(); i++) {

				barChartList.add(new XYChart.Data<String, Integer>(barChartData.get(i).getGoods(),
						(barChartData.get(i).getTotal())));

			}

			series.getData().clear();
			barChart.getData().clear();

			series.setName(localDateStr);
			series.setData(barChartList);
			barChart.getData().add(series);

		} catch (SQLException e) {

		}

	} // end of barChartSetting

	/**************************************
	 * 
	 * ��� : ��� ���� â�� ��Ʈ�� �����Ѵ�.
	 * 
	 * �¼�
	 * 
	 */
	private void iMbarChartSetting() {

		try {

			ObservableList<XYChart.Data<String, Integer>> barChartList = FXCollections.observableArrayList();
			XYChart.Series<String, Integer> series = new XYChart.Series<>();

			stackedBarChart.setTitle(localString + "��� ��Ȳ");

			if (!(barChartList.equals(null))) {
				barChartList.removeAll(barChartList);
			}

			iMTableDAO iMTableDAO = new iMTableDAO();
			ObservableList<iMTableVO> setDataForBarChart = iMTableDAO.barChartDatabase(localString);

			for (int i = 0; i < setDataForBarChart.size(); i++) {
				barChartList.add(new XYChart.Data<String, Integer>(setDataForBarChart.get(i).getName(),
						(setDataForBarChart.get(i).getEa())));
			}

			series.getData().clear();
			stackedBarChart.getData().clear();
			
			series.setName(localString);
			series.setData(barChartList);
			stackedBarChart.getData().add(series);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**************************
	 * 
	 * ��� : ��� ���� â�� ���õ� ���̺� ���� �����Ѵ�.
	 * 
	 * ���¼�
	 * 
	 */
	private void handlerButtonDeleteAction(ActionEvent e) {

		try {
			iMTableDAO iMTableDAO = new iMTableDAO();
			boolean deleteCheck = iMTableDAO.deleteData(selectTableViewVO.get(0).getNo());

			if (deleteCheck) {

				alertWaringDisplay(2, "��������", "��� �����ϴµ� �����Ͽ����ϴ�.", " �����Ͽ����ϴ�.");
				ObservableList<iMTableVO> deleteAfterData = FXCollections
						.observableArrayList(iMTableDAO.getiMTableTBLTotal());
				data.removeAll(data);
				data.addAll(deleteAfterData);
				iMtableView.setItems(data);
			} else {

				alertWaringDisplay(2, "��������", "��� �����ϴµ� �����Ͽ����ϴ�.", " �����߽��ϴ�.");

			}
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return;
	}
	
	/**********************************
	 * 
	 * ��� : �޷��� �����ϸ� �ش� ��¥�� �����͸� �ҷ��ͼ� ���̺� �信 �����ش�.
	 * 
	 * ���¼�
	 * 
	 */
	private void handlerDatePickerAction(ActionEvent e) {

		try {
			local = datePickerIMSelect.getValue();
			if (local.getDayOfMonth() < 10) {
				localString = String.valueOf(local.getYear()) + "-" + String.valueOf(local.getMonthValue()) + "-" + "0"
						+ String.valueOf(local.getDayOfMonth());
			} else {
				localString = String.valueOf(local.getYear()) + "-" + String.valueOf(local.getMonthValue()) + "-"
						+ String.valueOf(local.getDayOfMonth());
			}

			
			iMTableDAO iMTableDAO = new iMTableDAO();
			ObservableList<iMTableVO> loadToDateList = iMTableDAO.getListToDate(localString);
			
			iMtableView.setItems(loadToDateList);
			
			iMbarChartSetting();

		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// 1. ��� Ȯ�� ��ư�� ������ �� �߰� �޴�â ����
	private void handlerButtonIM(ActionEvent e) {

		try {
			AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/imView.fxml"));
			Stage stageDialog = new Stage(StageStyle.UTILITY);

			stageDialog.initModality(Modality.WINDOW_MODAL);
			stageDialog.initOwner(buttonIM.getScene().getWindow());
			stageDialog.setTitle("ǰ�� ��Ͽ� ���� ���� ȯ���մϴ�.");
			stageDialog.setResizable(false);

			Scene scene = new Scene(anchorPane);

			stageDialog.setScene(scene);
			stageDialog.show(); // window show

			DatePicker datethis = (DatePicker) anchorPane.lookup("#datethis");
			TextField iMNum = (TextField) anchorPane.lookup("#iMNum");
			ComboBox iMName = (ComboBox) anchorPane.lookup("#iMName");
			TextField iMPay = (TextField) anchorPane.lookup("#iMPay");
			Button iMOk = (Button) anchorPane.lookup("#iMOk");
			Button iMClose = (Button) anchorPane.lookup("#iMClose");

			/************************ �Է����� *************************/

			DecimalFormat format = new DecimalFormat("######");
			// �ܰ� �ڸ��� �Ǵ� ���� ����(�Է�)
			iMPay.setTextFormatter(new TextFormatter<>(e2 -> {

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

			DecimalFormat format1 = new DecimalFormat("###");
			// ���� �ڸ��� �Ǵ� ���� ����(�Է�)
			iMNum.setTextFormatter(new TextFormatter<>(e2 -> {

				if (e2.getControlNewText().isEmpty()) {
					return e2;
				}

				ParsePosition parsePosition = new ParsePosition(0);
				Object object = format1.parse(e2.getControlNewText(), parsePosition);

				if (object == null || parsePosition.getIndex() < e2.getControlNewText().length()
						|| e2.getControlNewText().length() == 4) {
					return null;
				} else {
					return e2;
				}

			}));

			/********************* �Է����ѹ� �������� ********************/

			// list ComboBox�� ���ڿ� ���� ����Ʈ ���� �ִ´�.
			ObservableList list = FXCollections.observableArrayList();

			list.add("����");
			list.add("���");
			list.add("����");
			list.add("����");
			list.add("Ƣ����");
			iMName.setItems(list);

			// ǰ���� �߰��� �� ��¥ ����
			datethis.setOnAction(e1 -> {

				LocalDate date = datethis.getValue(); // ���ڸ� ���������� ���� ����ش�.
//				datethis.setText("" + date);

			});

			// ��� ����Ʈ �ۼ��ϰ� ����ϱ� ��ư�� ������ ��
			iMOk.setOnAction(e1 -> {

//					// ��� ������ ��� �Է½� ����
				if (datethis.getValue().equals("") || iMNum.getText().equals("") || iMName.getValue().equals("")
						|| iMPay.getText().equals("")) {

					alertWaringDisplay(1, "��� ���", "����Ͽ� �����Ͽ����ϴ�.", "������ ��� �Է����ֽʽÿ�.");
					return;

				} else {

					// DB�� �Է� ������ �����Ѵ�.
					iMTableVO imTableVO = new iMTableVO(datethis.getValue().toString(),
							iMName.getSelectionModel().getSelectedItem().toString(), Integer.parseInt(iMPay.getText()),
							Integer.parseInt(iMNum.getText()));

					iMTableDAO imTableDAO = new iMTableDAO();

					imTableDAO.insertClientIM(imTableVO);

					alertWaringDisplay(1, "���ϵ帳�ϴ�.", "����Ͽ� �����Ͽ����ϴ�.", "���� �ð��ǽʽÿ�");
				} // end of if

				stageDialog.close(); // this is window close
				flagCheck = false; // this is �� �� �����

				iMOk.setOnAction(e2 -> {

					((Stage) iMClose.getScene().getWindow()).close();

				});
			});

			// 1) ����� â �ݱ� ��ư
			iMClose.setOnAction(e1 -> {

				((Stage) iMClose.getScene().getWindow()).close();

			}); // 1) end of ����� â �ݱ� ��ư

		} catch (IOException e1) {

			e1.printStackTrace();

		}

	}

	private void alertWaringDisplay(int type, String title, String headerText, String contentText) {

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

	// 2. �ݱ� ��ư�� ������ �� ������ â �ݱ�
	private void handlerButtonIMClose(ActionEvent e) {

		((Stage) buttonIMClose.getScene().getWindow()).close();

	}

	// 3. �˻� ��ư
	private void handlerButtonIMSearch(ActionEvent e) {

		try {
			if (textFieldIMSearch.getText().equals("")) {
				return;
			}
			ObservableList<iMTableVO> listIM = FXCollections.observableArrayList();
			iMTableDAO imTableDAO = new iMTableDAO();

			listIM = imTableDAO.getSearchData(textFieldIMSearch.getText());
			System.out.println(listIM.toString());
			if (listIM.isEmpty()) {

				throw new Exception("�˻�����");

			} else {
				data.removeAll(data);
				data.addAll(listIM);
				iMtableView.setItems(data);
			}

		} catch (Exception e1) {

			alertWaringDisplay(1, "�˻����", "�̸��˻�����", e1.toString());

		}

	}

	// 4. ��ü �˻� ��ư
	private void handlerButtonIMSearchAll(ActionEvent e) {

		ArrayList<iMTableVO> listIM = null;
		iMTableDAO imTableDAO = new iMTableDAO();
		iMTableVO imTableVO = null;

		listIM = imTableDAO.getiMTableTBLTotal();

		try {

			if (listIM == null) {

				throw new Exception("�˻�����");

			}

			data.removeAll(data);

			for (iMTableVO svo : listIM) {

				data.add(svo);

			}

			iMtableView.setItems(data);
			datePickerIMSelect.setValue(null);

		} catch (Exception e1) {

			alertWaringDisplay(1, "�˻����", "�̸��˻�����", e1.toString());

		}

	}

	// 5. ��Ʈ
	private void handlerTableView() {

		data = FXCollections.observableArrayList();

		// 5.1 ���̺� �信�� ����Ʈ ���̱� �ۼ�
		TableColumn colNo = new TableColumn("NO");
		colNo.setMaxWidth(75);
		colNo.setStyle("-fx-alignment: CENTER;");
		colNo.setCellValueFactory(new PropertyValueFactory("no"));

		TableColumn colDate = new TableColumn("��¥");
		colDate.setMaxWidth(130);
		colDate.setStyle("-fx-alignment: CENTER;");
		colDate.setCellValueFactory(new PropertyValueFactory("date"));

		TableColumn colName = new TableColumn("ǰ��");
		colName.setMaxWidth(130);
		colName.setStyle("-fx-alignment: CENTER;");
		colName.setCellValueFactory(new PropertyValueFactory("name"));

		TableColumn colPay = new TableColumn("�ܰ�");
		colPay.setMaxWidth(80);
		colPay.setStyle("-fx-alignment: CENTER;");
		colPay.setCellValueFactory(new PropertyValueFactory("pay"));

		TableColumn colEa = new TableColumn("����");
		colEa.setMaxWidth(75);
		colEa.setStyle("-fx-alignment: CENTER;");
		colEa.setCellValueFactory(new PropertyValueFactory("ea"));

		TableColumn colRemakers = new TableColumn("���");
		colRemakers.setMaxWidth(100);
		colRemakers.setStyle("-fx-alignment: CENTER;");
		colRemakers.setCellValueFactory(new PropertyValueFactory("remakers"));

		iMtableView.setItems(data);
		iMtableView.getColumns().addAll(colNo, colDate, colName, colPay, colEa, colRemakers);

	}
}