package ku.cs.kanison.controllers.report;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import ku.cs.kanison.services.DataSource;
import ku.cs.kanison.services.Info;
import ku.cs.kanison.services.ReportListFileDataSource;
import ku.cs.kanison.models.product.ProductList;
import ku.cs.kanison.models.report.ProductReport;
import ku.cs.kanison.models.report.Report;
import ku.cs.kanison.models.report.ReportList;
import ku.cs.kanison.services.ThemeMode;

import java.io.IOException;

public class ProductReportController {
    @FXML private VBox vBox;
    @FXML private TextField violateTextField;
    @FXML private AnchorPane pane;

    private Info info;
    private ProductList productList;
    private ReportList reportList;
    private DataSource<ReportList> reportListDataSource;
    private ToggleGroup group;
    private Alert alert;

    @FXML
    public void initialize() {
        info = (Info) FXRouter.getData();
        productList = info.getProductList();
        reportList = info.getReportList();
        alert = new Alert(Alert.AlertType.NONE);
        reportListDataSource = new ReportListFileDataSource();
        ThemeMode.setThemeMode(pane);
        setReportRadioBtn();
    }

    void setReportRadioBtn() {
        vBox.setSpacing(10);
        group = new ToggleGroup();

        RadioButton counterfeits = new RadioButton("สินค้าละเมิดลิขสิทธิ์");
        counterfeits.setUserData("สินค้าละเมิดลิขสิทธิ์");
        counterfeits.setSelected(true);

        RadioButton prohibited = new RadioButton("สินค้าต้องห้าม");
        prohibited.setUserData("สินค้าต้องห้าม");

        RadioButton listingPolicyViolations = new RadioButton("ละเมิดนโยบายการลงสินค้า");
        listingPolicyViolations.setUserData("ละเมิดนโยบายการลงสินค้า");

        RadioButton offensive = new RadioButton("สินค้าที่ส่งผลกระทบต่อบุคคล");
        offensive.setUserData("สินค้าที่ส่งผลกระทบต่อบุคคล");

        RadioButton fraudulent = new RadioButton("สินค้าที่มีการหลอกลวง");
        fraudulent.setUserData("สินค้าที่มีการหลอกลวง");

        RadioButton stolen = new RadioButton("ของโจร");
        stolen.setUserData("ของโจร");

        RadioButton others = new RadioButton("อื่นๆ");
        others.setUserData("อื่นๆ");

        counterfeits.setToggleGroup(group);
        prohibited.setToggleGroup(group);
        listingPolicyViolations.setToggleGroup(group);
        offensive.setToggleGroup(group);
        fraudulent.setToggleGroup(group);
        stolen.setToggleGroup(group);
        others.setToggleGroup(group);

        vBox.getChildren().addAll(counterfeits, prohibited, listingPolicyViolations, offensive, fraudulent, stolen, others);
    }

    @FXML
    void handleConfirmBtn(ActionEvent event) {
        String reason = null;
        if (group.getSelectedToggle() != null) {
            reason = group.getSelectedToggle().getUserData().toString();

            if (!violateTextField.getText().isBlank()) {
                Report productReport = new ProductReport(
                        productList.getThisProduct().getSellerUserName(),
                        reason,
                        violateTextField.getText(),
                        productList.getThisProduct().getID()
                        );
                reportList.addReport(productReport);
                reportListDataSource.writeData(reportList);

                try {
                    Info infoForPassing = new Info(info.getAccountList(), info.getProductList(), info.getReviewList(), info.getOrderList(), reportList);
                    FXRouter.goTo("product_detail", infoForPassing);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("ใส่เหตุผลที่รายงานด้วยครับ");
                alert.show();
            }
        }
    }

    @FXML
    void handleBackToProductDetailBtn(ActionEvent event) {
        try {
            FXRouter.goTo("product_detail", info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

