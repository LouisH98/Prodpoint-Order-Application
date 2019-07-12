package Application.MainApp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.File;

public class MainController {

    @FXML
    private VBox contentBox;

    @FXML
    private Button addElementButton;

    @FXML
    private Button removeAllButton;

    @FXML
    void addElement(ActionEvent event) {
        File f = new File("/Users/Louis/Desktop/stls/img/fulcrum ring.stl.png");
        contentBox.getChildren().add(new CustomController(f));
        System.out.println(contentBox.getChildren().size());
    }

    @FXML
    void removeAllElements(ActionEvent event) {
        contentBox.getChildren().clear();
    }

}


