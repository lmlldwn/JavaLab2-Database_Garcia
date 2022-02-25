import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static util.DateFormatterUtils.convertLocalDateTimeToString;


public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final SmsManager smsManager = new SmsManagerImpl();
    private static final PromoManager promoManager = new PromoManagerImpl();


    public static void main(String[] args) {

        String message = "";

        while ( !"quit".equalsIgnoreCase(message)) {

            HashMap<String, String> smsHashMap = init();
            message = smsHashMap.get("Message");

            Sms sms = smsChecker(smsHashMap);
            smsManager.insertSms(sms);

        }
      //init();

//      sampleSMS();
//      sms.showSummary();
//
//        retrieve by date
//        String startDate = "2022-01-01 01:02:01";
//        String endDate = "2022-03-01 01:00:00";
//        prep.retrieveSMSByDate(startDate,endDate);

//        retrieve sms by msisdn
//        String msisdn = "09209567110";
//        prep.retrieveSmsByMsisdnMsisdn(msisdn);
//
//
//        retrieve sms received by system
//         prep.retrieveSmsReceivedBySystem();
//
//        retrieve sms sent by system
//        prep.retrieveSmsSentBySystem();
//
//        ArrayList<String> nums = new ArrayList<>();
//        nums.add("09209561111");
//        nums.add("09111111112");
//        nums.add("09123456789");
//        prep.retrieveSmsByMsisdn(nums);
//
//        report.retrieveFailedTransactions();
//        report.retrieveFailedTransactions(SmsTypeEnum.PROMO_AVAIL.toString());
//        report.retrieveSuccessfulTransactions();
//        report.retrieveSuccessfulTransactions(SMSType.Registration.toString());
//        report.retrieveParticipants();
//        report.countSMSReceived();
//        report.countSMSSent();
    }

    public static HashMap<String, String> init(){
        String sender = "";
        String recipient = "System";
        String status;
        String transactionID;
        SmsTypeEnum type;
        Scanner scan = new Scanner(System.in);

        LOGGER.log(Level.INFO, "Enter Mobile Number: ");
        String msisdn = scan.nextLine();

        LOGGER.log(Level.INFO, "Enter Message: ");
        String message = scan.nextLine();

        LOGGER.log(Level.INFO, "Enter Short Code: ");
        String shortCode = scan.nextLine();

        HashMap<String, String> data = new HashMap<>();
        data.put("MSISDN", msisdn);
        data.put("Message", message);
        data.put("Short Code", shortCode);

        return data;
//        if ( message.equals("REGISTER") ) {
//            sender = registerName(msisdn, shortCode);
//        }
//

//        status = smsChecker(data);
//        type = SmsTypeEnum.PROMO_AVAIL;
//
//
//        transactionID= UUID.randomUUID().toString();
//        LocalDateTime timeStamp = LocalDateTime.now();
//
//
//        if ( sender.equals("")  || msisdn.equals("") ){
//            status = "Failed";
//        }
//
//        Sms sms = new Sms(msisdn,sender,recipient,shortCode,transactionID,timeStamp,status,type);
//        smsManager.insertSms(sms);
    }

    public static void promptRegisterName (String msisdn, String shortCode) {
        String registerMessage = "To complete the promo registration, please send 'Lastname, Firstname' to " + shortCode;
        generateSystemMessage(msisdn, shortCode);
        LOGGER.log(Level.INFO, registerMessage);
    }

    public static void generateSystemMessage(String msisdn, String shortCode){
        LocalDateTime timeStamp = LocalDateTime.now();
        String transactionID = UUID.randomUUID().toString();
        Sms systemSms = new Sms(msisdn, msisdn, "SYSTEM", shortCode, transactionID, timeStamp, "Success", SmsTypeEnum.SYSTEM_GENERATED);
        smsManager.insertSms(systemSms);
    }

    public static Sms smsChecker(HashMap<String,String> smsValues){

        String sender = "";
        String recipient = "SYSTEM";
        String transactionID = UUID.randomUUID().toString();
        LocalDateTime timeStamp = LocalDateTime.now();
        String status = "Failed";
        SmsTypeEnum type = SmsTypeEnum.SYSTEM_GENERATED;

        boolean proceed = true;
        String shortCode = smsValues.get("Short Code");
        String message = smsValues.get("Message");
        String msisdn = smsValues.get("MSISDN");

        if (smsValues.size() == 3){
            Set<Map.Entry<String, String>> set = smsValues.entrySet();
            Iterator<Map.Entry<String, String>> i = set.iterator();

            int count = 1;

            while( i.hasNext() && proceed){
                Map.Entry<String, String> me = i.next();
                if (count == 1){
                    //check if first item is a msisdn
                    if (!"MSISDN".equals(me.getKey())) {
                        //validate value
                        LOGGER.log(Level.SEVERE, "Invalid first item!");
                        proceed = false;
                    }
                }else if (count == 2){
                    //check if second item is message
                    if ("Message".equals(me.getKey())) {
                        //validate value
                        if ("REGISTER".equals(message)) {
                            promptRegisterName(msisdn, shortCode);
                            type = SmsTypeEnum.REGISTRATION;
                            status = "Failed";
                        }
                        if ( message.contains(", ") ) {
                            //check if previous message is REGISTER from same number???
                            // success if register is sent before name
                            sender = message;
                            String verifySender = smsManager.verifyRegistration(msisdn,shortCode);
                            if( verifySender == "") {
                                status="Success";
                            } else {
                                status="Failed";
                                sender = verifySender;

                                LOGGER.info("Number has already been registered.");
                                generateSystemMessage(msisdn, shortCode);
                            }
                            type = SmsTypeEnum.REGISTRATION;

                        }
                        if ("PROMO".equals(message)){
                            //check if sender registered???
                            type = SmsTypeEnum.PROMO_AVAIL;
                            sender = smsManager.verifyRegistration(msisdn,shortCode);
                            LOGGER.log(Level.INFO, "Short Code: {0}, Message: {1},  MSISDN: {2}", new Object[] { shortCode, message, msisdn });

                            if ( promoManager.validateShortCode( shortCode ) ) {
                                if ( promoManager.validatePromo( message, convertLocalDateTimeToString(LocalDateTime.now()) ) ) {
                                    LOGGER.info("Promo Code is valid");
                                    status = "Success";
                                } else {
                                    LOGGER.severe("Invalid promo code or no running promotions at the moment.");
                                    status = "Failed";
                                }
                            } else {
                                LOGGER.warning("Invalid short code.");
                                status = "Failed";
                            }
                        }
                    }else {
                        LOGGER.warning("Invalid second item!");
                        proceed = false;
                    }
                }else if (count == 3){
                    //check if third item is short code
                    if (!"Short Code".equals(me.getKey())) {
                        //validate value
                        LOGGER.log(Level.SEVERE, "Invalid third item!");
                        proceed = false;
                    }
                }
                count++;
            }
        }else{
            LOGGER.log(Level.WARNING,"Incorrect number of items. Passed map has: " + smsValues.size() + " items");
        }
        if(sender==""){
            status = "Failed";
        }
        return new Sms(msisdn,recipient,sender,shortCode,transactionID,timeStamp,status,type);
    }

    //Display of ALL Promos
    public static void displayPromos() {
        List<Promo> promos = promoManager.retrieveAllPromo();
        for(Promo promo : promos){
            LOGGER.info("Promo: " + promo);
            LOGGER.info(promo.getPromoCode());
            LOGGER.info(promo.getDetails());
            LOGGER.info(promo.getShortCode());
            LOGGER.info(promo.getStartDate().toString());
            LOGGER.info(promo.getEndDate().toString());
        }
    }

    public void showSms(List<Sms> smsList) {
        for(Sms sms : smsList){
            LOGGER.log(Level.INFO,"Transaction ID: " + sms.getTransactionID());
            LOGGER.log(Level.INFO,"MSISDN:         " + sms.getMsisdn());
            LOGGER.log(Level.INFO,"Recipient:      " + sms.getRecipient());
            LOGGER.log(Level.INFO,"Sender:         " + sms.getSender());
            LOGGER.log(Level.INFO,"Short Code:     " + sms.getShortCode());
            LOGGER.log(Level.INFO,"Time Stamp:     " + sms.getTimeStamp());
            LOGGER.log(Level.INFO,"Status:         " + sms.getStatus());
            LOGGER.log(Level.INFO,"SMS Type:       " + sms.getType());
        }
    }

    public void showNames(List<String> names) {
        for (String name : names ) {
            LOGGER.log(Level.INFO,"Name: " + name);
        }
    }

    public void showPromoSummary(String promoCode){

        Report report = new Report();
        LOGGER.log(Level.INFO, "List of Failed Transactions");
        showSms(report.retrieveFailedTransactions());
        LOGGER.log(Level.INFO, "List of Failed Transactions for Registration SMS");
        showSms(report.retrieveFailedTransactions(SmsTypeEnum.REGISTRATION.toString()));
        LOGGER.log(Level.INFO, "List of Failed Transactions for Promo Avail  SMS");
        showSms(report.retrieveFailedTransactions(SmsTypeEnum.PROMO_AVAIL.toString()));
        LOGGER.log(Level.INFO, "List of Failed Transactions for System Generated SMS");
        showSms(report.retrieveFailedTransactions(SmsTypeEnum.SYSTEM_GENERATED.toString()));

        LOGGER.log(Level.INFO, "List of Successful Transactions");
        showSms(report.retrieveSuccessfulTransactions());
        LOGGER.log(Level.INFO, "List of Successful Transactions for Registration SMS");
        showSms(report.retrieveSuccessfulTransactions(SmsTypeEnum.REGISTRATION.toString()));
        LOGGER.log(Level.INFO, "List of Successful Transactions for Promo Avail  SMS");
        showSms(report.retrieveSuccessfulTransactions(SmsTypeEnum.PROMO_AVAIL.toString()));
        LOGGER.log(Level.INFO, "List of Successful Transactions for System Generated SMS");
        showSms(report.retrieveSuccessfulTransactions(SmsTypeEnum.SYSTEM_GENERATED.toString()));

        LOGGER.log(Level.INFO,"List of Promo Participants");
        showNames(report.retrieveParticipants());

        LOGGER.info("Total SMS received by System: " + report.countSMSReceived());
        LOGGER.info("Total SMS sent by System: " + report.countSMSSent());

    }
}