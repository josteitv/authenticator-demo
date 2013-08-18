package models;

public class Login {

    public String username;
    public String password;

    public String validate() {
        if (User.authenticate(username, password) == null) {
            return "Invalid username or password";
        }
        return null;
    }

}
