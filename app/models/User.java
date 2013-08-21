package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import utils.PasswordUtils;
import utils.TwoStepVerification;

@Entity
public class User extends Model {

    @Id
    @Constraints.Required
    @Formats.NonEmpty
    public String username;

    @Constraints.Required
    public String password;

    public String secret;

    private static Finder<String, User> find = new Finder<>(String.class, User.class);

    public static User authenticate(String username, String password, String code) {
        User user = find.where().eq("username", username).findUnique();
        if (user == null) {
            return null;
        }

        boolean passwordVerified = PasswordUtils.verifyPassword(password, user.password);

        boolean codeVerified = TwoStepVerification.verifyCode(code, user.secret);

        return passwordVerified && codeVerified ? user : null;
    }

    public static void create(User user) {
        user.password = PasswordUtils.encryptPassword(user.password);
        user.save();
    }

}
