import Utilities.Formatter;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class prepareSMS implements ManageSMS{
    private DataAccess dataAccess = new DataAccess();
    private Formatter f = new Formatter();
    final private static Logger logger = Logger.getLogger(prepareSMS.class.getName());

    @Override
    public void insertSMS(SMS sms){

        //System.out.println("sms inserted");
        String query = "('" + sms.getTransactionID() + "','" + sms.getMsisdn() + "','" + sms.getRecipient() +
                    "','" + sms.getSender() + "','" + sms.getShortCode() + "','" + f.convertLocalDateTimeToString(sms.getTimeStamp()) +
                    "','" + sms.getStatus() + "','" + sms.getType() + "')";
        //INSERT INTO `SMS`.`SMS` (`TransactionID`, `MSISDN`, `Recipient`, `Sender`, `ShortCode`, `TimeStamp`, `Status`) VALUES ('123ASDF', '09209567110', 'MIMZ', 'Lemuel Aldwin', '1234', '2022-02-23 23:28:45');

        logger.info(query);

        dataAccess.connect();
        dataAccess.insertData("SMS",query);
        dataAccess.disconnect();
    }

    @Override
    public void retrieveSMSByDate(String startDate, String endDate){
        String query = "TimeStamp BETWEEN '" + startDate + "' AND '" + endDate + "'";
        System.out.println(query);
        dataAccess.connect();
        ArrayList<SMS> smsArrayList = dataAccess.retrieveSMSData("SMS",query);
        dataAccess.disconnect();
        showSMS(smsArrayList);
    }

    @Override
    public void retrieveSMSByPromoCode(String promoCode){
        String query = "PromoCode='" + promoCode + "'";
        dataAccess.connect();
        ArrayList<SMS> smsArrayList = dataAccess.retrieveSMSData("SMS",query);
        dataAccess.disconnect();
        showSMS(smsArrayList);
    }

    @Override
    public void retrieveSMSByMSISDN(String msisdn){
        String query = "MSISDN='" + msisdn + "'";
        dataAccess.connect();
        ArrayList<SMS> smsArrayList = dataAccess.retrieveSMSData("SMS",query);
        dataAccess.disconnect();
        showSMS(smsArrayList);
    }

    @Override
    public void retrieveSMSSystemSent(){
        String query = "Sender='System'";
        dataAccess.connect();
        ArrayList<SMS> smsArrayList = dataAccess.retrieveSMSData("SMS",query);
        dataAccess.disconnect();
        showSMS(smsArrayList);
    }

    @Override
    public void retrieveSMSSystemReceived(){
        String query = "Recipient='System'";
        dataAccess.connect();
        ArrayList<SMS> smsArrayList = dataAccess.retrieveSMSData("SMS",query);
        dataAccess.disconnect();
        showSMS(smsArrayList);
    }

    @Override
    public void retrieveSMSByMSISDN(ArrayList<String> msisdns){

        String query = "";
        int count = 0;
        int limit = msisdns.size();
        for(String msisdn : msisdns){
            query = query + "MSISDN='" + msisdn + "'";
            count++;
            if ( count < limit ){
                query = query + " or ";
            }
        }
        System.out.println(query);
        dataAccess.connect();
        ArrayList<SMS> smsArrayList = dataAccess.retrieveSMSData("SMS",query);
        dataAccess.disconnect();
        showSMS(smsArrayList);
    }

    public void retrieveFailedTransactions(){
        String query = "Status='Failed'";
        dataAccess.connect();
        ArrayList<SMS> smsArrayList = dataAccess.retrieveSMSData("SMS",query);
        dataAccess.disconnect();
        showSMS(smsArrayList);
    }

    public void retrieveFailedTransactions(String type){
        String query = "Status='Failed' AND Type='" + type + "'";
        dataAccess.connect();
        ArrayList<SMS> smsArrayList = dataAccess.retrieveSMSData("SMS",query);
        dataAccess.disconnect();
        showSMS(smsArrayList);
    }

    public void retrieveSuccessfulTransactions(){
        String query = "Status='Success'";
        dataAccess.connect();
        ArrayList<SMS> smsArrayList = dataAccess.retrieveSMSData("SMS",query);
        dataAccess.disconnect();
        showSMS(smsArrayList);
    }

    public void retrieveSuccessfulTransactions(String type){
        String query = "Status='Success' AND Type='" + type + "'";
        dataAccess.connect();
        ArrayList<SMS> smsArrayList = dataAccess.retrieveSMSData("SMS",query);
        dataAccess.disconnect();
        showSMS(smsArrayList);
    }

    public void retrieveParticipants(){
        String query = "SELECT DISTINCT Sender FROM SMS";
        dataAccess.connect();
        ArrayList<String> names = dataAccess.retrieveSingleColumn(query);
        dataAccess.disconnect();
        logger.log(Level.INFO,"List of Participants");
        for ( String str : names){
            logger.log(Level.INFO,"Name: " + str);
        }
    }

    public void countSMSReceived(){
        //number of SMS received by the SYSTEM
        String query = "SELECT COUNT(Recipient) FROM SMS WHERE Recipient='SYSTEM'";
        dataAccess.connect();
        ArrayList<String> count = dataAccess.retrieveSingleColumn(query);
        dataAccess.disconnect();
        for ( String str : count){
            logger.log(Level.INFO,"Number of SMS Received by SYSTEM: " + str);
        }
    }

    public void countSMSSent(){
        //number of SMS sent by the SYSTEM
        String query = "SELECT COUNT(Sender) FROM SMS WHERE Sender='SYSTEM'";
        dataAccess.connect();
        ArrayList<String> names = dataAccess.retrieveSingleColumn(query);
        dataAccess.disconnect();
        for ( String str : names){
            logger.log(Level.INFO,"Number of SMS Sent by SYSTEM: " + str);
        }
    }

    public void showSMS(ArrayList<SMS> smsArrayList){

        for(SMS sms : smsArrayList){
            logger.log(Level.INFO,"SMS Object:     " + sms);
            logger.log(Level.INFO,"Transaction ID: " + sms.getTransactionID());
            logger.log(Level.INFO,"MSISDN:         " + sms.getMsisdn());
            logger.log(Level.INFO,"Recipient:      " + sms.getRecipient());
            logger.log(Level.INFO,"Sender:         " + sms.getSender());
            logger.log(Level.INFO,"Short Code:     " + sms.getShortCode());
            logger.log(Level.INFO,"Time Stamp:     " + sms.getTimeStamp());
            logger.log(Level.INFO,"Status:         " + sms.getStatus());
            logger.log(Level.INFO,"SMS Type:       " + sms.getType());
        }

    }

    public void showSummary(){
//        String startDate = "2022-01-01 01:02:01";
//        String endDate = "2022-03-01 01:00:00";
//
//        retrieveSMSByDate(startDate,endDate);
//        countSMSReceived();
//        countSMSSent();
    }
}