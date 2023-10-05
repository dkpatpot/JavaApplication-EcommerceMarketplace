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
import ku.cs.kanison.services.Info;
import ku.cs.kanison.models.product.Product;
import ku.cs.kanison.models.product.ProductList;
import ku.cs.kanison.services.ThemeMode;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class SellerMarketplaceController {
    @FXML private Label storeNameLabel;
    @FXML private Circle circleImage;
    @FXML private GridPane grid;
    @FXML private AnchorPane pane;
    @FXML private ScrollPane scroll;

    private AccountList accountsList;
    private ProductList productList;
    private Info info;

    public void initialize() {
        info = (Info) FXRouter.getData();
        accountsList = info.getAccountList();
        productList = info.getProductList();
        ThemeMode.setThemeMode(pane);
        showStoreData();
        showProductsOfStore(productList.getThisSellerProductList(accountsList.searchUserAccountByUsername(productList.getThisProduct().getSellerUserName())));
    }

    private void showProductsOfStore(List<Product> products){
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

    private void showStoreData(){
        storeNameLabel.setText(((Seller)accountsList.searchUserAccountByUsername(productList.getThisProduct().getSellerUserName())).getStoreName());
        if (((UserAccount)accountsList.searchUserAccountByUsername(productList.getThisProduct().getSellerUserName())).haveUserImage()) {
            File f = new File("data/user_image/"+((UserAccount)accountsList.searchUserAccountByUsername(productList.getThisProduct().getSellerUserName())).getUserImage());
            circleImage.setFill(new ImagePattern(new Image("file:///" + f.getAbsolutePath())));
            System.out.println("data/user_image/"+((UserAccount)accountsList.getThisAccount()).getUserImage());
            System.out.println("non null");
            System.out.println(((UserAccount)accountsList.getThisAccount()).getUserImage());
        } else {
            circleImage.setFill(new ImagePattern(new Image(getClass().getResource("/ku/cs/images/default_profile.png").toExternalForm())));
        }
        new Tada(circleImage).play();
    }

    @FXML
    public void handleBackBtn(ActionEvent actionEvent) {
        try {
            Info infoForPassing = new Info(accountsList, productList, info.getReviewList(), info.getOrderList(), info.getReportList());
            FXRouter.goTo("product_detail", infoForPassing);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
