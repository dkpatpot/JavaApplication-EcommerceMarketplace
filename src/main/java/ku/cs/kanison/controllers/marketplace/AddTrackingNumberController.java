package ku.cs.kanison.controllers.marketplace;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import ku.cs.kanison.models.product.OrderList;
import ku.cs.kanison.services.Info;
import ku.cs.kanison.services.OrderListFileDataSource;
import ku.cs.kanison.services.ThemeMode;

import java.io.IOException;

public class AddTrackingNumberController {
    @FXML private TextField trackingNameTextField;
    @FXML private AnchorPane pane;

    private Info info;
    private OrderList orderList;
    private OrderListFileDataSource orderListFileDataSource;

    @FXML
    public void initialize() {
        info = (Info) FXRouter.getData();
        orderList = info.getOrderList();
        orderListFileDataSource = new OrderListFileDataSource();
        ThemeMode.setThemeMode(pane);
        System.out.println("initialize AddTrackingNumberController");
    }

    @FXML
    public void handleBackBtn(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("order_list", info);
        } catch (IOException e) {
            System.err.println("กลับไปที่หน้า order_list ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public void handleAddBtn(ActionEvent actionEvent) {
        orderList.getThisOrder().setTrackingNumber(trackingNameTextField.getText());
        orderListFileDataSource.writeData(orderList);
        try {
            Info infoForPassing = new Info(info.getAccountList(), info.getProductList(), info.getReviewList(),
                    orderList, info.getReportList());
            FXRouter.goTo("order_list", infoForPassing);
        } catch (IOException e) {
            System.err.println("กลับไปที่หน้า order_list ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
}
