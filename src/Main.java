import java.sql.*;
import java.util.HashMap;
import java.util.Scanner;
import Opciones.*;
import Utilidades.AnsiColorMap;
import Utilidades.Menu;

public class Main{
    static String
        // URL = "jdbc:mysql://148.225.60.126:3306/bd2?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true",
        // USER = "bd2",
        // PASSWORD = "MementoVivere";
    
        URL = "jdbc:mysql://localhost:3306/sistema_alumnos?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true",
        USER = "root",
        PASSWORD = "";  

    static HashMap<String,Opcion> opciones = new HashMap<>();

    public static void main(String[] args) {

        try {
            Connection c = DriverManager.getConnection(URL, USER, PASSWORD);
            opciones.put("ADD_USER",new AnadirUsuario());
            opciones.put("MOD_USER",new ModificarUsuario());
            opciones.put("LST_ALUM",new ListarAlumnos());
            opciones.put("LST_LOG",new ListarBitacora());
            opciones.put("EXIT",new Salir());

            
            Statement menuQuery = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

            String usertp = getUserType(c);
            String query = "SELECT opcion_nombre,opcion_valor FROM OPCIONES_MENU WHERE tipo_user = '%s';".formatted(usertp);
            ResultSet opcionesQuery = menuQuery.executeQuery(query);
            Menu menuOpciones = new Menu(opcionesQuery,"Operaciones disponibles para ["+usertp+"]",1);

            while(true){                
                    
                menuOpciones.setSalir(false);
                int opcion = menuOpciones.escogerOpcion();
                
                opcionesQuery.absolute(opcion+1);
                
                opciones.get(opcionesQuery.getString(2)).execute(c);
        
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
    }

    public static String getUserType(Connection c){

        Scanner sc = new Scanner(System.in);
        String username,password;

        do{
            System.out.print("Ingrese su nombre de usuario: ");
            username = sc.nextLine();
            System.out.print("Ingrese su contraseña: ");
            password = sc.nextLine();
    
            try {
                PreparedStatement st = c.prepareStatement(
                    "SELECT usertype FROM OZL_USERS WHERE `username` = ? AND `password` = SHA2(?,256)"
                );

                st.setString(1, username);
                st.setString(2, password);
    
                ResultSet rs =  st.executeQuery();
    
                if (rs.next()){
                    return rs.getString(1);
                }
                else{
                    System.out.println(AnsiColorMap.colorMap.get("RED")+"\nSu usuario y/o contraseña son incorrectos.\n\n"+AnsiColorMap.effectMap.get("RESET"));
                }
            } catch (SQLException e) {
                System.out.println("Hubo un error en la consulta");
                return "";
            }
        }while(true);


    }

}
