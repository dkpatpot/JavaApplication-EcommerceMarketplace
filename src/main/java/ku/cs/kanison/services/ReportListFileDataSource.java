package ku.cs.kanison.services;

import ku.cs.kanison.models.report.ProductReport;
import ku.cs.kanison.models.report.Report;
import ku.cs.kanison.models.report.ReportList;
import ku.cs.kanison.models.report.ReviewReport;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ReportListFileDataSource implements DataSource<ReportList> {
    private String fileName = "data" + File.separator + "Reported.csv";
    private ReportList reportList;

    public ReportListFileDataSource() {
        initialFileIfNotExist();
    }

    private void initialFileIfNotExist() {
        File file = new File("data");
        if (!file.exists()) {
            file.mkdir();
        }

        file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void readData() {
        FileReader reader = null;
        BufferedReader buffer = null;
        try {
            reader = new FileReader(fileName, StandardCharsets.UTF_8);
            buffer = new BufferedReader(reader);
            String line = "";
            while((line = buffer.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals("Product")) {
                    reportList.addReport(new ProductReport(data[1], data[2], data[3].replaceAll("<comma>", ","), Integer.parseInt(data[4])));
                } else if (data[0].equals("Review")) {
                    reportList.addReport(new ReviewReport(data[1], data[2], data[3].replaceAll("<comma>", ",")));
                }
            }
            buffer.close();
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ReportList getData() {
        reportList = new ReportList();
        readData();
        return reportList;
    }

    @Override
    public void writeData(ReportList reportList) {
        FileWriter fileWriter = null;
        PrintWriter writer = null;

        try {
            fileWriter = new FileWriter(fileName, StandardCharsets.UTF_8);
            writer = new PrintWriter(new BufferedWriter(fileWriter));

            for (Report report : reportList.getReports()) {
                writer.println(report.toCSV()); // polymorphism
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
