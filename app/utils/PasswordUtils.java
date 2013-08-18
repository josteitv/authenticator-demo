package utils;

import org.jasypt.util.password.StrongPasswordEncryptor;

public class PasswordUtils {

    public static void main(String[] args) {
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        String hashed = passwordEncryptor.encryptPassword("secret");
        System.out.println(hashed);
    }

    public static String encryptPassword(String plainPassword) {
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        return passwordEncryptor.encryptPassword(plainPassword);
    }

    public static boolean verifyPassword(String plainPassword, String encryptedPassword) {
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        return passwordEncryptor.checkPassword(plainPassword, encryptedPassword);
    }

}
