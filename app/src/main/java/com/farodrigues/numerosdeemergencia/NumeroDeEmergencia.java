package com.farodrigues.numerosdeemergencia;

public class NumeroDeEmergencia {

    private final String nome;

    private final String numero;

    public NumeroDeEmergencia(String nome, String numero) {
        this.nome = nome;
        this.numero = numero;
    }

    public String getNome() {
        return nome;
    }

    public String getNumero() {
        return numero;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof NumeroDeEmergencia) {
            return ((NumeroDeEmergencia) o).getNome().equals(nome);
        }
        return false;
    }
}
