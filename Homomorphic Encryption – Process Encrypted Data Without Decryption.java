import java.math.BigInteger;
import java.security.SecureRandom;

class Paillier {
    private BigInteger p, q, n, lambda;
    private BigInteger g;
    private BigInteger nsquare;
    private BigInteger mu;
    private int bitLength = 512;

    // Key Generation
    public Paillier() {
        keyGeneration();
    }

    private void keyGeneration() {
        p = new BigInteger(bitLength / 2, 64, new SecureRandom());
        q = new BigInteger(bitLength / 2, 64, new SecureRandom());
        n = p.multiply(q);
        nsquare = n.multiply(n);
        lambda = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        g = n.add(BigInteger.ONE);
        mu = lambda.modInverse(n);
    }

    // Encrypt a number
    public BigInteger encrypt(BigInteger m) {
        BigInteger r = new BigInteger(bitLength, new SecureRandom()).mod(n);
        return g.modPow(m, nsquare).multiply(r.modPow(n, nsquare)).mod(nsquare);
    }

    // Decrypt a number
    public BigInteger decrypt(BigInteger c) {
        BigInteger x = c.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n);
        return x.multiply(mu).mod(n);
    }

    // Homomorphic Addition: E(a) * E(b) = E(a + b)
    public BigInteger homomorphicAdd(BigInteger c1, BigInteger c2) {
        return c1.multiply(c2).mod(nsquare);
    }

    public static void main(String[] args) {
        Paillier paillier = new Paillier();

        BigInteger num1 = BigInteger.valueOf(10);
        BigInteger num2 = BigInteger.valueOf(15);

        // Encrypt numbers
        BigInteger enc1 = paillier.encrypt(num1);
        BigInteger enc2 = paillier.encrypt(num2);
        System.out.println("🔒 Encrypted Numbers: " + enc1 + ", " + enc2);

        // Homomorphic Addition
        BigInteger encryptedSum = paillier.homomorphicAdd(enc1, enc2);
        System.out.println("🔒 Homomorphic Computed (Encrypted Sum): " + encryptedSum);

        // Decrypt result
        BigInteger decryptedSum = paillier.decrypt(encryptedSum);
        System.out.println("🔓 Decrypted Sum: " + decryptedSum);
    }
}
