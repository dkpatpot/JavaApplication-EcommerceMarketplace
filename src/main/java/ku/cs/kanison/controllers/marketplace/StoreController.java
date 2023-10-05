package ku.cs.kanison.controllers.marketplace;

import animatefx.animation.SlideInUp;
import animatefx.animation.Tada;
import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.kanison.models.account.AccountList;
import ku.cs.kanison.models.account.Seller;
import ku.cs.kanison.models.account.UserAccount;
import ku.cs.kanison.models.product.Product;
import ku.cs.kanison.models.product.ProductList;
import ku.cs.kanison.services.Info;
import ku.cs.kanison.services.ThemeMode;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class StoreController {
    @FXML private Circle circleImage;
    @FXML private Label storeNameLabel;
    @FXML private Label usernameLabel;
    @FXML private GridPane grid;
    @FXML private AnchorPane pane;
    @FXML private ScrollPane scroll;

    private Info info;
    private AccountList accountsList;

    @FXML
    public void initialize() {
        info = (Info) FXRouter.getData();
        accountsList = info.getAccountList();
        ProductList productList = info.getProductList();
        System.out.println("initialize Store Controller");
        ThemeMode.setThemeMode(pane);
        showStoreData();
        showProducts(productList.getThisSellerProductList(accountsList.getThisAccount()));
    }

    private void showProducts(List<Product> products){
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setPannable(true);
        int column = 0;
        int row = 1;
        try {
            for (Product product : products) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ku/cs/product_for_seller.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                ProductForSellerController productForSellerController = fxmlLoader.getController();
                productForSellerController.setData(product, info);
                if (column == 3) {
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
    private void showStoreData(){
        storeNameLabel.setText(((Seller)accountsList.getThisAccount()).getStoreName());
        usernameLabel.setText(accountsList.getThisAccount().getUsername());
        if (((UserAccount)accountsList.getThisAccount()).haveUserImage()) {
            File f = new File("data/user_image/"+((UserAccount)accountsList.getThisAccount()).getUserImage());
            circleImage.setFill(new ImagePattern(new Image("file:///" + f.getAbsolutePath())));
        } else {
            circleImage.setFill(new ImagePattern(new Image(getClass().getResource("/ku/cs/images/default_profile.png").toExternalForm())));
        }
        new Tada(circleImage).play();
    }
    @FXML
    public void handleToEditStoreBtn(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("edit_store", info);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า edit_store ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public void handleToOrderListBtn(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("order_list", info);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า order_list ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public void handleToAddProductBtn(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("add_product", info);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า add_product ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public void handleBackToMarketplaceBtn(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("marketplace", info);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า marketplace ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public void handleToPromotionBtn(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("create_promotion_code", info);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า create_promotion_code ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

}
