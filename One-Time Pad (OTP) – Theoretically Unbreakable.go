package main

import (
	"crypto/rand"
	"encoding/base64"
	"fmt"
	"log"
)

// GenerateRandomKey creates a random key of the given length
func GenerateRandomKey(length int) []byte {
	key := make([]byte, length)
	_, err := rand.Read(key)
	if err != nil {
		log.Fatal(err)
	}
	return key
}

// OneTimePadEncrypt encrypts plaintext using OTP with the given key
func OneTimePadEncrypt(plainText []byte, key []byte) []byte {
	if len(plainText) != len(key) {
		log.Fatal("Key length must match plaintext length")
	}
	cipherText := make([]byte, len(plainText))
	for i := range plainText {
		cipherText[i] = plainText[i] ^ key[i] // XOR operation
	}
	return cipherText
}

// OneTimePadDecrypt decrypts OTP-encrypted text using the given key
func OneTimePadDecrypt(cipherText []byte, key []byte) []byte {
	return OneTimePadEncrypt(cipherText, key) // Decryption is the same as encryption
}

func main() {
	plainText := "Hello, Secure World!"
	plainBytes := []byte(plainText)

	// Generate a random key
	key := GenerateRandomKey(len(plainBytes))

	// Encrypt and Decrypt
	encryptedBytes := OneTimePadEncrypt(plainBytes, key)
	decryptedBytes := OneTimePadDecrypt(encryptedBytes, key)

	// Encode encrypted data in Base64 for readability
	encryptedBase64 := base64.StdEncoding.EncodeToString(encryptedBytes)
	keyBase64 := base64.StdEncoding.EncodeToString(key)

	fmt.Println("Plain Text:", plainText)
	fmt.Println("Generated Key (Base64):", keyBase64)
	fmt.Println("Encrypted Text (Base64):", encryptedBase64)
	fmt.Println("Decrypted Text:", string(decryptedBytes))
}
