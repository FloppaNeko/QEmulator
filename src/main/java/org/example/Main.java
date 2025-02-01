package org.example;


import org.example.quantum_operations.*;

public class Main {
    public static void main(String[] args) {
        demo2();
    }

    public static void qtest1() {
        QuantumRegister qr = new QuantumRegister(2);

        QuantumGates.Hadamard.apply(qr, 0);
        QuantumGates.Hadamard.apply(qr, 1);

        System.out.println(qr.toString());

        QuantumGates.Measure.apply(qr, 0);
        QuantumGates.Measure.apply(qr, 0);

        System.out.println(qr.toString());
    }

    public static void demo2() {
        QuantumRegister qr = new QuantumRegister(3);
        System.out.println(qr);

        for (int i = 0; i < 3; ++i) {
            QuantumGates.Hadamard.apply(qr, 0);
            qr.print();
            QuantumGates.Add.apply(qr, -1, new int[]{1, 2});
            qr.print();
            QuantumGates.CAdd.apply(qr, 2, 0, new int[]{1, 2});
            qr.print();

            System.out.println(qr);
        }
    }
}