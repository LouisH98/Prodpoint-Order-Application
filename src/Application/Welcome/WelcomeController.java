package Application.Welcome;

import Application.PropertiesHandler;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class WelcomeController {

    @FXML
    private JFXButton createNewButton;

    @FXML
    private JFXButton loadOrderButton;


    FXMLLoader openDirSelection(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/Application/ProcessingWindow/ProcessingLayout.fxml"));
            Parent myScene = loader.load();
            Stage stage = (Stage) createNewButton.getScene().getWindow();
            stage.setTitle("Select Folder");
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

    @FXML
    void createNewOrder(ActionEvent event) {

        openDirSelection();
//        PropertiesHandler.setDarkMode(true);
    }


    @FXML
    void loadOrder(ActionEvent event) {
//        MainController controller = openMainWindow().getController();

        //simulate loading a file

        //hide the window
        ((Node)(event.getSource())).getScene().getWindow().hide();

    }
}
