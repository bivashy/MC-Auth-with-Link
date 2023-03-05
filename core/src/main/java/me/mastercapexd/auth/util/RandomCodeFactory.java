package me.mastercapexd.auth.util;

import java.util.Random;

public class RandomCodeFactory {
    private static final char[] LETTERS = "abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray();
    private static final char[] NUMBERS = "1234567890".toCharArray();

    private RandomCodeFactory() {
    }

    public static int random(int min, int max) {
        if (min >= max)
            return -1;
        return new Random().nextInt(max - min + 1) + min;
    }

    public static char generateRandomCharacter() {
        boolean isLetter = (random(0, 1) == 1);

        if (isLetter) {
            return LETTERS[random(0, (LETTERS).length - 1)];
        }

        return NUMBERS[random(0, (NUMBERS).length - 1)];
    }

    public static char generateRandomCharacter(String characters) {
        char[] charactersArray = characters.toCharArray();
        return charactersArray[random(0, charactersArray.length - 1)];
    }

    public static String generateCode(int length) {
        if (length <= 0)
            return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++)
            stringBuilder.append(generateRandomCharacter());
        return stringBuilder.toString();
    }

    public static String generateCode(int length, String characters) {
        if (length <= 0)
            return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++)
            stringBuilder.append(generateRandomCharacter(characters));
        return stringBuilder.toString();
    }
}
