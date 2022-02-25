import java.time.LocalDateTime;

public class Sms {
    private String msisdn;          //mobile number of sender
    private String recipient;       //company or name of store
    private String sender;          //sender's name
    private String shortCode;       //number promocode is sent to
    private String transactionID;   //system-generated
    private LocalDateTime timeStamp;//current date and time when sms was received
    private String status;          //Status if message is accepted or not
    private SmsTypeEnum type;

    Sms(){}

    Sms(String msisdn, String recipient, String sender, String shortCode, String transactionID, LocalDateTime timeStamp, String status, SmsTypeEnum type){
        this.msisdn = msisdn;
        this.recipient = recipient;
        this.sender = sender;
        this.shortCode = shortCode;
        this.transactionID = transactionID;
        this.timeStamp = timeStamp;
        this.status = status;
        this.type = type;
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

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SmsTypeEnum getType() {
        return type;
    }

    public void setType(SmsTypeEnum type) {
        this.type = type;
    }
}
