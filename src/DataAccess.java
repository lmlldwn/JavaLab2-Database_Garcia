import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

//Data Access Layer
//Perform connections to the database
public class DataAccess {

    final private static Logger logger = Logger.getLogger(DataAccess.class.getName());
    private static Connection connection = null;

    public static void connect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/SMS?useTimezone-true&serverTimezone=UTC", "root", "Aldw!nSQL0215");
            logger.info("Connected");

        }catch (Exception e){
            logger.log(Level.SEVERE, "Not Connected", e);
        }
    }

    public static void disconnect(){
        try{
            if (connection != null){
                connection.close();
                logger.info("Connection closed.");
            }
        }catch (Exception e){
            logger.log(Level.SEVERE,"Not Connected", e);
        }
    }

}
