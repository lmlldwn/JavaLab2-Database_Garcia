import Utilities.Formatter;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {
    final private static Logger logger = Logger.getLogger(Main.class.getName());
    final private static Formatter f= new Formatter();
    final private static Scanner scan = new Scanner(System.in);
    private static DataAccess dataAccess = new DataAccess();

    public static void main(String[] args){

        //inputData();
        //sampleSMS();
        prepareSMS prep = new prepareSMS();
//      sms.showSummary();

        //retrieve by date
        //String startDate = "2022-01-01 01:02:01";
        //String endDate = "2022-03-01 01:00:00";
        //prep.retrieveSMSByDate(startDate,endDate);

//        retrieve sms by msisdn
//        String msisdn = "09209567110";
//        prep.retrieveSMSByMSISDN(msisdn);


//        retrieve sms received by system
//         prep.retrieveSMSSystemReceived();

//        retrieve sms sent by system
//        prep.retrieveSMSSystemSent();

//        ArrayList<String> nums = new ArrayList<>();
//        nums.add("09209561111");
//        nums.add("09111111112");
//        nums.add("09123456789");
//        prep.retrieveSMSByMSISDN(nums);

//        prep.retrieveFailedTransactions();
//        prep.retrieveFailedTransactions(SMSType.PromoAvail.toString());
//        prep.retrieveSuccessfulTransactions();
//        prep.retrieveSuccessfulTransactions(SMSType.Registration.toString());
//        prep.retrieveParticipants();
//        prep.countSMSReceived();
//        prep.countSMSSent();
    }

    //Enter Data from USER // MESSAGE received from user
    public static void inputData(){
        String msisdn = "";
        String sender = "";
        String message = "";
        String shortCode = "";
        String recipient = "System";
        String status = "Failed";
        SMSType type= SMSType.SystemGenerated;

            //prompt message
            //if register -> register -> write register to db -> prompt promo for same user -> check message
            // if not register -> validate promo

        logger.log(Level.INFO, "Enter Mobile Number: ");
        msisdn = scan.nextLine();

        logger.log(Level.INFO, "Enter Message: ");
        message = scan.nextLine();

        logger.log(Level.INFO, "Enter Short Code: ");
        shortCode = scan.nextLine();


        if ( message.equals("REGISTER") ){
            sender = registerName();
            status = "Success";
            type = SMSType.Registration;

            logger.log(Level.INFO, "Enter Mobile Number: ");
            msisdn = scan.nextLine();

            logger.log(Level.INFO, "Enter Message: ");
            message = scan.nextLine();

            logger.log(Level.INFO, "Enter Short Code: ");
            shortCode = scan.nextLine();
        }else {
            HashMap<String, String> data = new HashMap<>();
            data.put("MSISDN", msisdn);
            data.put("Message", message);
            data.put("Short Code", shortCode);
            status = smsChecker(data);                   //from sms checker function
            type = SMSType.PromoAvail;
        }
        //else is needed to write registration sms to db if not present, it will not write registration messages to db
        //try to add new table for registration of number to users to fix this situation

        String transactionID= UUID.randomUUID().toString(); //computer generated
        LocalDateTime timeStamp = LocalDateTime.now();      //computer generated

        if ( sender.equals("")){
            status = "Failed";
        }

        SMS sms = new SMS();
        sms.setShortCode(shortCode);
        sms.setMsisdn(msisdn);
        sms.setSender(sender);
        sms.setRecipient(recipient);
        sms.setStatus(status);
        sms.setTimeStamp(timeStamp);
        sms.setTransactionID(transactionID);
        sms.setType(type);

        prepareSMS prep = new prepareSMS();
        prep.insertSMS(sms);
    }

    public static String registerName (){
        String sender = "";
        logger.log(Level.INFO, "To complete the promo registration, please send 'Lastname, Firstname' to 1234555");
        sender = scan.nextLine();
//        senderLastName = scan.nextLine();
//        senderFirstName = scan.nextLine();
//        sender = senderFirstName + " " + senderLastName.replace(",","");
//        check sender name entered (remove comma)
        return sender;
    }
    public static String smsChecker(HashMap<String,String> values){

        boolean proceed = true;

        //check keys in the hashmap
        if (values.size() == 3){
            //logger.log(Level.INFO,"Correct item count!");
            Set set = values.entrySet();
            Iterator i = set.iterator();

            int count = 1;
            while(i.hasNext() && proceed == true){
                Map.Entry me = (Map.Entry)i.next();
                if (count == 1){
                    //check if first item is a msisdn
                    if (me.getKey() == "MSISDN"){
                        proceed = true;
                        //logger.log(Level.INFO, "Mobile number is the first item!");
                    }else{
                        logger.log(Level.SEVERE, "Invalid first item!");
                        proceed = false;
                    }
                }else if (count == 2){
                    //check if second item is message
                    if (me.getKey() == "Message"){
                        proceed = true;
                        //logger.log(Level.INFO, "Message is the second item!");
                    }else{
                        logger.log(Level.SEVERE, "Invalid second item!");
                        proceed = false;
                    }
                }else if (count == 3){
                    //check if third item is short code
                    if (me.getKey() == "Short Code"){
                        proceed = true;
                        //logger.log(Level.INFO, "Short code is the third item!");
                    }else{
                        logger.log(Level.SEVERE, "Invalid third item!");
                        proceed = false;
                    }
                }
                count++;
            }
        }else{
            logger.log(Level.WARNING,"Incorrect number of items. Passed map has: " + values.size() + " items");
            proceed = false;
        }

        //check if promo code is available
        if(proceed){
            logger.info("Short Code: " + values.get("Short Code"));
            logger.info("Message/PromoCode sent: " + values.get("Message"));
            logger.info("Mobile Number: " + values.get("MSISDN"));

            //check for short code in database
            //SELECT PromoCode, StartDate, EndDate FROM Promos WHERE ShortCode=values.get("Short Code")


            //SAMPLE OUT-OF-BOUNDS DATE
            //String date = "2021-02-12 00:01:21";
            //LocalDateTime dd = f.convertStringToLocalDateTime(date);

            String table = "Promos";
            String var = "ShortCode";
            String find = "ShortCode='" + values.get("Short Code") + "' AND" +
                        " PromoCode='" + values.get("Message") + "' AND" +
                        //"'" + f.convertLocalDateTimeToString(dd) + "' BETWEEN StartDate and EndDate";
                        "'" + f.convertLocalDateTimeToString(LocalDateTime.now()) + "' BETWEEN StartDate and EndDate";

            ArrayList<Promo> promos = dataAccess.retrievePromoData(table,find);

            if(validateShortCode(values.get("Short Code"))){
                if(!promos.isEmpty()){
                    logger.info("Promo Code is valid");
                    return "Success";
                }else{
                    logger.severe("Invalid promo code or no running promotions at the moment.");
                }
            }else{
                logger.warning("Invalid short code.");
            }
        }
        return "Failed";
    }
    public static boolean validateShortCode(String shortCode){

        String table = "Promos";
        String find = "ShortCode='" + shortCode + "'";
        ArrayList<Promo> promos = dataAccess.retrievePromoData(table,find);
        if(!promos.isEmpty()){
            //logger.info("Short Code exists");
            return true;
        }else{
            //logger.warning("Short Code does not exist!");
            return false;
        }
        //return false;
    }
    //Display of ALL Promos
    public static void displayPromos(ArrayList<Promo> promos){
        for(Promo promo : promos){
            System.out.println("Promo: " + promo);
            System.out.println(promo.getPromoCode());
            System.out.println(promo.getDetails());
            System.out.println(promo.getShortCode());
            System.out.println(promo.getStartDate());
            System.out.println(promo.getEndDate());
        }
    }

    public static void menu(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer
                .append("(1) Send SMS ")
                .append("(2) View SMS by Date ")
                .append("(3) View SMS by ???");
        logger.log(Level.INFO,stringBuffer.toString());
    }


}
