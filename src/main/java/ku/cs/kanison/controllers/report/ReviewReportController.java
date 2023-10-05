package ku.cs.kanison.controllers.report;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import ku.cs.kanison.services.DataSource;
import ku.cs.kanison.services.Info;
import ku.cs.kanison.services.ReportListFileDataSource;
import ku.cs.kanison.models.product.ReviewList;
import ku.cs.kanison.models.report.Report;
import ku.cs.kanison.models.report.ReportList;
import ku.cs.kanison.models.report.ReviewReport;
import ku.cs.kanison.services.ThemeMode;

import java.io.IOException;

public class ReviewReportController {
    @FXML private TextField violateTextField;
    @FXML private VBox vBox;
    @FXML private AnchorPane pane;

    private ToggleGroup group;
    private ReportList reportList;
    private DataSource<ReportList> reportListDataSource;
    private Info info;
    private Alert alert;
    private ReviewList reviewList;

    @FXML
    public void initialize() {
        info = (Info) FXRouter.getData();
        reportList = info.getReportList();
        reviewList = info.getReviewList();
        reportListDataSource = new ReportListFileDataSource();
        alert = new Alert(Alert.AlertType.NONE);
        ThemeMode.setThemeMode(pane);
        setReportRadioBtn();
    }

    private void setReportRadioBtn() {
        RadioButton abuseContent;
        RadioButton abuseComment;
        RadioButton harmingContent;
        RadioButton sexualContent;
        RadioButton otherViolate;

        vBox.setSpacing(10);
        group = new ToggleGroup();

        abuseContent = new RadioButton("เนื้อหาไม่เหมาะสม");
        abuseContent.setUserData("เนื้อหาไม่เหมาะสม");
        abuseContent.setSelected(true);

        abuseComment = new RadioButton("ความคิดเห็นไม่เหมาะสม");
        abuseComment.setUserData("ความคิดเห็นไม่เหมาะสม");

        harmingContent = new RadioButton("เนื้อหาอันตรายมีความรุนแรง");
        harmingContent.setUserData("เนื้อหาอันตรายมีความรุนแรง");

        sexualContent = new RadioButton("เนื้อหาในเชิงลามกอนาจาร");
        sexualContent.setUserData("เนื้อหาในเชิงลามกอนาจาร");

        otherViolate = new RadioButton("การละเมิดอื่นๆ");
        otherViolate.setUserData("การละเมิดอื่นๆ");

        abuseContent.setToggleGroup(group);
        abuseComment.setToggleGroup(group);
        harmingContent.setToggleGroup(group);
        sexualContent.setToggleGroup(group);
        otherViolate.setToggleGroup(group);

        vBox.getChildren().addAll(abuseContent, abuseComment, harmingContent, sexualContent, otherViolate);
    }

    @FXML
    void handleConfirmBtn(ActionEvent event) {
        String reason = null;
        if (group.getSelectedToggle() != null) {
            reason = group.getSelectedToggle().getUserData().toString();

            if (!violateTextField.getText().isBlank()) {
                Report report = new ReviewReport(reviewList.getThisReview().getUsername(), reason, violateTextField.getText());
                reportList.addReport(report);
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