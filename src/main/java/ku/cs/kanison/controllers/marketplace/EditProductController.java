package ku.cs.kanison.controllers.marketplace;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.kanison.services.DataSource;
import ku.cs.kanison.services.Info;
import ku.cs.kanison.services.ProductListFileDataSource;
import ku.cs.kanison.models.product.ProductList;
import ku.cs.kanison.services.ThemeMode;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class EditProductController {
    @FXML private ImageView productImage;
    @FXML private TextField productNameTextField;
    @FXML private TextField productPriceTextField;
    @FXML private Label nowQuantityLabel;
    @FXML private TextField addQuantityLabel;
    @FXML private ComboBox categoryComboBox;
    @FXML private AnchorPane pane;

    private Alert alert;
    private Info info;
    private ProductList productList;
    private DataSource<ProductList> productListDataSource;
    private String currentCategory;

    @FXML
    public void initialize() {
        alert = new Alert(Alert.AlertType.NONE);
        info = (Info) FXRouter.getData();
        productList = info.getProductList();
        productListDataSource = new ProductListFileDataSource();
        ThemeMode.setThemeMode(pane);
        showProductData();
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
        System.out.println("initialize EditProductController");
    }

    private void showProductData() {
        File f = new File("data/product_image/"+productList.getThisProduct().getID()+".png");
        Rectangle clip = new Rectangle(
                productImage.getFitWidth(), productImage.getFitHeight()
        );
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        productImage.setClip(clip);
        productImage.setImage(new Image("file:///" + f.getAbsolutePath()));
        productNameTextField.setText(productList.getThisProduct().getProductName());
        productPriceTextField.setText(productList.getThisProduct().getPrice()+"");
        nowQuantityLabel.setText(productList.getThisProduct().getQuantity()+"");
        categoryComboBox.setPromptText(productList.getThisProduct().getCategory());
        currentCategory = productList.getThisProduct().getCategory();
    }

    @FXML
    void handleChangeBtn(ActionEvent event) {
        try {
            double price = Double.parseDouble(productPriceTextField.getText());
            String name = productNameTextField.getText();
            if (name.isEmpty()) {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("กรุณาใส่ชื่อสินค้า");
                alert.show();
            }
            else if (price<0) {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("ไม่สามารถกำหนดราคาน้อยกว่า 0 ได้");
                alert.show();
            }
            else {
                productList.getThisProduct().setProductName(productNameTextField.getText());
                productList.getThisProduct().setPrice(Double.parseDouble(productPriceTextField.getText()));
                productList.getThisProduct().setCategory(currentCategory);
                productListDataSource = new ProductListFileDataSource();
                productListDataSource.writeData(productList);

                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setContentText("ทำการแก้ไขสินค้าเสร็จสิ้น");
                alert.show();
            }
        } catch (NumberFormatException e) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("ได้โปรดกรุณาใส่ตัวเลขให้ถูกต้อง");
            alert.show();
        }

        try {
            Info infoForPassing = new Info(info.getAccountList(), productList, info.getReviewList(),
                    info.getOrderList(), info.getReportList());
            FXRouter.goTo("edit_product", infoForPassing);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า edit_product ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    void handleUploadAnImageBtn(ActionEvent event) {
        FileChooser chooserImage = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Images","*.jpg", "*.png", "*.jpeg");
        chooserImage.getExtensionFilters().add(extFilter);
        File imageFile = chooserImage.showOpenDialog(new Stage());

        if (imageFile != null) {
            try {
                String imageName = imageFile.toURI().toURL().toString();
                productImage.setImage(new Image(imageName));
                File dest = new File("data/product_image/"+productList.getThisProduct().getID()+".png");
                Files.copy(imageFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                productListDataSource = new ProductListFileDataSource();
                productListDataSource.writeData(productList);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleBackToStoreBtn(ActionEvent actionEvent) {
        try {
            Info infoForPassing = new Info(info.getAccountList(), productList, info.getReviewList(),
                    info.getOrderList(), info.getReportList());
            FXRouter.goTo("store", infoForPassing);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า store ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public void handleAddBtn(ActionEvent actionEvent) {

        try {
            int add = Integer.parseInt(addQuantityLabel.getText());
            if (add>0) {
                productList.getThisProduct().addQuantity(add);
                productListDataSource = new ProductListFileDataSource();
                productListDataSource.writeData(productList);
                try {
                    Info infoForPassing = new Info(info.getAccountList(), productList, info.getReviewList(),
                            info.getOrderList(), info.getReportList());
                    FXRouter.goTo("edit_product", infoForPassing);
                } catch (IOException e) {
                    System.err.println("ไปที่หน้า edit_product ไม่ได้");
                    System.err.println("ให้ตรวจสอบการกำหนด route");
                }
            } else {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("ได้โปรดกรุณาใส่ค่าอย่างถูกต้องด้วยครับ");
                alert.show();
            }
        } catch (NumberFormatException e) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("ได้โปรดกรุณาใส่ค่าอย่างถูกต้องด้วยครับ");
            alert.show();
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
