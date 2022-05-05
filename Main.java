package todolist.application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import todolist.io.TarefaIO;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			TarefaIO.createFiles();
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/todolist/view/login.fxml"));
			Scene scene = new Scene(root,200,210);
			scene.getStylesheets().add(getClass().getResource("/todolist/view/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Login");
			primaryStage.setResizable(false);
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/todolist/imagens/lista.png")));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
