package Application.MainApp;

import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class CustomController extends HBox {


    @FXML
    private HBox hbox;

    @FXML
    private Label nameLabel;

    @FXML
    private Label quantityLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private Label colourLabel;



    File file;

    public CustomController(File f) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("customElement.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        file = f;

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }


    }

    public void setNameLabel(String s) {
        nameLabel.setText(s);
    }

    /*
    Put in initialize because if we put it in constructor the label is null still!
     */
    @FXML
    void initialize() {
        this.setNameLabel(file.getName());

        hbox.setOnMouseEntered(e-> hbox.setStyle("-fx-background-color:#3384f3;"));

        hbox.setOnMouseExited(e-> hbox.setStyle("-fx-background-color:rgba(0,0,0,0);"));
    }
}
