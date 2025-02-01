package org.example.quantum_operations;

import org.example.QuantumRegister;
import org.example.QuantumState;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

import java.util.Arrays;

public abstract class MatrixGate extends QuantumGate {
    protected void apply(int gate_length, IExpr matrix, QuantumRegister reg, int[] target_qubits) {
        assert target_qubits.length == gate_length;

        QuantumState[] result_states = new QuantumState[1 << reg.length];
        Arrays.fill(result_states, null);

        for (QuantumState prev_qs : reg.states) {
            if (prev_qs == null) {
                continue;
            }

            int[] prev_qs_array = prev_qs.stateFromIntToArray();

            int[] prev_target_qs_array = new int[gate_length];
            for (int i = 0; i < gate_length; ++i) {
                prev_target_qs_array[i] = prev_qs_array[target_qubits[i]];
            }

            int q_prev = QuantumState.stateFromArrayToInt(prev_target_qs_array);

            for (int q_next = 0; q_next < (1 << gate_length); ++q_next) {
                IExpr cf;
                cf = F.Part(matrix, F.ZZ(q_prev+1), F.ZZ(q_next+1)).eval();
                cf = F.Times(cf, prev_qs.coefficient);

                int[] next_target_qs_array = QuantumState.stateFromIntToArray(q_next, gate_length);

                int[] next_qs_array = prev_qs.stateFromIntToArray();

                for (int i = 0; i < gate_length; ++i) {
                    next_qs_array[target_qubits[i]] = next_target_qs_array[i];
                }

                QuantumState next_qs = new QuantumState(
                        prev_qs.length,
                        QuantumState.stateFromArrayToInt(next_qs_array),
                        cf,
                        prev_qs.state
                );

                if (next_qs.isZero()) {
                    continue;
                }

                if (result_states[next_qs.state] == null) {
                    result_states[next_qs.state] = next_qs;
                } else {
                    result_states[next_qs.state].addCoefficient(next_qs);
                    result_states[next_qs.state].addPredecessor(prev_qs.state);
                }
            }
        }

        reg.states = result_states;

        // reg.print();
    }


}
