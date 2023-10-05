package ku.cs.kanison.services;

import ku.cs.kanison.models.account.Account;
import ku.cs.kanison.models.account.AccountList;
import ku.cs.kanison.models.account.Seller;
import ku.cs.kanison.models.account.UserAccount;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class AccountListFileDataSource implements DataSource<AccountList> {
    private String fileName = "data" + File.separator + "Account.csv";
    private AccountList accountList;

    public AccountListFileDataSource() {
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
                if (data[0].equals("Admin")) {
                    accountList.addAccount((new Account(
                            data[0],
                            data[1],
                            data[2],
                            data[3])));
                } else if (data[0].equals("Seller")) {
                    // data[8] = storeName;
                    accountList.addAccount((new Seller(
                            data[0],
                            data[1],
                            data[2],
                            data[3],
                            data[4],
                            Integer.parseInt(data[5]),
                            data[6],
                            data[7],
                            data[8],
                            Integer.parseInt(data[9]))));
                } else if (data[0].equals("User")) {
                    accountList.addAccount((new UserAccount(
                            data[0],
                            data[1],
                            data[2],
                            data[3],
                            data[4],
                            Integer.parseInt(data[5]),
                            data[6],
                            data[7])));
                }
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
    public AccountList getData() {
        accountList = new AccountList();
        readData();
        return accountList;
    }

    @Override
    public void writeData(AccountList accountList) {
        FileWriter fileWriter = null;
        PrintWriter writer = null;
        try {
            fileWriter = new FileWriter(fileName, StandardCharsets.UTF_8);
            writer = new PrintWriter(new BufferedWriter(fileWriter));

            for(Account account: accountList.getAccounts()) {
                if ("Seller".equals(account.getRole())) {
                    String line = "Seller" + "," + account.getName() + "," + account.getUsername() + "," + account.getPassword() + ","
                            + ((UserAccount) account).getUserStatus() + "," + (((UserAccount) account).getLoginAttempts()) + ","
                            + (((UserAccount) account).getLastLogin()) + "," + (((UserAccount) account).getUserImage()) + ","
                            + ((Seller) account).getStoreName() + "," + ((Seller) account).getLowQuantityStore();
                    writer.println(line);
                } else if ("User".equals(account.getRole())) {
                    String line = "User" + "," + account.getName() + "," + account.getUsername() + "," + account.getPassword() + ","
                            + ((UserAccount) account).getUserStatus() + "," + (((UserAccount) account).getLoginAttempts()) + ","
                            + (((UserAccount) account).getLastLogin()) + "," + (((UserAccount) account).getUserImage());
                    writer.println(line);
                } else if ("Admin".equals(account.getRole())) {
                    String line = "Admin" + "," + account.getName() + "," + account.getUsername() + "," + account.getPassword();
                    writer.println(line);
                }
            }
            writer.flush();
            writer.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
