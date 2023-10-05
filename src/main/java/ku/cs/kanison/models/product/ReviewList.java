package ku.cs.kanison.models.product;

import java.util.ArrayList;

public class ReviewList {
    private ArrayList<Review> reviews;
    private Review thisReview;

    public ReviewList(){
        reviews = new ArrayList<>();
    }

    public void addReview(Review review){
        reviews.add(review);
    }
    public ArrayList<Review> getReviews() { return reviews; }

    public ArrayList<Review> getThisProductReview(Product product) {
        ArrayList<Review> thisProductReviewList = new ArrayList<>();
        for (Review thisProductReview : reviews) {
            int data = thisProductReview.getProductID();
            if (data==(product.getID())) {
                thisProductReviewList.add(thisProductReview);
            }
        }
        return thisProductReviewList;
    }

    public Review getThisReview() {
        return thisReview;
    }

    public void setThisReview(Review review) {
        thisReview = review;
    }
}

