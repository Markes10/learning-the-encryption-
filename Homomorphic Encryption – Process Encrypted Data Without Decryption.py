import tenseal as ts

def homomorphic_encryption_demo():
    # Step 1: Create a SEAL Context (BFV scheme)
    context = ts.context(
        scheme=ts.SCHEME_TYPE.BFV,
        poly_modulus_degree=8192,
        coeff_mod_bit_sizes=[60, 40, 40, 60]
    )
    context.global_scale = 2**40
    context.generate_galois_keys()
    context.generate_relin_keys()

    # Step 2: Encrypt Numbers
    secret_key = context.secret_key()
    encryptor = ts.bfv_vector(context, [5, 10, 15])  # Encrypts [5, 10, 15]

    print("🔒 Encrypted Numbers:", encryptor)

    # Step 3: Perform Homomorphic Operations
    encrypted_result = encryptor * 2 + 3  # (5 * 2 + 3, 10 * 2 + 3, 15 * 2 + 3)

    print("🔒 Homomorphically Computed (Encrypted):", encrypted_result)

    # Step 4: Decrypt the Result
    decryptor = encrypted_result.decrypt(secret_key)
    print("🔓 Decrypted Result:", decryptor)

if __name__ == "__main__":
    homomorphic_encryption_demo()
