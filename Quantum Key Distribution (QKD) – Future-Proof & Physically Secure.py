import random

# Define bases and bit values
BASES = ["+", "x"]  # "+" (rectilinear) and "x" (diagonal)
BITS = ["0", "1"]

def generate_quantum_bits(length):
    """Generate a random sequence of bits and bases for Alice (Sender)."""
    bits = [random.choice(BITS) for _ in range(length)]
    bases = [random.choice(BASES) for _ in range(length)]
    return bits, bases

def measure_qubits(bits, bases, receiver_bases):
    """Simulate Bob's measurement process based on his random bases."""
    received_bits = []
    for i in range(len(bits)):
        if bases[i] == receiver_bases[i]:  # Correct basis, measure correctly
            received_bits.append(bits[i])
        else:  # Wrong basis, random measurement outcome
            received_bits.append(random.choice(BITS))
    return received_bits

def sift_key(bits, bases, receiver_bases):
    """Sift the key: Keep only bits where Alice and Bob used the same basis."""
    key = [bits[i] for i in range(len(bits)) if bases[i] == receiver_bases[i]]
    return key

def simulate_eavesdropping(bits, bases):
    """Simulate an eavesdropper (Eve) intercepting and measuring qubits."""
    eve_bases = [random.choice(BASES) for _ in range(len(bits))]
    intercepted_bits = measure_qubits(bits, bases, eve_bases)  # Eve measures
    return intercepted_bits, eve_bases

def detect_eavesdropper(original_bits, intercepted_bits, matching_indices):
    """Detect eavesdropping by checking for mismatches in a sample of bits."""
    sample_size = len(matching_indices) // 2  # Use half for checking
    sample_indices = random.sample(matching_indices, sample_size)
    
    mismatches = sum(1 for i in sample_indices if original_bits[i] != intercepted_bits[i])
    error_rate = mismatches / sample_size
    return error_rate

def bb84_protocol(num_bits=20, eavesdrop=False):
    """Run the BB84 QKD protocol with optional eavesdropping."""
    print("\n--- Quantum Key Distribution (BB84 Simulation) ---")
    
    # Step 1: Alice prepares qubits
    alice_bits, alice_bases = generate_quantum_bits(num_bits)
    print(f"Alice's Bits:  {''.join(alice_bits)}")
    print(f"Alice's Bases: {''.join(alice_bases)}")

    # Step 2: Bob randomly chooses bases and measures
    bob_bases = [random.choice(BASES) for _ in range(num_bits)]
    bob_bits = measure_qubits(alice_bits, alice_bases, bob_bases)
    
    print(f"Bob's Bases:   {''.join(bob_bases)}")
    print(f"Bob's Bits:    {''.join(bob_bits)}")

    # Step 3: Optional Eavesdropping
    if eavesdrop:
        eve_bits, eve_bases = simulate_eavesdropping(alice_bits, alice_bases)
        print(f"Eve's Bases:   {''.join(eve_bases)}")
        print(f"Eve's Bits:    {''.join(eve_bits)}")

    # Step 4: Public basis comparison & key sifting
    matching_indices = [i for i in range(num_bits) if alice_bases[i] == bob_bases[i]]
    sifted_key = sift_key(alice_bits, alice_bases, bob_bases)
    print(f"\nSifted Key:    {''.join(sifted_key)} (Final shared key)")

    # Step 5: Error checking for eavesdropping detection
    if eavesdrop:
        error_rate = detect_eavesdropper(alice_bits, eve_bits, matching_indices)
        print(f"Error Rate:    {error_rate:.2%}")
        if error_rate > 10:  # Arbitrary threshold
            print("WARNING: Eavesdropping detected! Secure communication compromised.")
        else:
            print("No significant eavesdropping detected.")

# Run the QKD simulation
bb84_protocol(num_bits=20, eavesdrop=True)  # Enable eavesdropping simulation
