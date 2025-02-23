use rand::Rng;
use std::collections::HashMap;

// DNA Mapping Tables
fn get_dna_mappings() -> (HashMap<String, char>, HashMap<char, String>) {
    let mut binary_to_dna = HashMap::new();
    let mut dna_to_binary = HashMap::new();

    binary_to_dna.insert("00".to_string(), 'A');
    binary_to_dna.insert("01".to_string(), 'T');
    binary_to_dna.insert("10".to_string(), 'C');
    binary_to_dna.insert("11".to_string(), 'G');

    for (bin, dna) in &binary_to_dna {
        dna_to_binary.insert(*dna, bin.clone());
    }

    (binary_to_dna, dna_to_binary)
}

// Convert Text to Binary
fn text_to_binary(text: &str) -> String {
    text.chars()
        .map(|c| format!("{:08b}", c as u8))
        .collect::<Vec<String>>()
        .join("")
}

// Convert Binary to DNA
fn binary_to_dna(binary: &str) -> String {
    let (binary_to_dna, _) = get_dna_mappings();
    binary
        .as_bytes()
        .chunks(2)
        .map(|chunk| {
            let key = std::str::from_utf8(chunk).unwrap();
            *binary_to_dna.get(key).unwrap_or(&'A') // Default to 'A' if not found
        })
        .collect()
}

// Convert DNA to Binary
fn dna_to_binary(dna: &str) -> String {
    let (_, dna_to_binary) = get_dna_mappings();
    dna.chars()
        .map(|c| dna_to_binary.get(&c).unwrap().clone())
        .collect::<Vec<String>>()
        .join("")
}

// Generate Random DNA Key
fn generate_dna_key(length: usize) -> String {
    let nucleotides = ['A', 'T', 'C', 'G'];
    let mut rng = rand::thread_rng();
    (0..length)
        .map(|_| nucleotides[rng.gen_range(0..4)])
        .collect()
}

// XOR-Based DNA Encryption
fn dna_xor_encrypt(dna: &str, key: &str) -> String {
    let nucleotides = "ATCG";
    dna.chars()
        .zip(key.chars())
        .map(|(d, k)| {
            let dna_bin = nucleotides.find(d).unwrap();
            let key_bin = nucleotides.find(k).unwrap();
            nucleotides.chars().nth(dna_bin ^ key_bin).unwrap()
        })
        .collect()
}

// XOR-Based DNA Decryption
fn dna_xor_decrypt(encrypted_dna: &str, key: &str) -> String {
    let nucleotides = "ATCG";
    encrypted_dna
        .chars()
        .zip(key.chars())
        .map(|(e, k)| {
            let enc_bin = nucleotides.find(e).unwrap();
            let key_bin = nucleotides.find(k).unwrap();
            nucleotides.chars().nth(enc_bin ^ key_bin).unwrap()
        })
        .collect()
}

// Convert Binary to Text
fn binary_to_text(binary: &str) -> String {
    binary
        .as_bytes()
        .chunks(8)
        .map(|chunk| {
            let byte_str = std::str::from_utf8(chunk).unwrap();
            let byte = u8::from_str_radix(byte_str, 2).unwrap();
            byte as char
        })
        .collect()
}

// Encrypt Message
fn encrypt_message(message: &str) -> (String, String) {
    let binary_data = text_to_binary(message);
    let dna_sequence = binary_to_dna(&binary_data);
    let dna_key = generate_dna_key(dna_sequence.len());
    let encrypted_dna = dna_xor_encrypt(&dna_sequence, &dna_key);
    (encrypted_dna, dna_key)
}

// Decrypt Message
fn decrypt_message(encrypted_dna: &str, dna_key: &str) -> String {
    let decrypted_dna = dna_xor_decrypt(encrypted_dna, dna_key);
    let binary_data = dna_to_binary(&decrypted_dna);
    binary_to_text(&binary_data)
}

// Main Function
fn main() {
    let message = "HELLO DNA";
    println!("Original Message: {}", message);

    // Encrypt
    let (encrypted_dna, dna_key) = encrypt_message(message);
    println!("Encrypted DNA: {}", encrypted_dna);
    println!("DNA Key:      {}", dna_key);

    // Decrypt
    let decrypted_message = decrypt_message(&encrypted_dna, &dna_key);
    println!("Decrypted Message: {}", decrypted_message);
}
