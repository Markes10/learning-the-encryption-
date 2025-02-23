#include <iostream>
#include <openssl/evp.h>
#include <openssl/rand.h>
#include <vector>
#include <cstring>
#include <iomanip>

using namespace std;

const int AES_KEY_SIZE = 32;  // 256-bit key
const int AES_BLOCK_SIZE = 16; // AES block size

// Generate a random 256-bit AES key
vector<unsigned char> generate_aes_key() {
    vector<unsigned char> key(AES_KEY_SIZE);
    RAND_bytes(key.data(), AES_KEY_SIZE);
    return key;
}

// Generate a random IV (Initialization Vector)
vector<unsigned char> generate_iv() {
    vector<unsigned char> iv(AES_BLOCK_SIZE);
    RAND_bytes(iv.data(), AES_BLOCK_SIZE);
    return iv;
}

// AES-256 Encryption function
vector<unsigned char> aes_encrypt(const string &plain_text, const vector<unsigned char> &key, const vector<unsigned char> &iv) {
    EVP_CIPHER_CTX *ctx = EVP_CIPHER_CTX_new();
    EVP_EncryptInit_ex(ctx, EVP_aes_256_cbc(), NULL, key.data(), iv.data());

    vector<unsigned char> cipher_text(plain_text.size() + AES_BLOCK_SIZE);
    int len, cipher_len;

    EVP_EncryptUpdate(ctx, cipher_text.data(), &len, (unsigned char*)plain_text.c_str(), plain_text.length());
    cipher_len = len;
    EVP_EncryptFinal_ex(ctx, cipher_text.data() + len, &len);
    cipher_len += len;
    cipher_text.resize(cipher_len);

    EVP_CIPHER_CTX_free(ctx);
    return cipher_text;
}

// AES-256 Decryption function
string aes_decrypt(const vector<unsigned char> &cipher_text, const vector<unsigned char> &key, const vector<unsigned char> &iv) {
    EVP_CIPHER_CTX *ctx = EVP_CIPHER_CTX_new();
    EVP_DecryptInit_ex(ctx, EVP_aes_256_cbc(), NULL, key.data(), iv.data());

    vector<unsigned char> decrypted_text(cipher_text.size());
    int len, decrypted_len;

    EVP_DecryptUpdate(ctx, decrypted_text.data(), &len, cipher_text.data(), cipher_text.size());
    decrypted_len = len;
    EVP_DecryptFinal_ex(ctx, decrypted_text.data() + len, &len);
    decrypted_len += len;
    decrypted_text.resize(decrypted_len);

    EVP_CIPHER_CTX_free(ctx);
    return string(decrypted_text.begin(), decrypted_text.end());
}

// Convert vector to hex string
string to_hex(const vector<unsigned char> &data) {
    stringstream hex_stream;
    for (unsigned char c : data) {
        hex_stream << hex << setw(2) << setfill('0') << (int)c;
    }
    return hex_stream.str();
}

int main() {
    string plain_text = "Hello, Secure World!";
    
    vector<unsigned char> key = generate_aes_key();
    vector<unsigned char> iv = generate_iv();
    
    vector<unsigned char> encrypted_text = aes_encrypt(plain_text, key, iv);
    string decrypted_text = aes_decrypt(encrypted_text, key, iv);

    cout << "Plain Text: " << plain_text << endl;
    cout << "AES Key (Hex): " << to_hex(key) << endl;
    cout << "IV (Hex): " << to_hex(iv) << endl;
    cout << "Encrypted Text (Hex): " << to_hex(encrypted_text) << endl;
    cout << "Decrypted Text: " << decrypted_text << endl;

    return 0;
}
