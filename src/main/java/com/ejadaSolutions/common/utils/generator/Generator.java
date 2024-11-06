package com.ejadaSolutions.common.utils.generator;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Generator {
    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String NumbersString = "0123456789";
    private static final String NameString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";


    public static String generateRandomString() {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            int index = RANDOM.nextInt(ALPHANUMERIC_STRING.length());
            sb.append(ALPHANUMERIC_STRING.charAt(index));
        }
        return sb.toString();
    }

    public static String generateRandomName() {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            int index = RANDOM.nextInt(NameString.length());
            sb.append(NameString.charAt(index));
        }
        return sb.toString();
    }

    public static String generateRandomNumbers() {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 2; i++) {
            int index = RANDOM.nextInt(NumbersString.length());
            sb.append(NumbersString.charAt(index));
        }
        return sb.toString();
    }

    public static String generateRandomPhoneNumber() {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 12; i++) {
            int index = RANDOM.nextInt(NumbersString.length());
            sb.append(NumbersString.charAt(index));
        }
        return sb.toString();
    }

    public static String generateRandomEmail() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int index = RANDOM.nextInt(ALPHANUMERIC_STRING.length());
            sb.append(ALPHANUMERIC_STRING.charAt(index));
        }
        sb.append("@example.com");
        return sb.toString();
    }


    public static String getDatePlusDays(int days) {
        LocalDate datePlusDays = LocalDate.now().plusDays(days);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return datePlusDays.format(formatter);
    }
}