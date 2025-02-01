package org.example.quantum_operations;

import org.example.QuantumRegister;
import org.example.QuantumState;

import java.util.Arrays;


public class CAddInt extends QuantumGate {
    public void apply(QuantumRegister reg, int val, int control_qubit, int[] target_qubits) {
        QuantumState[] result_states = new QuantumState[1 << reg.length];
        Arrays.fill(result_states, null);

        for (QuantumState qs: reg.states) {
            if (qs == null) {
                continue;
            }

            int[] qs_array = qs.stateFromIntToArray();

            int[] target_qs_array = new int[target_qubits.length];
            for (int i = 0; i < target_qubits.length; ++i) {
                target_qs_array[i] = qs_array[target_qubits[i]];
            }
            int target_cq = qs_array[control_qubit];

            int q = QuantumState.stateFromArrayToInt(target_qs_array);
            q = (target_cq == 1 ? (q + val) % (1 << target_qubits.length) : q);

            target_qs_array = QuantumState.stateFromIntToArray(q, target_qubits.length);
            for (int i = 0; i < target_qubits.length; ++i) {
                qs_array[target_qubits[i]] = target_qs_array[i];
            }

            QuantumState next_qs = new QuantumState(
                    qs.length,
                    QuantumState.stateFromArrayToInt(qs_array),
                    qs.coefficient,
                    qs.state
            );

            if (next_qs.isZero()) {
                continue;
            }

            if (result_states[next_qs.state] == null) {
                result_states[next_qs.state] = next_qs;
            } else {
                result_states[next_qs.state].addCoefficient(next_qs);
                result_states[next_qs.state].addPredecessor(qs.state);
            }
        }

        reg.states = result_states;
    }

    @Override
    public void apply(QuantumRegister reg, int[] target_qubits, int[] args) {
        apply(reg, args[0], target_qubits[0], Arrays.copyOfRange(target_qubits, 1, target_qubits.length));
    }
}
