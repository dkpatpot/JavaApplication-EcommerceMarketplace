package ku.cs.kanison.controllers.user;

import animatefx.animation.Tada;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.kanison.models.account.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import com.github.saacsos.FXRouter;
import ku.cs.kanison.services.AccountListFileDataSource;
import ku.cs.kanison.services.DataSource;
import ku.cs.kanison.services.Info;
import ku.cs.kanison.services.ThemeMode;

public class ChangeProfileController {

    @FXML private PasswordField currentPassword;
    @FXML private PasswordField newPassword;
    @FXML private PasswordField confirmPassword;
    @FXML private Circle circleImage;
    @FXML private AnchorPane pane;

    private AccountList accountsList;
    private Account account;
    private Info info;
    private Alert alert;
    private DataSource<AccountList> accountListDataSource;

    @FXML
    public void initialize() {
        account = new Account();
        alert = new Alert(Alert.AlertType.NONE);
        info = (Info) FXRouter.getData();
        accountsList = info.getAccountList();
        ThemeMode.setThemeMode(pane);
        showImageProfile();
        System.out.println("initialize ChangeProfileController");
    }
    @FXML
    public void handleConfirmBtn(){
        if (currentPassword.getText().equals("") || newPassword.getText().equals("") || confirmPassword.getText().equals("")) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("กรุณากรอกรหัสผ่าน");
            alert.show();
        } else if (!currentPassword.getText().equals(accountsList.getThisAccount().getPassword())) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("รหัสผ่านเก่าไม่ถูกต้อง");
            alert.show();
        } else if (!account.isValidPassword(newPassword.getText())) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("กรุณาใส่รหัสผ่านตั้งแต่ 6-20 ตัว");
            alert.show();
        } else {
            if (accountsList.getThisAccount().getPassword().equals(currentPassword.getText())
                    && newPassword.getText().equals(confirmPassword.getText())
                    && !currentPassword.getText().equals(newPassword.getText()) && account.isValidPassword(newPassword.getText())) {
                accountsList.getThisAccount().setPassword(newPassword.getText());
                accountListDataSource = new AccountListFileDataSource();
                accountListDataSource.writeData(accountsList);

                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setContentText("Change Password Success");
                alert.show();

                try {
                    Info infoForPassing = new Info(accountsList, info.getProductList(), info.getReviewList(), info.getOrderList(), info.getReportList());
                    FXRouter.goTo("change_profile", infoForPassing);
                } catch (IOException e) {
                    System.err.println("ไปที่หน้า confirm ไม่ได้");
                    System.err.println("ให้ตรวจสอบการกำหนด route");
                }
            }
            else{
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("Password doesn't match.");
                alert.show();
            }
        }
    }
    @FXML void handleUploadAnImageBtn(ActionEvent actionEvent) throws IOException {
        FileChooser chooserImage = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Images","*.jpg", "*.png", "*.jpeg");
        chooserImage.getExtensionFilters().add(extFilter);
        File imageFile = chooserImage.showOpenDialog(new Stage());

        if(imageFile != null) {
            String imageName = imageFile.toURI().toURL().toString();
            circleImage.setFill(new ImagePattern(new Image(imageName)));
            File dest = new File ("data/user_image/"+accountsList.getThisAccount().getUsername()+".png");

            try {
                Files.copy(imageFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                ((UserAccount)accountsList.getThisAccount()).setUserImage(accountsList.getThisAccount().getUsername()+".png");
                accountListDataSource = new AccountListFileDataSource();
                accountListDataSource.writeData(accountsList);
            } catch (IOException e) {
                System.err.println("Image is not there");
            }
        }

    }

    @FXML
    public void handleBackBtn(){
        try {
            Info infoForPassing = new Info(accountsList, info.getProductList(), info.getReviewList(), info.getOrderList(), info.getReportList());
            FXRouter.goTo("marketplace", infoForPassing);
        } catch (IOException e) {
            System.err.println("กลับไปที่หน้า marketplace ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    private void showImageProfile() {
        if (((UserAccount)accountsList.getThisAccount()).haveUserImage()) {
            File f = new File("data/user_image/"+((UserAccount)accountsList.getThisAccount()).getUserImage());
            circleImage.setFill(new ImagePattern(new Image("file:///" + f.getAbsolutePath())));
        } else {
            circleImage.setFill(new ImagePattern(new Image(getClass().getResource("/ku/cs/images/default_profile.png").toExternalForm())));
        }
        new Tada(circleImage).play();
    }
}
