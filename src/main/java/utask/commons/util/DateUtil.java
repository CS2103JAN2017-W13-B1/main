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
            
    static {
        knownPatterns = new ArrayList<SimpleDateFormat>();
        knownPatterns.add(new SimpleDateFormat("ddMMyy"));
        knownPatterns.add(new SimpleDateFormat("ddMMyyyy"));
        knownPatterns.add(new SimpleDateFormat("dd MM yyyy"));
        
        wordPatterns = new HashMap<String, Integer>();
        wordPatterns.put("today", 0);
        wordPatterns.put("tomorrow", 1);
        wordPatterns.put("tmr", 1);
        wordPatterns.put("tmrw", 1);
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
                    //Continue checking 
                }
            }
        }
        
        return Optional.empty();
    }
    
    public static Optional<Date> parseStringByWord(String string) {
        if (wordPatterns.containsKey(string)){
            int offset = wordPatterns.get(string);
            Date date = new Date();
            offset += date.getDate();
            date.setDate(offset);
            return Optional.of(date);
        }
        
        return Optional.empty();
    }
    
    public static String getDeadlineFormat(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("DDMMYY");
        String formattedDate = formatter.format(date);
        return formattedDate;
    }
}
