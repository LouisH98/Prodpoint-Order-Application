package Application;


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

    public static final File ORDERS_FOLDER = new File(System.getProperty("user.home") + "/Prodpoint Orders");

    public static File getOrdersFolder() {
        return ORDERS_FOLDER;
    }

    /*
    Used to get info from an order folder and parse it into a wrapper object
     */
    public static OrderInfoWrapper getOrderInfo(File orderDir) throws IOException {
        Properties properties = new Properties();
        FileInputStream inputStream = new FileInputStream(orderDir.getPath() + "/order_properties.xml");
        properties.loadFromXML(inputStream);

        return new OrderInfoWrapper(
                properties.getProperty("orderID"),
                properties.getProperty("clientName"),
                LocalDate.parse(properties.getProperty("dueDate")),
                LocalDate.parse(properties.getProperty("lastModified"))
        );
    }


    public static String getOrderID(File orderDir) throws IOException{
        Properties properties = new Properties();
        FileInputStream is = new FileInputStream(orderDir.getPath() + "/order_properties.xml");
        properties.loadFromXML(is);
        return properties.getProperty("orderID");
    }

    public static void saveOrderInfo(File orderDir, String orderID, String clientName, LocalDate dueDate) throws IOException{
        Properties properties = new Properties();
        properties.put("orderID", orderID);
        properties.put("clientName", clientName);
        properties.put("dueDate", dueDate.toString());
        properties.put("lastModified", LocalDate.now().toString());

        FileOutputStream out = new FileOutputStream(orderDir.getPath() + "/order_properties.xml");
        properties.storeToXML(out, null);
        out.close();
    }


    public static void setDarkMode(boolean b){
        Preferences.userRoot().node(USER_NODE)
                .put(DARK_MODE_KEY, Boolean.toString(b));
    }

    public static boolean isDarkModeEnabled(){
        return Boolean.parseBoolean(Preferences.
                userRoot().node(USER_NODE).get(DARK_MODE_KEY, "false"));

    }
}
