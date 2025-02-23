import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DNACryptography {
    // DNA Mapping Tables
    private static final Map<String, Character> BINARY_TO_DNA = new HashMap<>();
    private static final Map<Character, String> DNA_TO_BINARY = new HashMap<>();

    static {
        BINARY_TO_DNA.put("00", 'A');
        BINARY_TO_DNA.put("01", 'T');
        BINARY_TO_DNA.put("10", 'C');
        BINARY_TO_DNA.put("11", 'G');

        for (Map.Entry<String, Character> entry : BINARY_TO_DNA.entrySet()) {
            DNA_TO_BINARY.put(entry.getValue(), entry.getKey());
        }
    }

    // Convert Text to Binary
    public static String textToBinary(String text) {
        StringBuilder binaryString = new StringBuilder();
        for (char c : text.toCharArray()) {
            binaryString.append(String.format("%08d", Integer.parseInt(Integer.toBinaryString(c))));
        }
        return binaryString.toString();
    }

    // Convert Binary to DNA
    public static String binaryToDna(String binary) {
        StringBuilder dna = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 2) {
            dna.append(BINARY_TO_DNA.get(binary.substring(i, i + 2)));
        }
        return dna.toString();
    }

    // Convert DNA to Binary
    public static String dnaToBinary(String dna) {
        StringBuilder binary = new StringBuilder();
        for (char nucleotide : dna.toCharArray()) {
            binary.append(DNA_TO_BINARY.get(nucleotide));
        }
        return binary.toString();
    }

    // Generate Random DNA Key
    public static String generateDnaKey(int length) {
        StringBuilder key = new StringBuilder();
        char[] nucleotides = { 'A', 'T', 'C', 'G' };
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            key.append(nucleotides[random.nextInt(4)]);
        }
        return key.toString();
    }

    // XOR-Based DNA Encryption
    public static String dnaXorEncrypt(String dna, String key) {
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < dna.length(); i++) {
            int dnaBin = "ATCG".indexOf(dna.charAt(i));
            int keyBin = "ATCG".indexOf(key.charAt(i));
            encrypted.append("ATCG".charAt(dnaBin ^ keyBin)); // XOR operation
        }
        return encrypted.toString();
    }

    // XOR-Based DNA Decryption
    public static String dnaXorDecrypt(String encryptedDna, String key) {
        StringBuilder decrypted = new StringBuilder();
        for (int i = 0; i < encryptedDna.length(); i++) {
            int encBin = "ATCG".indexOf(encryptedDna.charAt(i));
            int keyBin = "ATCG".indexOf(key.charAt(i));
            decrypted.append("ATCG".charAt(encBin ^ keyBin)); // Reverse XOR
        }
        return decrypted.toString();
    }

    // Convert Binary to Text
    public static String binaryToText(String binary) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 8) {
            text.append((char) Integer.parseInt(binary.substring(i, i + 8), 2));
        }
        return text.toString();
    }

    // Encrypt a Message
    public static String[] encryptMessage(String message) {
        String binaryData = textToBinary(message);
        String dnaSequence = binaryToDna(binaryData);
        String dnaKey = generateDnaKey(dnaSequence.length());
        String encryptedDna = dnaXorEncrypt(dnaSequence, dnaKey);
        return new String[] { encryptedDna, dnaKey };
    }

    // Decrypt a Message
    public static String decryptMessage(String encryptedDna, String dnaKey) {
        String decryptedDna = dnaXorDecrypt(encryptedDna, dnaKey);
        String binaryData = dnaToBinary(decryptedDna);
        return binaryToText(binaryData);
    }

    // Main Function
    public static void main(String[] args) {
        String message = "HELLO DNA";
        System.out.println("Original Message: " + message);

        // Encrypt
        String[] encryptionResult = encryptMessage(message);
        String encryptedDna = encryptionResult[0];
        String dnaKey = encryptionResult[1];

        System.out.println("Encrypted DNA: " + encryptedDna);
        System.out.println("DNA Key:       " + dnaKey);

        // Decrypt
        String decryptedMessage = decryptMessage(encryptedDna, dnaKey);
        System.out.println("Decrypted Message: " + decryptedMessage);
    }
}
