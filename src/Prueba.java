import java.util.Iterator;

public class Prueba {
    
    public static void main(String[] args) {
        Iterator<Module> modulos = ModuleLayer.boot().modules().iterator();
        Package[] paquetes = Package.getPackages();

        // while(modulos.hasNext()){image.png
        //     System.out.println(modulos.next().toString());
        // }

        for (Package package1 : paquetes) {
            System.out.println(package1.getName());
        }
    }
}
