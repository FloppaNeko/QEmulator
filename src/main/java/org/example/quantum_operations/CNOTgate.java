package org.example.quantum_operations;

import org.example.QuantumRegister;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

public class CNOTgate extends MatrixGate {
    private static final int gate_length = 2;
    private static final IExpr matrix = F.eval("{{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 0, 1}, {0, 0, 1, 0}}");

    @Override
    public void apply(QuantumRegister reg, int[] target_qubits, int[] args) {
        apply(gate_length, matrix, reg, target_qubits);
    }
}
