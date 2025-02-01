package org.example.quantum_operations;

import org.example.QuantumRegister;
import org.example.QuantumState;

import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

import java.util.Arrays;


public class Measurement extends QuantumGate {
    private static int getRandomState(IExpr[] probabilities) {
        IExpr x = F.eval(String.valueOf(Math.random()));

        IExpr f = F.ZZ(0);

        for (int q = 0; q < probabilities.length; ++q) {
            f = f.plus(probabilities[q]).eval();

            if (x.less(f).toString().equals("True")) {
                return q;
            }
        }

        throw new RuntimeException("Cannot get random state");
    }

    private static boolean fitsMeasurementResult(int[] state, int[] target_qubits, int[] measurement_result) {
        assert target_qubits.length == measurement_result.length;

        for (int i = 0; i < target_qubits.length; ++i) {
            if (measurement_result[i] != state[target_qubits[i]]) {
                return false;
            }
        }
        return true;
    }

    public void apply(QuantumRegister reg, int... target_qubits) {
        IExpr[] probabilities = new IExpr[1 << target_qubits.length];
        Arrays.fill(probabilities, F.ZZ(0));

        for (QuantumState qs : reg.states) {
            if (qs == null) {
                continue;
            }

            int[] qs_array = qs.stateFromIntToArray();
            int[] target_qs_array = new int[target_qubits.length];
            for (int i = 0; i < target_qubits.length; ++i) {
                target_qs_array[i] = qs_array[target_qubits[i]];
            }

            int target_q = QuantumState.stateFromArrayToInt(target_qs_array);

            probabilities[target_q] = F.Plus(probabilities[target_q], F.Power(F.Abs(qs.coefficient), 2)).eval();
        }

        int measured_q = getRandomState(probabilities);
        int[] measured_q_array = QuantumState.stateFromIntToArray(measured_q, target_qubits.length);

        for (int i = 0; i < target_qubits.length; ++i) {
            System.out.println("Qubit " + target_qubits[i] + " measured : " + measured_q_array[i]);
        }

        IExpr norm_cf = probabilities[measured_q].sqrt().eval();

        for (int qi = 0; qi < reg.states.length; ++qi) {
            QuantumState prev_qs = reg.states[qi];

            if (prev_qs == null) {
                continue;
            }

            if (fitsMeasurementResult(prev_qs.stateFromIntToArray(), target_qubits, measured_q_array)) {
                reg.states[qi] = new QuantumState(
                        prev_qs.length,
                        prev_qs.state,
                        prev_qs.coefficient.divide(norm_cf).eval(),
                        prev_qs.state
                );
            } else {
                reg.states[qi] = null;
            }
        }
    }

    @Override
    public void apply(QuantumRegister reg, int[] target_qubits, int[] args) {
        apply(reg, target_qubits);
    }
}
