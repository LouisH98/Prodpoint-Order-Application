package Application.CreateNewOrder;

import Application.AlertHandler;
import Application.MainApp.MainController;
import Application.PropertiesHandler;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;


public class NewOrderController {

    @FXML
    private TextField orderNumberField;

    @FXML
    private TextField clientNameField;

    @FXML
    private DatePicker dueDatePicker;

    @FXML
    private JFXButton createOrderButton;


    private MainController mainAppController;


    public void setMainController(MainController mainController) {
        mainAppController = mainController;
    }

    /*
    Function to create a new folder & properties file for an order
     */
    @FXML
    void createNewOrder(ActionEvent event) {

        boolean problem = false;

        //check boxes aren't empty
        if (orderNumberField.getText().length() == 0) {
            orderNumberField.setStyle("-fx-border-color: red");
            problem = true;
        }
        if (clientNameField.getText().length() == 0) {
            clientNameField.setStyle("-fx-border-color: red");
            problem = true;
        }
        if (dueDatePicker.getEditor().getText().length() == 0) {
            dueDatePicker.setStyle("-fx-border-color: red");
            problem = true;
        }

        if (problem) {
            return;
        }

        /*
        If there are no problems with the text boxes, we can then check if the order already exists.
        If it does then show the error message, if not then create the folder and save the properties
        of the order in there.
        Then we can set the main app's directory to point to this folder & close the window.
         */

        String orderNumberString = this.orderNumberField.getText();

        File orderFolder = new File(PropertiesHandler.getOrdersFolder().getPath() + "/" + orderNumberString);
        //Check if an order with the same name exists
        if (orderFolder.exists()) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Error", "Order Creation Failed",
                    "Order folder already exists, please try a different name");
        } else {

            boolean created = orderFolder.mkdirs();
            if (created) {
                System.out.println("Folder/s created");
            }

            //save the properties
            try {
                PropertiesHandler.saveOrderInfo(orderFolder, orderNumberString, clientNameField.getText(),
                        dueDatePicker.getValue(), PropertiesHandler.ORDER_STATUS_PROCESSING);
            } catch (IOException e) {
                System.out.println("Could not save order info");
            }


            //set the directory on the main controller
            mainAppController.setDirectory(orderFolder);



            //close the window
            Stage stage = (Stage) orderNumberField.getScene().getWindow();
            stage.close();
        }
    }


    @FXML
    void initialize() {
        //put this in a runLater because FXML components have not been injected yet
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //request focus on the button so we can see the placeholder text
                createOrderButton.requestFocus();

                //disable dates in the past
                dueDatePicker.setDayCellFactory(picker -> new DateCell() {
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        LocalDate today = LocalDate.now();

                        setDisable(empty || date.compareTo(today) < 0);
                    }
                });
            }
        });
    }
}
