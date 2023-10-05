package ku.cs.kanison.controllers.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

public class RegisterController {
    @FXML private TextField nameTextField;
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Circle circleImage;
    @FXML private AnchorPane pane;

    private Alert alert;
    private File imageFile;
    private AccountList accountsList;
    private Account accounts;
    private DataSource<AccountList> accountListDataSource;
    private Info info;

    @FXML
    public void initialize() {
        accounts = new Account();
        alert = new Alert(Alert.AlertType.NONE);
        info = (Info) FXRouter.getData();
        accountsList = info.getAccountList();
        circleImage.setFill(new ImagePattern(new Image(getClass().getResource("/ku/cs/images/default_profile.png").toExternalForm())));
        ThemeMode.setThemeMode(pane);
        System.out.println("initialize RegisterController");
    }

    @FXML
    public void handleRegisterBtn(ActionEvent actionEvent) {
        String name = nameTextField.getText();
        String usernameText = usernameTextField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        if(name.isEmpty()||usernameText.isEmpty()||password.isEmpty()||confirmPassword.isEmpty()){
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Please complete the information.");
            alert.show();
        } else if (!accounts.isValidName(name)) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Invalid name");
            alert.show();
        } else if (!accounts.isValidUsername(usernameText)) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Invalid username");
            alert.show();
            usernameTextField.setStyle("-fx-text-fill: red");
        } else if (!accounts.isValidPassword(password)){
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Invalid password");
            alert.show();
        } else if (!password.equals(confirmPassword)) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Password doesn't match.");
            alert.show();
        } else {
            if (!accountsList.isExistUsername(usernameText)) {
                accountListDataSource = new AccountListFileDataSource();
                if (imageFile==null) {
                    accountsList.addAccount(new UserAccount("User", name, usernameText, password, "Available", 0, "-" ));
                } else {
                    File dest = new File ("data/user_image/"+usernameText+".png");
                    try {
                        Files.copy(imageFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Image copied successfully.");
                    } catch (IOException e) {
                        System.out.println("Image is not there");
                    }
                    accountsList.addAccount(new UserAccount("User", name, usernameText, password, "Available", 0, "-" , usernameText+".png"));
                }
                accountListDataSource.writeData(accountsList);
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setContentText("Register Success");
                alert.show();
                try {
                    Info infoForPassing = new Info(accountsList, info.getProductList(), info.getReviewList(), info.getOrderList(), info.getReportList());
                    FXRouter.goTo("login", infoForPassing);
                } catch (IOException e) {
                    System.err.println("ไปที่หน้า login ไม่ได้");
                    System.err.println("ให้ตรวจสอบการกำหนด route");
                }
            }else if (accountsList.isExistUsername(usernameText)){
                accountListDataSource.writeData(accountsList);
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("Already registered.");
                alert.show();
                usernameTextField.setStyle("-fx-text-fill: red");
            }
        }
    }
    @FXML void handleUploadAnImageBtn(ActionEvent actionEvent) throws IOException {
        FileChooser chooserImage = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Images","*.jpg", "*.png", "*.jpeg");
        chooserImage.getExtensionFilters().add(extFilter);
        imageFile = chooserImage.showOpenDialog(new Stage());
        if(imageFile != null) {
            String imageName = imageFile.toURI().toURL().toString();
            circleImage.setFill(new ImagePattern(new Image(imageName)));
        }
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
