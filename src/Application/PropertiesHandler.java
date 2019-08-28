package Application;


import Application.MainApp.STLFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Properties;
import java.util.prefs.Preferences;

public class PropertiesHandler {

    public static final String USER_NODE = "ProdpointApp";
    public static final String DARK_MODE_KEY = "dark_mode_enabled";

    public static final String ORDER_PROPERTIES_FILE = "order_properties.xml";
    public static final File ORDERS_FOLDER = new File(System.getProperty("user.home") + "/Prodpoint Orders");

    public static final String ORDER_STATUS_PROCESSING = "PROCESSING";
    public static final String ORDER_STATUS_COMPLETED = "COMPLETED";

    public static File getOrdersFolder() {
        return ORDERS_FOLDER;
    }

    /*
    Used to get info from an order folder and parse it into a wrapper object
     */
    public static OrderInfoWrapper getOrderInfo(File orderDir) throws IOException {
        Properties properties = new Properties();
        FileInputStream inputStream = new FileInputStream(orderDir.getPath() + "/" + ORDER_PROPERTIES_FILE);
        properties.loadFromXML(inputStream);

        return new OrderInfoWrapper(
                properties.getProperty("orderID"),
                properties.getProperty("clientName"),
                LocalDate.parse(properties.getProperty("dueDate")),
                LocalDate.parse(properties.getProperty("lastModified")),
                properties.getProperty("status")
        );
    }

    /*
    Gets the orderID for a project directory
     */
    public static String getOrderID(File orderDir) throws IOException{
        Properties properties = new Properties();
        FileInputStream is = new FileInputStream(orderDir.getPath() + "/" + ORDER_PROPERTIES_FILE);
        properties.loadFromXML(is);
        return properties.getProperty("orderID");
    }

    public static void saveOrderInfo(File orderDir, String orderID, String clientName, LocalDate dueDate, String status) throws IOException{
        Properties properties = new Properties();
        properties.setProperty("orderID", orderID);
        properties.setProperty("clientName", clientName);
        properties.setProperty("dueDate", dueDate.toString());
        properties.setProperty("lastModified", LocalDate.now().toString());
        properties.setProperty("status", status);

        FileOutputStream out = new FileOutputStream(orderDir.getPath() + "/" + ORDER_PROPERTIES_FILE);
        properties.storeToXML(out, null);
        out.close();
    }

    /*
    Dark mode setting
     */
    public static void setDarkMode(boolean b){
        Preferences.userRoot().node(USER_NODE)
                .put(DARK_MODE_KEY, Boolean.toString(b));
    }

    public static boolean isDarkModeEnabled(){
        return Boolean.parseBoolean(Preferences.
                userRoot().node(USER_NODE).get(DARK_MODE_KEY, "false"));
    }

    /*
    This section is for getting / saving information on specific STL files in the ORDER_PROPERTIES_FILE
     */
    public static void saveFileInfo(STLFile f) throws IOException{
        Properties properties = new Properties();

        //load the file
        FileInputStream inputStream = new FileInputStream(f.getFile().getParentFile().getPath() + "/" + ORDER_PROPERTIES_FILE);
        properties.loadFromXML(inputStream);
        inputStream.close();

        //set properties
        properties.setProperty(f.getFile().getName() + ".quantity", Integer.toString(f.getQuantity()));
        properties.setProperty(f.getFile().getName() + ".colour", f.getColour());
        properties.setProperty(f.getFile().getName() + ".resolution",f.getResolution());
        properties.setProperty(f.getFile().getName() + ".plasticType", f.getPlasticType());
        properties.setProperty(f.getFile().getName() + ".notes", f.getNotes());

        //update last modified
        properties.setProperty("lastModified", LocalDate.now().toString());


        //save the file
        FileOutputStream outputStream = new FileOutputStream(f.getFile().getParentFile().getPath() + "/" + ORDER_PROPERTIES_FILE);
        properties.storeToXML(outputStream, null);
        outputStream.close();
    }

    public static STLFile getFileInfo(File f) throws IOException{
        //TODO check if the file info exists
        Properties properties = new Properties();

        //load the file
        FileInputStream inputStream = new FileInputStream(f.getParentFile() + "/" + ORDER_PROPERTIES_FILE);
        properties.loadFromXML(inputStream);
        inputStream.close();

        //create STLFile Java object
        STLFile newFile = new STLFile(f);

        int quantity = Integer.parseInt(properties.getProperty(f.getName() + ".quantity"));
        String plasticType = properties.getProperty(f.getName() + ".plasticType");
        String resolution = properties.getProperty(f.getName() + ".resolution");
        String colour = properties.getProperty(f.getName() + ".colour");
        String notes = properties.getProperty(f.getName() + ".notes");

        newFile.setColour(colour);
        newFile.setPlasticType(plasticType);
        newFile.setQuantity(quantity);
        newFile.setResolution(resolution);
        newFile.setNotes(notes);

        newFile.initAvailableColours();

        return newFile;
    }
}
