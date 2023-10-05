package ku.cs.kanison.models.product;

import ku.cs.kanison.models.promotion.Promotion;
import ku.cs.kanison.services.IllegalConditionException;

public class Order {
    private int orderNumber;
    private double subTotal;
    private double grandTotal;
    private int quantity;
    private int productID;
    private String username;
    private String trackingNumber;
    private String sellerName;

    public Order(int orderNumber, double subTotal, double grandTotal, int quantity, int productID, String username) {
        this.orderNumber = orderNumber;
        this.subTotal = subTotal;
        this.grandTotal = grandTotal;
        this.quantity = quantity;
        this.productID = productID;
        this.username = username;
        this.trackingNumber = "-";
    }

    public Order(int orderNumber, double subTotal, double grandTotal, int quantity, int productID, String username, String trackingNumber) {
        this.orderNumber = orderNumber;
        this.subTotal = subTotal;
        this.grandTotal = grandTotal;
        this.quantity = quantity;
        this.productID = productID;
        this.username = username;
        this.trackingNumber = trackingNumber;
    }

    public Order(int orderNumber, double subTotal, double grandTotal, int quantity, int productID, String username, String trackingNumber, String sellerName) {
        this.orderNumber = orderNumber;
        this.subTotal = subTotal;
        this.grandTotal = grandTotal;
        this.quantity = quantity;
        this.productID = productID;
        this.username = username;
        this.trackingNumber = trackingNumber;
        this.sellerName = sellerName;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getProductID() {
        return productID;
    }

    public String getUsername() {
        return this.username;
    }

    public String getSellerName() {
        return this.sellerName;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public boolean checkPromotion(Promotion promotion) throws IllegalConditionException {
        return promotion.usePromotion(this);
    }
}
