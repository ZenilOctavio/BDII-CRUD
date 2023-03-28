package Utilidades;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class Table {
    
    ArrayList <String> headers;
    ArrayList<Integer> column_lengths;
    ArrayList <ArrayList<String>> data; 
    String titulo;
    
    public Table(ArrayList <String> headers,ArrayList <ArrayList<String>> data){
        this.headers = headers;
        this.data = data;
        this.column_lengths = new ArrayList<Integer>();
        this.titulo = "";
        
        this.calculateTable();
    }
    
    public Table(ResultSet res){
        this.headers = new ArrayList<String>();
        this.column_lengths = new ArrayList<Integer>();
        this.data = new ArrayList<ArrayList<String>>();
        this.titulo = "";
        
        try{
            ResultSetMetaData metaDatos = res.getMetaData();
            int numeroColumnas = metaDatos.getColumnCount();
            for (int i = 1; i <= numeroColumnas; i++) {
                headers.add(metaDatos.getColumnName(i));
            }
            // System.out.println(headers.toString()); 

            while (res.next()) {
                this.data.add(new ArrayList<String>());
                for (int i = 1; i <= numeroColumnas; i++) {
                    this.data.get(this.data.size()-1).add(res.getString(i));
                    // System.out.println(res.getString(i));
                }
                // System.out.println(this.data.get(this.data.size()-1).toString());
            }
        }
        catch (SQLException error){
            System.out.println("Hubo un error en la creación de la tabla");
            System.out.println(error.getMessage());
        }
        this.calculateTable();
    }

    public ArrayList<String> getHeaders(){
        return this.headers;
    }

    public ArrayList<ArrayList<String>> getData(){
        return this.data;
    }

    public void calculateTable(){  
        for (int i = 0; i < headers.size(); i++){
            column_lengths.add(this.headers.get(i).length());
            // System.out.println(this.column_lengths.s);
        }
        // System.out.println(headers.toString());    
        // System.out.println(column_lengths.toString());    

        for (int i = 0; i < this.data.size(); i++){
            // System.out.println(data.get(i).toString());
            for (int j = 0; j < this.data.get(i).size(); j++){
                if (this.column_lengths.get(j) < this.data.get(i).get(j).length()){
                    this.column_lengths.set(j,this.data.get(i).get(j).length());
                }
            }
            // System.out.println(this.column_lengths.get(i));
        }

    }

    public void printHeaders(){
        int padLeft,padRight;

        System.out.print(            
            AnsiColorMap.colorMap.get("WHITE_BG")+
            AnsiColorMap.effectMap.get("BOLD")+
            AnsiColorMap.colorMap.get("BLACK")+
            "_"
        );

        for (int i = 0; i < headers.size(); i++){
            System.out.print("_".repeat(column_lengths.get(i)+3));
        }
        System.out.println();
        
        System.out.print("|");

        for (int i = 0; i < headers.size(); i++){
            padLeft = (column_lengths.get(i) - headers.get(i).length())/2;
            padRight = column_lengths.get(i) - padLeft - headers.get(i).length();
            System.out.print(
                " ".repeat(padLeft+1)+
                // AnsiColorMap.colorMap.get("WHITE_BG")+
                // AnsiColorMap.effectMap.get("BOLD")+
                // AnsiColorMap.colorMap.get("BLACK")+
                headers.get(i)+
                " ".repeat(padRight+1)+
                "|"
            );
        }

        System.out.print(                
            AnsiColorMap.effectMap.get("RESET")+
            "\n"
            // AnsiColorMap.colorMap.get("BLACK")
        );
    }

    public void printData(){
        int padLeft,padRight;
 
        for (int j = 0; j < data.size(); j++){
            System.out.print("|");
            for (int i = 0; i < data.get(j).size(); i++){
                padLeft = (column_lengths.get(i) - data.get(j).get(i).length())/2;
                padRight = column_lengths.get(i) - padLeft - data.get(j).get(i).length();
                System.out.print(
                    " ".repeat(padLeft+1)+
                    data.get(j).get(i)+
                    " ".repeat(padRight+1)+
                    "|"
                );
            }
            System.out.println();
        }
    }

    public void printTable(){
        int padLeft;
        int total = 0;

        for (int i = 0; i < this.column_lengths.size(); i++){
            total += this.column_lengths.get(i);
        }

        // padRight = (total - this.titulo.length()) / 2;
        padLeft = ((total + (this.column_lengths.size()+1) + (this.column_lengths.size()*2)) - this.titulo.length())/2;

        System.out.println(
            " ".repeat(padLeft-1)+
            AnsiColorMap.colorMap.get("WHITE_BG")+
            AnsiColorMap.effectMap.get("BOLD")+
            " "+
            this.titulo+
            " "+
            AnsiColorMap.effectMap.get("RESET")
        );
        
        printHeaders();
        printData();
    }

    public void flushTable(){
        printTable();
        this.data = null;
        this.headers = null;
        this.column_lengths = null;
    }
    public void setTitulo(String titulo){
        this.titulo = titulo;        
    }

    public static void main(String args[]){
        String[] headers = {"Header-1","Header-2","Header-3","Header-4","Header-5"};

        ArrayList<String> Cabeceras = new ArrayList<String>(Arrays.asList(headers));
        String[][] data = {
            {"Campo-1","Campo-2","Campo-3","Campo-4","Campo-5"},
            {"Campo-1","Campo-2","Campo-3","Campo-4","Campo-5"},
            {"Campo-1","Campo-2","Campo-35","Campo-4","Campo-5"},
            {"Campo-1vvv","Campo-2","Campo-3","Campo-4","Campo-512"},
            {"Campo-1","Campo-2","Campo-3","Campo-4","Campo-5"}
            // 10 8 8 8 9
        };

        ArrayList<ArrayList<String>> Datos = new ArrayList<ArrayList<String>>();

        for (int i = 0; i < data.length; i++){
            Datos.add(new ArrayList<String>(Arrays.asList(data[i])));
        }

        Table table = new Table(Cabeceras,Datos);

        table.setTitulo("Datos tomados");
        table.printTable();
        // table.printHeaders();
        // table.printData();
    }
}
