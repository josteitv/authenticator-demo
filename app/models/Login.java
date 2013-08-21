package models;

public class Login {

    public String username;
    public String password;
    public String code;

    public String validate() {
        if (User.authenticate(username, password, code) == null) {
            return "Invalid username or password";
        }
        return null;
    }

}
