use pqcrypto::kem::kyber1024::*;
use pqcrypto::traits::kem::*;

fn main() {
    // Step 1: Generate Key Pair (Kyber-1024)
    let (pk, sk) = keypair();
    println!("🔑 Key Pair Generated!");

    // Step 2: Encrypt a Message (Kyber Encapsulation)
    let (ciphertext, shared_secret_enc) = encapsulate(&pk);
    println!("🔒 Encryption Done!");

    // Step 3: Decrypt the Message (Kyber Decapsulation)
    let shared_secret_dec = decapsulate(&ciphertext, &sk);
    println!("🔓 Decryption Done!");

    // Step 4: Verify if Encryption & Decryption Matched
    if shared_secret_enc == shared_secret_dec {
        println!("✅ Shared Secret Matched! Encryption is secure.");
    } else {
        println!("❌ Shared Secret Mismatch! Something went wrong.");
    }
}
