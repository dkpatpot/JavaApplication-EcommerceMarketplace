package ku.cs.kanison.models.account;

public class Account {
    private String role;
    private String name;
    private String username;
    private String password;

    public Account() {
    }

    public Account(String role, String name, String username, String password) {
        this.role = role;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isValidPassword(String password) {
        return ((password!= null)
                && (!password.equals(""))
                && (password.matches("^[a-zA-Z0-9]{6,20}$")));
    }

    public boolean isValidUsername(String username) {
        return ((username!= null)
                && (!username.equals(""))
                && (username.matches("^[a-zA-Z]+(.+){3,20}$")));
    }

    public boolean isValidName(String name) {
        return ((name!= null) && (!name.equals("")));
    }

}
