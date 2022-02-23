import Utilities.Formatter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {
    final private static Logger logger = Logger.getLogger(Main.class.getName());
    final private static Formatter f= new Formatter();
    private static DataAccess dataAccess = new DataAccess();


    public static void main(String[] args){
        startDataBase();
        inputData();
        //sampleSMS();
        closeDataBase();
    }

//Database Tools
    public static void startDataBase(){
        dataAccess.setUserName(getCredentials("db.user"));
        dataAccess.setPassword(getCredentials("db.password"));
        dataAccess.connect();
    }

    public static void closeDataBase(){
        dataAccess.disconnect();
    }

    public static String getCredentials(String credentials){
        try(InputStream input
                    = new FileInputStream("/Users/lemuelaldwin.garcia/IdeaProjects/Lab03/src/config.properties")){
            Properties prop = new Properties();
            prop.load(input);
            logger.log(Level.INFO, "credentials: {0}", prop.getProperty(credentials) );
            return prop.getProperty(credentials);

        } catch (IOException ex){
            logger.log(Level.SEVERE,"IOException : ", ex);
        }
        return null;
    }
//End of Database Tools

    //write to database
    public static void writeData(SMS sms){
        String table = "SMS";
        String columns = "TransactionID,MSISDN,Recipient,Sender,ShortCode,TimeStamp";
        String values = sms.getTransactionID() + "," + sms.getMsisdn() + "," + sms.getRecipient() + "," + sms.getSender() + "," + sms.getShortCode() + "," + sms.getTimeStamp();
        logger.log(Level.INFO, "For database: " + values);
        //dataAccess.insertData(table, values);
    }

    //data from user
    public static void inputData(){

        Scanner scan = new Scanner(System.in);
        logger.log(Level.INFO, "Enter Mobile Number: ");
        String msisdn = scan.next();

        logger.log(Level.INFO, "Enter Message: ");
        String message = scan.next();

        logger.log(Level.INFO, "Enter Short Code: ");
        String shortCode = scan.next();

//        String mobileNumber = "09209567110";
//        String message = "PISO PIZZA";
//        String shortCode = "1234";



        HashMap<String, String> data = new HashMap<>();
        data.put("MSISDN",msisdn);
        data.put("Message",message);
        data.put("Short Code", shortCode);
        //String name = "Lemuel";
        //data.put("Name", name);

        smsChecker(data);
        //logger.log(Level.INFO, "Username is " + input);


        String sender = "Lemuel Garcia"; // needs to be user input
        String recipient = "LOLPH";                         //System or UserName depending on whom the sender is.
        String transactionID= UUID.randomUUID().toString(); //computer generated
        LocalDateTime timeStamp = LocalDateTime.now();      //computer generated
        String status = smsChecker(data);                          //from sms checker function

        String query = "('" + transactionID + "','" + msisdn + "','" + recipient + "','" + sender + "','" + shortCode + "','" + f.convertLocalDateTimeToString(timeStamp) + "','" + status + "')";
        //INSERT INTO `SMS`.`SMS` (`TransactionID`, `MSISDN`, `Recipient`, `Sender`, `ShortCode`, `TimeStamp`, `Status`) VALUES ('123ASDF', '09209567110', 'MIMZ', 'Lemuel Aldwin', '1234', '2022-02-23 23:28:45');
        //System.out.println(query);
        logger.info(query);
        //dataAccess.insertData("SMS",query);
    }
    public static void showPromoList(ArrayList<Promo> promos) {

//        private String promoCode;
//        private String details;
//        private String shortCode;
//        private Date startDate;
//        private Date endDate;
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

        //check the contents in the hashmap
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
    //getList of ALL Promos
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

    public static void sampleSMS(){
//        private String msisdn;
//        private String recipient;
//        private String sender;
//        private String shortCode;
//        private int transactionID;
//        private LocalDateTime timeStamp;
//        private String status;

        String msisdn = "09209567110";                      //user input
        String sender = "Lemuel Garcia";                    //Sender of message  system or person - user input if from person
        String shortCode = "1234";                          //series of numbers - user input
        String message = "PISO PIZZA";                      //user input

        String recipient = "LOLPH";                         //System or UserName depending on whom the sender is.
        String transactionID= UUID.randomUUID().toString(); //computer generated
        LocalDateTime timeStamp = LocalDateTime.now();      //computer generated
        String status = "Success";                          //from sms checker function

        String query = "('" + transactionID + "','" + msisdn + "','" + recipient + "','" + sender + "','" + shortCode + "','" + f.convertLocalDateTimeToString(timeStamp) + "','" + status + "')";
        //INSERT INTO `SMS`.`SMS` (`TransactionID`, `MSISDN`, `Recipient`, `Sender`, `ShortCode`, `TimeStamp`, `Status`) VALUES ('123ASDF', '09209567110', 'MIMZ', 'Lemuel Aldwin', '1234', '2022-02-23 23:28:45');
        //System.out.println(query);
        logger.info(query);
        //dataAccess.insertData("SMS",query);
    }







}
