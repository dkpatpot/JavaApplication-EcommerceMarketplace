package ku.cs.kanison.controllers.marketplace;

import com.github.saacsos.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import ku.cs.kanison.models.account.AccountList;
import ku.cs.kanison.models.account.Seller;
import ku.cs.kanison.services.Info;
import ku.cs.kanison.models.product.Product;
import ku.cs.kanison.models.product.ProductList;

import java.io.File;
import java.io.IOException;

public class ProductForSellerController {
    @FXML private Label productNameLabel;
    @FXML private Label priceLabel;
    @FXML private Label quantityLabel;
    @FXML private Rectangle productImage;
    @FXML private ImageView button;

    private Product product;
    private AccountList accountList;
    private ProductList productList;
    private Info info;

    @FXML void initialize() {
        info = (Info) FXRouter.getData();
        accountList = info.getAccountList();
        productList = info.getProductList();
        System.out.println("initialize EditStoreController");
    }

    public void setData(Product product, Info info) {
        File f = new File("data/product_image/"+product.getID()+".png");
        productImage.setFill(new ImagePattern(new Image("file:///" + f.getAbsolutePath())));
        productImage.setArcHeight(15);
        productImage.setArcWidth(15);
        this.product = product;
        this.info = info;
        productNameLabel.setText(product.getProductName());
        priceLabel.setText(product.getPrice()+"฿");
        quantityLabel.setText("คงเหลือ : " + product.getQuantity()+"");
        if (product.getQuantity() <= ((Seller)accountList.getThisAccount()).getLowQuantityStore()) {
            quantityLabel.setTextFill(Color.RED);
            button.setImage(new Image(getClass().getResource("/ku/cs/images/add-stock.png").toExternalForm()));
        }
    }

    @FXML
    private void handleMouseEvent(MouseEvent event) {
        System.out.println("goTo = " + product.getProductName() + ": " + product.getPrice());
        productList.setThisProduct(product);
        try {
            Info infoForPassing = new Info(info.getAccountList(), productList, info.getReviewList(), info.getOrderList(), info.getReportList());
            FXRouter.goTo("edit_product", infoForPassing);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
