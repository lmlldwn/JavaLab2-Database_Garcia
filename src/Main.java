import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {
    final private static Logger logger = Logger.getLogger(Main.class.getName());
    //private static DataAccess dataAccess = new DataAccess();

    public static void main(String[] args){
        /*DataAccess dataAccess = new DataAccess();
        dataAccess.setUserName(getCredentials("db.user"));
        dataAccess.setPassword(getCredentials("db.password"));
        dataAccess.connect();*/

        //setData();

//        DataAccess.disconnect();
        inputData();
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


    public static void setData(){
        SMS sms = new SMS();
        //java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        //java.sql.Timestamp sqlTimeStamp = new java.sql.Timestamp(date.getTime());
        //PreparedStatement ps =

        sms.setMsisdn("09209567110");
        sms.setRecipient("2213");
        sms.setSender("Lemuel Garcia");
        sms.setShortCode("PISO PIZZA");

        Date date = Calendar.getInstance().getTime();
        sms.setTimeStamp(date);
        System.out.println(date);
    }

    public static void writeData(SMS sms){
        String table = "SMS";
        String columns = "TransactionID,MSISDN,Recipient,Sender,ShortCode,TimeStamp";
        String values = sms.getTransactionID() + "," + sms.getMsisdn() + "," + sms.getRecipient() + "," + sms.getSender() + "," + sms.getShortCode() + "," + sms.getTimeStamp();
        logger.log(Level.INFO, "For database: " + values);
        //dataAccess.insertData(table, values);
    }

    public static void inputData(){

//        Scanner scan = new Scanner(System.in);
//        logger.log(Level.INFO, "Enter Mobile Number: ");
//        String mobileNumber = scan.next();
//
//        logger.log(Level.INFO, "Enter Message: ");
//        String message = scan.next();
//
//        logger.log(Level.INFO, "Enter Short Code: ");
//        String shortCode = scan.next();

        String mobileNumber = "09209567110";
        String message = "PISO";
        String shortCode = "1234";
        String name = "Lemuel";

        HashMap<String, String> data = new HashMap<>();
        data.put("MSISDN",mobileNumber);
        data.put("Message",message);
        data.put("Short Code", shortCode);


        SMS sms = new SMS();
        sms.smsChecker(data);
        //logger.log(Level.INFO, "Username is " + input);
    }


}
