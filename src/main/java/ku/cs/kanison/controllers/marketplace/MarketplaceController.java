package ku.cs.kanison.controllers.marketplace;

import animatefx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.kanison.models.account.AccountList;
import ku.cs.kanison.models.account.UserAccount;
import ku.cs.kanison.services.IllegalConditionException;
import ku.cs.kanison.services.Info;
import com.github.saacsos.FXRouter;
import ku.cs.kanison.models.product.Product;
import ku.cs.kanison.models.product.ProductList;
import ku.cs.kanison.services.ThemeMode;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MarketplaceController {
    @FXML private Label nameLabel;
    @FXML private Circle circleImage;
    @FXML private GridPane grid;
    @FXML private ComboBox comboBox;
    @FXML private ComboBox categoryComboBox;
    @FXML private AnchorPane pane;
    @FXML private ScrollPane scroll;
    @FXML private TextField maxTextField;
    @FXML private TextField minTextField;

    private AccountList accountsList;
    private ProductList productList;
    private Info info;
    private ArrayList<Product> currentProductList;
    private Alert alert;

    @FXML
    public void initialize() {
        alert = new Alert(Alert.AlertType.NONE);
        info = (Info) FXRouter.getData();
        accountsList = info.getAccountList();
        productList = info.getProductList();
        currentProductList = productList.sortProductByLatest();
        ThemeMode.setThemeMode(pane);
        showUserData();
        showProducts(productList.sortProductByLatest());
        comboBox.getItems().addAll(
                "latest",
                "ราคาจากน้อย ไป มาก",
                "ราคาจากมาก ไป น้อย"
        );
        categoryComboBox.getItems().addAll(
                "ประเภททั้งหมด",
                "เสื้อผ้า",
                "บำรุงและเสริมสวย",
                "สุขภาพ",
                "เครื่องประดับ",
                "เครื่องใช้ไฟฟ้า",
                "กระเป๋า",
                "เครื่องใช้",
                "ของเล่น",
                "อาหารและเครื่องดื่ม"
        );
        System.out.println("initialize MarketplaceController");
    }

    private void showUserData() {
        nameLabel.setText(accountsList.getThisAccount().getName());
        if (((UserAccount) accountsList.getThisAccount()).haveUserImage()) {
            File f = new File("data/user_image/" + ((UserAccount) accountsList.getThisAccount()).getUserImage());
            circleImage.setFill(new ImagePattern(new Image("file:///" + f.getAbsolutePath())));
        } else {
            circleImage.setFill(new ImagePattern(new Image(getClass().getResource("/ku/cs/images/default_profile.png").toExternalForm())));
        }

        new Tada(circleImage).play();
    }

    private void showProducts(List<Product> products){
        grid.getChildren().clear();
        int column = 0;
        int row = 1;
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setPannable(true);
        try {
            for (Product product : products) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ku/cs/product.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                ProductController productController = fxmlLoader.getController();
                productController.setData(product, info);
                if (column == 4) {
                    column = 0;
                    ++row;
                }
                grid.add(anchorPane, column++, row);
                GridPane.setMargin(anchorPane, new Insets(9.5));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        new SlideInUp(grid).play();
    }

    @FXML
    public void handleManageUserAccount(ActionEvent actionEvent) {
        try {
            Info infoForPassing = new Info(info.getAccountList(), info.getProductList(), info.getReviewList(), info.getOrderList(), info.getReportList());
            FXRouter.goTo("change_profile", infoForPassing);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า change profile ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
    @FXML
    public void handleLogoutBtn(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public void handleToStoreBtn(ActionEvent actionEvent) {
        String scene;
        if (accountsList.getThisAccount().getRole().equals("Seller")) {
            scene = "store";
        } else scene = "create_store";
        try {
            FXRouter.goTo(scene, info);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ไปที่หน้า store ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public void sortComboBox (ActionEvent event){
        if (comboBox.getValue().equals("latest")) {
            currentProductList = productList.sortProductByLatest(currentProductList);
            showProducts(productList.sortProductByLatest(currentProductList));
        }
        if (comboBox.getValue().equals("ราคาจากน้อย ไป มาก")) {
            currentProductList = productList.sortProductByAscending(currentProductList);
            showProducts(productList.sortProductByAscending(currentProductList));
        }
        if (comboBox.getValue().equals("ราคาจากมาก ไป น้อย")) {
            currentProductList = productList.sortProductByDescending(currentProductList);
            showProducts(productList.sortProductByDescending(currentProductList));
        }
    }

    @FXML
    public void sortByCategory (ActionEvent actionEvent) {
        comboBox.getSelectionModel().select(0);
        if (categoryComboBox.getValue().equals("ประเภททั้งหมด")) {
            currentProductList = productList.sortProductByLatest();
            showProducts(productList.sortProductByLatest());
        }
        if (categoryComboBox.getValue().equals("เสื้อผ้า")) {
            currentProductList = productList.sortProductByCategory(productList.sortProductByLatest(), "เสื้อผ้า");
            showProducts(productList.sortProductByCategory(productList.sortProductByLatest(), "เสื้อผ้า"));
        }
        if (categoryComboBox.getValue().equals("บำรุงและเสริมสวย")) {
            currentProductList = productList.sortProductByCategory(productList.sortProductByLatest(), "บำรุงและเสริมสวย");
            showProducts(productList.sortProductByCategory(productList.sortProductByLatest(), "บำรุงและเสริมสวย"));
        }
        if (categoryComboBox.getValue().equals("สุขภาพ")) {
            currentProductList = productList.sortProductByCategory(productList.sortProductByLatest(), "สุขภาพ");
            showProducts(productList.sortProductByCategory(productList.sortProductByLatest(), "สุขภาพ"));
        }
        if (categoryComboBox.getValue().equals("เครื่องประดับ")) {
            currentProductList = productList.sortProductByCategory(productList.sortProductByLatest(), "เครื่องประดับ");
            showProducts(productList.sortProductByCategory(productList.sortProductByLatest(), "เครื่องประดับ"));
        }
        if (categoryComboBox.getValue().equals("เครื่องใช้ไฟฟ้า")) {
            currentProductList = productList.sortProductByCategory(productList.sortProductByLatest(), "เครื่องใช้ไฟฟ้า");
            showProducts(productList.sortProductByCategory(productList.sortProductByLatest(), "เครื่องใช้ไฟฟ้า"));
        }
        if (categoryComboBox.getValue().equals("กระเป๋า")) {
            currentProductList = productList.sortProductByCategory(productList.sortProductByLatest(), "กระเป๋า");
            showProducts(productList.sortProductByCategory(productList.sortProductByLatest(), "กระเป๋า"));
        }
        if (categoryComboBox.getValue().equals("เครื่องใช้")) {
            currentProductList = productList.sortProductByCategory(productList.sortProductByLatest(), "เครื่องใช้");
            showProducts(productList.sortProductByCategory(productList.sortProductByLatest(), "เครื่องใช้"));
        }
        if (categoryComboBox.getValue().equals("ของเล่น")) {
            currentProductList = productList.sortProductByCategory(productList.sortProductByLatest(), "ของเล่น");
            showProducts(productList.sortProductByCategory(productList.sortProductByLatest(), "ของเล่น"));
        }
        if (categoryComboBox.getValue().equals("อาหารและเครื่องดื่ม")) {
            currentProductList = productList.sortProductByCategory(productList.sortProductByLatest(), "อาหารและเครื่องดื่ม");
            showProducts(productList.sortProductByCategory(productList.sortProductByLatest(), "อาหารและเครื่องดื่ม"));
        }
        maxTextField.clear();
        minTextField.clear();
    }

    @FXML
    public void handleShowProductsInRange(ActionEvent actionEvent) {
        if (minTextField.getText().trim().isEmpty() && maxTextField.getText().trim().isEmpty()){
            categoryComboBox.getSelectionModel().select(0);
            comboBox.getSelectionModel().select(0);
            showProducts(productList.sortProductByLatest());
            return;
        }
        try {
            double minPrice = Double.parseDouble(minTextField.getText());
            double maxPrice = Double.parseDouble(maxTextField.getText());
            categoryComboBox.getSelectionModel().select(0);
            comboBox.getSelectionModel().select(0);
            currentProductList = productList.rangePrice(minPrice, maxPrice);
            showProducts(productList.rangePrice(minPrice, maxPrice));
            minTextField.setText(String.valueOf(minPrice));
            maxTextField.setText(String.valueOf(maxPrice));
        }
        catch (NumberFormatException e){
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("กรุณาใส่ข้อมูลให้ถูกต้อง");
            alert.show();
        }
        catch (IllegalConditionException e){
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }
}
