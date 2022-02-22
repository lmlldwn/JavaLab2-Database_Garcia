import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
        String insertQuery = "INSERT INTO " + table + " VALUES ('" + data + "')";
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

    public ArrayList<String> retrieveData(String table){
        String selectQuery = "SELECT * FROM" + table;

        return null;
    }

}
