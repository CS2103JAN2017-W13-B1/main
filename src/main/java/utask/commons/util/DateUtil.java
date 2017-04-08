package utask.commons.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DateUtil {
    private static final String ALPHABET_PATTERN = "[a-zA-Z ]+";
    private static final List<SimpleDateFormat> knownPatterns;
    private static final Map<String, Integer> wordPatterns;

    private static final int DAYS_DIFFERENCE_TODAY = 0;
    private static final int DAYS_DIFFERENCE_TOMORROW = 1;

    private static final String[] SUPPORTED_DATE_FORMAT = {"ddMMyy"};
    private static final String[] WAYS_TO_SPELL_TODAY = {"today"};
    private static final String[] WAYS_TO_SPELL_TOMORROW = {"tomorrow", "tmr", "tmrw", "next day"};

    private static final Calendar calendar;

    static {
        calendar = Calendar.getInstance();
        knownPatterns = new ArrayList<SimpleDateFormat>();
        addSupportedDateFormatToKnownPattern(SUPPORTED_DATE_FORMAT);

        wordPatterns = new HashMap<String, Integer>();
        addWordsToWordPattern(WAYS_TO_SPELL_TODAY, DAYS_DIFFERENCE_TODAY);
        addWordsToWordPattern(WAYS_TO_SPELL_TOMORROW, DAYS_DIFFERENCE_TOMORROW);
    }

    private static void addWordsToWordPattern(String[] waysArray, int value) {
        for (int i = 0; i < waysArray.length; i++) {
            String key = waysArray[i];
            wordPatterns.put(key, value);
        }
    }

    private static void addSupportedDateFormatToKnownPattern(String[] dates) {
        for (int i = 0; i < dates.length; i++) {
            String key = dates[i];
            SimpleDateFormat dateFormat = new SimpleDateFormat(key);
            dateFormat.setLenient(false);
            knownPatterns.add(dateFormat);
        }
    }

    public static boolean isWordAValidDate(String string) {
        if (!string.isEmpty() && parseStringToDate(string).isPresent()) {
            return true;
        }
        return false;
    }

    public static Optional<Date> parseStringToDate(String string) {
        assert string != null && !string.isEmpty();
        if (string.matches(ALPHABET_PATTERN)) {
            return parseStringByWord(string);
        } else {
            for (SimpleDateFormat pattern : knownPatterns) {
                try {
                    Date date = pattern.parse(string);
                    return Optional.of(date);
                } catch (ParseException pe) {
                    //Continue to check for other patterns
                }
            }
        }
        return Optional.empty();
    }
    public static Optional<Date> parseStringByWord(String string) {
        if (wordPatterns.containsKey(string)) {
            int offset = wordPatterns.get(string);

            //TODO
            Date date = new Date();
            //As date() gets the current time, this ensure the time starts from 00
            date = clearTimeInDate(date);
            date = addDayOfMonthToDate(date, offset);
            return Optional.of(date);
        }

        return Optional.empty();
    }

//    public static String getDeadlineFormat(Date date) {
//        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy");
//        String formattedDate = formatter.format(date);
//        return formattedDate;
//    }

    public static Date addHHMMStringToDate(Date date, String hhmm) {
        assert hhmm != null && !hhmm.isEmpty() && hhmm.length() == 4;

        String hh = hhmm.substring(0, 2);
        String mm = hhmm.substring(2, 4);

        System.out.println(mm);
        int hours = Integer.parseInt(hh);
        int minutes = Integer.parseInt(mm);
        System.out.println(minutes);
        date = addHoursToDate(date, hours);
        date = addMinutesToDate(date, minutes);

        System.out.println(date);

        return date;
    }

    //hours in HH 24hrs
    public static Date addHoursToDate(Date date, int hours) {
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public static Date addMinutesToDate(Date date, int minutes) {
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public static Date addDayOfMonthToDate(Date date, int dayOfMonth) {
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, dayOfMonth);
        return calendar.getTime();
    }

    public static Date clearTimeInDate(Date date) {
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static String getFormattedTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String formattedTime = formatter.format(date);
        return formattedTime;
    }

    public static Date getEmptyDate() {
        Date now = new Date();
        now = clearTimeInDate(now);
        calendar.setTime(now);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.YEAR, 0);
        return calendar.getTime();
    }

    public static Date getDateUsingTimeComponentAndDateComponent(Date time, Date date) {
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, time.getHours());
        calendar.set(Calendar.MINUTE, time.getMinutes());
        calendar.set(Calendar.SECOND, time.getSeconds());
        return calendar.getTime();
    }
}
