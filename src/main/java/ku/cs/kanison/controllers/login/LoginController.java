package ku.cs.kanison.controllers.login;

import animatefx.animation.Flash;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import ku.cs.kanison.models.account.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.github.saacsos.FXRouter;
import ku.cs.kanison.models.product.OrderList;
import ku.cs.kanison.models.product.ProductList;
import ku.cs.kanison.models.product.ReviewList;
import ku.cs.kanison.models.report.ReportList;
import ku.cs.kanison.services.*;

public class LoginController {
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordField;
    @FXML private AnchorPane pane;
    @FXML private Button mode;

    private Info info;
    private Alert alert;
    private AccountList accountsList;
    private ProductList productList;
    private ReviewList reviewList;
    private OrderList orderList;
    private ReportList reportList;
    private DataSource<AccountList> accountListDataSource;
    private DataSource<ProductList> productListDataSource;
    private DataSource<ReviewList> reviewListDataSource;
    private DataSource<OrderList> orderListDataSource;
    private DataSource<ReportList> reportListDataSource;
    private ImageDirectory imageDirectory;
    public static boolean isLightMode = true;

    @FXML
    public void initialize() {
        alert = new Alert(Alert.AlertType.NONE);

        accountsList = new AccountList();
        productList = new ProductList();
        reviewList = new ReviewList();
        orderList = new OrderList();
        reportList = new ReportList();

        imageDirectory = new ImageDirectory();

        accountListDataSource = new AccountListFileDataSource();
        productListDataSource = new ProductListFileDataSource();
        reviewListDataSource = new ReviewListFileDataSource();
        orderListDataSource = new OrderListFileDataSource();
        reportListDataSource = new ReportListFileDataSource();

        accountsList = accountListDataSource.getData();
        productList = productListDataSource.getData();
        reviewList = reviewListDataSource.getData();
        orderList = orderListDataSource.getData();
        reportList = reportListDataSource.getData();

        if (!accountsList.isExistUsername("admin")) {
            accountsList.addAccount(new Account("Admin","admin", "admin", "admin"));
        }
        accountListDataSource.writeData(accountsList);
        ThemeMode.setThemeMode(pane);
        System.out.println("initialize LoginController");
    }

    @FXML
    public void handleDarkModeBtn(ActionEvent event) {
        isLightMode = !isLightMode;
        if (isLightMode) {
            ThemeMode.setLightMode(pane, mode);
        } else {
            ThemeMode.setDarkMode(pane, mode);
        }
        new Flash(pane).play();
    }

    @FXML
    public void handleLoginBtn(ActionEvent actionEvent) {
        String usernameText = usernameTextField.getText();
        String password = passwordField.getText();
        String scene;
        if(accountsList.checkUsernamePassword(usernameText,password)) {
            System.out.println("Login Success");
            if ("Admin".equals(accountsList.getThisAccount().getRole())) {
                scene = "admin_manager";
            } else {
                if(((UserAccount)(accountsList.getThisAccount())).getUserStatus().equals("Banned")) {
                    ((UserAccount)(accountsList.getThisAccount())).setLoginAttempts();
                    accountListDataSource = new AccountListFileDataSource();
                    accountListDataSource.writeData(accountsList);
                    scene = "login";
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.setContentText("คุณถูกระงับสิทธิ์การเข้าใช้งาน");
                    alert.show();
                } else {
                    LocalDateTime dateTime = LocalDateTime.now();
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    String formatDateTime = dateTime.format(format);
                    ((UserAccount)(accountsList.getThisAccount())).setLastLogin(formatDateTime);
                    accountListDataSource = new AccountListFileDataSource();
                    accountListDataSource.writeData(accountsList);
                    scene = "marketplace";
                }
            }
            try {
                info = new Info(accountsList, productList, reviewList, orderList, reportList);
                FXRouter.goTo(scene, info);
            } catch (IOException e) {
                System.err.println("ไปที่หน้า "+ scene + " ไม่ได้");
                System.err.println("ให้ตรวจสอบการกำหนด route");
            }
        }
        else{
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Username or password is incorrect.");
            alert.show();
        }
    }
    @FXML
    public void handleGoToCreatorBtn(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("creator");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า creator ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
    @FXML
    public void handleGoToRegisterBtn(ActionEvent actionEvent) {
        try {
            info = new Info(accountsList, productList, reviewList, orderList, reportList);
            FXRouter.goTo("register", info);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า register ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
    @FXML
        public void handleGoToSuggestionBtn(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("suggestion");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า suggestion ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
}
