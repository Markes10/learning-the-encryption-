package main

import (
	"crypto/aes"
	"crypto/cipher"
	"crypto/rand"
	"encoding/base64"
	"fmt"
	"io"
	"log"
)

// GenerateRandomKey creates a random 256-bit AES key
func GenerateRandomKey() []byte {
	key := make([]byte, 32) // 32 bytes = 256 bits
	_, err := rand.Read(key)
	if err != nil {
		log.Fatal(err)
	}
	return key
}

// GenerateRandomIV creates a random IV (Initialization Vector)
func GenerateRandomIV() []byte {
	iv := make([]byte, aes.BlockSize) // AES block size is 16 bytes
	_, err := rand.Read(iv)
	if err != nil {
		log.Fatal(err)
	}
	return iv
}

// EncryptAES256 encrypts a plaintext string using AES-256 in CBC mode
func EncryptAES256(plainText string, key, iv []byte) string {
	block, err := aes.NewCipher(key)
	if err != nil {
		log.Fatal(err)
	}

	// Pad plaintext to be a multiple of AES block size
	padding := aes.BlockSize - len(plainText)%aes.BlockSize
	paddedText := append([]byte(plainText), make([]byte, padding)...)

	cipherText := make([]byte, len(paddedText))
	mode := cipher.NewCBCEncrypter(block, iv)
	mode.CryptBlocks(cipherText, paddedText)

	return base64.StdEncoding.EncodeToString(cipherText) // Return Base64 encoded
}

// DecryptAES256 decrypts an AES-256 encrypted string
func DecryptAES256(cipherText string, key, iv []byte) string {
	block, err := aes.NewCipher(key)
	if err != nil {
		log.Fatal(err)
	}

	decodedCipherText, err := base64.StdEncoding.DecodeString(cipherText)
	if err != nil {
		log.Fatal(err)
	}

	if len(decodedCipherText)%aes.BlockSize != 0 {
		log.Fatal("Cipher text is not a multiple of block size")
	}

	plainText := make([]byte, len(decodedCipherText))
	mode := cipher.NewCBCDecrypter(block, iv)
	mode.CryptBlocks(plainText, decodedCipherText)

	// Remove padding
	plainText = plainText[:len(plainText)-int(plainText[len(plainText)-1])]

	return string(plainText)
}

func main() {
	plainText := "Hello, Secure World!"

	// Generate AES Key and IV
	key := GenerateRandomKey()
	iv := GenerateRandomIV()

	// Encrypt and Decrypt
	encryptedText := EncryptAES256(plainText, key, iv)
	decryptedText := DecryptAES256(encryptedText, key, iv)

	fmt.Println("Plain Text:", plainText)
	fmt.Println("AES Key (Base64):", base64.StdEncoding.EncodeToString(key))
	fmt.Println("IV (Base64):", base64.StdEncoding.EncodeToString(iv))
	fmt.Println("Encrypted Text:", encryptedText)
	fmt.Println("Decrypted Text:", decryptedText)
}
