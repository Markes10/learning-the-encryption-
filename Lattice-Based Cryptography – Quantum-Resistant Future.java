import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;
import org.bouncycastle.pqc.jcajce.spec.KyberParameterSpec;
import org.bouncycastle.pqc.jcajce.provider.KEMUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KEM;
import java.security.*;
import java.util.Arrays;

public class KyberPostQuantumEncryption {
    public static void main(String[] args) {
        try {
            // Step 1: Add Bouncy Castle Provider for Post-Quantum Crypto
            Security.addProvider(new BouncyCastlePQCProvider());

            // Step 2: Generate Kyber-1024 Key Pair
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("Kyber", "BCPQC");
            keyPairGen.initialize(KyberParameterSpec.kyber1024, new SecureRandom());
            KeyPair keyPair = keyPairGen.generateKeyPair();
            System.out.println("🔑 Key Pair Generated!");

            // Step 3: Encrypt a Message (Kyber Encapsulation)
            KEM kem = KEM.getInstance("Kyber", "BCPQC");
            kem.init(keyPair.getPublic(), new SecureRandom());
            KEM.Encapsulated enc = kem.encapsulate();
            byte[] encryptedSecret = enc.getEncapsulation();
            byte[] sharedSecretEnc = enc.getSecret();

            System.out.println("🔒 Encryption Done!");
            System.out.println("Encrypted Data: " + Arrays.toString(encryptedSecret));

            // Step 4: Decrypt the Message (Kyber Decapsulation)
            kem.init(keyPair.getPrivate(), null);
            byte[] sharedSecretDec = kem.decapsulate(encryptedSecret);
            System.out.println("🔓 Decryption Done!");

            // Step 5: Verify if Encryption & Decryption Matched
            if (Arrays.equals(sharedSecretEnc, sharedSecretDec)) {
                System.out.println("✅ Shared Secret Matched! Encryption is secure.");
            } else {
                System.out.println("❌ Shared Secret Mismatch! Something went wrong.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
