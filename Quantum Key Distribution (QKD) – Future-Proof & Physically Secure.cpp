#include <iostream>
#include <vector>
#include <cstdlib>
#include <ctime>
#include <algorithm>

using namespace std;

const char BASES[2] = {'+', 'x'}; // '+' = Rectilinear, 'x' = Diagonal
const char BITS[2] = {'0', '1'};  // Binary bits

// Generate a random bit sequence
vector<char> generate_bits(int length) {
    vector<char> bits;
    for (int i = 0; i < length; i++) {
        bits.push_back(BITS[rand() % 2]); // Randomly choose '0' or '1'
    }
    return bits;
}

// Generate random bases
vector<char> generate_bases(int length) {
    vector<char> bases;
    for (int i = 0; i < length; i++) {
        bases.push_back(BASES[rand() % 2]); // Randomly choose '+' or 'x'
    }
    return bases;
}

// Simulate Bob's measurement based on his random bases
vector<char> measure_qubits(const vector<char>& bits, const vector<char>& alice_bases, const vector<char>& bob_bases) {
    vector<char> measured_bits;
    for (size_t i = 0; i < bits.size(); i++) {
        if (alice_bases[i] == bob_bases[i]) { // Correct basis = Correct measurement
            measured_bits.push_back(bits[i]);
        } else { // Wrong basis = Random outcome
            measured_bits.push_back(BITS[rand() % 2]);
        }
    }
    return measured_bits;
}

// Sift the key: Keep bits where Alice and Bob used the same basis
vector<char> sift_key(const vector<char>& bits, const vector<char>& alice_bases, const vector<char>& bob_bases) {
    vector<char> key;
    for (size_t i = 0; i < bits.size(); i++) {
        if (alice_bases[i] == bob_bases[i]) {
            key.push_back(bits[i]);
        }
    }
    return key;
}

// Simulate eavesdropping by Eve
vector<char> eavesdrop(const vector<char>& bits, const vector<char>& alice_bases) {
    vector<char> eve_bases = generate_bases(bits.size()); // Eve picks random bases
    return measure_qubits(bits, alice_bases, eve_bases);  // Eve measures the qubits
}

// Detect eavesdropping by comparing a random subset of bits
bool detect_eavesdropping(const vector<char>& original_bits, const vector<char>& eve_bits, int sample_size) {
    int mismatches = 0;
    for (int i = 0; i < sample_size; i++) {
        int index = rand() % original_bits.size();
        if (original_bits[index] != eve_bits[index]) {
            mismatches++;
        }
    }
    double error_rate = (double)mismatches / sample_size * 100;
    cout << "Error Rate: " << error_rate << "%\n";
    return error_rate > 10; // If error rate > 10%, assume Eve was present
}

int main() {
    srand(time(0)); // Seed the random number generator
    int num_bits = 20; // Number of qubits to exchange

    // Step 1: Alice generates random bits and bases
    vector<char> alice_bits = generate_bits(num_bits);
    vector<char> alice_bases = generate_bases(num_bits);

    // Step 2: Bob selects random bases and measures the qubits
    vector<char> bob_bases = generate_bases(num_bits);
    vector<char> bob_bits = measure_qubits(alice_bits, alice_bases, bob_bases);

    // Step 3: Optional eavesdropping by Eve
    bool eavesdrop = true; // Set to false to disable Eve
    vector<char> eve_bits;
    if (eavesdrop) {
        eve_bits = eavesdrop(alice_bits, alice_bases);
    }

    // Step 4: Basis comparison & key sifting
    vector<char> sifted_key = sift_key(alice_bits, alice_bases, bob_bases);

    // Output Results
    cout << "\n--- Quantum Key Distribution (BB84 Simulation) ---\n";
    cout << "Alice's Bits:   ";
    for (char bit : alice_bits) cout << bit;
    cout << "\nAlice's Bases:  ";
    for (char base : alice_bases) cout << base;

    cout << "\nBob's Bases:    ";
    for (char base : bob_bases) cout << base;
    cout << "\nBob's Bits:     ";
    for (char bit : bob_bits) cout << bit;

    if (eavesdrop) {
        cout << "\nEve's Bits:     ";
        for (char bit : eve_bits) cout << bit;
    }

    cout << "\n\nSifted Key:     ";
    for (char bit : sifted_key) cout << bit;
    cout << " (Final Shared Key)\n";

    // Step 5: Eavesdropping detection
    if (eavesdrop) {
        int sample_size = sifted_key.size() / 2; // Use half of the key for error checking
        bool detected = detect_eavesdropping(alice_bits, eve_bits, sample_size);
        if (detected) {
            cout << "WARNING: Eavesdropping detected! Secure communication compromised.\n";
        } else {
            cout << "No significant eavesdropping detected. Communication is secure.\n";
        }
    }

    return 0;
}
