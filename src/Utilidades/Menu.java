package Utilidades;
import java.util.Arrays;
import java.sql.ResultSetMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class Menu{
    ArrayList<String> opciones;
    ArrayList<String> retornos;
    String nombre;
    boolean salir;

    public Menu(ArrayList<String> opciones, String nombre){
        this.opciones = opciones;
        this.nombre = nombre;
    }
    public Menu(String[] opciones, String nombre){
        this.opciones = new ArrayList<String>(Arrays.asList(opciones));
        this.nombre = nombre;
    }
    public Menu(ResultSetMetaData md, String nombre){
        this.opciones = new ArrayList<String>();
        this.nombre = nombre;

        try {
            int cantidad = md.getColumnCount();
            
            for (int i = 1; i <= cantidad; i++){
                this.opciones.add(md.getColumnName(i));
            }
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
    }
    public Menu(ResultSet conjunto, String nombre,int visibleColumn){
        this.opciones = new ArrayList<String>();
        this.nombre = nombre;

        try {
            while(conjunto.next()){
                this.opciones.add(conjunto.getString(visibleColumn));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setSalir(boolean valor){
        this.salir = valor;
    }
    public ArrayList<String> getOpciones(){
        return this.opciones;
    }

    public int escogerOpcion(){
        Scanner sc = new Scanner(System.in);
        int opcion = 0;
        boolean check;

        do{
            System.out.println(
                "\n\n\n"+
                AnsiColorMap.colorMap.get("BLUE")+
                "Menu de opciones <"+this.nombre+">"+
                AnsiColorMap.effectMap.get("RESET")
            );

            for (int i = 0; i < this.opciones.size(); i++){
                System.out.println(
                    // AnsiColorMap.colorMap.get("BLUE")+
                    "\t"+(i+1)+") "+this.opciones.get(i)+
                    AnsiColorMap.effectMap.get("RESET")
                );
                }
            
            if (this.salir){
                System.out.println(
                    AnsiColorMap.colorMap.get("YELLOW")+
                    "\t"+(this.opciones.size()+1)+") Cancelar operacion"+
                    AnsiColorMap.effectMap.get("RESET")
                        
                );
            }

            System.out.print(
                AnsiColorMap.colorMap.get("BLUE")+
                "INPUT:> "+
                AnsiColorMap.effectMap.get("RESET")
            );
            try {
                opcion = sc.nextInt();
                if (this.salir)
                    check = opcion > 0 && opcion <= this.opciones.size()+1;
                else 
                    check = opcion > 0 && opcion <= this.opciones.size();
                // System.out.println(this.opciones.size()+" - "+opcion+" "+check);
                if (!check){ 

                    System.out.println(
                        "\n\n\n"+
                        AnsiColorMap.colorMap.get("RED")+
                        "Ingresa un valor que se encuentre en el intervalo"+
                        AnsiColorMap.effectMap.get("RESET")+
                        "\n"
                    );
                    sc.nextLine();
                    continue;
                }
            } catch (Exception e) {
                System.out.println(
                    
                        "\n\n\n"+   
                        AnsiColorMap.colorMap.get("RED")+
                        "Ingresa un valor numérico válido"+
                        AnsiColorMap.effectMap.get("RESET")+
                        "\n"
                    );
                    sc.nextLine();
                    continue;
                    
            }
            break;

        }while(true);    
        System.out.print(String.format("\033[%dA",1)); 
        System.out.println(                
            AnsiColorMap.colorMap.get("BLUE")+
            "INPUT:> "+
            AnsiColorMap.effectMap.get("RESET")+
            ((opcion > this.opciones.size())? "Cancelar operacion": this.opciones.get(opcion-1))
        );
        
        return opcion-1;
        
    }
    public String escogerOpcionString(){
        int opcion = this.escogerOpcion();
        return (opcion >= this.opciones.size())?"SALIR":this.opciones.get(opcion-1);
    }

    public int getSize(){
        return this.opciones.size();
    }

    public static void main(String args[]){
        String[] opciones = {
            "Opcion 1",
            "Opcion 2",
            "Opcion 3",
            "Opcion 4"
        };

        Menu menu = new Menu(opciones,"Opciones");
        System.out.println(menu.escogerOpcion());
    }



}


