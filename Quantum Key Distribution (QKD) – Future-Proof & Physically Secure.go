package main

import (
	"fmt"
	"math/rand"
	"time"
)

const (
	numBits = 20 // Number of qubits exchanged
)

// Possible bases and bits
var bases = []rune{'+', 'x'}
var bits = []rune{'0', '1'}

// Generate a random bit sequence
func generateBits(length int) []rune {
	result := make([]rune, length)
	for i := range result {
		result[i] = bits[rand.Intn(2)]
	}
	return result
}

// Generate a random sequence of bases
func generateBases(length int) []rune {
	result := make([]rune, length)
	for i := range result {
		result[i] = bases[rand.Intn(2)]
	}
	return result
}

// Simulate Bob's measurement based on random bases
func measureQubits(bits, aliceBases, bobBases []rune) []rune {
	measuredBits := make([]rune, len(bits))
	for i := range bits {
		if aliceBases[i] == bobBases[i] {
			measuredBits[i] = bits[i] // Correct measurement
		} else {
			measuredBits[i] = bits[rand.Intn(2)] // Random result
		}
	}
	return measuredBits
}

// Sift the key: Keep bits where Alice and Bob used the same basis
func siftKey(bits, aliceBases, bobBases []rune) []rune {
	var key []rune
	for i := range bits {
		if aliceBases[i] == bobBases[i] {
			key = append(key, bits[i])
		}
	}
	return key
}

// Simulate eavesdropping by Eve
func eavesdrop(bits, aliceBases []rune) []rune {
	eveBases := generateBases(len(bits)) // Eve randomly chooses bases
	return measureQubits(bits, aliceBases, eveBases) // Eve measures qubits
}

// Detect eavesdropping by comparing a sample of bits
func detectEavesdropping(originalBits, eveBits []rune, sampleSize int) bool {
	mismatches := 0
	for i := 0; i < sampleSize; i++ {
		index := rand.Intn(len(originalBits))
		if originalBits[index] != eveBits[index] {
			mismatches++
		}
	}
	errorRate := float64(mismatches) / float64(sampleSize) * 100
	fmt.Printf("Error Rate: %.2f%%\n", errorRate)
	return errorRate > 10 // If error rate > 10%, assume Eve was present
}

func main() {
	rand.Seed(time.Now().UnixNano()) // Seed the random generator

	// Step 1: Alice generates random bits and bases
	aliceBits := generateBits(numBits)
	aliceBases := generateBases(numBits)

	// Step 2: Bob selects random bases and measures the qubits
	bobBases := generateBases(numBits)
	bobBits := measureQubits(aliceBits, aliceBases, bobBases)

	// Step 3: Optional eavesdropping by Eve
	eavesdropEnabled := true
	var eveBits []rune
	if eavesdropEnabled {
		eveBits = eavesdrop(aliceBits, aliceBases)
	}

	// Step 4: Basis comparison & key sifting
	siftedKey := siftKey(aliceBits, aliceBases, bobBases)

	// Output Results
	fmt.Println("\n--- Quantum Key Distribution (BB84 Simulation) ---")
	fmt.Println("Alice's Bits:  ", string(aliceBits))
	fmt.Println("Alice's Bases: ", string(aliceBases))
	fmt.Println("Bob's Bases:   ", string(bobBases))
	fmt.Println("Bob's Bits:    ", string(bobBits))

	if eavesdropEnabled {
		fmt.Println("Eve's Bits:    ", string(eveBits))
	}

	fmt.Print("\nSifted Key:    ")
	fmt.Println(string(siftedKey), "(Final Shared Key)")

	// Step 5: Eavesdropping detection
	if eavesdropEnabled {
		sampleSize := len(siftedKey) / 2 // Use half of the key for error checking
		detected := detectEavesdropping(aliceBits, eveBits, sampleSize)
		if detected {
			fmt.Println("WARNING: Eavesdropping detected! Secure communication compromised.")
		} else {
			fmt.Println("No significant eavesdropping detected. Communication is secure.")
		}
	}
}
