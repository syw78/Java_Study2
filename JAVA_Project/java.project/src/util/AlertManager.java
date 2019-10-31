package util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertManager {
	/*********************
	 * 
	 * ��� : ���� �׸���� ��Ƽ� ���� �س���
	 * 
	 * @author xim
	 *
	 */
	public enum AlertInfo {
		ERROR_LOAD_MEMBER(AlertType.ERROR, "����", "ȸ������ �ҷ����⿡ �����߽��ϴ�."),
		ERROR_LOAD_SCENE(AlertType.ERROR, "����", "ȭ�� �ε��� �����߽��ϴ�."),
		ERROR_UNKNOWN(AlertType.ERROR, "����", "�˼����� ������ �߻��߽��ϴ�."),
		ERROR_ACCESS_DB(AlertType.ERROR, "����", "�����ͺ��̽��� ���� �����߽��ϴ�."),
		ERROR_TASK_DB(AlertType.ERROR, "����", "�����ͺ��̽� �۾��� �����߽��ϴ�."), 
		ERROR_TASK(AlertType.ERROR, "����", "��û�۾��� �����߽��ϴ�."),
		ERROR_TASK_EMPTY(AlertType.ERROR, "����", "�׸��� �����ϴ�."),
		ERROR_TASK_DUPLICATE(AlertType.ERROR, "����", "�ߺ��� ���� �ֽ��ϴ�."),
		ERROR_TASK_DELETE(AlertType.ERROR, "����", "���� �� ���� �׸��Դϴ�."),
		FAIL_LOGIN(AlertType.WARNING, "�α��� ����", "���̵� �Ǵ� ��й�ȣ�� Ȯ���ϼ���."),
		FAIL_SIGNUP_INCOMPLETE_FORM(AlertType.WARNING, "���� ����", "��� �׸��� �ۼ��� �ּ���."),
		FAIL_SIGNUP_DUPLICATE_ID(AlertType.WARNING, "���� ����", "�̹� ������� ���̵� �Դϴ�."),
		FAIL_SALE_ADD_UNCHECK(AlertType.WARNING, "��� ����", "Ȯ�� ��ư�� ���� �� �õ����ּ���."),
		FAIL_LOAD_BARCHART_DATA(AlertType.WARNING, "���", "�����Ͱ� �����ϴ�."),
		FAIL_LOAD_IMAGE(AlertType.WARNING, "�̹��� �ҷ����� ����", "�߸��� �̹��� �ּ��Դϴ�."),

		SUCCESS_TASK(AlertType.INFORMATION, "�۾� �Ϸ�", "��û�۾��� �Ϸ��߽��ϴ�."),
		SUCCESS_SIGNUP(AlertType.INFORMATION, "���� �Ϸ�", "ȸ�� ������ �Ϸ��߽��ϴ�."),
		SUCCESS_CHECK(AlertType.INFORMATION, "Ȯ�� �Ϸ�", "�׸� Ȯ���� �Ϸ��߽��ϴ�."),
		
		FAIL_SEARCH_DATA(AlertType.WARNING, "�˻� ����", "�˻��� ���� �߽��ϴ�. �׸��� ������ �˻� �� �ּ���.");
		
		private AlertType type;
		private String header;
		private String message;

		AlertInfo(AlertType type, String header, String message) {
			this.type = type;
			this.header = header;
			this.message = message;
		}
	}
	
	//�̱���
	static private AlertManager shared = new AlertManager();

	private AlertManager() {
	}

	static public AlertManager getInstance() {
		return shared;
	}

	public void show(AlertInfo info, AlertActionHandler actionHandler) {
		show(info.type, info.header, info.message, actionHandler);
	}

	public void show(AlertType type, String header, String message, AlertActionHandler actionHandler) {
		Alert alert = new Alert(type);

		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.setResizable(false);
		alert.showAndWait();

		if (actionHandler != null) {
			actionHandler.action(alert.getResult());
		}

	}
}