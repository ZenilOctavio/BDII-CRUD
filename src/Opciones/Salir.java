package Opciones;

import java.sql.Connection;
import Utilidades.*;

public class Salir implements Opcion {

    public Salir(){}
    
    public void execute(Connection c){
        
        try {
            c.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(
            AnsiColorMap.colorMap.get("BLUE")+
            "Sesion terminada"+
            AnsiColorMap.effectMap.get("RESET")
        );
        System.exit(0);

    }

    public void sayHi(){
        System.out.println("Hola desde Salir");
    }

    
}
