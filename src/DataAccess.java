import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import static util.DateFormatterUtils.convertStringToLocalDateTime;

public class DataAccess {

    DataAccess (){
        setCredentials();
    }

    private static final Logger LOGGER = Logger.getLogger(DataAccess.class.getName());
    private Connection connection = null;
    private String userName;
    private String password;
    private String dbUrl;

    public void insertData(String table, String data) {
        String insertQuery = "INSERT INTO " + table + " VALUES " + data ;
        Statement statement = null;

        connect();
        try {
            statement = connection.createStatement();
            int result = statement.executeUpdate(insertQuery);
        } catch(SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Exception: ", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "ERROR IN CLOSING", e);
            }
        }
        LOGGER.log(Level.INFO, "Inserted Data: " + data);
        disconnect();
    }

    //SMS Functionalities

    public List<Sms> getSmsByDate(String startDate, String endDate) {
        String query = "SELECT * FROM SMS WHERE TimeStamp BETWEEN " + startDate + " and " + endDate;

        connect();

        Statement statement = null;
        ResultSet resultSet = null;
        List<Sms> smsList = new ArrayList<>();


        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                smsList.add(mapResultSetToSms(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch(Exception e) {
                LOGGER.log(Level.SEVERE, "ERROR IN CLOSING", e);
            }
        }
        disconnect();
        return smsList;
    }

    public List<Sms> getSmsByPromoCode(String promoCode) {
        String query = "SELECT * FROM SMS WHERE Details='" + promoCode + "'";
        Statement statement = null;
        ResultSet resultSet = null;
        List<Sms> smsList = new ArrayList<>();

        connect();

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                smsList.add(mapResultSetToSms(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch(Exception e) {
                LOGGER.log(Level.SEVERE, "ERROR IN CLOSING", e);
            }
        }
        disconnect();
        return smsList;
    }

    public List<Sms> getSmsByMsisdn(String msisdn) {
        String query = "SELECT * FROM SMS WHERE MSISDN='" + msisdn + "'";
        Statement statement = null;
        ResultSet resultSet = null;
        List<Sms> smsList = new ArrayList<>();

        connect();

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                smsList.add(mapResultSetToSms(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch(Exception e) {
                LOGGER.log(Level.SEVERE, "ERROR IN CLOSING", e);
            }
        }
        disconnect();
        return smsList;
    }

    public List<Sms> getSmsSentBySystem() {
        String query = "SELECT * FROM SMS WHERE Sender='SYSTEM'";
        Statement statement = null;
        ResultSet resultSet = null;
        List<Sms> smsList = new ArrayList<>();

        connect();

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                smsList.add(mapResultSetToSms(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch(Exception e) {
                LOGGER.log(Level.SEVERE, "ERROR IN CLOSING", e);
            }
        }
        disconnect();

        return smsList;
    }

    public List<Sms> getSmsReceivedBySystem() {
        String query = "SELECT * FROM SMS WHERE Recipient='SYSTEM'";
        Statement statement = null;
        ResultSet resultSet = null;
        List<Sms> smsList = new ArrayList<>();

        connect();

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                smsList.add(mapResultSetToSms(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch(Exception e) {
                LOGGER.log(Level.SEVERE, "ERROR IN CLOSING", e);
            }
        }
        disconnect();

        return smsList;
    }

    public List<Sms> getFailedTransaction(String shortCode) {
        String query = "SELECT * FROM SMS WHERE Status='Failed' AND ShortCode='" + shortCode + "'";
        Statement statement = null;
        ResultSet resultSet = null;
        List<Sms> smsList = new ArrayList<>();

        connect();

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                smsList.add(mapResultSetToSms(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch(Exception e) {
                LOGGER.log(Level.SEVERE, "ERROR IN CLOSING", e);
            }
        }
        disconnect();
        return smsList;
    }

    public List<Sms> getFailedTransactionBySmsType(String shortCode, String type) {
        String query = "SELECT * FROM SMS WHERE Status='Failed' AND Type='" + type + "' AND ShortCode='" + shortCode + "'";
        Statement statement = null;
        ResultSet resultSet = null;
        List<Sms> smsList = new ArrayList<>();

        connect();

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                smsList.add(mapResultSetToSms(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch(Exception e) {
                LOGGER.log(Level.SEVERE, "ERROR IN CLOSING", e);
            }
        }
        disconnect();
        return smsList;
    }

    public List<Sms> getSuccessfulTransaction(String shortCode) {
        String query = "SELECT * FROM SMS WHERE Status='Success' AND ShortCode='" + shortCode + "'";
        Statement statement = null;
        ResultSet resultSet = null;
        List<Sms> smsList = new ArrayList<>();

        connect();

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                smsList.add(mapResultSetToSms(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch(Exception e) {
                LOGGER.log(Level.SEVERE, "ERROR IN CLOSING", e);
            }
        }
        disconnect();
        return smsList;
    }

    public List<Sms> getSuccessfulTransactionBySmsType(String shortCode, String type) {
        String query = "SELECT * FROM SMS WHERE Status='Success' AND Type='" + type + "' AND ShortCode='" + shortCode + "'";
        Statement statement = null;
        ResultSet resultSet = null;
        List<Sms> smsList = new ArrayList<>();

        connect();

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                smsList.add(mapResultSetToSms(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch(Exception e) {
                LOGGER.log(Level.SEVERE, "ERROR IN CLOSING", e);
            }
        }
        disconnect();
        return smsList;
    }

    public List<String> getPeoplePerPromo(String shortCode) {
        String query = "SELECT Sender FROM SMS WHERE Sender != 'SYSTEM' AND SENDER != '' AND ShortCode='" + shortCode + "'";
        Statement statement = null;
        ResultSet resultSet = null;
        List<String> stringList = new ArrayList<>();

        connect();

        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                stringList.add(resultSet.getString(1));
            }

        }catch(SQLException e){
            LOGGER.log(Level.SEVERE,"SQLException",e);
        }finally {
            try{
                if (statement != null){
                    statement.close();
                }
                if (resultSet != null){
                    resultSet.close();
                }
            }catch(Exception e) {
                LOGGER.log(Level.SEVERE, "ERROR IN CLOSING", e);
            }
        }
        disconnect();
        return stringList;
    }

    public String checkRegistration(String msisdn, String shortCode) {
        String query = "SELECT Sender FROM SMS WHERE MSISDN='" + msisdn + "' AND ShortCode='" + shortCode + "' AND Type='REGISTRATION' AND Status='Success'";
        Statement statement = null;
        ResultSet resultSet = null;
        String string = "";

        connect();

        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                string = resultSet.getString(1);
            }

        }catch(SQLException e){
            LOGGER.log(Level.SEVERE,"SQLException",e);
        }finally {
            try{
                if (statement != null){
                    statement.close();
                }
                if (resultSet != null){
                    resultSet.close();
                }
            }catch(Exception e) {
                LOGGER.log(Level.SEVERE, "ERROR IN CLOSING", e);
            }
        }
        disconnect();
        return string;
    }

    public int countSMSReceivedBySystem(String shortCode) {
        String query = "SELECT COUNT(Recipient) FROM SMS WHERE Recipient='SYSTEM' AND ShortCode='" + shortCode + "'";
        Statement statement = null;
        ResultSet resultSet = null;
        int count = 0;

        connect();

        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                count = resultSet.getInt(1);
            }

        }catch(SQLException e){
            LOGGER.log(Level.SEVERE,"SQLException",e);
        }finally {
            try{
                if (statement != null){
                    statement.close();
                }
                if (resultSet != null){
                    resultSet.close();
                }
            }catch(Exception e) {
                LOGGER.log(Level.SEVERE, "ERROR IN CLOSING", e);
            }
        }
        disconnect();
        return count;
    }

    public int countSMSSentBySystem(String shortCode) {
        String query = "SELECT COUNT(Sender) FROM SMS WHERE Sender='SYSTEM' AND ShortCode='" + shortCode + "'";
        Statement statement = null;
        ResultSet resultSet = null;
        int count = 0;

        connect();

        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                count = resultSet.getInt(1);
            }

        }catch(SQLException e){
            LOGGER.log(Level.SEVERE,"SQLException",e);
        }finally {
            try{
                if (statement != null){
                    statement.close();
                }
                if (resultSet != null){
                    resultSet.close();
                }
            }catch(Exception e) {
                LOGGER.log(Level.SEVERE, "ERROR IN CLOSING", e);
            }
        }
        disconnect();
        return count;
    }


    //Promo Functionalities

    public void insertPromo(Promo promo) {
        String query = "('" + promo.getPromoCode() + "','" + promo.getDetails() + "','" + promo.getShortCode() +
                "','" + promo.getStartDate() + "','" + promo.getEndDate() + "')";
        insertData("Promos",query);
    }

    public List<Promo> getPromoData() {
        String query = "SELECT * FROM Promos";

        Statement statement = null;
        ResultSet resultSet = null;
        List<Promo> promoList = new ArrayList<>();
        connect();
        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                promoList.add(mapResultSetToPromo(resultSet));
            }
        }catch(SQLException e){
            LOGGER.log(Level.SEVERE,"SQLException",e);
        }finally {
            try{
                if (statement != null){
                    statement.close();
                }
                if (resultSet != null){
                    resultSet.close();
                }
            }catch(Exception e) {
                LOGGER.log(Level.SEVERE, "ERROR IN CLOSING", e);
            }
        }
        disconnect();
        return promoList;
    }

    public List<Promo> getPromoDataWithCondition(String find) {
        String query = "SELECT * FROM Promos WHERE " + find;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Promo> promoList = new ArrayList<>();

        connect();
        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                promoList.add(mapResultSetToPromo(resultSet));
            }

        }catch(SQLException e){
            LOGGER.log(Level.SEVERE,"SQLException",e);
        }finally {
            try{
                if (statement != null){
                    statement.close();
                }
                if (resultSet != null){
                    resultSet.close();
                }
            }catch(Exception e) {
                LOGGER.log(Level.SEVERE, "ERROR IN CLOSING", e);
            }
        }
        disconnect();
        return promoList;
    }

    public List<Promo> getPromoByShortCode(String shortCode) {
        String find = "ShortCode='" + shortCode + "'";
        return getPromoDataWithCondition(find);
    }

    public List<Promo> getValidPromoByTimeStamp(String timeStamp ) {
        String find = timeStamp + "' BETWEEN StartDate AND EndDate";
        return getPromoDataWithCondition(find);
    }

    public List<Promo> getValidPromoByPromoCodeAndTimeStamp(String promoCode, String timeStamp ) {
        String find = "PromoCode='" + promoCode + "' AND '" + timeStamp + "' BETWEEN StartDate AND EndDate";
        return getPromoDataWithCondition(find);
    }


    //private functions

    private void setCredentials(){
        try(InputStream input
                    = new FileInputStream("/Users/lemuelaldwin.garcia/IdeaProjects/Lab03/src/config.properties")){
            Properties prop = new Properties();
            prop.load(input);
            this.userName = prop.getProperty("db.user");
            this.password = prop.getProperty("db.password");
            this.dbUrl = prop.getProperty("db.url");
        } catch (IOException ex){
            LOGGER.log(Level.SEVERE,"IOException : ", ex);
        }
    }

    private void connect() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    dbUrl,userName,password);
            LOGGER.info("Connected");
        }catch (Exception e){
            LOGGER.log(Level.SEVERE, "Not Connected", e);
        }
    }

    private void disconnect() {
        try{
            if (connection != null) {
                connection.close();
                LOGGER.info("Connection closed.");
            }
        }catch (Exception e) {
            LOGGER.log(Level.SEVERE,"Not Connected", e);
        }
    }

    private Sms mapResultSetToSms(ResultSet resultSet) throws SQLException {
        Sms sms = new Sms();
        sms.setTransactionID(resultSet.getString(1));
        sms.setMsisdn(resultSet.getString(2));
        sms.setRecipient(resultSet.getString(3));
        sms.setSender(resultSet.getString(4));
        sms.setShortCode(resultSet.getString(5));
        sms.setTimeStamp(convertStringToLocalDateTime(resultSet.getString(6)));
        sms.setStatus(resultSet.getString(7));
        sms.setType(SmsTypeEnum.toEnum(resultSet.getString(8)));

        return sms;
    }

    private Promo mapResultSetToPromo(ResultSet resultSet) throws SQLException {
        Promo promo = new Promo();
        promo.setPromoCode(resultSet.getString(1));
        promo.setDetails(resultSet.getString(2));
        promo.setShortCode(resultSet.getString(3));
        String startDate = resultSet.getString(4);
        String endDate = resultSet.getString(5);

        promo.setStartDate(convertStringToLocalDateTime(startDate));
        promo.setEndDate(convertStringToLocalDateTime(endDate));

        return promo;
    }

}
