import java.util.*;

public class BB84QKD {
    private static final char[] BASES = {'+', 'x'}; // '+' = Rectilinear, 'x' = Diagonal
    private static final char[] BITS = {'0', '1'};  // Binary bits
    private static final Random rand = new Random();

    // Generate a random sequence of bits
    private static char[] generateBits(int length) {
        char[] bits = new char[length];
        for (int i = 0; i < length; i++) {
            bits[i] = BITS[rand.nextInt(2)];
        }
        return bits;
    }

    // Generate random bases
    private static char[] generateBases(int length) {
        char[] bases = new char[length];
        for (int i = 0; i < length; i++) {
            bases[i] = BASES[rand.nextInt(2)];
        }
        return bases;
    }

    // Simulate Bob's measurement based on his random bases
    private static char[] measureQubits(char[] bits, char[] aliceBases, char[] bobBases) {
        char[] measuredBits = new char[bits.length];
        for (int i = 0; i < bits.length; i++) {
            if (aliceBases[i] == bobBases[i]) { // Correct basis = Correct measurement
                measuredBits[i] = bits[i];
            } else { // Wrong basis = Random outcome
                measuredBits[i] = BITS[rand.nextInt(2)];
            }
        }
        return measuredBits;
    }

    // Sift the key: Keep bits where Alice and Bob used the same basis
    private static List<Character> siftKey(char[] bits, char[] aliceBases, char[] bobBases) {
        List<Character> key = new ArrayList<>();
        for (int i = 0; i < bits.length; i++) {
            if (aliceBases[i] == bobBases[i]) {
                key.add(bits[i]);
            }
        }
        return key;
    }

    // Simulate eavesdropping by Eve
    private static char[] eavesdrop(char[] bits, char[] aliceBases) {
        char[] eveBases = generateBases(bits.length); // Eve picks random bases
        return measureQubits(bits, aliceBases, eveBases);  // Eve measures the qubits
    }

    // Detect eavesdropping by comparing a random subset of bits
    private static boolean detectEavesdropping(char[] originalBits, char[] eveBits, int sampleSize) {
        int mismatches = 0;
        for (int i = 0; i < sampleSize; i++) {
            int index = rand.nextInt(originalBits.length);
            if (originalBits[index] != eveBits[index]) {
                mismatches++;
            }
        }
        double errorRate = (double) mismatches / sampleSize * 100;
        System.out.println("Error Rate: " + String.format("%.2f", errorRate) + "%");
        return errorRate > 10; // If error rate > 10%, assume Eve was present
    }

    public static void main(String[] args) {
        int numBits = 20; // Number of qubits to exchange

        // Step 1: Alice generates random bits and bases
        char[] aliceBits = generateBits(numBits);
        char[] aliceBases = generateBases(numBits);

        // Step 2: Bob selects random bases and measures the qubits
        char[] bobBases = generateBases(numBits);
        char[] bobBits = measureQubits(aliceBits, aliceBases, bobBases);

        // Step 3: Optional eavesdropping by Eve
        boolean eavesdrop = true; // Set to false to disable Eve
        char[] eveBits = new char[numBits];
        if (eavesdrop) {
            eveBits = eavesdrop(aliceBits, aliceBases);
        }

        // Step 4: Basis comparison & key sifting
        List<Character> siftedKey = siftKey(aliceBits, aliceBases, bobBases);

        // Output Results
        System.out.println("\n--- Quantum Key Distribution (BB84 Simulation) ---");
        System.out.println("Alice's Bits:   " + new String(aliceBits));
        System.out.println("Alice's Bases:  " + new String(aliceBases));
        System.out.println("Bob's Bases:    " + new String(bobBases));
        System.out.println("Bob's Bits:     " + new String(bobBits));

        if (eavesdrop) {
            System.out.println("Eve's Bits:     " + new String(eveBits));
        }

        System.out.print("\nSifted Key:     ");
        for (char bit : siftedKey) {
            System.out.print(bit);
        }
        System.out.println(" (Final Shared Key)");

        // Step 5: Eavesdropping detection
        if (eavesdrop) {
            int sa
