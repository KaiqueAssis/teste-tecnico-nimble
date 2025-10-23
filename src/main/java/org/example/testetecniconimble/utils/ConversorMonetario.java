package org.example.testetecniconimble.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class ConversorMonetario {

    private static final int CENTAVOS_POR_REAL = 100;
    private static final int ESCALA = 2; // Duas casas decimais


    public static BigDecimal centavosParaReais(BigInteger valorEmCentavos) {
        if (valorEmCentavos == null) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(valorEmCentavos)
                .divide(
                        BigDecimal.valueOf(CENTAVOS_POR_REAL),
                        ESCALA,
                        RoundingMode.HALF_EVEN
                );
    }


    public static BigInteger reaisParaCentavos(BigDecimal valorEmReais) {
        if (valorEmReais == null) {
            return BigInteger.ZERO;
        }


        return valorEmReais
                .setScale(ESCALA, RoundingMode.HALF_EVEN)
                .multiply(BigDecimal.valueOf(CENTAVOS_POR_REAL))
                .toBigInteger();
    }
}
