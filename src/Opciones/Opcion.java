package Opciones;
import java.sql.Connection;

public interface Opcion {
    public void execute(Connection c);
    public void sayHi();
}



