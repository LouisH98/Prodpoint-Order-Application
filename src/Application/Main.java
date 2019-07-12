package Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Welcome/welcome.fxml"));
        primaryStage.setTitle("Prodpoint Order Application");
        primaryStage.setScene(new Scene(root));
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.show();

        root.getStylesheets().add(getClass().getResource("light_style.css").toExternalForm());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
