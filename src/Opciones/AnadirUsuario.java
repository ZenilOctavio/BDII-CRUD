package Opciones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.sql.Statement;
import Utilidades.AnsiColorMap;

public class AnadirUsuario implements Opcion {
    static Pattern noInjection = Pattern.compile("(ALTER|CREATE|DELETE|DROP|EXEC(UTE){0,1}|INSERT( +INTO){0,1}|MERGE|SELECT|UPDATE|UNION( +ALL){0,1})",Pattern.CASE_INSENSITIVE);
    static String
    URL = "jdbc:mysql://localhost:3306/sistema_alumnos?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true",
    USER = "root",
    PASSWORD = "";

    HashMap<String,Pattern> expresiones;
    String selectUsuarios = "SELECT id,username,`password`,usertype,email FROM OZL_USERS WHERE 0;";
    static String insert = "INSERT INTO OZL_USERS (`username`,`password`,`usertype`,`email`) VALUES ('%s',SHA2('%s',256),'%s','%s');";
    

    public AnadirUsuario(){

        this.expresiones = new HashMap<>();
        // this.expresiones.put("password", Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$"));
        this.expresiones.put("password", Pattern.compile("^[A-Za-z0-9]+$"));
        this.expresiones.put("usertype", Pattern.compile("(USER|ADMIN)"));
        this.expresiones.put("username", Pattern.compile("^[A-Za-z0-9]+$"));
        this.expresiones.put("email", Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"));
        
    }
    
    
    public void execute (Connection c){
        Scanner sc = new Scanner(System.in);
        String username,email,password,usertype;

        System.out.println("\n\n\nAÑADIR USUARIO\n\n");
        
        do{
            System.out.print(
                AnsiColorMap.colorMap.get("BLUE")+
                "Ingrese su nombre de usuario: "+
                AnsiColorMap.effectMap.get("RESET")
            );
            username = sc.nextLine();
        }while(!verificarIngreso("username", 18, username));

        do{
            System.out.print(
                AnsiColorMap.colorMap.get("BLUE")+
                "Ingrese su contraseña: "+
                AnsiColorMap.effectMap.get("RESET")
            );
            password = sc.nextLine();
        }while(!verificarIngreso("password", 64, password));

        do{
            System.out.print(
                AnsiColorMap.colorMap.get("BLUE")+
                "Ingrese el email: "+
                AnsiColorMap.effectMap.get("RESET")
            );
            email = sc.nextLine();
        }while(!verificarIngreso("email", 55, email));

        do{
            System.out.print(
                AnsiColorMap.colorMap.get("BLUE")+
                "Ingrese el tipo de usuario: "+
                AnsiColorMap.effectMap.get("RESET")
            );
            usertype = sc.nextLine();
        }while(!verificarIngreso("usertype", 55, usertype));

        insert = insert.formatted(username,password,usertype,email);

        try {
            Statement st = c.createStatement();
            int rows = st.executeUpdate(insert);

            if (rows == 0){
                System.out.println(
                    AnsiColorMap.colorMap.get("RED")+
                    "No se pudo crear el nuevo usuario."+
                    AnsiColorMap.effectMap.get("RESET")
                );
            }
            else{
                System.out.println(
                    AnsiColorMap.colorMap.get("GREEN")+
                    "El usuario %s se registró exitosamente.".formatted(username)+
                    AnsiColorMap.effectMap.get("RESET")
                );
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Hubo un error en la creación de un usuario");
        }

        
    }

    public boolean verificarIngreso(String nombre,int longitud,String ingreso){
        Matcher injection = noInjection.matcher(ingreso);
        Matcher column = this.expresiones.get(nombre).matcher(ingreso);
        if (ingreso.length() > longitud || injection.matches() || !column.matches()){

            System.out.println(
                "\n\n\n"+
                AnsiColorMap.colorMap.get("RED")+
                "Formato de usuario inválido"+
                AnsiColorMap.effectMap.get("RESET")+
                "\n\n\n"
            );
            
            return false;
        }

        return true;
        
    }


    public static void main(String args[]){

        try {
            Connection c = DriverManager.getConnection(URL, USER, PASSWORD);

            AnadirUsuario u = new AnadirUsuario();
            u.execute(c);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        
    }

    public void sayHi(){
        System.out.println("Hola desde Anadir usuario");
    }

    
}
