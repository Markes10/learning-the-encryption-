#include <iostream>
#include <seal/seal.h>

using namespace std;
using namespace seal;

int main() {
    // Step 1: Set up the encryption parameters
    EncryptionParameters params(scheme_type::bfv);
    size_t poly_modulus_degree = 8192;
    params.set_poly_modulus_degree(poly_modulus_degree);
    params.set_coeff_modulus(CoeffModulus::BFVDefault(poly_modulus_degree));
    params.set_plain_modulus(PlainModulus::Batching(poly_modulus_degree, 20));

    // Step 2: Create SEAL context
    SEALContext context(params);

    // Step 3: Generate Keys
    KeyGenerator keygen(context);
    auto secret_key = keygen.secret_key();
    PublicKey public_key;
    keygen.create_public_key(public_key);
    Encryptor encryptor(context, public_key);
    Evaluator evaluator(context);
    Decryptor decryptor(context, secret_key);

    // Step 4: Encrypt two numbers
    int num1 = 5, num2 = 10;
    Plaintext plain1(to_string(num1));
    Plaintext plain2(to_string(num2));
    Ciphertext encrypted1, encrypted2;
    encryptor.encrypt(plain1, encrypted1);
    encryptor.encrypt(plain2, encrypted2);

    cout << "🔒 Encrypted Numbers: " << encrypted1.save_size() << " bytes each" << endl;

    // Step 5: Perform Homomorphic Operations
    Ciphertext encrypted_result;
    evaluator.multiply(encrypted1, encrypted2, encrypted_result); // num1 * num2 (5 * 10)

    // Step 6: Decrypt the result
    Plaintext decrypted_result;
    decryptor.decrypt(encrypted_result, decrypted_result);
    
    cout << "🔓 Decrypted Result: " << decrypted_result.to_string() << endl; // Should be 50

    return 0;
}
