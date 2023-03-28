import java.lang.reflect.*;
import java.security.Provider;
import java.util.ServiceLoader;

import Opciones.*;

public class CargaOpciones{
    public static void main(String[] args) {
        AnadirUsuario adduser = new AnadirUsuario();
        ModificarUsuario moduser = new ModificarUsuario();

        
        System.out.println(AnadirUsuario.class.getModule().getName());
    }
}