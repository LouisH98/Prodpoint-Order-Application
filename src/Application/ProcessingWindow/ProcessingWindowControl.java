package Application.ProcessingWindow;

import Application.STLProcessor;
import Application.MainApp.MainController;

import com.jfoenix.controls.JFXButton;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ProcessingWindowControl {

    File directory;

    Stage stage;

    @FXML
    private Label processingLab;

    @FXML
    private Label progressLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private JFXButton selectFolderButton;

    FXMLLoader openMainWindow(){
        try{

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Application/MainApp/main.fxml"));
            Parent mainScene = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Order Logging Application");
            stage.setScene(new Scene(mainScene));
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
    private void setDirectory(){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("STL Folder Selection");
        directory = chooser.showDialog(selectFolderButton.getScene().getWindow());

        if(directory != null){
            if(STLProcessor.countSTL(directory) == 0){
                processingLab.setText("No STL's found. Please try again.");
            }
            else{
                processFiles();
            }
        }
    }

    public void processFiles(){

        Stage stage = (Stage) progressBar.getScene().getWindow();
        stage.setTitle("Processing");
        if(directory.isDirectory() && directory != null) {
            progressBar.setVisible(true);
            selectFolderButton.setVisible(false);
            processingLab.setText("Processing...");
            progressLabel.setVisible(true);

            //carry out the processing on a separate thread
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    //get list of files and call processFile for each one
                    if(directory != null && directory.isDirectory()){
                        int count = 0;
                        int numFiles = directory.listFiles().length;
                        for(File f : directory.listFiles()){
                            String result = STLProcessor.makeThumbnail(f);

                            if(result.length() > 0 ){
                                updateMessage(f.getName());
                                this.cancel();
                            }

                            updateProgress(++count, numFiles);
                            updateMessage(count + " of " + numFiles);

                        }
                    }
                    return null;
                }
            };

            progressBar.progressProperty().bind(task.progressProperty());
            progressLabel.textProperty().bind(task.messageProperty());

            Thread t = new Thread(task);
            t.setDaemon(true);
            t.start();

            task.setOnSucceeded(e->{
                //open main window and load files in
                MainController mainController = openMainWindow().getController();

                //TODO Populate the list
                mainController.setDirectory(directory);

                //close this window
                progressLabel.getScene().getWindow().hide();

            });
        }
    }
}
