package Application.MainApp;

import Application.*;
import Application.CreateNewOrder.NewOrderController;
import Application.ProcessingWindow.ProcessingWindowControl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainController {

    /*
    Injected FXML Components
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


    /*

     */
    public void setDirectory(File directory) {
        this.directory = directory;
        infoLabel.setText("Please import some files\n" +
                "(File -> Import STL Files)");

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

        //TODO Check if valid project directory

        //clear the table
        projectFiles.clear();
        fileTable.getItems().clear();
    }

    public void setMainApp(Main main) {
        this.mainApp = main;
    }

    public void generateColumns(boolean isNew) {
        File[] stlFiles = directory.listFiles();
        if (stlFiles != null) {
            for (File f : stlFiles) {
                if (STLProcessor.isSTL(f)) {
                    STLFile currentSTLFile;
                    //if we are creating a new project then
                    if (isNew) {
                        currentSTLFile = new STLFile(f);
                        try {
                            //save the file properties for new file
                            PropertiesHandler.saveFileInfo(currentSTLFile);
                            currentSTLFile.initAvailableColours();
                            projectFiles.add(currentSTLFile);
                        } catch (IOException e) {
                            System.out.println("Failed to save file info to file");
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
            }

            //now add all the STL files in the observable array to the Table
            fileTable.setItems(projectFiles);
        }
    }

    //this function updates the bottom info labels with the info from the order
    public void setOrderInfoLabels(){
        OrderInfoWrapper orderInfo = null;
        try{
            orderInfo = PropertiesHandler.getOrderInfo(directory);
        }
        catch (IOException e){
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Could not load order info", "Could not load order info. Please try again.");
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

            //TODO set status button to status
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
    void openProject(ActionEvent event) {
        DirectoryChooser projectChooser = new DirectoryChooser();
        projectChooser.setInitialDirectory(PropertiesHandler.getOrdersFolder());
        projectChooser.setTitle("Chose Project");
        File projectDirectory = projectChooser.showDialog(infoLabel.getScene().getWindow());

        if (projectDirectory != null) {
            if (projectDirectory.isDirectory()) {
                setDirectory(projectDirectory);
                generateColumns(false);
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


        /*
        Cell factories for custom cells
         */

        //custom cell factory to open the file with default program
        imagePreviewColumn.setCellFactory(param -> {
            TableCell<STLFile, ImageView> cell = new TableCell<STLFile, ImageView>() {
                @Override
                protected void updateItem(ImageView item, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                    } else {
                        STLFile stlFile = (STLFile) getTableRow().getItem();

                        ImageView imageView = stlFile.getPreview();
                        setGraphic(imageView);

                        setOnMouseClicked(event -> {
                            if (event.getClickCount() == 2) {
                                try {
                                    Desktop.getDesktop().open(stlFile.getFile());
                                } catch (IOException e) {
                                    AlertHandler.showAlert(Alert.AlertType.ERROR, "Could not open the STL file", "Could not open the STL file");
                                }
                            }
                        });
                    }
                }
            };

            return cell;
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
            Event Handler to allow verification of content when enter pressed
            (Checks if value can be parsed to integer)
             */
            EventHandler<KeyEvent> enterKeyEventHandler = event -> {
                if(cell.getTableRow().getItem() != null){
                    STLFile file = (STLFile) cell.getTableRow().getItem();

                    if (event.getCode() == KeyCode.ENTER) {

                        try {
                            int newValue = Integer.parseInt(spinner.getEditor().textProperty().get());

                            file.setQuantity(newValue);
                        }
                        catch (NumberFormatException e) {
                            //reset the text to original value
                            spinner.getEditor().setText(file.getQuantity() + "");
                        }
                    }
                    event.consume();
                }
            };


            spinner.getEditor().addEventHandler(KeyEvent.KEY_PRESSED, enterKeyEventHandler);

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

                    //first check if the text is a valid integer
                    try{
                        int newItemQuantity = Integer.parseInt(
                                spinner.getEditor().getText()
                        );

                        spinner.increment(0); // won't change value, but will commit editor

                    }
                    catch (NumberFormatException e){
                        if(cell.getTableRow().getItem() != null){
                            STLFile file = (STLFile) cell.getTableRow().getItem();
                            AlertHandler.showAlert(Alert.AlertType.ERROR, "Invalid Number", "Please enter a valid number");
                            spinner.getEditor().setText(file.getQuantity() + "");
                        }
                    }
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
    }
}

