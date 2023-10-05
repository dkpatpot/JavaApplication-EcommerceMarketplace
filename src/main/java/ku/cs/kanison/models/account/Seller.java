package ku.cs.kanison.models.account;

import ku.cs.kanison.models.product.Product;

import java.util.ArrayList;

public class Seller extends UserAccount{
    private String storeName;
    private int lowQuantityStore;

    public Seller(String role, String name, String username, String password, String userStatus, int loginAttempts, String lastLogin, String userImage,String storeName) {
        super(role, name, username, password, userStatus, loginAttempts, lastLogin, userImage);
        this.storeName = storeName;
        this.lowQuantityStore = 0;
    }

    public Seller(String role, String name, String username, String password, String userStatus, int loginAttempts, String lastLogin, String storeName) {
        super(role, name, username, password, userStatus, loginAttempts, lastLogin);
        this.storeName = storeName;
        this.lowQuantityStore = 0;
    }

    public Seller(String role, String name, String username, String password, String userStatus, int loginAttempts, String lastLogin, String userImage,String storeName, int lowQuantityStore) {
        super(role, name, username, password, userStatus, loginAttempts, lastLogin, userImage);
        this.storeName = storeName;
        this.lowQuantityStore = lowQuantityStore;
    }

    public Seller(String role, String name, String username, String password, String userStatus, int loginAttempts, String lastLogin, String storeName, int lowQuantityStore) {
        super(role, name, username, password, userStatus, loginAttempts, lastLogin);
        this.storeName = storeName;
        this.lowQuantityStore = lowQuantityStore;
    }


    public Seller(String storeName) {
        this.storeName = storeName;
    }
    public String getStoreName() {
        return storeName;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getLowQuantityStore() {
        return lowQuantityStore;
    }

    public void setLowQuantityStore(int lowQuantityStore) {
        this.lowQuantityStore = lowQuantityStore;
    }
}
