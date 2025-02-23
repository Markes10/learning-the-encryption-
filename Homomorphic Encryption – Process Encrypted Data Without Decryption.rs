use concrete::*;

fn main() -> Result<(), CryptoAPIError> {
    // Step 1: Create Encryption Parameters
    let config = ConfigBuilder::new()
        .enable_default_parameters()
        .build();

    // Step 2: Generate Keys
    let keygen = KeyGenerator::new(config);
    let (client_key, server_key) = keygen.generate();

    // Step 3: Encrypt two numbers
    let encrypted_num1 = client_key.encrypt(10)?; // Encrypt 10
    let encrypted_num2 = client_key.encrypt(20)?; // Encrypt 20

    println!("🔒 Numbers Encrypted Successfully!");

    // Step 4: Perform Homomorphic Addition (E(10) + E(20) = E(30))
    let encrypted_sum = server_key.add(&encrypted_num1, &encrypted_num2)?;

    // Step 5: Decrypt the result
    let decrypted_result = client_key.decrypt(&encrypted_sum)?;

    println!("🔓 Decrypted Sum: {}", decrypted_result); // Should print 30

    Ok(())
}
