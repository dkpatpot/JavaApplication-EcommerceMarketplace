package ku.cs.kanison.controllers.marketplace;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import ku.cs.kanison.models.account.AccountList;
import ku.cs.kanison.models.account.Seller;
import ku.cs.kanison.services.AccountListFileDataSource;
import ku.cs.kanison.services.DataSource;
import ku.cs.kanison.services.Info;
import ku.cs.kanison.services.ThemeMode;

import java.io.IOException;

public class EditStoreController {
    @FXML private TextField storeNameTextField;
    @FXML private TextField lowQuantityTextField;
    @FXML private AnchorPane pane;

    private Info info;
    private AccountList accountList;
    private DataSource<AccountList> accountListDataSource;

    public void initialize() {
        info = (Info) FXRouter.getData();
        accountList = info.getAccountList();
        accountListDataSource = new AccountListFileDataSource();
        ThemeMode.setThemeMode(pane);
        System.out.println("initialize EditStoreController");
        showData();
    }

    public void showData() {
        storeNameTextField.setText(((Seller)accountList.getThisAccount()).getStoreName());
        lowQuantityTextField.setText(((Seller)accountList.getThisAccount()).getLowQuantityStore()+"");
    }

    @FXML
    public void handleBackBtn(ActionEvent actionEvent) throws IOException {
        try {
            FXRouter.goTo("store", info);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า store ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public void handleConfirmBtn(ActionEvent actionEvent) throws IOException {
        ((Seller) accountList.getThisAccount()).setStoreName(storeNameTextField.getText());
        ((Seller) accountList.getThisAccount()).setLowQuantityStore(Integer.parseInt(lowQuantityTextField.getText()));
        accountListDataSource = new AccountListFileDataSource();
        accountListDataSource.writeData(accountList);
        try {
            Info infoForPassing = new Info(accountList, info.getProductList(), info.getReviewList(), info.getOrderList(), info.getReportList());
            FXRouter.goTo("store", infoForPassing);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า store ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
}
