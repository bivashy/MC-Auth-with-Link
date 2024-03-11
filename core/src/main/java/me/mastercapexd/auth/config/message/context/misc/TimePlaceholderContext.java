package me.mastercapexd.auth.config.message.context.misc;

import java.util.List;

import com.bivashy.auth.api.config.PluginConfig;
import me.mastercapexd.auth.config.message.context.placeholder.MessagePlaceholderContext;
import me.mastercapexd.auth.config.message.context.placeholder.PlaceholderProvider;
import me.mastercapexd.auth.util.PluralizeUtil;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class TimePlaceholderContext extends MessagePlaceholderContext {
    protected final PluginConfig config;
//    protected final Long duration;

    public TimePlaceholderContext(PluginConfig config, Long durationInSeconds) {
        this.config = config;
//        this.duration = duration;

        DateTime end = new DateTime();
        end.getMillis();

        Duration duration = new Duration(durationInSeconds * 1000);

        long d = duration.getStandardDays();
        long h = duration.getStandardHours();
        long m = duration.getStandardMinutes();
        long s = duration.getStandardSeconds();

        String daysPluralized = PluralizeUtil.pluralize(d, processPluralsList(config.getDayPluralsStringList(), "день"));
        String hoursPluralized = PluralizeUtil.pluralize(h - d * 24, processPluralsList(config.getHourPluralsStringList(), "час"));
        String minutesPluralized = PluralizeUtil.pluralize(m - h * 60 - d * 24, processPluralsList(config.getMinutePluralsStringList(), "минута"));
        String secondsPluralized = PluralizeUtil.pluralize(s - m * 60 - h * 60 - d * 24, processPluralsList(config.getSecondPluralsStringList(), "секунда"));

        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendDays()
                .appendSuffix(" " + daysPluralized)
                .appendSeparator(", ", " и ")
                .appendHours()
                .appendSuffix(" " + hoursPluralized)
                .appendSeparator(", ", " и ")
                .appendMinutes()
                .appendSuffix(" " + minutesPluralized)
                .appendSeparator(", ", " и ")
                .appendSeconds()
                .appendSuffix(" " + secondsPluralized)
                .appendSeparator(", ", " и ")
                .toFormatter();

        Period period = duration.toPeriod();
        Period dayTimePeriod = period.normalizedStandard(PeriodType.dayTime());
        registerPlaceholderProvider(PlaceholderProvider.of(formatter.print(dayTimePeriod), "%time%"));
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
