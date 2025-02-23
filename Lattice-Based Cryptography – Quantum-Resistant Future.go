package main

import (
	"crypto/rand"
	"fmt"
	"log"

	"github.com/cloudflare/circl/pke/kyber"
)

func main() {
	// Step 1: Generate Key Pair (Kyber-1024)
	pk, sk := kyber.KEM512().GenerateKeyPair()
	fmt.Println("🔑 Key Pair Generated!")

	// Step 2: Encrypt a Message (Kyber Encapsulation)
	ct, sharedSecretEnc, err := pk.Encrypt(rand.Reader)
	if err != nil {
		log.Fatal("Encryption failed:", err)
	}
	fmt.Println("🔒 Encryption Done!")

	// Step 3: Decrypt the Message (Kyber Decapsulation)
	sharedSecretDec, err := sk.Decrypt(ct)
	if err != nil {
		log.Fatal("Decryption failed:", err)
	}
	fmt.Println("🔓 Decryption Done!")

	// Step 4: Verify if Encryption & Decryption Matched
	if string(sharedSecretEnc) == string(sharedSecretDec) {
		fmt.Println("✅ Shared Secret Matched! Encryption is secure.")
	} else {
		fmt.Println("❌ Shared Secret Mismatch! Something went wrong.")
	}
}
