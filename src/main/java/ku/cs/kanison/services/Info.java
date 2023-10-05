package ku.cs.kanison.services;

import ku.cs.kanison.models.account.*;
import ku.cs.kanison.models.product.OrderList;
import ku.cs.kanison.models.product.ProductList;
import ku.cs.kanison.models.product.ReviewList;
import ku.cs.kanison.models.report.ReportList;

public class Info {
    private AccountList accountList;
    private ProductList productList;
    private ReviewList reviewList;
    private OrderList orderList;
    private ReportList reportList;

    public Info(AccountList accountList, ProductList productList, ReviewList reviewList, OrderList orderList, ReportList reportList) {
        this.accountList = accountList;
        this.productList = productList;
        this.reviewList = reviewList;
        this.orderList = orderList;
        this.reportList = reportList;
    }

    public AccountList getAccountList() {
        return accountList;
    }

    public ProductList getProductList() { return productList; }

    public ReviewList getReviewList() {
        return reviewList;
    }

    public OrderList getOrderList() {
        return orderList;
    }

    public ReportList getReportList() {
        return reportList;
    }
}
