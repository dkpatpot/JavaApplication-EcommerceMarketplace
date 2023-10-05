package ku.cs.kanison.services;

import ku.cs.kanison.models.product.Product;
import ku.cs.kanison.models.product.ProductList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProductListFileDataSource implements DataSource<ProductList> {
    private String fileName = "data" + File.separator + "Product.csv";
    private ProductList productList;

    public ProductListFileDataSource() {
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
                productList.addProduct(new Product(
                        data[0].replaceAll("<comma>", ","),
                        data[1].replaceAll("<comma>", ","),
                        Double.parseDouble(data[2]),
                        Integer.parseInt(data[3]),
                        LocalDateTime.parse(data[4],format),
                        data[5],
                        Integer.parseInt(data[6]),
                        Double.parseDouble(data[7]),
                        Integer.parseInt(data[8]),
                        data[9],
                        data[10]));
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
    public ProductList getData() {
        productList = new ProductList();
        readData();
        return productList;
    }

    @Override
    public void writeData(ProductList productList) {
        FileWriter fileWriter = null;
        PrintWriter writer = null;

        try {
            fileWriter = new FileWriter(fileName, StandardCharsets.UTF_8);
            writer = new PrintWriter(new BufferedWriter(fileWriter));

            for (Product product: productList.getProducts()) {
                String line = product.getProductName().replaceAll(",", "<comma>") + ","
                        + product.getProductDetail().replaceAll(",", "<comma>") + ","
                        + product.getPrice() + ","
                        + product.getQuantity() + ","
                        + product.formatHistory()+ ","
                        + product.getProductImage()+ ","
                        + product.getID()+ ","
                        + product.getAllRating() + ","
                        + product.getCountRating() + ","
                        + product.getSellerUserName() + ","
                        + product.getCategory();
                writer.println(line);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
