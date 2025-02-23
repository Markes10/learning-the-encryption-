use rand::Rng;

// Define the number of qubits exchanged
const NUM_BITS: usize = 20;

// Possible bases and bits
const BASES: [char; 2] = ['+', 'x'];
const BITS: [char; 2] = ['0', '1'];

// Generate a random sequence of bits
fn generate_bits(length: usize) -> Vec<char> {
    let mut rng = rand::thread_rng();
    (0..length).map(|_| BITS[rng.gen_range(0..2)]).collect()
}

// Generate a random sequence of bases
fn generate_bases(length: usize) -> Vec<char> {
    let mut rng = rand::thread_rng();
    (0..length).map(|_| BASES[rng.gen_range(0..2)]).collect()
}

// Simulate Bob's measurement based on his random bases
fn measure_qubits(bits: &Vec<char>, alice_bases: &Vec<char>, bob_bases: &Vec<char>) -> Vec<char> {
    let mut rng = rand::thread_rng();
    bits.iter()
        .enumerate()
        .map(|(i, &bit)| {
            if alice_bases[i] == bob_bases[i] {
                bit // Correct measurement
            } else {
                BITS[rng.gen_range(0..2)] // Random measurement
            }
        })
        .collect()
}

// Sift the key: Keep bits where Alice and Bob used the same basis
fn sift_key(bits: &Vec<char>, alice_bases: &Vec<char>, bob_bases: &Vec<char>) -> Vec<char> {
    bits.iter()
        .enumerate()
        .filter_map(|(i, &bit)| if alice_bases[i] == bob_bases[i] { Some(bit) } else { None })
        .collect()
}

// Simulate eavesdropping by Eve
fn eavesdrop(bits: &Vec<char>, alice_bases: &Vec<char>) -> Vec<char> {
    let eve_bases = generate_bases(bits.len()); // Eve randomly chooses bases
    measure_qubits(bits, alice_bases, &eve_bases) // Eve measures qubits
}

// Detect eavesdropping by comparing a sample of bits
fn detect_eavesdropping(original_bits: &Vec<char>, eve_bits: &Vec<char>, sample_size: usize) -> bool {
    let mut rng = rand::thread_rng();
    let mut mismatches = 0;

    for _ in 0..sample_size {
        let index = rng.gen_range(0..original_bits.len());
        if original_bits[index] != eve_bits[index] {
            mismatches += 1;
        }
    }

    let error_rate = (mismatches as f64 / sample_size as f64) * 100.0;
    println!("Error Rate: {:.2}%", error_rate);

    error_rate > 10.0 // If error rate > 10%, assume Eve was present
}

fn main() {
    let mut rng = rand::thread_rng();

    // Step 1: Alice generates random bits and bases
    let alice_bits = generate_bits(NUM_BITS);
    let alice_bases = generate_bases(NUM_BITS);

    // Step 2: Bob selects random bases and measures the qubits
    let bob_bases = generate_bases(NUM_BITS);
    let bob_bits = measure_qubits(&alice_bits, &alice_bases, &bob_bases);

    // Step 3: Optional eavesdropping by Eve
    let eavesdrop_enabled = true;
    let mut eve_bits = Vec::new();
    if eavesdrop_enabled {
        eve_bits = eavesdrop(&alice_bits, &alice_bases);
    }

    // Step 4: Basis comparison & key sifting
    let sifted_key = sift_key(&alice_bits, &alice_bases, &bob_bases);

    // Output Results
    println!("\n--- Quantum Key Distribution (BB84 Simulation) ---");
    println!("Alice's Bits:   {:?}", alice_bits.iter().collect::<String>());
    println!("Alice's Bases:  {:?}", alice_bases.iter().collect::<String>());
    println!("Bob's Bases:    {:?}", bob_bases.iter().collect::<String>());
    println!("Bob's Bits:     {:?}", bob_bits.iter().collect::<String>());

    if eavesdrop_enabled {
        println!("Eve's Bits:     {:?}", eve_bits.iter().collect::<String>());
    }

    println!("\nSifted Key:     {:?} (Final Shared Key)", sifted_key.iter().collect::<String>());

    // Step 5: Eavesdropping detection
    if eavesdrop_enabled {
        let sample_size = sifted_key.len() / 2; // Use half of the key for error checking
        let detected = detect_eavesdropping(&alice_bits, &eve_bits, sample_size);
        if detected {
            println!("WARNING: Eavesdropping detected! Secure communication compromised.");
        } else {
            println!("No significant eavesdropping detected. Communication is secure.");
        }
    }
}
