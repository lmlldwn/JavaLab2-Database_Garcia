import java.util.ArrayList;
import java.util.Date;

public class prepareSMS implements ManageSMS{

    @Override
    public void insertSMS(){
        System.out.println("sms inserted");
    }

    @Override
    public String retrieveSMSByDate(Date startDate, Date endDate){
        return null;
    }

    @Override
    public String retrieveSMSByPromoCode(String promoCode){
        return null;
    }

    @Override
    public String retrieveSMSByMSISDN(long msisdn){
        return null;
    }

    @Override
    public String retrieveSMSSystemSent(){
        return null;
    }

    @Override
    public String retrieveSMSSystemReceived(){
        return null;
    }

    @Override
    public String retrieveSMSBroadcast(ArrayList<String> msisdns){
        return null;
    }
}