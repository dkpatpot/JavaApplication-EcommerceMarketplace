package ku.cs.kanison.services;

import ku.cs.kanison.models.promotion.PercentDiscountPromotion;
import ku.cs.kanison.models.promotion.ProductQuantityBasedDiscountPromotion;
import ku.cs.kanison.models.promotion.Promotion;
import ku.cs.kanison.models.promotion.PromotionList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PromotionListFileDataSource implements DataSource<PromotionList> {
    private String fileName = "data" + File.separator + "Promotion.csv";
    private PromotionList promotionList;

    public PromotionListFileDataSource() {
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
                if (data[0].equals("PD")) {
                    promotionList.addPromotion(new PercentDiscountPromotion(data[1], data[2], Double.parseDouble(data[3]), Double.parseDouble(data[4])));
                } else if (data[0].equals("PQBD")) {
                    promotionList.addPromotion(new ProductQuantityBasedDiscountPromotion(data[1], data[2], Integer.parseInt(data[3]), Double.parseDouble(data[4])));
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
    public PromotionList getData() {
        promotionList = new PromotionList();
        readData();
        return promotionList;
    }

    @Override
    public void writeData(PromotionList promotionList) {
        FileWriter fileWriter = null;
        PrintWriter writer = null;
        try {
            fileWriter = new FileWriter(fileName, StandardCharsets.UTF_8);
            writer = new PrintWriter(new BufferedWriter(fileWriter));

            for (Promotion promotion : promotionList.getPromotions()) {
                writer.println(promotion.toCSV()); // polymorphism
            }
            writer.flush();
            writer.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
