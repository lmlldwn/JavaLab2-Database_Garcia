import java.util.List;


public class Report {


    private final DataAccess dataAccess = new DataAccess();

    Report(){
        this.shortCode = "12345555";
    }

    Report(String shortCode){
        this.shortCode = shortCode;
    }

    private final String shortCode;

    public List<Sms> retrieveFailedTransactions() {
        return dataAccess.getFailedTransaction(shortCode);
    }

    public List<Sms> retrieveFailedTransactions(String type) {
        return dataAccess.getFailedTransactionBySmsType(shortCode,type);
    }

    public List<Sms> retrieveSuccessfulTransactions() {
        return dataAccess.getSuccessfulTransaction(shortCode);
    }

    public List<Sms> retrieveSuccessfulTransactions(String type) {
        return dataAccess.getSuccessfulTransactionBySmsType(shortCode,type);
    }

    public List<String> retrieveParticipants() {
        return dataAccess.getPeoplePerPromo(shortCode);
    }

    public int countSMSReceived(){
        return dataAccess.countSMSReceivedBySystem(shortCode);
    }

    public int countSMSSent(){
        return dataAccess.countSMSSentBySystem(shortCode);
    }
}
