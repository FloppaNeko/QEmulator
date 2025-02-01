package org.example.quantum_operations;

import org.example.QuantumRegister;

public abstract class QuantumGate {
    public abstract void apply(QuantumRegister reg, int[] target_qubits, int[] args);

    public static QuantumGate unite(QuantumGate[] gates) {
        assert gates.length >= 1;

        return new QuantumGate() {
            @Override
            public void apply(QuantumRegister reg, int[] target_qubits, int[] args) {
                for (QuantumGate gate : gates) {
                    gate.apply(reg, target_qubits, args);

                    // Predecessor states composition TBD
                }
            }
        };
    }
}
