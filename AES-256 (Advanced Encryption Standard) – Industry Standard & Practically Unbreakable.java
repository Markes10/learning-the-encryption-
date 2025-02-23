import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AES256Encryption {

    // Generate a random 256-bit AES key
    public static SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // AES-256
        return keyGen.generateKey();
    }

    // Generate a random IV (Initialization Vector)
    public static byte[] generateIV() {
        byte[] iv = new byte[16]; // AES block size is 16 bytes
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    // Encrypt a plaintext string using AES-256
    public static String encrypt(String plainText, SecretKey secretKey, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypt an AES-256 encrypted string
    public static String decrypt(String cipherText, SecretKey secretKey, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(decryptedBytes);
    }

    public static void main(String[] args) {
        try {
            String plainText = "Hello, Secure World!";
            
            // Generate AES key and IV
            SecretKey aesKey = generateAESKey();
            byte[] iv = generateIV();

            // Encrypt and decrypt
            String encryptedText = encrypt(plainText, aesKey, iv);
            String decryptedText = decrypt(encryptedText, aesKey, iv);

            // Display results
            System.out.println("Plain Text: " + plainText);
            System.out.println("AES Key (Base64): " + Base64.getEncoder().encodeToString(aesKey.getEncoded()));
            System.out.println("IV (Base64): " + Base64.getEncoder().encodeToString(iv));
            System.out.println("Encrypted Text: " + encryptedText);
            System.out.println("Decrypted Text: " + decryptedText);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
