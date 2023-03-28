import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import Utilidades.*;

public class DBManager {
    private Connection c;
    static int instances = 1;

    static String
    URL = "jdbc:mysql://localhost:3306/sistema_alumnos?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true",
    USER = "root",
    PASSWORD = "";  
    
    private DBManager(){}

    public static DBManager getDBManager(String USERNAME, String PASSWORD ) throws MoreThanOneInstanceException{
        if (DBManager.instances < 1){
            throw new MoreThanOneInstanceException("Solo se permite instanciar la clase DBManager una vez");
        }
        try {
            DBManager dbm = new DBManager();
            dbm.c = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            DBManager.instances--;
            return dbm;
        } catch (SQLException exep) {
            System.out.println(exep.getMessage());
            return null;
        }

    }
    
    public static DBManager getDBManager() throws MoreThanOneInstanceException{
        if (DBManager.instances < 1){
            throw new MoreThanOneInstanceException("Solo se permite instanciar la clase DBManager una vez");
        }
        try {
            DBManager dbm = new DBManager();
            dbm.c = DriverManager.getConnection(DBManager.URL, DBManager.USER,DBManager.PASSWORD);
            DBManager.instances--;
            return dbm;
        } catch (SQLException exep) {
            System.out.println(exep.getMessage());
            return null;
        }
    }

    //TODO establecer un tipo de filtros y selección de campos
    private ResultSet getTable(String table){
        try{
            Statement st = this.c.createStatement();
            return st.executeQuery("SELECT * FROM `%s`".formatted(table));
        }
        catch(SQLException exce){
            System.out.println(exce.getMessage());
        }
        return null;
    }

    public ResultSet getAlumnos(){
        return getTable("alumnos");
    }
    public ResultSet getBitacora(){
        return getTable("ozl_bitacora");
    }
    public ResultSet getOpciones(){
        return getTable("opciones_menu");
    }
    public ResultSet getUsers(){
        return getTable("ozl_users");
    }

    public static void main(String[] args) {
        try {
            DBManager db = DBManager.getDBManager();
            Table tablita = new Table(db.getAlumnos());
            tablita.flushTable();
            
        } catch (Exception e) {
            
        }
    }
}

class MoreThanOneInstanceException extends Exception{
    public MoreThanOneInstanceException(String errorMessage){
        super(errorMessage);
    }
}
