package ku.cs.kanison.controllers.marketplace;

import com.github.saacsos.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import ku.cs.kanison.services.Info;
import ku.cs.kanison.models.product.Product;
import ku.cs.kanison.models.product.ProductList;
import java.io.File;
import java.io.IOException;

public class ProductController {
    @FXML private Label productNameLabel;
    @FXML private Label priceLabel;
    @FXML private Rectangle productImage;

    private Product product;
    private Info info;

    @FXML void initialize() {
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
    }

    @FXML
    private void handleMouseEvent(MouseEvent event) {
        System.out.println("goTo = " + product.getProductName() + ": " + product.getPrice());
        ProductList productList = info.getProductList();
        productList.setThisProduct(product);
        try {
            FXRouter.goTo("product_detail", info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
