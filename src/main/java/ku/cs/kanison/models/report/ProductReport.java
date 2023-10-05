package ku.cs.kanison.models.report;

public class ProductReport extends Report {
    private int productID;

    public ProductReport(String username, String reason, String message, int productID) {
        super("Product", username, reason, message);
        this.productID = productID;
    }

    @Override
    public String toCSV() {
        return getType() + "," + getUsername() + "," + getReason() + "," + getMessage().replaceAll(",", "<comma>") + "," + productID;
    }
}
