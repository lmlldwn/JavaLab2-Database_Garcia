package util;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatterUtils {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String convertLocalDateTimeToString(LocalDateTime date){
        return formatter.format(date);
    }

    public static LocalDateTime convertStringToLocalDateTime(String date){
        return LocalDateTime.parse(date, formatter);
    }

}
