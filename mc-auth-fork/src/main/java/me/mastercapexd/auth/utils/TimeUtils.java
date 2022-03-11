package me.mastercapexd.auth.utils;

import java.util.Calendar;

public final class TimeUtils {

	public static long parseTime(String timeString) {
		Calendar calendar = Calendar.getInstance();
		for (String rawText : timeString.split("\\s+")) {
			String formatted = rawText.toLowerCase().replaceAll("\\d", "");
			if (formatted.equals("d"))
				calendar.add(Calendar.DATE, Integer.parseInt(rawText.replace("d", "")));
			if (formatted.equals("h"))
				calendar.add(Calendar.HOUR, Integer.parseInt(rawText.replace("h", "")));
			if (formatted.equals("ms"))
				calendar.add(Calendar.MILLISECOND, Integer.parseInt(rawText.replace("ms", "")));
			if (formatted.equals("m"))
				calendar.add(Calendar.MINUTE, Integer.parseInt(rawText.replace("m", "")));
			if (formatted.equals("s"))
				calendar.add(Calendar.SECOND, Integer.parseInt(rawText.replace("s", "")));
		}
		return calendar.getTimeInMillis() - System.currentTimeMillis();
	}
}