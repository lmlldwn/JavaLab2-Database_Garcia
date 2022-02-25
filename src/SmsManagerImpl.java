import java.util.ArrayList;
import java.util.List;

import static util.DateFormatterUtils.convertLocalDateTimeToString;

public class SmsManagerImpl implements SmsManager {
    private final DataAccess dataAccess = new DataAccess();

    @Override
    public void insertSms(Sms sms) {
        String query = "('" + sms.getTransactionID() + "','" + sms.getMsisdn() + "','" + sms.getRecipient() +
                "','" + sms.getSender() + "','" + sms.getShortCode() + "','" + convertLocalDateTimeToString(sms.getTimeStamp()) +
                "','" + sms.getStatus() + "','" + sms.getType() + "','" + sms.getMessage() + "')";

        dataAccess.insertData("SMS",query);
    }

    @Override
    public List<Sms> retrieveSmsByDate (String startDate, String endDate) {
        return dataAccess.getSmsByDate(startDate, endDate);
    }

    @Override
    public List<Sms> retrieveSmsByPromoCode(String promoCode) {
        String shortCode = "";
        List<Promo> promoList = dataAccess.getShortCodeByPromoCode(promoCode);
        for ( Promo promo : promoList ){
            shortCode = promo.getShortCode();
        }
        return dataAccess.getSmsByShortCode(shortCode);
    }

    @Override
    public List<Sms> retrieveSmsByMsisdn(String msisdn) {
        return dataAccess.getSmsByMsisdn(msisdn);
    }

    @Override
    public List<Sms> retrieveSmsSentBySystem() {
        return dataAccess.getSmsSentBySystem();
    }

    @Override
    public List<Sms> retrieveSmsReceivedBySystem() {
        return dataAccess.getSmsReceivedBySystem();
    }

    @Override
    public List<Sms> retrieveSmsByMsisdn(List<String> msisdns) {
        List<Sms> smsList = new ArrayList<>();
        for (String msisdn : msisdns){
            smsList.addAll(dataAccess.getSmsByMsisdn(msisdn));
        }
        return smsList;
    }

    @Override
    public String verifyRegistration(String msisdn, String shortCode) {
        return dataAccess.checkRegistration(msisdn, shortCode);
    }


}
