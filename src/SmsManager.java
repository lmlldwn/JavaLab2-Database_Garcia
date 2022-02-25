import java.util.List;

public interface SmsManager {

    void insertSms(Sms sms);
    List<Sms> retrieveSmsByDate(String startDate, String endDate);
    List<Sms> retrieveSmsByPromoCode(String promoCode);
    List<Sms> retrieveSmsByMsisdn(String msisdn);
    List<Sms> retrieveSmsSentBySystem();
    List<Sms> retrieveSmsReceivedBySystem();
    List<Sms> retrieveSmsByMsisdn(List<String> msisdns);

    String verifyRegistration(String shortCode, String msisdn);


    //add registration to this interface
    //sms checker?
}
