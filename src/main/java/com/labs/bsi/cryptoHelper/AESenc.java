package com.labs.bsi.cryptoHelper;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
public class AESenc {
    private static final String ALGO = "AES";
    private static final byte[] keyValue
            = new byte[]{'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};

    //encrypts string and returns encrypted string
    public static String encrypt(String data, String key) throws Exception {
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, generateKey(key.getBytes()));
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encVal);
    }

    //decrypts string and returns plain text
    public static String decrypt(String encryptedData, String key) throws Exception {
        encryptedData = encryptedData.replaceAll("\\s+", "+");
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, generateKey(key.getBytes()));
        byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }

    // Generate a new encryption key.
    private static Key generateKey(byte[] keyValue) throws Exception {
        return new SecretKeySpec(keyValue, ALGO);
    }
}