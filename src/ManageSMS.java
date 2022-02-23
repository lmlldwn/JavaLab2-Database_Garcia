import java.util.ArrayList;
import java.util.Date;

//lab 03.1.2 - classes
public interface ManageSMS {
    void insertSMS();
    String retrieveSMSByDate(Date startDate, Date endDate);
    String retrieveSMSByPromoCode(String promoCode);
    String retrieveSMSByMSISDN(long msisdn);
    String retrieveSMSSystemSent();
    String retrieveSMSSystemReceived();
    String retrieveSMSBroadcast(ArrayList<String> msisdns);
}
