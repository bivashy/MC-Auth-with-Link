package com.bivashy.auth.api.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TimeUtils {
    private TimeUtils() {
    }

    private static final String YEAR_PATTERN = createTimeKeyPattern("y|г");
    private static final String MONTH_PATTERN = createTimeKeyPattern("mo|мес");
    private static final String WEEK_PATTERN = createTimeKeyPattern("w|н");
    private static final String DAY_PATTERN = createTimeKeyPattern("d|д");
    private static final String HOUR_PATTERN = createTimeKeyPattern("h|ч");
    private static final String MINUTE_PATTERN = createTimeKeyPattern("m|м");
    private static final String SECOND_PATTERN = createTimeKeyPattern("s|с|");
    private static final String MILLISECOND_PATTERN = createTimeKeyPattern("ms|мс");

    private static final Pattern TIME_PATTERN =
            Pattern.compile(YEAR_PATTERN + MONTH_PATTERN + WEEK_PATTERN + DAY_PATTERN + HOUR_PATTERN + MINUTE_PATTERN + SECOND_PATTERN + MILLISECOND_PATTERN,
                    Pattern.CASE_INSENSITIVE);
    private static final int[] CALENDAR_TYPE = {Calendar.YEAR, Calendar.MONTH, Calendar.WEEK_OF_YEAR, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY,
            Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND};

    public static boolean canParseDate(String input) {
        return TIME_PATTERN.matcher(input).find();
    }

    public static long parseDuration(String input) {
        if (input == null)
            return 0;
        Matcher dateMatcher = TIME_PATTERN.matcher(input);
        long currentTimeMillis = System.currentTimeMillis();
        Calendar calendar = new GregorianCalendar();
        while(dateMatcher.find())
            for (int i = 0; i < CALENDAR_TYPE.length; i++)
                calendar.add(CALENDAR_TYPE[i], getMatcherGroupInt(dateMatcher, i + 1));
        return calendar.getTimeInMillis() - currentTimeMillis;
    }

    private static String createTimeKeyPattern(String timeKeyRegex) {
        return "(?:([0-9]+)\\s*(?:" + timeKeyRegex + ")[a-z]*[,\\s]*)?";
    }

    private static int getMatcherGroupInt(Matcher matcher, int groupIndex) {
        String group = matcher.group(groupIndex);
        if (group != null && !group.isEmpty())
            return Integer.parseInt(matcher.group(groupIndex));
        return 0;
    }
}