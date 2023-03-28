package Opciones;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import Utilidades.Table;

public class ListarBitacora implements Opcion {
    static String selectBitacora = "SELECT * FROM OZL_BITACORA";
    public void execute (Connection c){
        try {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(selectBitacora);
            Table tabla = new Table(rs);

            tabla.flushTable();

            System.out.print("Presione ENTER para continuar...");
            System.in.read();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void sayHi(){
        System.out.println("Hola desde Listar bitacora");
    }

}
