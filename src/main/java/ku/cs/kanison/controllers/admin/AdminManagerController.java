package ku.cs.kanison.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Callback;
import ku.cs.kanison.controllers.login.LoginController;
import ku.cs.kanison.models.account.*;
import ku.cs.kanison.services.Info;
import com.github.saacsos.FXRouter;
import ku.cs.kanison.services.ThemeMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class AdminManagerController {
    private Info info;
    private AccountList accountsList;

    @FXML StackPane pane;
    @FXML ListView<UserAccount> listView;
    @FXML private Label adminNameLabel;
    @FXML private AnchorPane anchorPane;

    @FXML
    public void initialize() {
        info = (Info) FXRouter.getData();
        accountsList = info.getAccountList();
        ThemeMode.setThemeMode(anchorPane);
        showUserData();
        System.out.println("initialize AdminManagerController");
    }

    void showUserData() {
        adminNameLabel.setText(accountsList.getThisAccount().getName());
        ArrayList<UserAccount> sorted = new ArrayList<>(accountsList.getUserAccounts());
        sorted.sort(Comparator.comparing((UserAccount userAccount) -> userAccount.getLocalDateTimeLastLogin()).reversed()
                .thenComparing(Comparator.comparing((UserAccount userAccount) -> userAccount.getLocalDateTimeLastLogin())));
        ObservableList<UserAccount> userListView = FXCollections.observableArrayList(sorted);
        listView.setItems(userListView);
        listView.setCellFactory(new Callback<ListView<UserAccount>, ListCell<UserAccount>>() {
            @Override
            public ListCell<UserAccount> call(ListView<UserAccount> listView) {
                return new CustomListCell();
            }
        });
    }

    private class CustomListCell extends ListCell<UserAccount> {
        private HBox content;
        private Text name;
        private Text username;
        private Text dateTime;
        private Text storeName;
        private Circle circleImage;

        public CustomListCell() {
            super();
            name = new Text();
            username = new Text();
            dateTime = new Text();
            storeName = new Text();
            VBox vBox = new VBox(name, username, dateTime, storeName);
            circleImage = new Circle(30);
            content = new HBox(circleImage, vBox);
            content.setAlignment(Pos.CENTER);
            HBox.setHgrow(vBox, Priority.ALWAYS);
            content.setSpacing(10);
        }

        @Override
        protected void updateItem(UserAccount userAccount, boolean empty) {
            super.updateItem(userAccount, empty);
            if (userAccount != null && !empty) { // <== test for null item and empty parameter
                name.setText("Name : " + userAccount.getName());
                username.setText("Username : " + userAccount.getUsername());
                if (userAccount.getRole().equals("Seller")) {
                    storeName.setText("Store name : " + ((Seller)userAccount).getStoreName());
                } else {
                    storeName.setText("ยังไม่ได้สร้างร้านค้า");
                }
                dateTime.setText("วันเวลาที่เข้าใช้ล่าสุด : " + userAccount.getLastLogin());
                if (userAccount.haveUserImage()) {
                    File f = new File("data/user_image/"+userAccount.getUserImage());
                    circleImage.setFill(new ImagePattern(new Image("file:///" + f.getAbsolutePath())));
                } else {
                    circleImage.setFill(new ImagePattern(new Image(getClass().getResource("/ku/cs/images/default_profile.png").toExternalForm())));
                }
                if (!LoginController.isLightMode) {
                    name.setFill(Color.WHITE);
                    username.setFill(Color.WHITE);
                    dateTime.setFill(Color.WHITE);
                    storeName.setFill(Color.WHITE);
                }
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }
    }

    @FXML void handleGoToShowReportedUserBtn(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("reported_user", info);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า reported_user ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML void handleGoToChangePasswordBtn(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("change_password", info);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า change_password ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML void handleLogoutBtn(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
}
