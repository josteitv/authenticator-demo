package utils;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;

public class TwoStepVerification {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Generates a 10 bit random secret to share between mobile phone and
     * server. The secret is Base32 encoded.
     * 
     * @return a Base32 encoded string of 10 random bytes
     */
    public static String generateSecret() {
        byte[] buffer = new byte[10];
        SECURE_RANDOM.nextBytes(buffer);
        return new String(new Base32().encode(buffer));
    }

    /**
     * Creates an URL pointing to a QR code image used to import the secret into
     * Google Authenticator. By using this method to create the QR code, the
     * secret is sent to Google. If you do not want to share the secret with
     * Google, the QR code must be created by a QR code library.
     * 
     * @param label
     *            The label to display inside Google Authenticator on the mobile
     *            phone
     * @param secret
     *            The secret to share between the server and the phone
     * @return An URL pointing to a QR code image hosted by Google
     */
    public static String createQrCodeUrl(String label, String secret) {
        String format = "https://chart.googleapis.com/chart"
                + "?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s%%3Fsecret%%3D%s";
        return String.format(format, label, secret);
    }

    /**
     * Verifies if a code is valid for a given secret. The code is valid for 90
     * seconds. The current system time is used in the calculation.
     * 
     * @param code
     *            The user supplied code to verify
     * @param secret
     *            The shared secret between the mobile phone and the server
     * @return true if the code is valid, false if the code is invalid
     */
    public static boolean verifyCode(String code, String secret) {
        try {
            // Number of 30 second intervals since epoch
            long timesteps = System.currentTimeMillis() / 1000L / 30L;
            byte[] secretBytes = new Base32().decode(secret);
            int codeInt = Integer.parseInt(code);

            // A window where the code is valid
            int window = 1;
            for (int i = -window; i <= window; i++) {
                if (hotp(secretBytes, timesteps + i) == codeInt) {
                    return true;
                }
            }
        } catch (Exception e) {
            // Catch all types of exceptions and return false
        }
        return false;
    }

    // Implementation of HOTP as described in RFC4226 and RFC6238
    private static long hotp(byte[] secret, long timesteps) {

        // Convert number of timesteps to binary
        byte[] timeBytes = ByteBuffer.allocate(8).putLong(timesteps).array();

        // Generate a sha1-hmac of the current timestep, using the secret as the key
        byte[] hmac;
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec key = new SecretKeySpec(secret, "RAW");
            mac.init(key);
            hmac = mac.doFinal(timeBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        // Extract code from HMAC
        int offset = hmac[hmac.length - 1] & 0xf;
        int binary = ((hmac[offset] & 0x7f) << 24)
                | ((hmac[offset + 1] & 0xff) << 16)
                | ((hmac[offset + 2] & 0xff) << 8)
                | (hmac[offset + 3] & 0xff);

        // Return a 6 digit code
        return binary % 1000000;
    }

}
