package ku.cs.kanison.controllers.login;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import ku.cs.kanison.services.ThemeMode;

import java.io.IOException;

public class SuggestionController {
    @FXML private AnchorPane pane;

    @FXML
    public void initialize() {
        ThemeMode.setThemeMode(pane);
    }

    @FXML
    public void handleBackToLoginBtn(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            System.err.println("กลับไปที่หน้า login ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
}
