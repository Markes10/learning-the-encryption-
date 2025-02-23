use rand::Rng;
use base64::{encode, decode};

// Generate a random key of the same length as the plaintext
fn generate_random_key(length: usize) -> Vec<u8> {
    let mut rng = rand::thread_rng();
    (0..length).map(|_| rng.gen::<u8>()).collect()
}

// Encrypt using One-Time Pad
fn one_time_pad_encrypt(plain_text: &str, key: &[u8]) -> Vec<u8> {
    if plain_text.len() != key.len() {
        panic!("Key length must match plaintext length");
    }
    plain_text.as_bytes()
        .iter()
        .zip(key.iter())
        .map(|(&p, &k)| p ^ k) // XOR operation
        .collect()
}

// Decrypt using One-Time Pad
fn one_time_pad_decrypt(cipher_text: &[u8], key: &[u8]) -> String {
    let decrypted_bytes: Vec<u8> = cipher_text.iter()
        .zip(key.iter())
        .map(|(&c, &k)| c ^ k) // XOR operation
        .collect();
    String::from_utf8(decrypted_bytes).expect("Invalid UTF-8")
}

fn main() {
    let plain_text = "Hello, Secure World!";
    let key = generate_random_key(plain_text.len());

    let encrypted_bytes = one_time_pad_encrypt(plain_text, &key);
    let decrypted_text = one_time_pad_decrypt(&encrypted_bytes, &key);

    // Encode encrypted data in Base64 for readability
    let encrypted_base64 = encode(&encrypted_bytes);
    let key_base64 = encode(&key);

    println!("Plain Text: {}", plain_text);
    println!("Generated Key (Base64): {}", key_base64);
    println!("Encrypted Text (Base64): {}", encrypted_base64);
    println!("Decrypted Text: {}", decrypted_text);
}
