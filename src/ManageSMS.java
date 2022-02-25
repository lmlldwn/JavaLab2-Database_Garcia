import java.util.ArrayList;
import java.util.Date;

//lab 03.1.2 - classes
public interface ManageSMS {

    void insertSMS(SMS sms);
    void retrieveSMSByDate(String startDate, String endDate);
    void retrieveSMSByPromoCode(String promoCode);
    void retrieveSMSByMSISDN(String msisdn);
    void retrieveSMSSystemSent();
    void retrieveSMSSystemReceived();
    void retrieveSMSByMSISDN(ArrayList<String> msisdns);
}
