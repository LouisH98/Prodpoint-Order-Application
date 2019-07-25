package Application.MainApp;

import Application.CreateNewOrder.NewOrderController;
import Application.Main;
import Application.ProcessingWindow.ProcessingWindowControl;
import Application.PropertiesHandler;
import Application.STLProcessor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainController {

    @FXML
    private MenuItem createNewOrderButton;

    @FXML
    private MenuItem loadOrderButton;

    @FXML
    private MenuItem importFilesButton;

    @FXML
    private Label infoLabel;

    private File directory;

    private Main mainApp;



    public void setDirectory(File directory) {
        this.directory = directory;
        infoLabel.setText("Please import some files\n" +
                "(File -> Import STL Files)");

        //set the window title to reflect the directory change
        try{
            ((Stage)infoLabel.getScene().getWindow()).setTitle("(" +PropertiesHandler.getOrderID(directory) + ") - Prodpoint Order Application");
        }
        catch (IOException e){
            System.out.println("Could not get orderID to set title");
            e.printStackTrace();
        }

        //allow the user to import files if the directory is valid
        if(directory != null){
            if(directory.isDirectory()){
                importFilesButton.setDisable(false);
            }
        }
    }

    public void setMainApp(Main main){
        this.mainApp = main;
    }

    @FXML
    void createNewOrder(ActionEvent e){
        /*
        This function should open the dir selection to import the files
         */
        FXMLLoader loader = mainApp.openWindow("/Application/CreateNewOrder/NewOrderLayout.fxml");
        NewOrderController newOrderController = loader.getController();
        newOrderController.setMainController(this);
    }

    @FXML
    void importSTLFiles(ActionEvent e){
        FXMLLoader loader = mainApp.openWindow("/Application/ProcessingWindow/ProcessingLayout.fxml");
        ((ProcessingWindowControl) loader.getController()).setProjectDirectory(this.directory);
    }


}


