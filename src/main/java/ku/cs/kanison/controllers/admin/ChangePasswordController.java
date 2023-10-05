package ku.cs.kanison.controllers.admin;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import ku.cs.kanison.models.account.Account;
import ku.cs.kanison.models.account.AccountList;
import ku.cs.kanison.services.AccountListFileDataSource;
import ku.cs.kanison.services.DataSource;
import ku.cs.kanison.services.Info;
import ku.cs.kanison.services.ThemeMode;

import java.io.IOException;

public class ChangePasswordController {
    @FXML private PasswordField confirmPassword;
    @FXML private PasswordField newPassword;
    @FXML private PasswordField currentPassword;
    @FXML private AnchorPane pane;

    private Info info;
    private AccountList accountsList;
    private Alert alert;

    @FXML
    public void initialize() {
        alert = new Alert(Alert.AlertType.NONE);
        info = (Info) FXRouter.getData();
        accountsList = info.getAccountList();
        ThemeMode.setThemeMode(pane);
        System.out.println("initialize ChangeProfileController");
    }

    @FXML
    void handleBackBtn(ActionEvent event) {
        try {
            FXRouter.goTo("admin_manager", info);
        } catch (IOException e) {
            System.err.println("กลับไปที่หน้า admin_manager ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    void handleConfirmBtn(ActionEvent event) {
        Account account = new Account();
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

                DataSource<AccountList> accountListDataSource = new AccountListFileDataSource();
                accountListDataSource.writeData(accountsList);

                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setContentText("Change Password Success");
                alert.show();

                try {
                    Info infoForPassing = new Info(accountsList, info.getProductList(), info.getReviewList(), info.getOrderList(), info.getReportList());
                    FXRouter.goTo("change_password", infoForPassing);
                } catch (IOException e) {
                    System.err.println("ไปที่หน้า change_password ไม่ได้");
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
}
