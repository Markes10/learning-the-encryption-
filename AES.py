# learning-the-encryption-
# This is the AES Technick  
from Crypto.Cipher import AES  
from Crypto.Util.Padding import pad, unpad  
import os  
import base64  

# Function to encrypt data  
def encrypt(plain_text, key):  
    # Generate a random IV  
    iv = os.urandom(16)  
    
    # Create AES cipher object  
    cipher = AES.new(key, AES.MODE_CBC, iv)  
    
    # Pad the plain text to make it a multiple of 16 bytes  
    padded_text = pad(plain_text.encode(), AES.block_size)  
    
    # Encrypt the data  
    encrypted_text = cipher.encrypt(padded_text)  
    
    # Return the IV and encrypted data, both base64-encoded  
    return base64.b64encode(iv + encrypted_text).decode('utf-8')  

# Function to decrypt data  
def decrypt(encrypted_text, key):  
    # Decode the base64-encoded data  
    encrypted_data = base64.b64decode(encrypted_text)  
    
    # Extract the IV and the actual encrypted text  
    iv = encrypted_data[:16]  
    encrypted_text = encrypted_data[16:]  
    
    # Create AES cipher object  
    cipher = AES.new(key, AES.MODE_CBC, iv)  
    
    # Decrypt the data  
    decrypted_padded_text = cipher.decrypt(encrypted_text)  
    
    # Unpad the decrypted data  
    decrypted_text = unpad(decrypted_padded_text, AES.block_size).decode('utf-8')  
    
    return decrypted_text  

# Example usage  
if __name__ == "__main__":  
    # AES key must be either 16, 24, or 32 bytes long  
    key = os.urandom(16)  # Generate a random key  

    original_text = "This is a secret message."  
    print("Original:", original_text)  
    
    encrypted = encrypt(original_text, key)  
    print("Encrypted:", encrypted)  
    
    decrypted = decrypt(encrypted, key)  
    print("Decrypted:", decrypted)
