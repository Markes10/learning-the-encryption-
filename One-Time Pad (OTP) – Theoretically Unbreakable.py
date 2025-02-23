import base64
import random
import string

def one_time_pad_encrypt(plain_text, key):
    if len(key) < len(plain_text):
        raise ValueError("Key must be at least as long as the plaintext")
    cipher_text = ''.join(chr(ord(p) ^ ord(k)) for p, k in zip(plain_text, key))
    return base64.b64encode(cipher_text.encode()).decode()

def one_time_pad_decrypt(cipher_text, key):
    cipher_text = base64.b64decode(cipher_text.encode()).decode()
    return ''.join(chr(ord(c) ^ ord(k)) for c, k in zip(cipher_text, key))

def generate_random_key(length):
    return ''.join(random.choices(string.ascii_letters + string.digits, k=length))

# Example Usage
plain_text = "Hello, Secure World!"
key = generate_random_key(len(plain_text))

# One-Time Pad Encryption and Decryption
otp_encrypted = one_time_pad_encrypt(plain_text, key)
print("OTP Encrypted:", otp_encrypted)
print("OTP Decrypted:", one_time_pad_decrypt(otp_encrypted, key))
