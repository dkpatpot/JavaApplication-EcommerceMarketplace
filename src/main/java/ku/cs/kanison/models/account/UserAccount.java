package ku.cs.kanison.models.account;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserAccount extends Account {
    private String lastLogin;
    private String userStatus;
    private int loginAttempts;
    private String userImage;
    public UserAccount() {
    }
    public UserAccount(String role, String name, String username, String password, String userStatus, int loginAttempts, String lastLogin) {
        super(role, name, username, password);
        this.lastLogin = lastLogin;
        this.userStatus= userStatus;
        this.loginAttempts = loginAttempts;
        this.userImage = "-";
    }

    public UserAccount(String role, String name, String username, String password, String userStatus, int loginAttempts, String lastLogin, String userImage) {
        super(role, name, username, password);
        this.lastLogin = lastLogin;
        this.userStatus= userStatus;
        this.loginAttempts = loginAttempts;
        this.userImage = userImage;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public String getUserImage() {
        return userImage;
    }

    public LocalDateTime getLocalDateTimeLastLogin() {
        if (lastLogin.equals("-")) {
            LocalDateTime dateTimeForNull = LocalDateTime.MIN;
            return dateTimeForNull;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(lastLogin, formatter);
        return dateTime;
    }

    public boolean haveUserImage() {
        if (getUserImage().equals(getUsername() + ".png")) {
            return true;
        } else {
            return false;
        }
    }
    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }
    public void setUserStatus() {
        if (this.userStatus.equals("Available")) {
            this.userStatus = "Banned";
        } else {
            this.userStatus = "Available";
            this.loginAttempts = 0;
        }
    }
    public void setLoginAttempts() {
        this.loginAttempts++;
    }
}
