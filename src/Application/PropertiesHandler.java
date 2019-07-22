package Application;


import java.util.prefs.Preferences;

public class PropertiesHandler {

    public static final String USER_NODE = "ProdpointApp";
    public static final String DARK_MODE_KEY = "dark_mode_enabled";

    public static void setDarkMode(boolean b){
        Preferences.userRoot().node(USER_NODE)
                .put(DARK_MODE_KEY, Boolean.toString(b));
    }

    public static boolean isDarkModeEnabled(){
        return Boolean.parseBoolean(Preferences.
                userRoot().node(USER_NODE).get(DARK_MODE_KEY, "false"));

    }
}
