import os
import base64
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad, unpad

def aes_encrypt(plain_text, key):
    cipher = AES.new(key, AES.MODE_CBC, iv=key[:16])
    encrypted_bytes = cipher.encrypt(pad(plain_text.encode(), AES.block_size))
    return base64.b64encode(encrypted_bytes).decode()

def aes_decrypt(cipher_text, key):
    cipher = AES.new(key, AES.MODE_CBC, iv=key[:16])
    decrypted_bytes = unpad(cipher.decrypt(base64.b64decode(cipher_text)), AES.block_size)
    return decrypted_bytes.decode()

# Example Usage
plain_text = "Hello, Secure World!"
aes_key = os.urandom(32)  # 256-bit key

aes_encrypted = aes_encrypt(plain_text, aes_key)
print("AES Encrypted:", aes_encrypted)
print("AES Decrypted:", aes_decrypt(aes_encrypted, aes_key))