package main

import (
	"fmt"
	"math/big"

	"github.com/tuneinsight/lattigo/v4/bfv"
	"github.com/tuneinsight/lattigo/v4/rlwe"
)

func main() {
	// Step 1: Create encryption parameters
	params, err := bfv.NewParametersFromLiteral(bfv.PN12QP109)
	if err != nil {
		panic(err)
	}

	// Step 2: Generate keys
	kgen := bfv.NewKeyGenerator(params)
	sk, pk := kgen.GenKeyPair()
	encryptor := bfv.NewEncryptor(params, pk)
	decryptor := bfv.NewDecryptor(params, sk)
	evaluator := bfv.NewEvaluator(params, rlwe.EvaluationKey{})

	// Step 3: Encrypt two integers
	encoder := bfv.NewEncoder(params)
	plaintext1 := bfv.NewPlaintext(params)
	plaintext2 := bfv.NewPlaintext(params)

	encoder.Encode([]uint64{10}, plaintext1)
	encoder.Encode([]uint64{20}, plaintext2)

	ciphertext1 := encryptor.EncryptNew(plaintext1)
	ciphertext2 := encryptor.EncryptNew(plaintext2)

	fmt.Println("🔒 Numbers Encrypted Successfully!")

	// Step 4: Perform Homomorphic Addition (E(10) + E(20) = E(30))
	ciphertextSum := evaluator.AddNew(ciphertext1, ciphertext2)

	// Step 5: Decrypt the result
	decryptedResult := bfv.NewPlaintext(params)
	decryptor.Decrypt(ciphertextSum, decryptedResult)

	var result []uint64
	encoder.Decode(decryptedResult, &result)

	fmt.Println("🔓 Decrypted Sum:", result[0]) // Should print 30
}
