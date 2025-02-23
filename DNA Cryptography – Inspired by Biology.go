package main

import (
	"fmt"
	"math/rand"
	"strings"
	"time"
)

// DNA Mapping Tables
var binaryToDNA = map[string]rune{
	"00": 'A',
	"01": 'T',
	"10": 'C',
	"11": 'G',
}

var dnaToBinary = map[rune]string{
	'A': "00",
	'T': "01",
	'C': "10",
	'G': "11",
}

// Convert Text to Binary
func textToBinary(text string) string {
	binaryString := ""
	for _, c := range text {
		binaryString += fmt.Sprintf("%08b", c)
	}
	return binaryString
}

// Convert Binary to DNA
func binaryToDna(binary string) string {
	var dna strings.Builder
	for i := 0; i < len(binary); i += 2 {
		dna.WriteRune(binaryToDNA[binary[i:i+2]])
	}
	return dna.String()
}

// Convert DNA to Binary
func dnaToBinaryString(dna string) string {
	var binary strings.Builder
	for _, nucleotide := range dna {
		binary.WriteString(dnaToBinary[nucleotide])
	}
	return binary.String()
}

// Generate Random DNA Key
func generateDnaKey(length int) string {
	nucleotides := "ATCG"
	rand.Seed(time.Now().UnixNano())
	var key strings.Builder
	for i := 0; i < length; i++ {
		key.WriteByte(nucleotides[rand.Intn(4)])
	}
	return key.String()
}

// XOR-Based DNA Encryption
func dnaXorEncrypt(dna, key string) string {
	var encrypted strings.Builder
	dnaMap := "ATCG"
	for i := range dna {
		dnaBin := strings.IndexRune(dnaMap, rune(dna[i]))
		keyBin := strings.IndexRune(dnaMap, rune(key[i]))
		encrypted.WriteByte(dnaMap[dnaBin^keyBin]) // XOR operation
	}
	return encrypted.String()
}

// XOR-Based DNA Decryption
func dnaXorDecrypt(encryptedDna, key string) string {
	var decrypted strings.Builder
	dnaMap := "ATCG"
	for i := range encryptedDna {
		encBin := strings.IndexRune(dnaMap, rune(encryptedDna[i]))
		keyBin := strings.IndexRune(dnaMap, rune(key[i]))
		decrypted.WriteByte(dnaMap[encBin^keyBin]) // Reverse XOR
	}
	return decrypted.String()
}

// Convert Binary to Text
func binaryToText(binary string) string {
	var text strings.Builder
	for i := 0; i < len(binary); i += 8 {
		charValue := 0
		fmt.Sscanf(binary[i:i+8], "%b", &charValue)
		text.WriteByte(byte(charValue))
	}
	return text.String()
}

// Encrypt Message
func encryptMessage(message string) (string, string) {
	binaryData := textToBinary(message)
	dnaSequence := binaryToDna(binaryData)
	key := generateDnaKey(len(dnaSequence))
	encryptedDna := dnaXorEncrypt(dnaSequence, key)
	return encryptedDna, key
}

// Decrypt Message
func decryptMessage(encryptedDna, key string) string {
	decryptedDna := dnaXorDecrypt(encryptedDna, key)
	binaryData := dnaToBinaryString(decryptedDna)
	return binaryToText(binaryData)
}

// Main Function
func main() {
	message := "HELLO DNA"
	fmt.Println("Original Message:", message)

	// Encrypt
	encryptedDna, dnaKey := encryptMessage(message)
	fmt.Println("Encrypted DNA:", encryptedDna)
	fmt.Println("DNA Key:      ", dnaKey)

	// Decrypt
	decryptedMessage := decryptMessage(encryptedDna, dnaKey)
	fmt.Println("Decrypted Message:", decryptedMessage)
}
