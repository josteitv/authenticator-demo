package utils;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;

public class TwoStepVerification {

    /**
     * Generates 10 bytes of random bits and then Base32 encode them.
     * 
     * @return a Base32 encoded string of 10 random bytes
     */
    public static String generateSecret() {
        byte[] buffer = new byte[10];
        new SecureRandom().nextBytes(buffer);
        return new String(new Base32().encode(buffer));
    }

    public static String createQrCodeUrl(String label, String secret) {
        String format = "https://chart.googleapis.com/chart"
                + "?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s%%3Fsecret%%3D%s";
        return String.format(format, label, secret);
    }

    public static boolean verifyCode(String code, String secret) {
        try {
            // Number of 30 second intervals since epoch
            long timesteps = System.currentTimeMillis() / 1000L / 30L;
            byte[] secretBytes = new Base32().decode(secret);
            int codeInt = Integer.parseInt(code);

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

        // Generate a sha1-hmac of the current timestep, using the secret as the
        // key
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
