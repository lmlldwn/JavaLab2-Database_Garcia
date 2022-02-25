import Utilities.Formatter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;



//Data Access Layer
//Perform connections to the database
public class DataAccess {

    final private static Logger logger = Logger.getLogger(DataAccess.class.getName());
    final private Formatter f = new Formatter();
    private static Connection connection = null;

    private String userName;
    private String password;

    DataAccess (){
        setCredentials();
    }

    DataAccess(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    private void setCredentials(){
        try(InputStream input
                    = new FileInputStream("/Users/lemuelaldwin.garcia/IdeaProjects/Lab03/src/config.properties")){
            Properties prop = new Properties();
            prop.load(input);
            this.userName = prop.getProperty("db.user");
            this.password = prop.getProperty("db.password");
        } catch (IOException ex){
            logger.log(Level.SEVERE,"IOException : ", ex);
        }
    }

    public void connect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/SMS?useTimezone-true&serverTimezone=UTC",userName,password);
            logger.info("Connected");
        }catch (Exception e){
            logger.log(Level.SEVERE, "Not Connected", e);
        }
    }

    public void disconnect(){
        try{
            if (connection != null){
                connection.close();
                logger.info("Connection closed.");
            }
        }catch (Exception e){
            logger.log(Level.SEVERE,"Not Connected", e);
        }
    }

    public void insertData(String table, String columns, String data){
        String insertQuery = "INSERT INTO " + table + " (" + columns + ") " + " VALUES ('" + data + "')";
        Statement statement = null;
        int result = 0;

        try{
            statement = connection.createStatement();
            result = statement.executeUpdate(insertQuery);
        } catch(SQLException e){
            logger.log(Level.SEVERE, "SQL Exception: ", e);
        } finally {
            try{
                if (statement != null){
                    statement.close();
                }
            } catch (Exception e){
                logger.log(Level.SEVERE, "ERROR IN CLOSING", e);
            }
        }
        logger.log(Level.INFO, "Inserted Data: " + data);
    }

    public void insertData(String table, String data){
        String insertQuery = "INSERT INTO " + table + " VALUES " + data ;
        Statement statement = null;
        int result = 0;

        try{
            statement = connection.createStatement();
            result = statement.executeUpdate(insertQuery);
        } catch(SQLException e){
            logger.log(Level.SEVERE, "SQL Exception: ", e);
        } finally {
            try{
                if (statement != null){
                    statement.close();
                }
            } catch (Exception e){
                logger.log(Level.SEVERE, "ERROR IN CLOSING", e);
            }
        }
        logger.log(Level.INFO, "Inserted Data: " + data);
    }

    public ArrayList<Promo> retrievePromoData(String table){
        //fix query for lab 3.1.2
        String selectQuery = "SELECT * FROM " + table;

        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<String> result = new ArrayList<>();

        ArrayList<Promo> promos = new ArrayList<>();

        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(selectQuery);


            while(resultSet.next()){
                Promo promo = new Promo();
                promo.setPromoCode(resultSet.getString(1));
                promo.setDetails(resultSet.getString(2));
                promo.setShortCode(resultSet.getString(3));
                String startDate = resultSet.getString(4);
                String endDate = resultSet.getString(5);

                promo.setStartDate(f.convertStringToLocalDateTime(startDate));
                promo.setEndDate(f.convertStringToLocalDateTime(endDate));

                promos.add(promo);
            }
        }catch(SQLException e){
            logger.log(Level.SEVERE,"SQLException",e);
        }finally {
            try{
                if (statement != null){
                    statement.close();
                }
                if (resultSet != null){
                    resultSet.close();
                }
            }catch(Exception e) {
                logger.log(Level.SEVERE, "ERROR IN CLOSING", e);
            }
        }
        //logger.log(Level.INFO,"Retrieved : {0} ", result);
        return promos;
    }

    public ArrayList<Promo> retrievePromoData(String table, String find){
        //fix query for lab 3.1.2
        String selectQuery = "SELECT * FROM " + table + " WHERE " + find;
        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<Promo> promos = new ArrayList<>();

        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(selectQuery);


            while(resultSet.next()){
                Promo promo = new Promo();
                promo.setPromoCode(resultSet.getString(1));
                promo.setDetails(resultSet.getString(2));
                promo.setShortCode(resultSet.getString(3));
                String startDate = resultSet.getString(4);
                String endDate = resultSet.getString(5);

                promo.setStartDate(f.convertStringToLocalDateTime(startDate));
                promo.setEndDate(f.convertStringToLocalDateTime(endDate));
                promos.add(promo);
            }

        }catch(SQLException e){
            logger.log(Level.SEVERE,"SQLException",e);
        }finally {
            try{
                if (statement != null){
                    statement.close();
                }
                if (resultSet != null){
                    resultSet.close();
                }
            }catch(Exception e) {
                logger.log(Level.SEVERE, "ERROR IN CLOSING", e);
            }
        }
        //logger.log(Level.INFO,"Retrieved : {0} ", result);
        return promos;
    }

    public ArrayList<SMS> retrieveSMSData(String table){
        //fix query for lab 3.1.2
        String selectQuery = "SELECT * FROM " + table ;

        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<SMS> smsArrayList = new ArrayList<>();

        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(selectQuery);


            while(resultSet.next()){
                SMS sms = new SMS();
                sms.setTransactionID(resultSet.getString(1));
                sms.setMsisdn(resultSet.getString(2));
                sms.setRecipient(resultSet.getString(3));
                sms.setSender(resultSet.getString(4));
                sms.setShortCode(resultSet.getString(5));
                sms.setStatus(resultSet.getString(7));

                String timeStamp = resultSet.getString(6);
                sms.setTimeStamp(f.convertStringToLocalDateTime(timeStamp));

                String type = resultSet.getString(8);
                if (type.equals(SMSType.PromoAvail)){
                    sms.setType(SMSType.PromoAvail);
                }else if (type.equals(SMSType.Registration)){
                    sms.setType(SMSType.Registration);
                }else if (type.equals(SMSType.SystemGenerated)){
                    sms.setType(SMSType.SystemGenerated);
                }
                smsArrayList.add(sms);
            }

        }catch(SQLException e){
            logger.log(Level.SEVERE,"SQLException",e);
        }finally {
            try{
                if (statement != null){
                    statement.close();
                }
                if (resultSet != null){
                    resultSet.close();
                }
            }catch(Exception e) {
                logger.log(Level.SEVERE, "ERROR IN CLOSING", e);
            }
        }
        return smsArrayList;
    }

    public ArrayList<SMS> retrieveSMSData(String table, String find){
        //fix query for lab 3.1.2
        String selectQuery = "SELECT * FROM " + table + " WHERE " + find;

        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<SMS> smsArrayList = new ArrayList<>();

        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(selectQuery);

            while(resultSet.next()){
                SMS sms = new SMS();
                sms.setTransactionID(resultSet.getString(1));
                sms.setMsisdn(resultSet.getString(2));
                sms.setRecipient(resultSet.getString(3));
                sms.setSender(resultSet.getString(4));
                sms.setShortCode(resultSet.getString(5));
                sms.setStatus(resultSet.getString(7));

                String timeStamp = resultSet.getString(6);
                sms.setTimeStamp(f.convertStringToLocalDateTime(timeStamp));

                String type = resultSet.getString(8);
                if (type.equals(SMSType.PromoAvail.toString())){
                    sms.setType(SMSType.PromoAvail);
                }else if (type.equals(SMSType.Registration.toString())){
                    sms.setType(SMSType.Registration);
                }else if (type.equals(SMSType.SystemGenerated.toString())){
                    sms.setType(SMSType.SystemGenerated);
                }
                smsArrayList.add(sms);
            }

        }catch(SQLException e){
            logger.log(Level.SEVERE,"SQLException",e);
        }finally {
            try{
                if (statement != null){
                    statement.close();
                }
                if (resultSet != null){
                    resultSet.close();
                }
            }catch(Exception e) {
                logger.log(Level.SEVERE, "ERROR IN CLOSING", e);
            }
        }
        return smsArrayList;
    }

    public ArrayList<String> retrieveSingleColumn(String selectQuery){
        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<String> stringArrayList = new ArrayList<>();

        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(selectQuery);

            while(resultSet.next()){
                stringArrayList.add(resultSet.getString(1));
            }

        }catch(SQLException e){
            logger.log(Level.SEVERE,"SQLException",e);
        }finally {
            try{
                if (statement != null){
                    statement.close();
                }
                if (resultSet != null){
                    resultSet.close();
                }
            }catch(Exception e) {
                logger.log(Level.SEVERE, "ERROR IN CLOSING", e);
            }
        }
        return stringArrayList;
    }



}
