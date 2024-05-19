package org.tp1.Workshop;

public final class NIF {

    private final String nif;

    public NIF(String nif) {
        if (!validate(nif))
            throw new IllegalArgumentException("Invalid NIF");
        this.nif = nif;
    }

    public static boolean validate(String number) {
        final int max = 9;
        if (!number.matches("[0-9]+") || number.length() != max)
            return false;
        int checkSum = 0;
        for (int i = 0; i < max - 1; i++) {
            checkSum += (number.charAt(i) - '0') * (max - i);
        }
        int checkDigit = 11 - (checkSum % 11);
        if (checkDigit > 9)
            checkDigit = 0;
        return checkDigit == number.charAt(max - 1) - '0';
    }

    public String getNIF() {
        return nif;
    }

    public void print() {
        System.out.println("NIF [nif=" + nif + "]");
    }
}