package ku.cs.kanison.services;

import ku.cs.kanison.models.product.Review;
import ku.cs.kanison.models.product.ReviewList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReviewListFileDataSource implements DataSource<ReviewList> {
    private String fileName = "data" + File.separator + "Review.csv";
    private ReviewList reviewList;

    public ReviewListFileDataSource() {
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
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String line = "";

            while((line = buffer.readLine()) != null) {
                String[] data = line.split(",");
                reviewList.addReview(new Review(
                        data[0],
                        Integer.parseInt(data[1]),
                        LocalDateTime.parse(data[2],format),
                        data[3].replaceAll("<comma>", ","),
                        Integer.parseInt(data[4])));
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
    public ReviewList getData() {
            reviewList = new ReviewList();
        readData();
        return reviewList;
    }

    @Override
    public void writeData(ReviewList reviewList) {
        FileWriter fileWriter = null;
        PrintWriter writer = null;
        try {
            fileWriter = new FileWriter(fileName, StandardCharsets.UTF_8);
            writer = new PrintWriter(new BufferedWriter(fileWriter));

            for (Review review: reviewList.getReviews()) {
                String line = review.getUsername() + ","
                        + review.getProductID() + ","
                        + review.formatHistory() + ","
                        + review.getComment().replaceAll(",", "<comma>") + ","
                        + review.getStar();
                writer.println(line);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
