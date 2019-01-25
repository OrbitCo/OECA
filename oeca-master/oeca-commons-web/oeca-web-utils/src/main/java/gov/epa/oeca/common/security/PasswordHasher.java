package gov.epa.oeca.common.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;

/**
 * @author dfladung
 */
public class PasswordHasher {


    public static String hashPassword(String password) throws Exception {
        char[] passwordChars = password.toCharArray();
        byte[] saltBytes = PasswordHasher.class.getName().getBytes();
        PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, 1024, 192);
        SecretKeyFactory key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hashedPassword = key.generateSecret(spec).getEncoded();
        return String.format("%x", new BigInteger(1, hashedPassword));
    }

    public static boolean validatePassword(String password, String hashedPassword) throws Exception {
        return password != null && hashedPassword != null && hashPassword(password).equals(hashedPassword);
    }

    public static void main(String[] args) {
        try {
            String hash = hashPassword(args[0]);
            System.out.print(hash);
        } catch (Exception e) {
            System.err.print(e);
        }
    }
}
