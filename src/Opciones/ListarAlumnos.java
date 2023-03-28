package Opciones;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import Utilidades.Table;

public class ListarAlumnos implements Opcion {
    static String selectAlumnos = "SELECT * FROM alumnos";
    public void execute (Connection c){
        try {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(selectAlumnos);
            Table tabla = new Table(rs);

            tabla.setTitulo("ALUMNOS");
            tabla.flushTable();

            System.out.print("Presione ENTER para continuar...");
            System.in.read();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void sayHi(){
        System.out.println("Hola desde Listar alumnos");
    }


}
