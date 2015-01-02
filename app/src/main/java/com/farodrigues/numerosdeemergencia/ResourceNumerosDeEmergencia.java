package com.farodrigues.numerosdeemergencia;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by felipe on 02/01/15.
 */
public class ResourceNumerosDeEmergencia {

    final List<NumeroDeEmergencia> numerosDeEmergenciaBrasileiros;

    public ResourceNumerosDeEmergencia() {
        numerosDeEmergenciaBrasileiros = new ArrayList<>();
        numerosDeEmergenciaBrasileiros.add(new NumeroDeEmergencia("Policia Militar", "190"));
        numerosDeEmergenciaBrasileiros.add(new NumeroDeEmergencia("Bombeiros", "193"));
        numerosDeEmergenciaBrasileiros.add(new NumeroDeEmergencia("Ambulância", "192"));
    }

    public List<NumeroDeEmergencia> getNumerosDeEmergenciaBrasileiros() {
        return numerosDeEmergenciaBrasileiros;
    }

}