package utask.commons.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DateUtil {
    private static final String ALPHABET_PATTERN = "[a-zA-Z]+";
    private static List<SimpleDateFormat> knownPatterns;
    private static Map<String, Integer> wordPatterns;

    private static final int DAYS_DIFFERENCE_TODAY = 0;
    private static final int DAYS_DIFFERENCE_TOMORROW = 1;

    private static final String[] SUPPORTED_DATE_FORMAT = {"ddMMyy", "dd MM", "ddMMyyyy"};
    private static final String[] WAYS_TO_SPELL_TODAY = {"today"};
    private static final String[] WAYS_TO_SPELL_TOMORROW = {"tomorrow", "tmr", "tmrw"};

    static {
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
            knownPatterns.add(new SimpleDateFormat(key));
        }
    }

    public static boolean isValidDate(String string) {
        if (parseStringToDate(string).isPresent()) {
            return true;
        }

        return false;
    }

    public static Optional<Date> parseStringToDate(String string) {
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
            Date date = new Date();
            offset += date.getDate();
            date.setDate(offset);
            return Optional.of(date);
        }

        return Optional.empty();
    }

    public static String getDeadlineFormat(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy");
        String formattedDate = formatter.format(date);
        return formattedDate;
    }
}
