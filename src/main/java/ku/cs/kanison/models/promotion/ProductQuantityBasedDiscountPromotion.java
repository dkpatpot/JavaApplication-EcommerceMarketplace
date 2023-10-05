package ku.cs.kanison.models.promotion;

import ku.cs.kanison.models.product.Order;
import ku.cs.kanison.services.IllegalConditionException;

// โปรโมชันส่วนลด X บาท เมื่อซื้อสินค้า Y ชิ้น
public class ProductQuantityBasedDiscountPromotion extends Promotion {
    private int minimumProductQuantity;
    private double discount;

    public ProductQuantityBasedDiscountPromotion(String code, String sellerUsername, int minimumProductQuantity, double discount) {
        super(code, sellerUsername);
        this.minimumProductQuantity = minimumProductQuantity;
        this.discount = discount;
    }

    @Override
    public boolean usePromotion(Order order) throws IllegalConditionException {
        if (order.getSellerName().equals(getSellerUsername())) {
            if (order.getQuantity() >= minimumProductQuantity) {
                return true;
            } else throw new IllegalConditionException("ไม่สามารถใช้งานโค้ดนี้ได้ เนื่องจากจำนวนสินค้าน้อยกว่าที่กำหนด");
        }
        throw new IllegalConditionException("ไม่สามารถใช้งานโค้ดกับสินค้าของร้านนี้ได้");
    }

    @Override
    public double calDiscount(Order order){
        try {
            if (usePromotion(order)) {
                double grandTotal = order.getSubTotal() - ((int)(order.getQuantity()/minimumProductQuantity) * discount);
                if (grandTotal>=0) return grandTotal;
                return 0;
                    // เช่น โปรโมชันส่วนลด 50 บาท เมื่อซื้อสินค้า 2 ชิ้น => ซื้อสินค้า 5 ชิ้น จะได้รับส่วนลด 100 บาท กรณีนี้หากซื้อ 10 ชิ้นจะได้ลด 100 บาทเท่าเดิม
            }
        } catch (IllegalConditionException e) {
            e.printStackTrace();
        }
        return order.getSubTotal();
    }

    @Override
    public String toCSV() {
        return "PQBD," + getCode() + "," + getSellerUsername() + "," + minimumProductQuantity + "," + discount;

    }
}
