package me.mastercapexd.auth.util;

import java.util.List;

public class PluralizeUtil {
    public static String pluralize(long num, List<String> variants) {
        if (variants.size() < 2)
            return "";
        if (variants.size() < 3)
            variants.add(variants.get(1));
        return pluralize(num, variants.get(0), variants.get(1), variants.get(2));
    }

    public static String pluralize(long num, String firstVariant, String secondVariant) {
        return PluralizeUtil.pluralize(num, firstVariant, secondVariant, secondVariant);
    }

    public static String pluralize(long num, String firstVariant, String secondVariant, String thirdVariant) {

        long preLastDigit = num % 100 / 10;

        if (preLastDigit == 1) {
            return thirdVariant;
        }

        switch ((int) (num % 10)) {
            case 1:
                return firstVariant;
            case 2:
            case 3:
            case 4:
                return secondVariant;
            default:
                return thirdVariant;
        }

    }
}
