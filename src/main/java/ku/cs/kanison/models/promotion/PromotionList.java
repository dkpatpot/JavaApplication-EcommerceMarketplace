package ku.cs.kanison.models.promotion;

import java.util.ArrayList;

public class PromotionList {
    private ArrayList<Promotion> promotions;

    public PromotionList() {
        promotions = new ArrayList<>();
    }

    public ArrayList<Promotion> getPromotions() {
        return promotions;
    }

    public void addPromotion(Promotion promotion) {
        promotions.add(promotion);
    }

    public boolean isExistCode(String code) {
        for(Promotion promotion: promotions) {
            if (promotion.getCode().equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }

    public Promotion searchPromotionByCode(String promotionCode) {
        for(Promotion promotion : promotions) {
            if (promotion.getCode().equalsIgnoreCase(promotionCode)) {
                return promotion;
            }
        }
        return null;
    }
}
