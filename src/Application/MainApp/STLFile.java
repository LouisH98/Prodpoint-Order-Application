package Application.MainApp;

/*
Class by Louis Holdsworth

This class represents each STL file imported
Contains information on:
- The actual file
- The plasticType of plastic
- The color of the plastic
- The quantity of the item
- A reference of the image
*/



import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class STLFile {
    public static String[] PLASTIC_TYPES = {"PLA", "ABS", "TPU", "PETG"};
    public static int PLASTIC_PLA = 0;
    public static int PLASTIC_ABS = 1;
    public static int PLASTIC_TPU = 2;
    public static int PLASTIC_PETG = 3;
    //TODO load these from file
    public static String[] PLA_COLOURS = {"Black", "Light Grey", "Grey", "White",
            "Silver", "Gold", "Red", "Orange",
            "Yellow", "Green", "Blue", "Matt Black",
            "Matt White", "Matt Moss Grey", "Cosmic Silver",
            "Cosmic Gold", "Cosmic Grey", "Cosmic Red", "Cosmic Blue"};
    public static String[] ABS_COLOURS = {"Black", "White"};
    public static String[] TPU_COLOURS = {"Black", "Sky Blue"};
    public static String[] PETG_COLOURS = {"Black"};
    public static Map<String, String[]> COLOUR_MAP;
    public static String[] RESOLUTIONS = {"0.2", "0.15", "0.1"};

    //fill the map
    static {
        COLOUR_MAP = new HashMap<>();
        COLOUR_MAP.put(PLASTIC_TYPES[PLASTIC_PLA], PLA_COLOURS);
        COLOUR_MAP.put(PLASTIC_TYPES[PLASTIC_ABS], ABS_COLOURS);
        COLOUR_MAP.put(PLASTIC_TYPES[PLASTIC_TPU], TPU_COLOURS);
        COLOUR_MAP.put(PLASTIC_TYPES[PLASTIC_PETG], PETG_COLOURS);

        //make the map unmodifiable
        COLOUR_MAP = Collections.unmodifiableMap(COLOUR_MAP);
    }

    private File file;
    private String fileName;
    private int quantity;
    private String plasticType;
    private StringProperty colour;
    private Image preview;
    private String resolution;
    private ObservableList<String> availableColours;

    public STLFile(File f){
        /*
        Set default values
         */
        this.file = f;
        this.plasticType = PLASTIC_TYPES[PLASTIC_PLA];
        this.colour = new SimpleStringProperty(PLA_COLOURS[0]);
        this.resolution = RESOLUTIONS[2];
        this.quantity = 1;
        this.fileName = f.getName();
        this.availableColours = FXCollections.observableArrayList();

        //try to load the image
        try{
            this.preview = new Image(new FileInputStream(f.getParentFile().getPath() + "/img/" + f.getName()+".png"));
        }
        catch (FileNotFoundException e){
            System.out.println("Image file for the file could not be found");
            e.printStackTrace();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getFile() {
        return file;
    }

    public String getColour() {
        return colour.get();
    }

    public void setColour(String colour) {
        this.colour.set(colour);
    }

    public void initAvailableColours(){this.availableColours.setAll(COLOUR_MAP.get(this.plasticType));}

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPlasticType() {
        return plasticType;
    }

    public void setPlasticType(String plasticType) {
        this.plasticType = plasticType;
        availableColours.setAll(COLOUR_MAP.get(plasticType));
    }

    public void resetColour(){
        this.colour.set(COLOUR_MAP.get(plasticType)[0]);
    }

    public StringProperty getColourProperty(){
        return colour;
    }

    public ImageView getPreview(){
        return new ImageView(preview);
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public ObservableList<String> getAvailableColours(){
        return this.availableColours;
    }
}
