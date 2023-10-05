package ku.cs.kanison.controllers.marketplace;

import com.github.saacsos.FXRouter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Callback;
import ku.cs.kanison.controllers.login.LoginController;
import ku.cs.kanison.models.account.AccountList;
import ku.cs.kanison.models.account.Seller;
import ku.cs.kanison.models.account.UserAccount;
import ku.cs.kanison.models.product.Order;
import ku.cs.kanison.models.product.OrderList;
import ku.cs.kanison.models.product.ProductList;
import ku.cs.kanison.services.Info;
import ku.cs.kanison.services.ThemeMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class OrderListController {
    @FXML private Circle circleImage;
    @FXML private Label storeNameLabel;
    @FXML private Label usernameLabel;
    @FXML private ListView<Order> shippedOrderListView;
    @FXML private ListView<Order> orderedListView;
    @FXML private AnchorPane pane;


    private Info info;
    private AccountList accountList;
    private ProductList productList;
    private OrderList orderList;

    @FXML
    public void initialize() {
        info = (Info) FXRouter.getData();
        accountList = info.getAccountList();
        productList = info.getProductList();
        orderList = info.getOrderList();
        ThemeMode.setThemeMode(pane);
        showShippedOrderData();
        showOrderData();
        showStoreData();
        System.out.println("initialize OrderListController");
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
    void showOrderData() {
        ArrayList<Order> sorted = new ArrayList<>(orderList.getThisStoreUnshippedOrderList(orderList.getThisStoreOderList(accountList.getThisAccount())));
        sorted.sort(Comparator.comparing((Order order) -> order.getOrderNumber()).thenComparing(Comparator.comparing((Order order) -> order.getOrderNumber())));
        ObservableList<Order> orderListView = FXCollections.observableArrayList(sorted);
        orderedListView.setItems(orderListView);
        orderedListView.setCellFactory(new Callback<ListView<Order>, ListCell<Order>>() {
            @Override
            public ListCell<Order> call(ListView<Order> listView) {
                return new CustomUnshippedOrderListCell();
            }
        });
    }
    void showShippedOrderData() {
        ArrayList<Order> sorted = new ArrayList<>(orderList.getThisStoreShippedOrderList(orderList.getThisStoreOderList(accountList.getThisAccount())));
        sorted.sort(Comparator.comparing((Order order) -> order.getOrderNumber()).thenComparing(Comparator.comparing((Order order) -> order.getOrderNumber())));
        ObservableList<Order> orderListView = FXCollections.observableArrayList(sorted);
        shippedOrderListView.setItems(orderListView);
        shippedOrderListView.setCellFactory(new Callback<ListView<Order>, ListCell<Order>>() {
            @Override
            public ListCell<Order> call(ListView<Order> listView) {
                return new CustomShippedOrderListCell();
            }
        });
    }

    private class CustomUnshippedOrderListCell extends ListCell<Order> {
        private HBox contentBox;
        private Rectangle rectangleImage;
        private Text subTotal;
        private Text grandTotal;
        private Text quantity;
        private Text usernameWithOrderNumber;
        private Text productName;
        private Text trackingNumber;
        private Text orderNumber;
        private Button button;

        public CustomUnshippedOrderListCell() {
            super();
            usernameWithOrderNumber = new Text();
            subTotal = new Text();
            grandTotal = new Text();
            quantity = new Text();
            productName = new Text();
            trackingNumber = new Text();
            orderNumber = new Text();
            VBox vBox = new VBox(orderNumber,usernameWithOrderNumber,productName,subTotal,grandTotal,quantity,trackingNumber);
            rectangleImage = new Rectangle(55,55);
            button = new Button("Track");
            contentBox = new HBox(rectangleImage, vBox, button);
            contentBox.setAlignment(Pos.CENTER);
            HBox.setHgrow(vBox, Priority.ALWAYS);
            contentBox.setSpacing(10);
        }

        @Override
        protected void updateItem(Order order, boolean empty) {
            super.updateItem(order, empty);
            if (order != null && !empty) { // <== test for null item and empty parameter
                System.out.println(order.getUsername());
                usernameWithOrderNumber.setText("Username : "+order.getUsername()); //ยังรับชื่อคนสั่งของไม่ได้
                productName.setText("Product : "+(productList.searchProductByID(order.getProductID())).getProductName());
                subTotal.setText("Sub Total : "+order.getSubTotal()+" baht.");
                grandTotal.setText("Grand Total : "+order.getGrandTotal()+" baht.");
                quantity.setText("Quantity : "+order.getQuantity());
                orderNumber.setText("Order NO#"+order.getOrderNumber());
                button.setTextFill(Color.RED);
                button.setStyle("-fx-background-color: white;" +
                        "-fx-border-color: red;" + "-fx-background-radius: 20px;" + "-fx-border-radius: 20px");
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        try {
                            orderList.setThisOrder(order);
                            Info infoForPassing = new Info(info.getAccountList(), info.getProductList(), info.getReviewList(), orderList, info.getReportList());
                            FXRouter.goTo("add_tracking_number", infoForPassing);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                File f = new File("data/product_image/"+order.getProductID()+".png");
                rectangleImage.setFill(new ImagePattern(new Image("file:///" + f.getAbsolutePath())));
                if (!LoginController.isLightMode) {
                    usernameWithOrderNumber.setFill(Color.WHITE);
                    subTotal.setFill(Color.WHITE);
                    grandTotal.setFill(Color.WHITE);
                    quantity.setFill(Color.WHITE);
                    productName.setFill(Color.WHITE);
                    trackingNumber.setFill(Color.WHITE);
                    orderNumber.setFill(Color.WHITE);
                }
                setGraphic(contentBox);
            } else {
                setGraphic(null);
            }
        }
    }

    private class CustomShippedOrderListCell extends ListCell<Order> {
        private HBox contentBox;
        private Rectangle rectangleImage;
        private Text subTotal;
        private Text grandTotal;
        private Text quantity;
        private Text usernameWithOrderNumber;
        private Text productName;
        private Text trackingNumber;
        private Text orderNumber;

        public CustomShippedOrderListCell() {
            super();
            usernameWithOrderNumber = new Text();
            subTotal = new Text();
            grandTotal = new Text();
            quantity = new Text();
            productName = new Text();
            trackingNumber = new Text();
            orderNumber = new Text();
            VBox vBox = new VBox(orderNumber,usernameWithOrderNumber,productName,subTotal,grandTotal,quantity,trackingNumber);
            rectangleImage = new Rectangle(55,55);
            contentBox = new HBox(rectangleImage, vBox);
            contentBox.setAlignment(Pos.CENTER);
            HBox.setHgrow(vBox, Priority.ALWAYS);
            contentBox.setSpacing(10);
        }

        @Override
        protected void updateItem(Order order, boolean empty) {
            super.updateItem(order, empty);
            if (order != null && !empty) { // <== test for null item and empty parameter
                System.out.println(order.getUsername());
                usernameWithOrderNumber.setText("Username : "+order.getUsername()); //ยังรับชื่อคนสั่งของไม่ได้
                subTotal.setText("Sub Total : "+order.getSubTotal()+" baht.");
                grandTotal.setText("Grand Total : "+order.getGrandTotal()+" baht.");
                quantity.setText("Quantity : "+order.getQuantity());
                productName.setText("Product : "+(productList.searchProductByID(order.getProductID())).getProductName());
                trackingNumber.setText("Tracking number : "+order.getTrackingNumber());
                orderNumber.setText("Order NO#"+order.getOrderNumber());
                File f = new File("data/product_image/"+order.getProductID()+".png");
                rectangleImage.setFill(new ImagePattern(new Image("file:///" + f.getAbsolutePath())));
                if (!LoginController.isLightMode) {
                    usernameWithOrderNumber.setFill(Color.WHITE);
                    subTotal.setFill(Color.WHITE);
                    grandTotal.setFill(Color.WHITE);
                    quantity.setFill(Color.WHITE);
                    productName.setFill(Color.WHITE);
                    trackingNumber.setFill(Color.WHITE);
                    orderNumber.setFill(Color.WHITE);
                }
                setGraphic(contentBox);
            } else {
                setGraphic(null);
            }
        }
    }

    private void showStoreData(){
        storeNameLabel.setText(((Seller)accountList.getThisAccount()).getStoreName());
        usernameLabel.setText(accountList.getThisAccount().getUsername());
        if (((UserAccount)accountList.getThisAccount()).haveUserImage()) {
            File f = new File("data/user_image/"+((UserAccount)accountList.getThisAccount()).getUserImage());
            circleImage.setFill(new ImagePattern(new Image("file:///" + f.getAbsolutePath())));
            System.out.println("data/user_image/"+((UserAccount)accountList.getThisAccount()).getUserImage());
            System.out.println("non null");
            System.out.println(((UserAccount)accountList.getThisAccount()).getUserImage());
        } else {
            circleImage.setFill(new ImagePattern(new Image(getClass().getResource("/ku/cs/images/default_profile.png").toExternalForm())));
        }
    }
}