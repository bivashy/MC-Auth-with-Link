
package me.mastercapexd.auth.utils;

import java.util.Random;

public class RandomCodeFactory {
	private static final String letters = "abcdefghijklmnopqrstuvwxyz".toUpperCase();
	private static final String numbers = "1234567890";
	public static int random(int min, int max) {
		if (min >= max) {
			return -1;
		}
		Random r = new Random();
		return r.nextInt(max - min + 1) + min;
	}

	public static char generateRandomCharacter() {
		boolean isLetter = (random(0, 1) == 1);

		if (isLetter) {
			return letters.toCharArray()[random(0, (letters.toCharArray()).length - 1)];
		}

		return numbers.toCharArray()[random(0, (numbers.toCharArray()).length - 1)];
	}

	public static String generateCode(int length) {
		if(length<=0) return "";
		String s = "";
		for (int i = 0; i < length; i++) {
			s = s + generateRandomCharacter();
		}
		return s;
	}
}
