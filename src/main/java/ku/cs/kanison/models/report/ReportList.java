package ku.cs.kanison.models.report;

import java.util.ArrayList;

public class ReportList {

    private ArrayList<Report> reports;

    public ReportList() {
        reports = new ArrayList<>();
    }

    public ArrayList<Report> getReports() {
        return reports;
    }

    public void addReport(Report report) {
        reports.add(report);
    }
}
