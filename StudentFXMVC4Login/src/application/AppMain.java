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
		//1.~3. 번까지 진행한 root.fxml을 가져온다. 
		Parent hBox =FXMLLoader.load(getClass().getResource("/view/login.fxml"));
		// 4. 루트 컨테이너를 scene에 집어넣는다.
		Scene scene = new Scene(hBox);
		//5. scene에 외부스타일을 집어넣는다.
		//scene.getStylesheets().add(getClass().getResource("app.css").toString());
		//6. stage에 scene 를 집어넣는다.
		primaryStage.setTitle("로그인");
		primaryStage.setScene(scene);
		primaryStage.setResizable(true);
		primaryStage.show();
	}
}
