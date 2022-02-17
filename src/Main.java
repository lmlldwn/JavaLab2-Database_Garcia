public class Main {

    public static void main(String[] args){
        DataAccess dataAccess = new DataAccess();
        dataAccess.connect();
        dataAccess.disconnect();
    }
}
