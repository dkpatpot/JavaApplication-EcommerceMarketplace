package ku.cs.kanison.models.product;
import ku.cs.kanison.models.account.Account;
import ku.cs.kanison.services.IllegalConditionException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ProductList {
    private Product thisProduct;
    private ArrayList<Product> products;

    public ProductList(){
        products = new ArrayList<>();
    }

    public void addProduct(Product product){
        products.add(product);
    }

    public ArrayList<Product> getProducts() { return products; }

    public int countProduct(){
        return products.size();
    }

    public Product getThisProduct() {
        return thisProduct;
    }

    public ArrayList<Product> getThisSellerProductList(Account account) {
        ArrayList<Product> thisSellerProductList = new ArrayList<>();
        for (Product thisSellerProduct : products) {
            String data = thisSellerProduct.getSellerUserName();
            if (data.equals(account.getUsername())) {
                thisSellerProductList.add(thisSellerProduct);
            }
        }
        return thisSellerProductList;
    }

    public void setThisProduct(Product product) {
        thisProduct = product;
    }

    public ArrayList<Product> sortProductByAscending(ArrayList<Product> products){
        ArrayList<Product> sorted = new ArrayList<>(products);
        sorted.sort(Comparator.comparing((Product product) -> product.getPrice())
                .thenComparing(Comparator.comparing((Product product) -> product.getPrice())));
        return sorted;
    }

    public ArrayList<Product> sortProductByDescending(ArrayList<Product> products){
        ArrayList<Product> sorted = new ArrayList<>(products);
        sorted.sort(Comparator.comparing((Product product) -> product.getPrice()).reversed()
                .thenComparing(Comparator.comparing((Product product) -> product.getPrice())));
        return sorted;
    }

    public ArrayList<Product> sortProductByLatest(){
        ArrayList<Product> sorted = new ArrayList<>(products);
        sorted.sort(Comparator.comparing((Product product) -> product.getTimeHistory()).reversed()
                .thenComparing(Comparator.comparing((Product product) -> product.getTimeHistory())));
        return sorted;
    }

    public ArrayList<Product> sortProductByLatest(ArrayList<Product> products){
        ArrayList<Product> sorted = new ArrayList<>(products);
        sorted.sort(Comparator.comparing((Product product) -> product.getTimeHistory()).reversed()
                .thenComparing(Comparator.comparing((Product product) -> product.getTimeHistory())));
        return sorted;
    }

    public Product searchProductByID(int id) {
        for (Product product: products) {
            if (product.getID()==id) {
                return product;
            }
        }
        return null;
    }

    public ArrayList<Product> sortProductByCategory(ArrayList<Product> sortedByPrice, String category) {
        ArrayList<Product> sortedByCategory = new ArrayList<>();
        for (Product product: sortedByPrice) {
            if (category.equals(product.getCategory())) {
                sortedByCategory.add(product);
            }
        }
        return sortedByCategory;
    }

    public ArrayList<Product> rangePrice(double min, double max) throws IllegalConditionException {
        ArrayList<Product> range = new ArrayList<>();
        if (min < 0 || max <= 0 || min > max) {
            throw new IllegalConditionException("กรุณาใส่ข้อมูลให้ถูกต้อง");
        }
        for (Product product: products) {
            if (product.getPrice() >= min && product.getPrice() <= max) {
                range.add(product);
            }
        }
        Collections.reverse(range);
        return range;
    }

    public int findProductID() {
        return countProduct() + 1;
    }
}
