package ku.cs.kanison.models.report;

public class ReviewReport extends Report{

    public ReviewReport(String username, String reason, String message) {
        super("Review", username, reason, message);
    }

    @Override
    public String toCSV() {
        return getType() + "," + getUsername() + "," + getReason() + "," + getMessage().replaceAll(",", "<comma>");
    }
}
