#include <iostream>
#include <string>
#include <cstdlib>
#include <ctime>

using namespace std;

// Function to generate a random key of given length
string generate_random_key(int length) {
    string key;
    for (int i = 0; i < length; i++) {
        key += char(rand() % 256); // Generate random ASCII character
    }
    return key;
}

// Function to encrypt using One-Time Pad
string one_time_pad_encrypt(const string& plain_text, const string& key) {
    if (plain_text.length() != key.length()) {
        throw invalid_argument("Key length must match plaintext length");
    }
    
    string cipher_text;
    for (size_t i = 0; i < plain_text.length(); i++) {
        cipher_text += plain_text[i] ^ key[i]; // XOR operation
    }
    return cipher_text;
}

// Function to decrypt using One-Time Pad
string one_time_pad_decrypt(const string& cipher_text, const string& key) {
    return one_time_pad_encrypt(cipher_text, key); // Decryption is same as encryption
}

int main() {
    srand(time(0)); // Seed for randomness

    string plain_text = "Hello, Secure World!";
    string key = generate_random_key(plain_text.length());

    string encrypted_text = one_time_pad_encrypt(plain_text, key);
    string decrypted_text = one_time_pad_decrypt(encrypted_text, key);

    cout << "Plain Text: " << plain_text << endl;
    cout << "Generated Key: " << key << endl;
    cout << "Encrypted Text: ";
    for (char c : encrypted_text) cout << hex << (int)(unsigned char)c << " "; // Print in hex
    cout << endl;
    cout << "Decrypted Text: " << decrypted_text << endl;

    return 0;
}
