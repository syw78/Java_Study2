package application;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppMain extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//1.~3. ������ ������ root.fxml�� �����´�. 
		Parent hBox =FXMLLoader.load(getClass().getResource("/view/login.fxml"));
		// 4. ��Ʈ �����̳ʸ� scene�� ����ִ´�.
		Scene scene = new Scene(hBox);
		//5. scene�� �ܺν�Ÿ���� ����ִ´�.
		//scene.getStylesheets().add(getClass().getResource("app.css").toString());
		//6. stage�� scene �� ����ִ´�.
		primaryStage.setTitle("�α���");
		primaryStage.setScene(scene);
		primaryStage.setResizable(true);
		primaryStage.show();
	}
}
