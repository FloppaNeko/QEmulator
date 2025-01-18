package org.example;


import org.example.quantum_operations.CNOTgate;
import org.example.quantum_operations.HadamardGate;
import org.example.quantum_operations.Measurement;

public class Main {
    public static void main(String[] args) {
        QuantumRegister qr = new QuantumRegister(2);

        HadamardGate.apply(qr, 0);
        HadamardGate.apply(qr, 1);

        System.out.println(qr.toString());

        Measurement.apply(qr, 0);
        Measurement.apply(qr, 0);

        System.out.println(qr.toString());
    }
}