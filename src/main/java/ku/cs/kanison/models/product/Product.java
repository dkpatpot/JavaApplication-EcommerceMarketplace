package ku.cs.kanison.models.product;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Product {
    private String productName;
    private String productDetail;
    private double price;
    private int quantity;
    private LocalDateTime timeHistory;
    private String image;
    private String productImage;
    private String category;
    private int id;
    private double allRating;
    private int countRating;
    private String sellerUserName;

    public Product() {}

    public Product(String productName, String productDetail, double price, int quantity, LocalDateTime timeHistory,
                   String productImage, int id, double allRating, int countRating, String sellerUserName, String category) {
        this.productName = productName;
        this.productDetail = productDetail;
        this.price = price;
        this.quantity = quantity;
        this.timeHistory = timeHistory;
        this.productImage = productImage;
        this.id = id;
        this.allRating = allRating;
        this.countRating = countRating;
        this.sellerUserName = sellerUserName;
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public String getProductImage() {
        return productImage;
    }

    public int getID() {
        return id;
    }

    public double getAllRating() {
        return allRating;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getRating() {
        return allRating/countRating;
    }

    public int getCountRating() {
        return countRating;
    }

    public String getSellerUserName() {
        return sellerUserName;
    }

    public void setRating(double star) {
        allRating += star;
        countRating += 1;
    }

    public LocalDateTime getTimeHistory() {
        return timeHistory;
    }
    public void setProductName(String productName){ this.productName = productName; }
    public void setPrice(double price){
        this.price = price;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String formatHistory(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formatDateTime = timeHistory.format(format);
        return formatDateTime;
    }

    public void addQuantity(int add) {
        if (add>0) {
            this.quantity += add;
        }
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double createTotal(int quantity) {
        return price * quantity;
    }

    public void decreaseQuantity(int quantity) {
        this.quantity = this.quantity - quantity;
    }
}
