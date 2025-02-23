import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class OneTimePad {
    
    // Generate a random key of the same length as the plaintext
    public static byte[] generateRandomKey(int length) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[length];
        random.nextBytes(key);
        return key;
    }

    // Encrypt using One-Time Pad
    public static byte[] encrypt(byte[] plainText, byte[] key) {
        if (plainText.length != key.length) {
            throw new IllegalArgumentException("Key length must match plaintext length");
        }
        byte[] cipherText = new byte[plainText.length];
        for (int i = 0; i < plainText.length; i++) {
            cipherText[i] = (byte) (plainText[i] ^ key[i]); // XOR operation
        }
        return cipherText;
    }

    // Decrypt using One-Time Pad
    public static byte[] decrypt(byte[] cipherText, byte[] key) {
        return encrypt(cipherText, key); // Decryption is the same as encryption
    }

    public static void main(String[] args) {
        String plainText = "Hello, Secure World!";
        byte[] plainBytes = plainText.getBytes(StandardCharsets.UTF_8);
        
        byte[] key = generateRandomKey(plainBytes.length);
        byte[] encryptedBytes = encrypt(plainBytes, key);
        byte[] decryptedBytes = decrypt(encryptedBytes, key);
        
        // Encode encrypted data in Base64 to make it readable
        String encryptedBase64 = Base64.getEncoder().encodeToString(encryptedBytes);
        String decryptedText = new String(decryptedBytes, StandardCharsets.UTF_8);
        
        System.out.println("Plain Text: " + plainText);
        System.out.println("Generated Key (Base64): " + Base64.getEncoder().encodeToString(key));
        System.out.println("Encrypted Text (Base64): " + encryptedBase64);
        System.out.println("Decrypted Text: " + decryptedText);
    }
}
