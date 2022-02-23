package Utilities;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Formatter {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String convertLocalDateTimeToString(LocalDateTime date){
        return formatter.format(date);
    }

    public LocalDateTime convertStringToLocalDateTime(String date){
        return LocalDateTime.parse(date, formatter);
    }

}
