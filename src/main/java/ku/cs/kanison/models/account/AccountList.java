package ku.cs.kanison.models.account;

import java.util.ArrayList;

public class AccountList {

    private Account thisAccount;
    private ArrayList<Account> accounts;

    public AccountList() {
        accounts = new ArrayList<>();
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public ArrayList<UserAccount> getUserAccounts() {
        ArrayList<UserAccount> userAccounts = new ArrayList<>();
        for (Account acc : accounts) {
            if (acc instanceof UserAccount) {
                userAccounts.add((UserAccount) acc);
            }
        }
        return userAccounts;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public boolean isExistUsername(String username) {
        for(Account acc: accounts) {
            if(acc.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    public boolean checkUsernamePassword(String username,String password){
        for(Account account : accounts) {
            if(account.getUsername().equals(username) && account.getPassword().equals(password)) {
                thisAccount = account;
                return true;
            }
        }
        return false;
    }

    public void setThisAccountToSeller(String storeName) {
        int index = accounts.indexOf(thisAccount);
        if (((UserAccount)thisAccount).haveUserImage()) {
            thisAccount = new Seller("Seller", thisAccount.getName(), thisAccount.getUsername(), thisAccount.getPassword(),
                    ((UserAccount) thisAccount).getUserStatus(), ((UserAccount) thisAccount).getLoginAttempts(), ((UserAccount) thisAccount).getLastLogin(), ((UserAccount) thisAccount).getUserImage(), storeName);
        } else {
            thisAccount = new Seller("Seller", thisAccount.getName(), thisAccount.getUsername(), thisAccount.getPassword(),
                    ((UserAccount) thisAccount).getUserStatus(), ((UserAccount) thisAccount).getLoginAttempts(),((UserAccount) thisAccount).getLastLogin(), storeName);
        }
        accounts.set(index, thisAccount);
    }

    public Account getThisAccount() {
        return thisAccount;
    }

    public UserAccount searchUserAccountByUsername(String username){
        for (Account account: accounts){
            if (account.getUsername().equals(username)){
                return (UserAccount) account;
            }
        }
        return null;
    }
}
