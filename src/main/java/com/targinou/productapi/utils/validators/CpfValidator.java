package com.targinou.productapi.utils.validators;

import java.util.function.Predicate;

public class CpfValidator {

    public static Predicate<String> validateCPF() {
        return cpf -> {
            cpf = cpf.replaceAll("[^0-9]", "");

            if (cpf.length() != 11) {
                return false;
            }

            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }
            int firstDigit = 11 - (sum % 11);
            if (firstDigit >= 10) {
                firstDigit = 0;
            }

            if (Character.getNumericValue(cpf.charAt(9)) != firstDigit) {
                return false;
            }

            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }
            int secondDigit = 11 - (sum % 11);
            if (secondDigit >= 10) {
                secondDigit = 0;
            }

            return Character.getNumericValue(cpf.charAt(10)) == secondDigit;
        };
    }
}

