package org.example.quantum_operations;

import org.example.QuantumRegister;
import org.example.QuantumState;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

import java.util.Arrays;

public class QuantumGates {


    public static void hadamard(QuantumRegister reg, int... target_qubits) {
        IExpr matrix = F.eval("(1/Sqrt(2) * {{1, 1}, {1, -1}})");
        int gate_length = 1;

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
//                IExpr cf = F.Times(qs.coefficient, F.Part(matrix, F.ZZ(q_prev+1), F.ZZ(i+1)).eval()).eval();
//                System.out.println("|%s>".formatted(String.valueOf(i)) + " = " + cf.toString());

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
                        q_prev
                );

                if (result_states[next_qs.state] == null) {
                    result_states[next_qs.state] = next_qs;
                } else {
                    result_states[next_qs.state].addCoefficient(next_qs);
                    result_states[next_qs.state].addPredecessor(q_prev);
                }
            }
        }

        reg.states = result_states;
    }
}
