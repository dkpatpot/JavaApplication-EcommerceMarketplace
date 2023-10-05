package ku.cs.kanison.controllers.admin;

import com.github.saacsos.FXRouter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Callback;
import ku.cs.kanison.controllers.login.LoginController;
import ku.cs.kanison.models.account.AccountList;
import ku.cs.kanison.services.AccountListFileDataSource;
import ku.cs.kanison.services.DataSource;
import ku.cs.kanison.services.Info;
import ku.cs.kanison.models.report.Report;
import ku.cs.kanison.models.report.ReportList;
import ku.cs.kanison.services.ThemeMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class ReportedUserController {
    @FXML private ListView<Report> reportedListView;
    @FXML private Label adminNameLabel;
    @FXML private AnchorPane pane;

    private ReportList reportList;
    private AccountList accountsList;
    private Info info;
    private DataSource<AccountList> accountListDataSource;

    @FXML
    public void initialize() {
        info = (Info) FXRouter.getData();
        reportList = info.getReportList();
        accountsList = info.getAccountList();
        accountListDataSource = new AccountListFileDataSource();
        ThemeMode.setThemeMode(pane);
        showReportedUserData();
        System.out.println("initialize ReportUserController");
    }
    void showReportedUserData() {
        ArrayList<Report> sorted = new ArrayList<>(reportList.getReports());
        adminNameLabel.setText(accountsList.getThisAccount().getName());
        sorted.sort(Comparator.comparing((Report report) -> report.getUsername()).thenComparing(Comparator.comparing((Report report) -> report.getType())));
        ObservableList<Report> reportListView = FXCollections.observableArrayList(sorted);
        reportedListView.setItems(reportListView);
        reportedListView.setCellFactory(new Callback<ListView<Report>, ListCell<Report>>() {
            @Override
            public ListCell<Report> call(ListView<Report> listView) {
                return new CustomListCell();
            }
        });
    }
    private class CustomListCell extends ListCell<Report> {
        private HBox contentBox;
        private Circle circleImage;
        private Text typeWithReportedUser;
        private Text reason;
        private Text message;
        private Text loginAttempts;
        private Button button;

        public CustomListCell() {
            super();
            typeWithReportedUser = new Text();
            reason = new Text();
            message = new Text();
            loginAttempts = new Text();
            button = new Button();
            VBox vBox = new VBox(typeWithReportedUser,reason,message,loginAttempts);
            circleImage = new Circle(30);
            HBox.setHgrow(vBox, Priority.ALWAYS);
            contentBox = new HBox(circleImage, vBox, button);
            contentBox.setAlignment(Pos.CENTER);
            contentBox.setSpacing(10);
        }
        @Override
        protected void updateItem(Report report, boolean empty) {
            super.updateItem(report, empty);
            if (report != null && !empty) { // <== test for null item and empty parameter
                typeWithReportedUser.setText("Reported user : "+report.getUsername()+" Type : "+report.getType()+"");
                reason.setText("Reasons : "+report.getReason()+"");
                message.setText("Message : "+report.getMessage()+"");
                if ((accountsList.searchUserAccountByUsername(report.getUsername())).getUserStatus().equals("Banned")) {
                    loginAttempts.setText("จำนวนครั้งที่พยายามเข้า : " + (accountsList.searchUserAccountByUsername(report.getUsername())).getLoginAttempts() + " ครั้ง");
                    loginAttempts.setFill(Color.RED);
                    button.setText("Unban");
                    button.setTextFill(Color.GREEN);
                    button.setStyle("-fx-background-color: white;" +
                            "-fx-border-color: green;" + "-fx-background-radius: 20px;" + "-fx-border-radius: 20px");
                } else {
                    loginAttempts.setText("");
                    button.setText("Ban");
                    button.setTextFill(Color.RED);
                    button.setStyle("-fx-background-color: white;" +
                            "-fx-border-color: red;" + "-fx-background-radius: 20px;" + "-fx-border-radius: 20px");
                }
                button.setOnAction(event -> getListView().getItems());
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        (accountsList.searchUserAccountByUsername(report.getUsername())).setUserStatus();
                        accountListDataSource.writeData(accountsList);
                        try {
                            Info infoForPassing = new Info(accountsList, info.getProductList(), info.getReviewList(), info.getOrderList(), reportList);
                            FXRouter.goTo("reported_user", infoForPassing);
                        } catch (IOException e) {
                            System.err.println("ไปที่หน้า reported_user ไม่ได้");
                            System.err.println("ให้ตรวจสอบการกำหนด route");
                        }
                    }
                });
                if ((accountsList.searchUserAccountByUsername(report.getUsername())).haveUserImage()) {
                    File f = new File("data/user_image/"+(accountsList.searchUserAccountByUsername(report.getUsername())).getUserImage());
                    circleImage.setFill(new ImagePattern(new Image("file:///" + f.getAbsolutePath())));
                } else {
                    circleImage.setFill(new ImagePattern(new Image(getClass().getResource("/ku/cs/images/default_profile.png").toExternalForm())));
                }
                if (!LoginController.isLightMode) {
                    typeWithReportedUser.setFill(Color.WHITE);
                    reason.setFill(Color.WHITE);
                    message.setFill(Color.WHITE);
                }
                setGraphic(contentBox);
            } else {
                setGraphic(null);
            }
        }
    }
    @FXML void handleBackBtn(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("admin_manager");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า admin_manager ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

}
