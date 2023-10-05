package ku.cs.kanison.models.product;

import ku.cs.kanison.models.account.Account;

import java.util.ArrayList;

public class OrderList {
    private ArrayList<Order> orders;
    private Order thisOrder;

    public OrderList(){
        orders = new ArrayList<>();
    }
    public Order getThisOrder() {
        return thisOrder;
    }
    public void removeThisOrder() {
        orders.remove(countOrder()-1);
    }
    public void setThisOrder(Order order) {
        thisOrder = order;
    }
    public void addOrder(Order order){
        orders.add(order);
    }
    public ArrayList<Order> getOrders() { return orders; }
    public int countOrder(){return orders.size();}

    public ArrayList<Order> getThisStoreOderList(Account account) {
        ArrayList<Order> thisStoreOderList = new ArrayList<>();
        for (Order thisStoreOder : orders) {
            String data = thisStoreOder.getSellerName();
            if (data.equals(account.getUsername())) {
                thisStoreOderList.add(thisStoreOder);
            }
        }
        return thisStoreOderList;
    }

    public ArrayList<Order> getThisStoreUnshippedOrderList(ArrayList<Order> orderArrayList) {
        ArrayList<Order> unshippedOrderList = new ArrayList<>();
        for (Order order: orderArrayList) {
            if (order.getTrackingNumber().equals("-")) {
                unshippedOrderList.add(order);
            }
        }
        return unshippedOrderList;
    }

    public ArrayList<Order> getThisStoreShippedOrderList(ArrayList<Order> orderArrayList) {
        ArrayList<Order> shippedOrderList = new ArrayList<>();
        for (Order order: orderArrayList) {
            if (!(order.getTrackingNumber().equals("-"))) {
                shippedOrderList.add(order);
            }
        }
        return shippedOrderList;
    }

    public int createOrderNumber() {
        return countOrder() + 1;
    }
}
