#include <iostream>
#include <string>
#include <bitset>
#include <ctime>
#include <cstdlib>

using namespace std;

// DNA Mapping
const string BINARY_TO_DNA[4] = {"A", "T", "C", "G"};
const string DNA_TO_BINARY = "00AT10CG11";

// Convert Text to Binary
string textToBinary(const string &text) {
    string binaryString = "";
    for (char c : text) {
        binaryString += bitset<8>(c).to_string();
    }
    return binaryString;
}

// Convert Binary to DNA Sequence
string binaryToDna(const string &binary) {
    string dna = "";
    for (size_t i = 0; i < binary.length(); i += 2) {
        int index = stoi(binary.substr(i, 2), nullptr, 2);
        dna += BINARY_TO_DNA[index];
    }
    return dna;
}

// Convert DNA to Binary Sequence
string dnaToBinary(const string &dna) {
    string binary = "";
    for (char nucleotide : dna) {
        size_t index = DNA_TO_BINARY.find(nucleotide);
        if (index != string::npos && index % 2 == 0) {
            binary += DNA_TO_BINARY.substr(index + 1, 2);
        }
    }
    return binary;
}

// Generate Random DNA Key
string generateDnaKey(int length) {
    string key = "";
    string nucleotides = "ATCG";
    srand(time(0));
    for (int i = 0; i < length; i++) {
        key += nucleotides[rand() % 4];
    }
    return key;
}

// XOR-Based DNA Encryption
string dnaXorEncrypt(const string &dna, const string &key) {
    string encrypted = "";
    for (size_t i = 0; i < dna.length(); i++) {
        int dnaBin = DNA_TO_BINARY.find(dna[i]) / 2;
        int keyBin = DNA_TO_BINARY.find(key[i]) / 2;
        encrypted += BINARY_TO_DNA[dnaBin ^ keyBin]; // XOR operation
    }
    return encrypted;
}

// XOR-Based DNA Decryption
string dnaXorDecrypt(const string &encryptedDna, const string &key) {
    string decrypted = "";
    for (size_t i = 0; i < encryptedDna.length(); i++) {
        int encBin = DNA_TO_BINARY.find(encryptedDna[i]) / 2;
        int keyBin = DNA_TO_BINARY.find(key[i]) / 2;
        decrypted += BINARY_TO_DNA[encBin ^ keyBin]; // Reverse XOR
    }
    return decrypted;
}

// Convert Binary to Text
string binaryToText(const string &binary) {
    string text = "";
    for (size_t i = 0; i < binary.length(); i += 8) {
        text += char(bitset<8>(binary.substr(i, 8)).to_ulong());
    }
    return text;
}

// Encrypt Message
void encryptMessage(const string &message, string &encryptedDna, string &key) {
    string binaryData = textToBinary(message);
    string dnaSequence = binaryToDna(binaryData);
    key = generateDnaKey(dnaSequence.length());
    encryptedDna = dnaXorEncrypt(dnaSequence, key);
}

// Decrypt Message
string decryptMessage(const string &encryptedDna, const string &key) {
    string decryptedDna = dnaXorDecrypt(encryptedDna, key);
    string binaryData = dnaToBinary(decryptedDna);
    return binaryToText(binaryData);
}

// Main Function
int main() {
    string message = "HELLO DNA";
    cout << "Original Message: " << message << endl;

    string encryptedDna, key;
    encryptMessage(message, encryptedDna, key);

    cout << "Encrypted DNA: " << encryptedDna << endl;
    cout << "DNA Key:       " << key << endl;

    string decryptedMessage = decryptMessage(encryptedDna, key);
    cout << "Decrypted Message: " << decryptedMessage << endl;

    return 0;
}
