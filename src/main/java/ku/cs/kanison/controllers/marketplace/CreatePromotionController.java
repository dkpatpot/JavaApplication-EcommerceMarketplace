package ku.cs.kanison.controllers.marketplace;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import ku.cs.kanison.models.account.AccountList;
import ku.cs.kanison.models.promotion.PercentDiscountPromotion;
import ku.cs.kanison.models.promotion.ProductQuantityBasedDiscountPromotion;
import ku.cs.kanison.models.promotion.PromotionList;
import ku.cs.kanison.services.DataSource;
import ku.cs.kanison.services.Info;
import ku.cs.kanison.services.PromotionListFileDataSource;
import ku.cs.kanison.services.ThemeMode;

import java.io.IOException;

public class CreatePromotionController {
    @FXML private TextField minYBahtTextField;
    @FXML private TextField discountXPercentTextField;
    @FXML private TextField minYQuantityTextField;
    @FXML private TextField discountXBahtTextField;
    @FXML private TextField codeTextField1;
    @FXML private TextField codeTextField2;
    @FXML private AnchorPane pane;

    private Info info;
    private Alert alert;
    private AccountList accountsList;
    private PromotionList promotionList;
    private DataSource<PromotionList> promotionListDataSource;

    @FXML
    public void initialize() {
        info = (Info) FXRouter.getData();
        accountsList = info.getAccountList();
        promotionList = new PromotionList();
        promotionListDataSource = new PromotionListFileDataSource();
        promotionList = promotionListDataSource.getData();
        alert = new Alert(Alert.AlertType.NONE);
        ThemeMode.setThemeMode(pane);
        System.out.println("initialize CreatePromotionController");
    }

    @FXML
    void handleBackBtn(ActionEvent event) {
        try {
            FXRouter.goTo("store", info);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า store ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    void handleConfirm1Btn(ActionEvent event) {
        try {
            if (!promotionList.isExistCode(codeTextField1.getText())) {
                if (codeTextField1.getText() == null || codeTextField1.getText().equals("") || codeTextField1.getText().trim().equals("")) {
                    alert.setContentText("กรุณากรอกโค้ด!");
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.show();
                } else if (Double.parseDouble(minYBahtTextField.getText()) <= 0) {
                    alert.setContentText("กรุณาใส่ราคาสินค้าขั้นต่ำเป็นตัวเลขตั้งแต่ 1 บาทขึ้นไป");
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.show();
                    minYBahtTextField.clear();
                } else if (Double.parseDouble(discountXPercentTextField.getText()) <= 0 || Double.parseDouble(discountXPercentTextField.getText()) > 100) {
                    alert.setContentText("กรุณาใส่ส่วนลดเปอร์เซ็นต์เป็นตัวเลขตั้งแต่ 1-100");
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.show();
                    discountXPercentTextField.clear();
                } else {
                    promotionList.addPromotion(new PercentDiscountPromotion(codeTextField1.getText().trim(), accountsList.getThisAccount().getUsername(),
                            Double.parseDouble(minYBahtTextField.getText()), Double.parseDouble(discountXPercentTextField.getText())));
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setContentText("สร้างโค้ดสำเร็จ");
                    promotionListDataSource.writeData(promotionList);
                    alert.show();
                    codeTextField1.clear();
                    minYBahtTextField.clear();
                    discountXPercentTextField.clear();
                }
            } else {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("มีโค้ดนี้อยู่แล้วในระบบ กรุณากรอกโค้ดใหม่");
                alert.show();
                codeTextField1.clear();
            }
        } catch (NumberFormatException e) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("กรุณากรอกราคาสินค้าขั้นต่ำและส่วนลดเป็นตัวเลข");
            alert.show();
        }
    }

    @FXML
    void handleConfirm2Btn(ActionEvent event) {
        try {
            if (!promotionList.isExistCode(codeTextField2.getText())) {
                if (codeTextField2.getText() == null || codeTextField2.getText().equals("") || codeTextField2.getText().trim().equals("")) {
                    alert.setContentText("กรุณากรอกโค้ด!");
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.show();
                } else if (Integer.parseInt(minYQuantityTextField.getText()) <= 0) {
                    alert.setContentText("กรุณาใส่จำนวนสินค้าขั้นต่ำเป็นตัวเลขจำนวนเต็มตั้งแต่ 1 ชิ้นขึ้นไป");
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.show();
                    minYQuantityTextField.clear();
                } else if (Double.parseDouble(discountXBahtTextField.getText()) <= 0) {
                    alert.setContentText("กรุณาใส่ส่วนลดเป็นตัวเลขตั้งแต่ 1 บาทขึ้นไป");
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.show();
                    discountXBahtTextField.clear();
                } else {
                    promotionList.addPromotion(new ProductQuantityBasedDiscountPromotion(codeTextField2.getText(), accountsList.getThisAccount().getUsername(),
                            Integer.parseInt(minYQuantityTextField.getText()), Double.parseDouble(discountXBahtTextField.getText())));
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setContentText("สร้างโค้ดสำเร็จ");
                    promotionListDataSource.writeData(promotionList);
                    alert.show();
                    codeTextField2.clear();
                    minYQuantityTextField.clear();
                    discountXBahtTextField.clear();}
            } else {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("มีโค้ดนี้อยู่แล้วในระบบ กรุณากรอกโค้ดใหม่");
                alert.show();
                codeTextField2.clear();
            }
        } catch (NumberFormatException e) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("กรุณากรอกจำนวนชิ้นขั้นต่ำและส่วนลดเป็นตัวเลข");
            alert.show();
        }
    }
}
