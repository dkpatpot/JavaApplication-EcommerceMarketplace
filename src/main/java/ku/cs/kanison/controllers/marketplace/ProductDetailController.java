package ku.cs.kanison.controllers.marketplace;

import animatefx.animation.Tada;
import com.github.saacsos.FXRouter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.util.Callback;
import ku.cs.kanison.controllers.login.LoginController;
import ku.cs.kanison.models.account.AccountList;
import ku.cs.kanison.models.account.Seller;
import ku.cs.kanison.services.*;
import ku.cs.kanison.models.product.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class ProductDetailController {
    @FXML private TextField reviewTextField;
    @FXML private ListView<Review> reviewListView;
    @FXML private Label productNameLabel;
    @FXML private ImageView productImage;
    @FXML private Label storeNameLabel;
    @FXML private SVGPath star1;
    @FXML private SVGPath star2;
    @FXML private SVGPath star3;
    @FXML private SVGPath star4;
    @FXML private SVGPath star5;
    @FXML private Circle circle;
    @FXML private Label availableLabel;
    @FXML private Label productDetailLabel;
    @FXML private Label productQuantityLabel;
    @FXML private Label productPriceLabel;
    @FXML private Label availableTexLabel;
    @FXML private Label averageRatingLabel;
    @FXML private Label numberOfReviewerLabel;
    @FXML private AnchorPane pane;

    private ReviewList reviewList;
    private AccountList accountList;
    private ProductList productList;
    private OrderList orderList;
    private Info info;
    private int currentUserStar; //เอามารับค่าที่ User ให้ดาวแล้วไป Add ใน Product กับ Review
    private int userProductQuantity = 1; //เอามารับค่าจำนวนสินค้าที่จะสั่ง
    private Alert alert;
    private DataSource<ReviewList> reviewListDataSource;
    private DataSource<ProductList> productListDataSource;

    @FXML
    public void initialize() {
        info = (Info) FXRouter.getData();
        alert = new Alert(Alert.AlertType.NONE);
        accountList = info.getAccountList();
        productList = info.getProductList();
        reviewList = info.getReviewList();
        orderList = info.getOrderList();
        reviewListDataSource = new ReviewListFileDataSource();
        productListDataSource = new ProductListFileDataSource();

        ThemeMode.setThemeMode(pane);
        showUserData();
        showProductDetail();
        showStoreData();
        System.out.println("initialize ProductDetailController");
    }

    void showUserData() {
        ArrayList<Review> sorted = new ArrayList<>(reviewList.getThisProductReview(productList.getThisProduct()));
        sorted.sort(Comparator.comparing((Review review) -> review.formatHistory()).reversed()
                .thenComparing(Comparator.comparing((Review review) -> review.formatHistory())));
        ObservableList<Review> reviewObservableList = FXCollections.observableArrayList(sorted);
        reviewListView.setItems(reviewObservableList);
        reviewListView.setCellFactory(new Callback<ListView<Review>, ListCell<Review>>() {
            @Override
            public ListCell<Review> call(ListView<Review> reviewListView) {
                return new CustomListCell();
            }
        });
    }

    private void showStoreData(){
        storeNameLabel.setText(((Seller)(accountList.searchUserAccountByUsername
                (productList.getThisProduct().getSellerUserName()))).getStoreName());
        if ((accountList.searchUserAccountByUsername(productList.getThisProduct().getSellerUserName())).haveUserImage()) {
            File f = new File("data/user_image/"+(accountList.searchUserAccountByUsername
                    (productList.getThisProduct().getSellerUserName()).getUserImage()));
            circle.setFill(new ImagePattern(new Image("file:///" + f.getAbsolutePath())));
        } else {
            circle.setFill(new ImagePattern(new Image(getClass().getResource
                    ("/ku/cs/images/default_profile.png").toExternalForm())));
        }
    }

    private class CustomListCell extends ListCell<Review> {
        private HBox content;
        private Circle circleImage;
        private Text userReview;
        private Button button;
        private Text nameAndTimestamp;
        private Text star;

        public CustomListCell() {
            super();
            nameAndTimestamp = new Text();
            userReview = new Text();
            star = new Text();
            VBox vBox = new VBox(nameAndTimestamp, userReview, star);
            circleImage = new Circle(25);
            button = new Button("Report");
            content = new HBox(circleImage, vBox, button);
            content.setAlignment(Pos.CENTER);
            HBox.setHgrow(vBox, Priority.ALWAYS);
            content.setSpacing(10);
        }

        @Override
        protected void updateItem(Review review, boolean empty) {
            super.updateItem(review, empty);
            if (review != null && !empty) { // <== test for null item and empty parameter
                nameAndTimestamp.setText(accountList.searchUserAccountByUsername(review.getUsername()).getName()
                        + " (" + review.formatHistory() + ")" );
                userReview.setText(review.getComment());
                star.setText("★".repeat(review.getStar()));
                button.setTextFill(Color.RED);
                button.setStyle("-fx-background-color: white;" +
                        "-fx-border-color: red;" + "-fx-background-radius: 20px;" + "-fx-border-radius: 20px");
                button.setOnAction(event -> getListView().getItems());
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        try {
                            reviewList.setThisReview(review);
                            Info infoForPassing = new Info(accountList, productList, reviewList, info.getOrderList(),
                                    info.getReportList());
                            FXRouter.goTo("review_report", infoForPassing);
                        } catch (IOException e) {
                            System.err.println("ไปที่หน้า review_report ไม่ได้");
                            System.err.println("ให้ตรวจสอบการกำหนด route");
                        }
                    }
                });
                if ((accountList.searchUserAccountByUsername(review.getUsername())).haveUserImage()) {
                    File f = new File("data/user_image/"+(accountList.searchUserAccountByUsername
                            (review.getUsername()).getUserImage()));
                    circleImage.setFill(new ImagePattern(new Image("file:///" + f.getAbsolutePath())));
                } else {
                    circleImage.setFill(new ImagePattern(new Image(getClass().getResource
                            ("/ku/cs/images/default_profile.png").toExternalForm())));
                }
                if (!LoginController.isLightMode) {
                    nameAndTimestamp.setFill(Color.WHITE);
                    userReview.setFill(Color.WHITE);
                    star.setFill(Color.WHITE);
                }
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }
    }

    private void showProductDetail() {
        productNameLabel.setText((productList.getThisProduct().getProductName()));
        File f = new File("data/product_image/"+productList.getThisProduct().getID()+".png");
        Rectangle clip = new Rectangle(
                productImage.getFitWidth(), productImage.getFitHeight()
        );
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        productImage.setClip(clip);
        productImage.setImage(new Image("file:///" + f.getAbsolutePath()));
        new Tada(productImage).play();
        if (productList.getThisProduct().getQuantity() <= (((Seller)accountList.searchUserAccountByUsername
                (productList.getThisProduct().getSellerUserName()))).getLowQuantityStore()) {
            availableLabel.setText(""+productList.getThisProduct().getQuantity());
            availableLabel.setTextFill(Color.RED);
            availableTexLabel.setTextFill(Color.RED);
        } else {
            availableLabel.setText(""+productList.getThisProduct().getQuantity());
        }
        if (productList.getThisProduct().getCountRating()==0){
            averageRatingLabel.setText("0");
        }
        else{
            averageRatingLabel.setText(String.format("%.2f",productList.getThisProduct().getRating()));
        }
        System.out.println("avg : "+productList.getThisProduct().getRating());
        System.out.println(("count : "+productList.getThisProduct().getCountRating()));
        System.out.println(("all : "+productList.getThisProduct().getAllRating()));
        productDetailLabel.setText(productList.getThisProduct().getProductDetail());
        productQuantityLabel.setText("1");
        productPriceLabel.setText(""+productList.getThisProduct().getPrice());
        numberOfReviewerLabel.setText(""+productList.getThisProduct().getCountRating());
        productListDataSource.writeData(productList);
    }

    public void handleSendBtn(ActionEvent actionEvent){
        String userReviewText = reviewTextField.getText();
        if (currentUserStar == 0) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("กรุณาใส่ดาว");
            alert.show();
        }
        else if(userReviewText.trim().isBlank()) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("กรุณาใส่ความคิดเห็น");
            alert.show();
        } else {
            reviewList.addReview(new Review(
                    (accountList.getThisAccount()).getUsername(), productList.getThisProduct().getID(), LocalDateTime.now(),
                    userReviewText, currentUserStar));
            productList.getThisProduct().setRating(currentUserStar);
            reviewListDataSource.writeData(reviewList);
            try {
                FXRouter.goTo("product_detail", info);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleBackToMarketplaceBtn(ActionEvent actionEvent) {
        try {
            Info infoForPassing = new Info(accountList, productList, reviewList, info.getOrderList(), info.getReportList());
            FXRouter.goTo("marketplace", infoForPassing);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public double handleStarOne(MouseEvent mouseEvent) {
        System.out.println("mouse click star 1 detected!");
        currentUserStar = 1;
        String svgPath = "M12,17.27L18.18,21l-1.64-7.03L22,9.24l-7.19-0.61L12,2L9.19,8.63L2,9.24l5.46,4.73L5.82,21L12,17.27z";
        String svgEmptyPath = "M22 9.24l-7.19-.62L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21 12 17.27 18.18 21l-1.63-7.03L22" +
                " 9.24zM12 15.4l-3.76 2.27 1-4.28-3.32-2.88 4.38-.38L12 6.1l1.71 4.04 4.38.38-3.32 2.88 1 4.28L12 15.4z";
        star5.setContent(svgEmptyPath);
        star4.setContent(svgEmptyPath);
        star3.setContent(svgEmptyPath);
        star2.setContent(svgEmptyPath);
        star1.setContent(svgPath);
        return currentUserStar;
    }

    @FXML
    public double handleStarTwo(MouseEvent mouseEvent) {
        System.out.println("mouse click star 2 detected!");
        currentUserStar = 2;
        String svgPath = "M12,17.27L18.18,21l-1.64-7.03L22,9.24l-7.19-0.61L12,2L9.19,8.63L2,9.24l5.46,4.73L5.82,21L12,17.27z";
        String svgEmptyPath = "M22 9.24l-7.19-.62L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21 12 17.27 18.18 21l-1.63-7.03L22" +
                " 9.24zM12 15.4l-3.76 2.27 1-4.28-3.32-2.88 4.38-.38L12 6.1l1.71 4.04 4.38.38-3.32 2.88 1 4.28L12 15.4z";
        star5.setContent(svgEmptyPath);
        star4.setContent(svgEmptyPath);
        star3.setContent(svgEmptyPath);
        star1.setContent(svgPath);
        star2.setContent(svgPath);
        return currentUserStar;
    }

    @FXML
    public double handleStarThree(MouseEvent mouseEvent) {
        System.out.println("mouse click star 3 detected!");
        currentUserStar = 3;
        String svgPath = "M12,17.27L18.18,21l-1.64-7.03L22,9.24l-7.19-0.61L12,2L9.19,8.63L2,9.24l5.46,4.73L5.82,21L12,17.27z";
        String svgEmptyPath = "M22 9.24l-7.19-.62L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21 12 17.27 18.18 21l-1.63-7.03L22" +
                " 9.24zM12 15.4l-3.76 2.27 1-4.28-3.32-2.88 4.38-.38L12 6.1l1.71 4.04 4.38.38-3.32 2.88 1 4.28L12 15.4z";
        star5.setContent(svgEmptyPath);
        star4.setContent(svgEmptyPath);
        star1.setContent(svgPath);
        star2.setContent(svgPath);
        star3.setContent(svgPath);
        return currentUserStar;
    }

    @FXML
    public double handleStarFour(MouseEvent mouseEvent) {
        System.out.println("mouse click star 4 detected!");
        currentUserStar = 4;
        String svgPath = "M12,17.27L18.18,21l-1.64-7.03L22,9.24l-7.19-0.61L12,2L9.19,8.63L2,9.24l5.46,4.73L5.82,21L12,17.27z";
        String svgEmptyPath = "M22 9.24l-7.19-.62L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21 12 17.27 18.18 21l-1.63-7.03L22" +
                " 9.24zM12 15.4l-3.76 2.27 1-4.28-3.32-2.88 4.38-.38L12 6.1l1.71 4.04 4.38.38-3.32 2.88 1 4.28L12 15.4z";
        star5.setContent(svgEmptyPath);
        star1.setContent(svgPath);
        star2.setContent(svgPath);
        star3.setContent(svgPath);
        star4.setContent(svgPath);
        return currentUserStar;
    }

    @FXML
    public double handleStarFive(MouseEvent mouseEvent) {
        System.out.println("mouse click star 5 detected!");
        currentUserStar = 5;
        String svgPath = "M12,17.27L18.18,21l-1.64-7.03L22,9.24l-7.19-0.61L12,2L9.19,8.63L2,9.24l5.46,4.73L5.82,21L12,17.27z";
        star1.setContent(svgPath);
        star2.setContent(svgPath);
        star3.setContent(svgPath);
        star4.setContent(svgPath);
        star5.setContent(svgPath);
        return currentUserStar;
    }

    @FXML
    public int handlePlusBtn(MouseEvent mouseEvent) {
        System.out.println("mouse click plus detected!");
        if (userProductQuantity==productList.getThisProduct().getQuantity()) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("This product has only " + productList.getThisProduct().getQuantity() + " pieces.");
            alert.show();
            return userProductQuantity;
        }
        userProductQuantity++;
        productQuantityLabel.setText("" + userProductQuantity);
        return userProductQuantity;
    }

    @FXML
    public int handleMinusBtn(MouseEvent mouseEvent) {
        System.out.println("mouse click minus detected!");
        if (userProductQuantity==1) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("You cannot select the number of products less than 1.");
            alert.show();
            return userProductQuantity;
        }
        userProductQuantity--;
        productQuantityLabel.setText("" + userProductQuantity);
        return userProductQuantity;
    }

    @FXML
    public void handlePurchaseBtn(ActionEvent actionEvent) {
        if (productList.getThisProduct().getQuantity()==0) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("This product is out of stock.");
            alert.show();
        }
        else {
            Order thisOrder = new Order(orderList.createOrderNumber(), productList.getThisProduct().
                    createTotal(userProductQuantity), productList.getThisProduct().createTotal(userProductQuantity),
                    userProductQuantity, productList.getThisProduct().getID(), accountList.getThisAccount().getUsername(),
                    "-", productList.getThisProduct().getSellerUserName());
            orderList.addOrder(thisOrder);
            orderList.setThisOrder(thisOrder);
            try {
                Info infoForPassing = new Info(accountList, productList, reviewList, orderList, info.getReportList());
                FXRouter.goTo("confirm_purchase", infoForPassing);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleReportProductBtn(ActionEvent event) {
        try {
            Info infoForPassing = new Info(accountList, productList, reviewList, orderList, info.getReportList());
            FXRouter.goTo("product_report", infoForPassing);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSellerStore(MouseEvent mouseEvent) {
        System.out.printf("mouse click handleSellerStore detected!");
        try {
            Info infoForPassing = new Info(accountList, productList, reviewList, orderList, info.getReportList());
            FXRouter.goTo("seller_marketplace", infoForPassing);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
