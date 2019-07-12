package Application.MainApp;

import Application.STLProcessor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class MainController {

    @FXML
    private VBox contentBox;

    @FXML
    private Button addElementButton;

    @FXML
    private Button removeAllButton;

    @FXML
    private Label helpLab;

    @FXML
    private MenuItem selectFolder;

    private File directory;


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


    public void setDirectory(File directory) {
        this.directory = directory;
        System.out.println(directory.getName() + " selected");
        helpLab.setText(STLProcessor.countSTL(directory) + " STL files loaded");
    }

    public void populateFiles(){
        //TODO create a customElement for each file in the directory
    }
}


