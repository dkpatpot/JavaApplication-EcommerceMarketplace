package ku.cs.kanison.models.promotion;

import ku.cs.kanison.models.product.Order;
import ku.cs.kanison.services.IllegalConditionException;

// โปรโมชันส่วนลด X เปอร์เซ็นต์เมื่อซื้อตั้งแต่ Y บาทขึ้นไป
public class PercentDiscountPromotion extends Promotion{
    private double minimumPrice;
    private double percent;

    public PercentDiscountPromotion(String code, String sellerUsername, double price, double percent) {
        super(code, sellerUsername);
        this.minimumPrice = price;
        this.percent = percent;
    }

    @Override
    public boolean usePromotion(Order order) throws IllegalConditionException {
        if (order.getSellerName().equals(getSellerUsername())) {
            if (order.getSubTotal() >= minimumPrice) {
                return true;
            } else throw new IllegalConditionException("ไม่สามารถใช้งานโค้ดนี้ได้ เนื่องจากซื้อสินค้าน้อยกว่าราคาที่กำหนด");
        }
        throw new IllegalConditionException("ไม่สามารถใช้งานโค้ดกับสินค้าของร้านนี้ได้");
    }

    @Override
    public double calDiscount(Order order){
        try {
            if (usePromotion(order))
            return order.getSubTotal() - (order.getSubTotal()*(percent/100.0));
        } catch (IllegalConditionException e) {
            e.printStackTrace();
        }
        return order.getSubTotal();
    }

    @Override
    public String toCSV() {
        return "PD," + getCode() + "," + getSellerUsername() + "," + minimumPrice + "," + percent;

    }
}
