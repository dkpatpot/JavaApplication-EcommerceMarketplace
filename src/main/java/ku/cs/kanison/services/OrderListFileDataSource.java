package ku.cs.kanison.services;

import ku.cs.kanison.models.product.Order;
import ku.cs.kanison.models.product.OrderList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class OrderListFileDataSource implements DataSource<OrderList> {
    private String fileName = "data" + File.separator + "Order.csv";
    private OrderList orderList;

    public OrderListFileDataSource() {
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
                orderList.addOrder(new Order(
                        Integer.parseInt(data[0]),
                        Double.parseDouble(data[1]),
                        Double.parseDouble(data[2]),
                        Integer.parseInt(data[3]),
                        Integer.parseInt(data[4]),
                        data[5],
                        data[6],
                        data[7]
                ));
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
    public OrderList getData() {
        orderList = new OrderList();
        readData();
        return orderList;
    }

    @Override
    public void writeData(OrderList orderList) {
        FileWriter fileWriter = null;
        PrintWriter writer = null;

        try {
            fileWriter = new FileWriter(fileName, StandardCharsets.UTF_8);
            writer = new PrintWriter(new BufferedWriter(fileWriter));

            for (Order order : orderList.getOrders()) {
                String line = order.getOrderNumber() + "," +
                        order.getSubTotal() + "," +
                        order.getGrandTotal() + "," +
                        order.getQuantity() + "," +
                        order.getProductID() + "," +
                        order.getUsername() + "," +
                        order.getTrackingNumber() + "," +
                        order.getSellerName();
                writer.println(line);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
