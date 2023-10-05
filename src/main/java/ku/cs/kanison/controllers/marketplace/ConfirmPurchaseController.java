package ku.cs.kanison.controllers.marketplace;

import com.github.saacsos.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import ku.cs.kanison.models.account.AccountList;
import ku.cs.kanison.models.product.OrderList;
import ku.cs.kanison.models.product.ProductList;
import ku.cs.kanison.models.promotion.PromotionList;
import ku.cs.kanison.services.*;
import java.io.IOException;

public class ConfirmPurchaseController {
    @FXML private Label productName;
    @FXML private Label quantity;
    @FXML private Label price;
    @FXML private Label totalPrice;
    @FXML private Label paymentAmountLabel;
    @FXML private TextField promotionCodeTextField;
    @FXML private AnchorPane pane;

    private Info info;
    private ProductList productList;
    private OrderList orderList;
    private AccountList accountList;
    private PromotionList promotionList;
    private DataSource<OrderList> orderListDataSource;
    private DataSource<ProductList> productListDataSource;
    private Alert alert;

    @FXML
    public void initialize() {
        DataSource<PromotionList> promotionListDataSource = new PromotionListFileDataSource();
        info = (Info) FXRouter.getData();
        promotionList = new PromotionList();
        promotionList = promotionListDataSource.getData();
        orderList = info.getOrderList();
        accountList = info.getAccountList();
        productList = info.getProductList();
        orderListDataSource = new OrderListFileDataSource();
        productListDataSource = new ProductListFileDataSource();
        alert = new Alert(Alert.AlertType.NONE);
        ThemeMode.setThemeMode(pane);
        showData();
        System.out.println("initialize ConfirmPurchaseController");
    }

    public void showData(){
        productName.setText(productList.getThisProduct().getProductName());
        price.setText(productList.getThisProduct().getPrice()+"");
        quantity.setText(orderList.getThisOrder().getQuantity()+"");
        totalPrice.setText(orderList.getThisOrder().getSubTotal()+"");
        paymentAmountLabel.setText(orderList.getThisOrder().getGrandTotal()+"");
    }

    @FXML
    public void handleUseCodeBtn() {
        String promotionCode = promotionCodeTextField.getText();
        if (promotionList.isExistCode(promotionCode)) {
            try {
                if (orderList.getThisOrder().checkPromotion(promotionList.searchPromotionByCode(promotionCode))) {
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setContentText("ใช้งานโค้ดสำเร็จ :)");
                    alert.show();
                    double grandTotal = promotionList.searchPromotionByCode(promotionCode).calDiscount(orderList.getThisOrder());
                    paymentAmountLabel.setText(grandTotal+"");
                    orderList.getThisOrder().setGrandTotal(grandTotal);
                    orderListDataSource.writeData(orderList);
                }
            } catch (IllegalConditionException e) {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText(e.getMessage());
                alert.show();
            }
        } else {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("ไม่พบโค้ดในระบบ กรุณากรอกโค้ดให้ถูกต้อง");
            alert.show();
        }
    }

    @FXML
    public void handleBackBtn() {
        orderList.removeThisOrder();
        try {
            Info infoForPassing = new Info(accountList, productList, info.getReviewList(), orderList, info.getReportList());
            FXRouter.goTo("product_detail", infoForPassing);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า product_detail ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public void handleConfirmBtn() {
        orderListDataSource.writeData(orderList);
        productList.getThisProduct().decreaseQuantity(orderList.getThisOrder().getQuantity());
        productListDataSource.writeData(productList);
        try {
            Info infoForPassing = new Info(accountList, productList, info.getReviewList(), orderList, info.getReportList());
            FXRouter.goTo("product_detail", infoForPassing);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า product_detail ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setContentText("สั่งซื้อสำเร็จ");
        alert.show();
    }
}
