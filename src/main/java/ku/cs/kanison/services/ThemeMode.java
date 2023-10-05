package ku.cs.kanison.services;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import ku.cs.kanison.controllers.login.LoginController;

public class ThemeMode {

    public static void setThemeMode(AnchorPane parent) {
        if (LoginController.isLightMode) {
            setLightMode(parent);
        } else {
            setDarkMode(parent);
        }
    }

    public static void setLightMode(AnchorPane parent) {
        parent.getStylesheets().clear();
        parent.getStylesheets().add(ThemeMode.class.getResource("/ku/cs/styles/style.css").toExternalForm());
    }

    public static void setDarkMode(AnchorPane parent) {
        parent.getStylesheets().clear();
        parent.getStylesheets().add(ThemeMode.class.getResource("/ku/cs/styles/dark-mode.css").toExternalForm());
    }

    public static void setLightMode(AnchorPane parent, Button mode) {
        setLightMode(parent);
        mode.setText("Dark Mode");
    }

    public static void setDarkMode(AnchorPane parent, Button mode) {
        setDarkMode(parent);
        mode.setText("Light Mode");
    }
}
