package Application.MainApp;

import Application.*;
import Application.CreateNewOrder.NewOrderController;
import Application.ProcessingWindow.ProcessingWindowControl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainController {

    /*
    Injected FXML Components
     */

    /*
    TODO DEBUG REMOVE WHEN DEPLOYING
     */

    @FXML
    private MenuItem removeFiles;

    /*
    END DEBUG ITEMS
     */

    @FXML
    private MenuItem createNewOrderButton;

    @FXML
    private MenuItem openProjectButton;

    @FXML
    private MenuItem importFilesButton;

    @FXML
    private Label infoLabel;

    @FXML
    private TableView<STLFile> fileTable;

    @FXML
    private TableColumn<STLFile, ImageView> imagePreviewColumn;

    @FXML
    private TableColumn<STLFile, String> fileNameColumn;

    @FXML
    private TableColumn<STLFile, String> materialColumn;

    @FXML
    private TableColumn<STLFile, String> colourColumn;

    @FXML
    private TableColumn<STLFile, Integer> quantityColumn;

    @FXML
    private TableColumn<STLFile, String> resolutionColumn;

    @FXML
    private TableColumn<STLFile, String> notesColumn;

    @FXML
    private HBox orderInfoContainer;

    @FXML
    private Label orderNumberLabel;

    @FXML
    private Label clientNameLabel;

    @FXML
    private Label lastUpdatedLabel;

    @FXML
    private Label dueDateLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Button statusButton;

    private File directory;

    private Main mainApp;

    private ObservableList<STLFile> projectFiles = FXCollections.observableArrayList();


    //TODO REMOVE WHEN DONE
    @FXML
    void removeAllProjectFiles(ActionEvent event) {
        projectFiles.clear();
    }

    @FXML
    void debugRefreshTable(ActionEvent event) {
        fileTable.refresh();
    }

    /*
    Checks for a order_properties.xml file, returns true if one is found.
     */
    boolean isValidProjectDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.getName().equals("order_properties.xml")) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
    Sets the project directory - sets up labels and calls functions to generate columns
     */
    public void setDirectory(File directory) {
        this.directory = directory;
        infoLabel.setText("Please import some files\n" +
                "(Project -> Import STL Files)");

        //set the window title to reflect the directory change
        try {
            ((Stage) infoLabel.getScene().getWindow()).setTitle("(" + PropertiesHandler.getOrderID(directory) + ") - Prodpoint Order Application");
        } catch (IOException e) {
            System.out.println("Could not get orderID to set title");
            e.printStackTrace();
        }

        //allow the user to import files if the directory is valid
        if (directory.isDirectory()) {
            importFilesButton.setDisable(false);
            setOrderInfoLabels();
        }

        //clear the table
        projectFiles.clear();
    }

    public void setMainApp(Main main) {
        this.mainApp = main;
    }


    public void generateColumns(boolean isNewProject) {
        projectFiles.clear();
        ArrayList<File> stlFiles = STLProcessor.getSTLFiles(directory);
        for (File f : stlFiles) {
            STLFile currentSTLFile;
            //if we are creating a new project then create new objects
            if (isNewProject) {
                currentSTLFile = new STLFile(f);
                try {
                    //save the file properties for new file
                    PropertiesHandler.saveFileInfo(currentSTLFile);
                    currentSTLFile.initAvailableColours();
                    projectFiles.add(currentSTLFile);
                } catch (IOException e) {
                    System.out.println("Failed to save file info to file when generating table");
                    e.printStackTrace();
                }
            }
            //otherwise create STL objects with loaded info
            else {
                try {
                    currentSTLFile = PropertiesHandler.getFileInfo(f);
                    projectFiles.add(currentSTLFile);
                } catch (IOException e) {
                    System.out.println("Failed to load file info");
                }
            }
        }
        //now add all the STL files in the observable array to the Table
        fileTable.setItems(projectFiles);
    }

    //this function updates the bottom info labels with the info from the order
    public void setOrderInfoLabels(){
        OrderInfoWrapper orderInfo = null;
        try{
            orderInfo = PropertiesHandler.getOrderInfo(directory);
        }
        catch (IOException e){
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Could not load order info when setting labels", "Could not load order info. Please try again.");
        }
        //if we can get the info, set the labels
        if(orderInfo != null){
            orderNumberLabel.setText(orderInfo.getOrderID());
            clientNameLabel.setText( orderInfo.getClientName());
            lastUpdatedLabel.setText(orderInfo.getLastModified().toString());
            dueDateLabel.setText(orderInfo.getDueDate().toString());

            //set info labels to visible
            for(Node n : orderInfoContainer.getChildren()){
                n.setVisible(true);
            }
        }
    }

    @FXML
    void createNewOrder(ActionEvent e) {
        /*
        This function should open the dir selection to import the files
         */
        FXMLLoader loader = mainApp.openWindow("/Application/CreateNewOrder/NewOrderLayout.fxml", "Create new order");
        NewOrderController newOrderController = loader.getController();
        newOrderController.setMainController(this);
    }

    @FXML
    void openImportWindow(ActionEvent e) {
        FXMLLoader loader = mainApp.openWindow("/Application/ProcessingWindow/ProcessingLayout.fxml", "Import files");
        ProcessingWindowControl control = loader.getController();
        control.setProjectDirectory(this.directory);
        control.setMainWindow(this);
    }

    @FXML
    void importFileToProject(ActionEvent event) {

        //open file selector
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import STL File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("STL Files (*.stl)", "*.stl"));
        File newFile = fileChooser.showOpenDialog(lastUpdatedLabel.getScene().getWindow());

        /*
        Import the file into project directory
         */
        File importedFile = null;

        try {
            importedFile = ProcessingWindowControl.importFile(newFile.getPath(), directory.getPath() + "/" + newFile.getName());
        } catch (IOException e) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Could not import file", "Reason: " + e.getMessage());
        }


        if (importedFile != null) {
            /*
            Generate thumbnail
             */
            STLProcessor.makeThumbnail(importedFile);


            /*
            Make STLFile object and save the info
             */
            STLFile newSTLFile = new STLFile(importedFile);
            try {
                PropertiesHandler.saveFileInfo(newSTLFile);
            } catch (IOException e) {
                System.out.println("Could not save file info while importing single file");
            }

            fileTable.getItems().add(newSTLFile);
            fileTable.refresh();
        }
    }

    @FXML
    void openProject(ActionEvent event) {
        DirectoryChooser projectChooser = new DirectoryChooser();
        projectChooser.setInitialDirectory(PropertiesHandler.getOrdersFolder());
        projectChooser.setTitle("Chose Project");
        File projectDirectory = projectChooser.showDialog(infoLabel.getScene().getWindow());

        if (projectDirectory != null) {
            if (isValidProjectDirectory(projectDirectory)) {
                setDirectory(projectDirectory);
                generateColumns(false);
            } else {
                AlertHandler.showAlert(Alert.AlertType.ERROR, "Invalid Project Directory", "This folder doesn't appear to be a project directory. Please try choosing the folder again.");
            }
        }
    }


    @FXML
    void initialize() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                infoLabel.getScene().getStylesheets().add(getClass().getResource("/Application/light_style.css").toExternalForm());
            }
        });


        //Set Cell Value Factories for initial values
        imagePreviewColumn.setCellValueFactory(new PropertyValueFactory<>("preview"));
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        materialColumn.setCellValueFactory(new PropertyValueFactory<>("plasticType"));
        colourColumn.setCellValueFactory(new PropertyValueFactory<>("colour"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        resolutionColumn.setCellValueFactory(new PropertyValueFactory<>("resolution"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));



        /*
        Cell factories for custom cells
         */
        //custom cell factory to open the file with default program
        imagePreviewColumn.setCellFactory(param -> new TableCell<STLFile, ImageView>() {
            @Override
            protected void updateItem(ImageView item, boolean empty) {
                if (empty) {
                    setGraphic(null);
                } else {
                    STLFile stlFile = (STLFile) getTableRow().getItem();
                    if (stlFile != null) {
                        ImageView imageView = stlFile.getPreview();
                        setGraphic(imageView);

                        //open the file if double clicked
                        setOnMouseClicked(event -> {
                            if (event.getClickCount() == 1) {
                                try {
                                    Desktop.getDesktop().open(stlFile.getFile());
                                } catch (IOException e) {
                                    AlertHandler.showAlert(Alert.AlertType.ERROR, "Could not open the STL file", "Could not open the STL file");
                                }
                            }
                        });

                        setOnMouseEntered(event -> {
                            setCursor(Cursor.HAND);
                        });

                        setOnMouseExited(event -> {
                            setCursor(Cursor.DEFAULT);
                        });
                    }
                }
            }
        });

        materialColumn.setCellFactory(param -> {
            ComboBoxTableCell<STLFile, String> comboCell = new ComboBoxTableCell<STLFile, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                }
            };
            comboCell.getItems().setAll(STLFile.PLASTIC_TYPES);
            return comboCell;
        });

        materialColumn.setOnEditCommit(e -> {

            STLFile editedFile = e.getRowValue();
            editedFile.setPlasticType(e.getNewValue());
            editedFile.resetColour();


            try {
                PropertiesHandler.saveFileInfo(editedFile);
            } catch (IOException ex) {
                System.out.println("Could not save changes..");
            }
        });


        colourColumn.setCellFactory(param -> new ComboBoxTableCell<STLFile, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    if (!empty) {
                        STLFile stlFile = (STLFile) this.getTableRow().getItem();
                        if (stlFile != null) {
                            Platform.runLater(() -> getItems().setAll(stlFile.getAvailableColours()));
                        }
                    }
                }
            }
        });

        colourColumn.setOnEditCommit(e -> {
            if (e.getNewValue() != null) {
                STLFile file = e.getRowValue();
                file.setColour(e.getNewValue());

                try {
                    PropertiesHandler.saveFileInfo(file);
                } catch (IOException ex) {
                    System.out.println("Could not save changes..");
                }

            }
        });


        quantityColumn.setCellFactory(param -> {
            SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);
            Spinner<Integer> spinner = new Spinner<>(spinnerValueFactory);
            spinner.setEditable(true);

            /*
            Disallow any non-integers from being entered
             */
            spinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    spinner.getEditor().setText(newValue.replaceAll("[^\\d]", ""));
                }
            });

            TableCell<STLFile, Integer> cell = new TableCell<STLFile, Integer>() {
                @Override
                public void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        if (!empty) {
                            setText(item.toString());
                            STLFile stlFile = (STLFile) getTableRow().getItem();
                            if (stlFile != null) {
                                spinnerValueFactory.setValue(stlFile.getQuantity());
                            }
                        }
                    } else {
                        setGraphic(null);
                        setText(null);
                    }
                }

                @Override
                public void startEdit() {
                    super.startEdit();
                    if (!isEmpty()) {
                        setGraphic(spinner);
                        setText(null);
                    } else {
                        setText(null);
                    }
                }

                @Override
                public void cancelEdit() {
                    super.cancelEdit();
                    if (!isEmpty()) {
                        setGraphic(null);
                        setText(spinner.getValue().toString());
                    } else {
                        setText(null);
                    }
                }
            };


            /*
            When the spinner is toggled up or down, we want to save the changes.
             */
            spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (!oldValue.equals(newValue)) {
                    STLFile file = (STLFile) cell.getTableRow().getItem();

                    file.setQuantity(newValue);

                    try {
                        PropertiesHandler.saveFileInfo(file);
                    } catch (IOException ex) {
                        System.out.println("Could not save changes..");
                    }
                }
            });

            //save value on focus lost
            spinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    spinner.increment(0); // won't change value, but will commit editor
                }
            });

            spinner.setPrefWidth(90);
            return cell;
        });

        resolutionColumn.setCellFactory(param -> {
            ComboBoxTableCell<STLFile, String> comboCell = new ComboBoxTableCell<STLFile, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                }
            };
            comboCell.getItems().setAll(STLFile.RESOLUTIONS);
            return comboCell;
        });

        resolutionColumn.setOnEditCommit(e -> {

            STLFile editedFile = e.getRowValue();
            editedFile.setResolution(e.getNewValue());
            editedFile.resetColour();


            try {
                PropertiesHandler.saveFileInfo(editedFile);
            } catch (IOException ex) {
                System.out.println("Could not save changes..");
            }
        });

        notesColumn.setCellFactory(TextAreaTableCell.forTableColumn());
        notesColumn.setOnEditCommit(e -> {
            STLFile editedFile = e.getRowValue();
            System.out.println(e.getNewValue());
            editedFile.setNotes(e.getNewValue());

            //save the file
            try {
                PropertiesHandler.saveFileInfo(editedFile);
            } catch (IOException ex) {
                System.out.println("Could not save changes..");
            }
        });
    }
}

