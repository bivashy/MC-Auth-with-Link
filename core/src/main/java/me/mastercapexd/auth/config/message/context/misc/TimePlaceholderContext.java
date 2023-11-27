package me.mastercapexd.auth.config.message.context.misc;

import java.util.List;

import com.bivashy.auth.api.config.PluginConfig;
import me.mastercapexd.auth.config.message.context.placeholder.MessagePlaceholderContext;
import me.mastercapexd.auth.config.message.context.placeholder.PlaceholderProvider;

public class TimePlaceholderContext extends MessagePlaceholderContext {
    protected final PluginConfig config;
    protected final Long time;

    public TimePlaceholderContext(PluginConfig config, Long time) {
        this.config = config;
        this.time = time;
        registerPlaceholderProvider(PlaceholderProvider.of(getHumanReadableTime(), "%time%"));
    }

    private String getHumanReadableTime() {
        String res = "";
        long days = time / 86400;
        long hours = time % 86400 / 3600;
        long minutes = time % 86400 % 3600 / 60;
        long seconds = time % 86400 % 3600 % 60;

        List<String> dayPlurals = processPluralsList(config.getDayPluralsStringList(), "день");
        List<String> hourPlurals = processPluralsList(config.getHourPluralsStringList(), "час");
        List<String> minutePlurals = processPluralsList(config.getMinutePluralsStringList(), "минута");
        List<String> secondPlurals = processPluralsList(config.getSecondPluralsStringList(), "секунда");

        if (days != 0)
            res += pluralize(days, dayPlurals) + ((hours != 0) ? (minutes != 0 ? ", " : " и ") : "");
        if (hours != 0)
            res += pluralize(hours, hourPlurals) + ((minutes != 0) ? (seconds != 0 ? ", " : " и ") : "");
        if (minutes != 0)
            res += pluralize(minutes, minutePlurals) + ((seconds != 0) ? " и " : "");
        if (seconds != 0)
            res += pluralize(seconds, secondPlurals);

        return res;
    }

    private String pluralize(long num, List<String> variants) {
        if (variants.size() < 2)
            return "";
        if (variants.size() < 3)
            variants.add(variants.get(1));
        return pluralize(num, variants.get(0), variants.get(1), variants.get(2));
    }

    private String pluralize(long num, String firstVariant, String secondVariant) {
        return pluralize(num, firstVariant, secondVariant, secondVariant);
    }

    private String pluralize(long num, String firstVariant, String secondVariant, String thirdVariant) {

        long preLastDigit = num % 100 / 10;

        if (preLastDigit == 1) {
            return num + " " + thirdVariant;
        }

        switch ((int) (num % 10)) {
            case 1:
                return num + " " + firstVariant;
            case 2:
            case 3:
            case 4:
                return num + " " + secondVariant;
            default:
                return num + " " + thirdVariant;
        }

    }

    private List<String> processPluralsList(List<String> plurals, String defaultValue) {
        if (plurals.isEmpty())
            plurals.add(defaultValue);
        if (plurals.size() < 2)
            plurals.add(plurals.get(0));
        if (plurals.size() < 3)
            plurals.add(plurals.get(1));
        return plurals;
    }
}
