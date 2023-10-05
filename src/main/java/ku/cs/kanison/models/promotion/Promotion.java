package ku.cs.kanison.models.promotion;

import ku.cs.kanison.models.product.Order;
import ku.cs.kanison.services.IllegalConditionException;

public abstract class Promotion {
    private String code;
    private String sellerUsername; // เก็บ username ของร้านค้าเพื่อเอาไว้ตรวจสอบว่าใช้ code กับสินค้านี้ได้หรือไม่ อย่างเช่นกรณีที่นำ code ของอีกร้านหนึ่งไปใช้กับอีกร้านหนึ่ง

    public Promotion(String code, String sellerUsername) {
        this.code = code;
        this.sellerUsername = sellerUsername;
    }

    public abstract boolean usePromotion(Order order) throws IllegalConditionException;

    public abstract double calDiscount(Order order);

    public String getCode() {
        return code;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public abstract String toCSV();
}
