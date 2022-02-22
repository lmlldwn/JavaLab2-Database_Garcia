import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SMS {
    private String msisdn;
    private String recipient;
    private String sender;
    private String shortCode;
    private int transactionID;
    private Date timeStamp;

    final private static Logger logger = Logger.getLogger(SMS.class.getName());

    public void smsChecker(HashMap<String,String> values){
        if (values.size() == 3){
            //logger.log(Level.INFO,"Correct item count!");

            Set set = values.entrySet();
            Iterator i = set.iterator();

            int count = 1;
            while(i.hasNext()){
                Map.Entry me = (Map.Entry)i.next();
                if (count == 1){
                    //check if first item is a msisdn
                    if (me.getKey() == "MSISDN"){
                        logger.log(Level.INFO, "Mobile number is the first item!");
                    }else{
                        logger.log(Level.SEVERE, "Invalid first item!");
                        break;
                    }
                }else if (count == 2){
                    //check if second item is message
                    if (me.getKey() == "Message"){
                        logger.log(Level.INFO, "Message is the second item!");
                    }else{
                        logger.log(Level.SEVERE, "Invalid second item!");
                        break;
                    }
                }else if (count == 3){
                    //check if third item is short code
                    if (me.getKey() == "Short Code"){
                        logger.log(Level.INFO, "Short code is the third item!");
                    }else{
                        logger.log(Level.SEVERE, "Invalid third item!");
                        break;
                    }
                }
                count++;
            }
        }else{
            logger.log(Level.WARNING,"Incorrect number of items. Passed map has: " + values.size() + " items");
        }

    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
