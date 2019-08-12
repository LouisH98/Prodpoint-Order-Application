package Application;

import Application.MainApp.MainController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    Parent root;
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainApp/main.fxml"));
        root = loader.load();
        primaryStage.setTitle("Prodpoint Order Application");
        primaryStage.setScene(new Scene(root));
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);

        MainController mainController = loader.getController();
        mainController.setMainApp(this);


        primaryStage.show();
    }


    /*
    Function to open a scene on a new window
     */
    public FXMLLoader openWindow(String sceneURI, String title){
        try{
            FXMLLoader loader = new FXMLLoader(Main.class
                    .getResource(sceneURI));
            Parent myScene = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(myScene));
            stage.sizeToScene();
            stage.setResizable(false);
            stage.show();


            return loader;

        }
        catch (IOException e){
            e.printStackTrace();
        }

        return null;

    }

    public static void main(String[] args) {
        launch(args);
    }
}
