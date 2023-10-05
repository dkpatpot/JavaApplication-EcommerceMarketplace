package ku.cs.kanison.models.report;

public abstract class Report {
    private String type;
    private String username;
    private String reason;
    private String message;

    public Report(String type, String username, String reason, String message) {
        this.type = type;
        this.username = username;
        this.reason = reason;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getReason() {
        return reason;
    }

    public String getMessage() {
        return message;
    }

    public abstract String toCSV();
}
