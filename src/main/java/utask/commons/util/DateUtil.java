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

//@@author A0139996A
/*Helper functions for handling date parsing and creation of date object.*/
public class DateUtil {
    private static final int HOUR_START_INDEX = 0;
    private static final int HOUR_END_INDEX = 2;
    private static final int MINUTE_START_INDEX = 2;
    private static final int MINUTE_END_INDEX = 4;
    //The positon of the last character of the string + 1, is the same as the end of hhmm string
    private static final int VALID_HHMM_LENGTH = MINUTE_END_INDEX;
    private static final String DISPLAYED_FORMATTED_TIME = "HHmm";
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

    //Verifies that we know how to parse the string
    public static boolean isWordAValidDate(String string) {
        if (!string.isEmpty() && parseStringToDate(string).isPresent()) {
            return true;
        }

        return false;
    }

    //Parse the string and return as Optional<Date>
    public static Optional<Date> parseStringToDate(String string) {
        assert string != null && !string.isEmpty();

        if (string.matches(ALPHABET_PATTERN)) { //If it only contain alphabetical characters
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
    private static Optional<Date> parseStringByWord(String string) {
        if (wordPatterns.containsKey(string)) {
            int offset = wordPatterns.get(string);

            Date date = new Date();
            date = clearTimeInDate(date);
            date = addDayOfMonthToDate(date, offset);
            return Optional.of(date);
        }

        return Optional.empty();
    }

    public static Date addHHMMStringToDate(Date date, String hhmm) {
        assert hhmm != null && !hhmm.isEmpty() && hhmm.length() == VALID_HHMM_LENGTH;
        assert date != null;

        String hh = hhmm.substring(HOUR_START_INDEX, HOUR_END_INDEX);
        String mm = hhmm.substring(MINUTE_START_INDEX, MINUTE_END_INDEX);

        int hours = Integer.parseInt(hh);
        int minutes = Integer.parseInt(mm);
        date = addHoursToDate(date, hours);
        date = addMinutesToDate(date, minutes);

        return date;
    }

    //hours in HH 24hrs
    private static Date addHoursToDate(Date date, int hours) {
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    private static Date addMinutesToDate(Date date, int minutes) {
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    private static Date addDayOfMonthToDate(Date date, int dayOfMonth) {
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, dayOfMonth);
        return calendar.getTime();
    }

    public static String getFormattedTime(Date date) {
        assert date != null;
        SimpleDateFormat formatter = new SimpleDateFormat(DISPLAYED_FORMATTED_TIME);
        String formattedTime = formatter.format(date);
        return formattedTime;
    }

    private static Date clearTimeInDate(Date date) {
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
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

    //Composite two dates together, which is used in updating of timestamp
    public static Date getDateUsingTimeComponentAndDateComponent(Date time, Date date) {
        assert time != null && date != null;
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, time.getHours());
        calendar.set(Calendar.MINUTE, time.getMinutes());
        calendar.set(Calendar.SECOND, time.getSeconds());
        return calendar.getTime();
    }
}
