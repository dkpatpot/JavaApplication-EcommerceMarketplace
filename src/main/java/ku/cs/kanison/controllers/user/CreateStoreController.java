package ku.cs.kanison.controllers.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import ku.cs.kanison.models.account.*;
import com.github.saacsos.FXRouter;
import ku.cs.kanison.services.AccountListFileDataSource;
import ku.cs.kanison.services.DataSource;
import ku.cs.kanison.services.Info;
import ku.cs.kanison.services.ThemeMode;

import java.io.IOException;

public class CreateStoreController {
    @FXML private TextField storeNameTextField;
    @FXML private AnchorPane pane;

    private Info info;
    private AccountList accountsList;

    @FXML
    public void initialize() {
        info = (Info) FXRouter.getData();
        accountsList = info.getAccountList();
        ThemeMode.setThemeMode(pane);
        System.out.println("initialize RegisterController");
    }

    @FXML
    public void handleBackBtn(){
        try {
            FXRouter.goTo("marketplace", info);
        } catch (IOException e) {
            System.err.println("กลับไปที่หน้า marketplace ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public void handleCreateStore(ActionEvent actionEvent) {
        String storeName = storeNameTextField.getText();
        accountsList.setThisAccountToSeller(storeName);
        DataSource<AccountList> accountListDataSource = new AccountListFileDataSource();
        accountListDataSource.writeData(accountsList);
        try {
            Info infoForPassing = new Info(accountsList, info.getProductList(), info.getReviewList(), info.getOrderList(), info.getReportList());
            FXRouter.goTo("store", infoForPassing);
        } catch (IOException e) {
            System.err.println("กลับไปที่หน้า store ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
}
