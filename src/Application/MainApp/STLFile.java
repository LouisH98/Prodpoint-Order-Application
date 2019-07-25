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

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class STLFile {
    private File file;
    private Color color;
    private int quantity;
    private String plasticType;
    private Image imageLocation;

    //TODO replace these with loaded options from a file
    public static String PLASTIC_ABS = "ABS";
    public static String PLASTIC_PLA = "PLA";
    public static String PLASTIC_PET = "PET";

    public STLFile(File f){
        this.file = f;

        //try to load the image
        try{
            this.imageLocation = new Image(new FileInputStream(f.getPath() + "/img/" + f.getName())+".png");
        }
        catch (FileNotFoundException e){
            System.out.println("Image file for the file could not be found");
        }

        color = Color.BLUE;
        quantity = 1;
        plasticType = PLASTIC_ABS;
    }

    public File getFile() {
        return file;
    }


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

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
    }
}
