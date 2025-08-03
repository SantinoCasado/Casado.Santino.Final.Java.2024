package Enums;

public enum MedidasConcentracion {
    CERO_PORCIENTO("0%"),
    CINCO_PORCIENTO("5%"),
    DIEZ_PORCIENTO("10%"),
    QUINCE_PORCIENTO("15%"),
    VEINTE_PORCIENTO("20%"),
    VEINTICINCO_PORCIENTO("25%"),
    TREINTA_PORCIENTO("30%"),
    TREINTA_Y_CINCO_PORCIENTO("35%"),
    CUARENTA_PORCIENTO("40%"),
    CUARENTA_Y_CINCO_PORCIENTO("45%"),
    CINCUENTA_PORCIENTO("50%"),
    CINCUENTA_Y_CINCO_PORCIENTO("55%"),
    SESENTA_PORCIENTO("60%"),
    SESENTA_Y_CINCO_PORCIENTO("65%"),
    SETENTA_PORCIENTO("70%"),
    SETENTA_Y_CINCO_PORCIENTO("75%"),
    OCHENTA_PORCIENTO("80%"),
    OCHENTA_Y_CINCO_PORCIENTO("85%"),
    NOVENTA_PORCIENTO("90%"),
    NOVENTA_Y_CINCO_PORCIENTO("95%"),
    CIEN_PORCIENTO("100%");

    private final String valor;

    MedidasConcentracion(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}