use aes::Aes256;
use cbc::{cipher::{KeyIvInit, BlockEncryptMut, BlockDecryptMut}, Encryptor, Decryptor};
use rand::Rng;
use base64::{encode, decode};

type Aes256CbcEnc = Encryptor<Aes256>;
type Aes256CbcDec = Decryptor<Aes256>;

const BLOCK_SIZE: usize = 16; // AES block size is 16 bytes

// Generate a random 256-bit AES key
fn generate_aes_key() -> [u8; 32] {
    rand::thread_rng().gen::<[u8; 32]>() // Generate 32 random bytes (256 bits)
}

// Generate a random IV (Initialization Vector)
fn generate_iv() -> [u8; BLOCK_SIZE] {
    rand::thread_rng().gen::<[u8; BLOCK_SIZE]>() // Generate 16 random bytes
}

// Encrypt a plaintext string using AES-256 in CBC mode
fn encrypt(plain_text: &str, key: &[u8; 32], iv: &[u8; BLOCK_SIZE]) -> String {
    let mut buffer = plain_text.as_bytes().to_vec();
    
    // Padding: Extend to multiple of BLOCK_SIZE
    let padding = BLOCK_SIZE - (buffer.len() % BLOCK_SIZE);
    buffer.extend(vec![padding as u8; padding]);

    let mut encrypted_data = vec![0u8; buffer.len()];
    let encryptor = Aes256CbcEnc::new(key.into(), iv.into());
    encryptor.encrypt_padded_mut::<cbc::cipher::block_padding::Pkcs7>(&mut buffer, &mut encrypted_data).unwrap();

    encode(&encrypted_data) // Return Base64-encoded encrypted text
}

// Decrypt an AES-256 encrypted string
fn decrypt(cipher_text: &str, key: &[u8; 32], iv: &[u8; BLOCK_SIZE]) -> String {
    let encrypted_data = decode(cipher_text).expect("Invalid Base64");

    let mut decrypted_data = vec![0u8; encrypted_data.len()];
    let decryptor = Aes256CbcDec::new(key.into(), iv.into());
    decryptor.decrypt_padded_mut::<cbc::cipher::block_padding::Pkcs7>(&mut decrypted_data, &encrypted_data).unwrap();

    String::from_utf8(decrypted_data).expect("Invalid UTF-8")
}

fn main() {
    let plain_text = "Hello, Secure World!";
    
    // Generate AES Key and IV
    let key = generate_aes_key();
    let iv = generate_iv();

    // Encrypt and Decrypt
    let encrypted_text = encrypt(plain_text, &key, &iv);
    let decrypted_text = decrypt(&encrypted_text, &key, &iv);

    println!("Plain Text: {}", plain_text);
    println!("AES Key (Hex): {}", hex::encode(key));
    println!("IV (Hex): {}", hex::encode(iv));
    println!("Encrypted Text (Base64): {}", encrypted_text);
    println!("Decrypted Text: {}", decrypted_text);
}
