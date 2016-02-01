package com.farodrigues.numerosdeemergencia;

import com.farodrigues.numerosdeemergencia.model.Contact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by frodr30 on 1/22/16.
 */
public class BrazilianEmergencyNumbers {
    public static List<Contact> getNumbers() {
        return Arrays.asList(new Contact("190 - Policia Militar", "190"), new Contact("193 - Bombeiros", "193"), new Contact("192 - Ambulância", "192"));
    }
}
