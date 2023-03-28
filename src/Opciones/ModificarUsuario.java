package Opciones;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;
import Utilidades.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ModificarUsuario implements Opcion{
    static Pattern noInjection = Pattern.compile("(ALTER|CREATE|DELETE|DROP|EXEC(UTE){0,1}|INSERT( +INTO){0,1}|MERGE|SELECT|UPDATE|UNION( +ALL){0,1})",Pattern.CASE_INSENSITIVE);
    HashMap<String,Pattern> expresiones;

    String selectUsuarios = "SELECT id,username,`password`,usertype,email FROM OZL_USERS;";
    String update = "UPDATE OZL_USERS SET `%s` = %s WHERE id = %s;";

    public ModificarUsuario(){
        this.expresiones = new HashMap<>();
        // this.expresiones.put("password", Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$"));
        this.expresiones.put("password", Pattern.compile("^[A-Za-z0-9]+$"));
        this.expresiones.put("usertype", Pattern.compile("(USER|ADMIN)"));
        this.expresiones.put("username", Pattern.compile("^[A-Za-z0-9]+$"));
        this.expresiones.put("email", Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"));
        
    }

    
    
    public void execute(Connection c){
        Scanner sc = new Scanner(System.in);
        int id;
        try{
            Statement statement = c.createStatement();
            ResultSet resultados = statement.executeQuery(selectUsuarios);
            ResultSetMetaData columnas = resultados.getMetaData();
            Table tablaUsuarios = new Table(resultados);

            tablaUsuarios.setTitulo("MODIFICAR USUARIO");

            do{ //Ingresar el ID
                // System.out.println("\n\n\n\n\t\t\t\t\t=====  MODDIFICAR USUARIO  =====");
                tablaUsuarios.printTable();
                System.out.println("\n");
                System.out.print("Ingrese el id del usuario que desea modificar: ");
                id = sc.nextInt();

            } while(id < 1 || id > tablaUsuarios.getData().size());

            Menu menuColumnas = new Menu(tablaUsuarios.getHeaders(),"Campo a modificar");
            menuColumnas.setSalir(true);
            

            int opcion = menuColumnas.escogerOpcion();
            if (opcion >= menuColumnas.getSize()) {
                System.out.println("\n\nOperacion cancelada\n");
                return;
            }
            boolean check;
            String scan = "";

            do{
                System.out.print(
                    "\n\n"+
                    AnsiColorMap.colorMap.get("BLUE")+
                    "Ingrese el nuevo valor para ["+
                    columnas.getColumnName(opcion+1)+
                    ":"+
                    columnas.getColumnTypeName(opcion+1)+
                    "("+
                    columnas.getColumnDisplaySize(opcion+1)+
                    ")]: "+
                    AnsiColorMap.effectMap.get("RESET")
                );
                sc.nextLine();
                scan = sc.nextLine();
                check = verificarIngreso(columnas.getColumnName(opcion+1),columnas.getColumnDisplaySize(opcion+1), scan);
            }while(!check);
            
            Statement updateQuery = c.createStatement();
            // System.out.println(update);

            if (columnas.getColumnName(opcion+1).contentEquals("password")){
                scan = String.format("SHA2(\'%s\',256)",scan);
            }
            else if (!columnas.getColumnName(opcion+1).contentEquals("id")){
                scan = String.format("\'%s\'", scan);
            }
            // System.out.println(scan);

            update = String.format(update,columnas.getColumnName(opcion+1),scan,id);
            // System.out.println(update);


            int rows = updateQuery.executeUpdate(update);
            
            System.out.println(
                "\n\n\n"+
                (
                    (rows > 0)?
                        AnsiColorMap.colorMap.get("GREEN")+
                        "Se actualizaron %s registro(s)".formatted(rows)
                        :
                        AnsiColorMap.colorMap.get("YELLOW")+
                        "No se actualizó ningún registro"

                )+
                AnsiColorMap.effectMap.get("RESET")
            );
            
        }
        catch(SQLException err){
            System.out.println(err.getMessage());
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
    
    public static void main(String[] args) {
        String  URL = "jdbc:mysql://localhost:3306/sistema_alumnos?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true",
                USER = "root",
                PASSWORD = "";

        try {
            Connection c = DriverManager.getConnection(URL, USER, PASSWORD);
            ModificarUsuario mod = new ModificarUsuario();
            mod.execute(c);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void sayHi(){
        System.out.println("Hola desde Modificar usuario");
    }

}