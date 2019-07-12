package Application.Welcome;

import Application.MainApp.MainController;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeController {

    @FXML
    private JFXButton createNewButton;

    @FXML
    private JFXButton loadOrderButton;


    FXMLLoader openMainWindow(){
        try{

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Application/MainApp/main.fxml"));
            Parent mainScene = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Main Window");
            stage.setScene(new Scene(mainScene));
            stage.sizeToScene();
            stage.show();

            return loader;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    void createNewOrder(ActionEvent event) {

        //open the window and get a reference to the controller
        MainController controller = openMainWindow().getController();

        //simulate calling a function

        //hide this window
        ((Node)(event.getSource())).getScene().getWindow().hide();


    }


    @FXML
    void loadOrder(ActionEvent event) {
        MainController controller = openMainWindow().getController();

        //simulate loading a file

        //hide the window
        ((Node)(event.getSource())).getScene().getWindow().hide();

    }

}
