package org.example.quantum_operations;

import org.example.QuantumRegister;
import org.example.QuantumState;

import java.util.Arrays;


public class AddInt extends QuantumGate {
    public void apply(QuantumRegister reg, int val, int[] target_qubits) {
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

            int q = QuantumState.stateFromArrayToInt(target_qs_array);
            q = (q + val) % (1 << target_qubits.length);
            if (q < 0) {
                q += (1 << target_qubits.length);
            }

            target_qs_array = QuantumState.stateFromIntToArray(q, target_qubits.length);
            for (int i = 0; i < target_qubits.length; ++i) {
                qs_array[target_qubits[i]] = target_qs_array[i];
            }

            int new_qs = QuantumState.stateFromArrayToInt(qs_array);

            assert result_states[new_qs] == null;
            result_states[new_qs] = new QuantumState(
                    qs.length,
                    new_qs,
                    qs.coefficient,
                    qs.state
            );
        }

        reg.states = result_states;
    }

    @Override
    public void apply(QuantumRegister reg, int[] target_qubits, int[] args) {
        assert args.length == 1;
        apply(reg, args[0], target_qubits);
    }
}
