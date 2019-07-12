package Application.MainApp;

/*
Class by Louis Holdsworth

This class represents each STL file imported
Contains information on:
- The actual file
- The plasticType of plastic
- The color of the plastic
- The quantity of the item
 */

import javafx.scene.paint.Color;

import java.io.File;

public class STLFile {
    private File file;
    private Color color;
    private int quanitity;
    private String plasticType;

    public STLFile(File f){
        this.file = f;
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

    public int getQuanitity() {
        return quanitity;
    }

    public void setQuanitity(int quanitity) {
        this.quanitity = quanitity;
    }

    public String getPlasticType() {
        return plasticType;
    }

    public void setPlasticType(String plasticType) {
        this.plasticType = plasticType;
    }
}
