package ku.cs.kanison.models.product;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Review {
    private String username;
    private int productID;
    private String comment;
    private LocalDateTime timeHistory;
    private int star;

    public Review(String username, int productID, LocalDateTime timeHistory, String comment, int star) {
        this.username = username;
        this.productID = productID;
        this.comment = comment;
        this.timeHistory = timeHistory;
        this.star = star;
    }

    public String getUsername(){return username;}

    public int getProductID() {
        return productID;
    }

    public String getComment(){return comment;}

    public int getStar(){return star;}

    public String formatHistory(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formatDateTime = timeHistory.format(format);
        return formatDateTime;
    }

}
