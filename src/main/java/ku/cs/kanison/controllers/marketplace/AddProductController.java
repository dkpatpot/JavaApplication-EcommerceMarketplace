package ku.cs.kanison.controllers.marketplace;

import animatefx.animation.Tada;
import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.kanison.models.account.AccountList;
import ku.cs.kanison.models.product.Product;
import ku.cs.kanison.models.product.ProductList;
import ku.cs.kanison.services.DataSource;
import ku.cs.kanison.services.Info;
import ku.cs.kanison.services.ProductListFileDataSource;
import ku.cs.kanison.services.ThemeMode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

public class AddProductController {
    @FXML private TextField productNameTextField;
    @FXML private TextField productPriceTextField;
    @FXML private TextField quantityTextField;
    @FXML private TextField productDetailTextField;
    @FXML private Rectangle rectangleImage;
    @FXML private ComboBox categoryComboBox;
    @FXML private AnchorPane pane;

    private File imageFile;
    private ProductList productList;
    private String currentCategory = "-";
    private Info info;
    private AccountList accountsList;
    private Alert alert;

    @FXML
    public void initialize() {
        info = (Info) FXRouter.getData();
        accountsList = info.getAccountList();
        productList = info.getProductList();
        alert = new Alert(Alert.AlertType.NONE);
        ThemeMode.setThemeMode(pane);
        categoryComboBox.getItems().addAll(
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
        System.out.println("initialize AddProductController");
    }

    @FXML
    public void handleAddProductBtn(ActionEvent actionEvent) {
        DataSource<ProductList> productListDataSource = new ProductListFileDataSource();
        String name = productNameTextField.getText();
        String detail = productDetailTextField.getText();
            if (imageFile==null) {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("กรุณาเพิ่มรูปภาพสินค้า");
                alert.show();
            }
            else if (currentCategory.equals("-")) {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("กรุณาเลือกประเภทของสินค้า");
                alert.show();
            }
            else if (name.trim().isBlank()) {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("กรุณาใส่ชื่อสินค้า");
                alert.show();
            }
            else if (detail.trim().isBlank()) {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("กรุณาใส่รายละเอียดสินค้า");
                alert.show();
            }
            else {
                try {
                    String productDetail = productDetailTextField.getText();
                    double price = Double.parseDouble(productPriceTextField.getText());
                    int quantity = Integer.parseInt(quantityTextField.getText());

                    if (price>0&&quantity>0) {
                        File dest = new File("data/product_image/" + (productList.findProductID()) + ".png");
                        try {
                            Files.copy(imageFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e) {
                            System.err.println("Image is not there");
                        }
                        Product product = new Product(name, productDetail, price, quantity, LocalDateTime.now(),
                                productList.findProductID() + ".png", productList.findProductID(),
                                0, 0, accountsList.getThisAccount().getUsername(), currentCategory);
                        productList.addProduct(product);
                        productListDataSource.writeData(productList);
                        alert.setAlertType(Alert.AlertType.INFORMATION);
                        alert.setContentText("ทำการเพิ่มสินค้าเสร็จสิ้น");
                        alert.show();

                        try {
                            Info infoForPassing = new Info(accountsList, productList, info.getReviewList(),
                                    info.getOrderList(), info.getReportList());
                            FXRouter.goTo("store", infoForPassing);
                        } catch (IOException e) {
                            System.err.println("ไปที่หน้า store ไม่ได้");
                            System.err.println("ให้ตรวจสอบการกำหนด route");
                        }
                    } else {
                        alert.setAlertType(Alert.AlertType.WARNING);
                        alert.setContentText("ไม่สามารถใส่ตัวเลขน้อยกว่า 0 ได้");
                        alert.show();
                    }
                } catch (NumberFormatException e) {
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.setContentText("กรุณาใส่ตัวเลขให้ถูกต้อง");
                    alert.show();
                }
            }
        }

    @FXML
    public void handleBackToStoreBtn(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("store");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า store ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML void handleUploadProductImageBtn(ActionEvent actionEvent) throws IOException {
        FileChooser chooserImage = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Images","*.jpg", "*.png", "*.jpeg");
        chooserImage.getExtensionFilters().add(extFilter);
        imageFile = chooserImage.showOpenDialog(new Stage());
        if(imageFile != null) {
            String imageName = imageFile.toURI().toURL().toString();
            rectangleImage.setFill(new ImagePattern(new Image(imageName)));
            new Tada(rectangleImage).play();
        }
    }

    @FXML
    public void chooseCategory(ActionEvent actionEvent) {
        if (categoryComboBox.getValue().equals("เสื้อผ้า")) {
            currentCategory = "เสื้อผ้า";
        }
        if (categoryComboBox.getValue().equals("บำรุงและเสริมสวย")) {
            currentCategory = "บำรุงและเสริมสวย";
        }
        if (categoryComboBox.getValue().equals("สุขภาพ")) {
            currentCategory = "สุขภาพ";
        }
        if (categoryComboBox.getValue().equals("เครื่องประดับ")) {
            currentCategory = "เครื่องประดับ";
        }
        if (categoryComboBox.getValue().equals("เครื่องใช้ไฟฟ้า")) {
            currentCategory = "เครื่องใช้ไฟฟ้า";
        }
        if (categoryComboBox.getValue().equals("กระเป๋า")) {
            currentCategory = "กระเป๋า";
        }
        if (categoryComboBox.getValue().equals("เครื่องใช้")) {
            currentCategory = "เครื่องใช้";
        }
        if (categoryComboBox.getValue().equals("ของเล่น")) {
            currentCategory = "ของเล่น";
        }
        if (categoryComboBox.getValue().equals("อาหารและเครื่องดื่ม")) {
            currentCategory = "อาหารและเครื่องดื่ม";
        }
    }
}
