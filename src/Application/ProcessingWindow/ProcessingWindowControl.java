package Application.ProcessingWindow;

import Application.AlertHandler;
import Application.MainApp.MainController;
import Application.STLProcessor;
import com.jfoenix.controls.JFXButton;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProcessingWindowControl {

    File stlDirectory;

    File projectDirectory;

    MainController mainWindow;

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
        stlDirectory = chooser.showDialog(selectFolderButton.getScene().getWindow());

        if(stlDirectory != null){
            if(STLProcessor.countSTL(stlDirectory) == 0){
                processingLab.setText("No STL's found. Please try again.");
            }
            else{
                importAndProcessFiles();
            }
        }
    }

    public void setProjectDirectory(File dir){
        this.projectDirectory = dir;
    }

    public void setMainWindow(MainController controller){
        mainWindow = controller;
    }

    public static File importFile(String from, String to) throws Exception {
        Path src = Paths.get(from);
        Path dest = Paths.get(to);
        Files.copy(src, dest);

        return dest.toFile();
    }

    /*
    This function calls the stl-thumb command for each file in the stlDirectory (in a new thread) and shows the progress on a bar.
     */
    public void importAndProcessFiles(){

        Stage stage = (Stage) progressBar.getScene().getWindow();
        stage.setTitle("Importing");
        //copy all STL files to the project directory
        File[] srcFiles = stlDirectory.listFiles();
        if(srcFiles != null){
            for(File f : srcFiles){
                try{
                    importFile(f.getPath(), projectDirectory.getPath() + "/" + f.getName());

                } catch (Exception e) {
                    System.out.println("Failed to import file: " + f.getName());
                }
            }
        }


        if(stlDirectory.isDirectory() && stlDirectory != null) {
            progressBar.setVisible(true);
            selectFolderButton.setVisible(false);
            processingLab.setText("Processing...");
            progressLabel.setVisible(true);



            //carry out the processing on a separate thread
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    //get list of files and call processFile for each one
                    File[] filesInDirectory = projectDirectory.listFiles();

                    if(projectDirectory != null && projectDirectory.isDirectory() && filesInDirectory != null){
                        int count = 0;
                        int numFiles = STLProcessor.countSTL(projectDirectory);
                        for(File f : filesInDirectory){
                            String result = STLProcessor.makeThumbnail(f);

                            if(result.length() > 0 ){
                                updateMessage(f.getName());
                                this.cancel();
                            }

                            if (STLProcessor.isSTL(f)) {
                                updateProgress(++count, numFiles);
                                updateMessage(count + " of " + numFiles);
                            }
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
                AlertHandler.showAlert(Alert.AlertType.INFORMATION, "Files imported", STLProcessor.countSTL(projectDirectory) + " files imported","The STL files have been imported, the original folder can now be moved / deleted");

                mainWindow.generateColumns(true);

                //close this window
                progressLabel.getScene().getWindow().hide();

            });
        }
    }

}
