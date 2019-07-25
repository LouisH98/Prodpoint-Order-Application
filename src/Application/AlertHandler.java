package Application;

import javafx.scene.control.Alert;


/*
Class to handle alerts
 */
public class AlertHandler {

    public static void showAlert(Alert.AlertType type, String title, String header, String message){
        Alert alert = new Alert(type, message);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.show();
    }

    public static void showAlert(Alert.AlertType type, String title, String message){
        Alert alert = new Alert(type, message);
        alert.setTitle(title);
        alert.show();
    }
}
