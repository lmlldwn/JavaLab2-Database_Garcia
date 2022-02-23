import Utilities.Formatter;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.text.Format;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;



//Data Access Layer
//Perform connections to the database
public class DataAccess {

    final private static Logger logger = Logger.getLogger(DataAccess.class.getName());
    private static Connection connection = null;

    private String userName;
    private String password;

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setPassword(String password) {
        this.password = password;
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
                logger.log(Level.INFO, resultSet.getString(1) + " : " + resultSet.getString(2)
                        + " : " + resultSet.getString(3) + " : " + resultSet.getString(4)
                        + " : " + resultSet.getString(5));

                Promo promo = new Promo();
                promo.setPromoCode(resultSet.getString(1));
                promo.setDetails(resultSet.getString(2));
                promo.setShortCode(resultSet.getString(3));

                String startDate = resultSet.getString(4);
                String endDate = resultSet.getString(5);

                Formatter f = new Formatter();

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

                Formatter f = new Formatter();

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

}
