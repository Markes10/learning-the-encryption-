import random

# DNA Mapping for Encoding and Decoding
binary_to_dna = {
    "00": "A",
    "01": "T",
    "10": "C",
    "11": "G"
}

dna_to_binary = {v: k for k, v in binary_to_dna.items()}

# Generate a Random DNA Key
def generate_dna_key(length):
    return ''.join(random.choice("ATCG") for _ in range(length))

# Convert Text to Binary
def text_to_binary(text):
    return ''.join(format(ord(char), '08b') for char in text)

# Convert Binary to DNA
def binary_to_dna_sequence(binary):
    return ''.join(binary_to_dna[binary[i:i+2]] for i in range(0, len(binary), 2))

# Convert DNA to Binary
def dna_to_binary_sequence(dna):
    return ''.join(dna_to_binary[nucleotide] for nucleotide in dna)

# XOR-Based DNA Encryption
def dna_xor_encrypt(dna_sequence, dna_key):
    encrypted_dna = []
    for i in range(len(dna_sequence)):
        encrypted_dna.append(binary_to_dna[str(int(dna_to_binary[dna_sequence[i]]) ^ int(dna_to_binary[dna_key[i]]))])
    return ''.join(encrypted_dna)

# XOR-Based DNA Decryption
def dna_xor_decrypt(encrypted_dna, dna_key):
    decrypted_dna = []
    for i in range(len(encrypted_dna)):
        decrypted_dna.append(binary_to_dna[str(int(dna_to_binary[encrypted_dna[i]]) ^ int(dna_to_binary[dna_key[i]]))])
    return ''.join(decrypted_dna)

# Convert Binary to Text
def binary_to_text(binary):
    return ''.join(chr(int(binary[i:i+8], 2)) for i in range(0, len(binary), 8))

# Encrypt a Message Using DNA Cryptography
def encrypt_message(message):
    binary_data = text_to_binary(message)
    dna_sequence = binary_to_dna_sequence(binary_data)
    dna_key = generate_dna_key(len(dna_sequence))
    encrypted_dna = dna_xor_encrypt(dna_sequence, dna_key)
    return encrypted_dna, dna_key

# Decrypt a Message Using DNA Cryptography
def decrypt_message(encrypted_dna, dna_key):
    decrypted_dna = dna_xor_decrypt(encrypted_dna, dna_key)
    binary_data = dna_to_binary_sequence(decrypted_dna)
    return binary_to_text(binary_data)

# Test the DNA Cryptography System
if __name__ == "__main__":
    message = "HELLO DNA"
    print(f"Original Message: {message}")

    # Encrypt
    encrypted_dna, dna_key = encrypt_message(message)
    print(f"Encrypted DNA: {encrypted_dna}")
    print(f"DNA Key:       {dna_key}")

    # Decrypt
    decrypted_message = decrypt_message(encrypted_dna, dna_key)
    print(f"Decrypted Message: {decrypted_message}")
