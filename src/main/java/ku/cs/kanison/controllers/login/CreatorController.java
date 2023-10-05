package ku.cs.kanison.controllers.login;

import animatefx.animation.Jello;
import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import ku.cs.kanison.services.ThemeMode;

import java.io.IOException;

public class CreatorController {
    @FXML private AnchorPane pane;
    @FXML private ImageView phat;
    @FXML private ImageView copy;
    @FXML private ImageView byu;
    @FXML private ImageView frame;

    @FXML
    public void initialize() {
        ThemeMode.setThemeMode(pane);
        new Jello(phat).play();
        new Jello(copy).play();
        new Jello(byu).play();
        new Jello(frame).play();
    }

    @FXML
    public void handleBackToLoginBtn(ActionEvent actionEvent) throws IOException {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            System.err.println("กลับไปที่หน้า login ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
}
