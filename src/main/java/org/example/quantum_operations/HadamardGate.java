package org.example.quantum_operations;

import org.example.QuantumRegister;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

public class HadamardGate extends MatrixGate {
    private final static int gate_length = 1;
    private final static IExpr matrix = F.eval("(1/Sqrt(2) * {{1, 1}, {1, -1}})");

    @Override
    public void apply(QuantumRegister reg, int[] target_qubits, int[] args) {
        apply(gate_length, matrix, reg, target_qubits);
    }

    public void apply(QuantumRegister reg, int target_qubit) {
        apply(reg, new int[]{target_qubit}, new int[]{});
    }
}
