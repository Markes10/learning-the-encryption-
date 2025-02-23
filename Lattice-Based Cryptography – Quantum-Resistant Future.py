from pqcrypto.kem.kyber1024 import generate_keypair, encrypt, decrypt
import base64

# Step 1: Generate Key Pair (Quantum-Resistant)
public_key, secret_key = generate_keypair()
print("🔑 Key Pair Generated!")

# Step 2: Encrypt a Secret Message
message = b"Hello, Quantum World!"
ciphertext, shared_secret_enc = encrypt(public_key)

# Step 3: Decrypt the Message
shared_secret_dec = decrypt(ciphertext, secret_key)

# Step 4: Display Results
print("🔒 Encrypted Ciphertext:", base64.b64encode(ciphertext).decode())
print("🔓 Decrypted Shared Secret Matches:", shared_secret_dec == shared_secret_enc)
