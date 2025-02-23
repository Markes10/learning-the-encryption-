#include <stdio.h>
#include <stdint.h>
#include <string.h>
#include "api.h"  // Kyber API from PQClean
#include "randombytes.h"

#define MESSAGE_SIZE 32  // Size of the shared secret

int main() {
    uint8_t public_key[CRYPTO_PUBLICKEYBYTES];   // Public Key
    uint8_t secret_key[CRYPTO_SECRETKEYBYTES];   // Private Key
    uint8_t ciphertext[CRYPTO_CIPHERTEXTBYTES];  // Encrypted Message
    uint8_t shared_secret_enc[MESSAGE_SIZE];     // Shared Secret (Encryption)
    uint8_t shared_secret_dec[MESSAGE_SIZE];     // Shared Secret (Decryption)

    // Step 1: Generate Key Pair (Public & Private)
    crypto_kem_keypair(public_key, secret_key);
    printf("🔑 Key Pair Generated!\n");

    // Step 2: Encrypt (Generate Ciphertext + Shared Secret)
    crypto_kem_enc(ciphertext, shared_secret_enc, public_key);
    printf("🔒 Encryption Done!\n");

    // Step 3: Decrypt (Retrieve Shared Secret)
    crypto_kem_dec(shared_secret_dec, ciphertext, secret_key);
    printf("🔓 Decryption Done!\n");

    // Step 4: Verify if Encryption & Decryption Matched
    if (memcmp(shared_secret_enc, shared_secret_dec, MESSAGE_SIZE) == 0) {
        printf("✅ Shared Secret Matched! Encryption is secure.\n");
    } else {
        printf("❌ Shared Secret Mismatch! Something went wrong.\n");
    }

    return 0;
}
