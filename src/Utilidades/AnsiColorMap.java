package Utilidades;
import java.util.HashMap;

public class AnsiColorMap {
    public static final HashMap<String, String> effectMap = new HashMap<String, String>() {{
        put("RESET", "\u001B[0m");
        put("BOLD", "\u001B[1m");
        put("FAINT", "\u001B[2m");
        put("ITALIC", "\u001B[3m");
        put("UNDERLINE", "\u001B[4m");
        put("BLINK", "\u001B[5m");
        put("REVERSE", "\u001B[7m");
        put("STRIKE", "\u001B[9m");
    }};
    
    public static final HashMap<String, String> colorMap = new HashMap<String, String>() {{
        put("BLACK", "\u001B[30m");
        put("RED", "\u001B[31m");
        put("GREEN", "\u001B[32m");
        put("YELLOW", "\u001B[33m");
        put("BLUE", "\u001B[34m");
        put("PURPLE", "\u001B[35m");
        put("CYAN", "\u001B[36m");
        put("WHITE", "\u001B[37m");
        put("BLACK_BG", "\u001B[40m");
        put("RED_BG", "\u001B[41m");
        put("GREEN_BG", "\u001B[42m");
        put("YELLOW_BG", "\u001B[43m");
        put("BLUE_BG", "\u001B[44m");
        put("PURPLE_BG", "\u001B[45m");
        put("CYAN_BG", "\u001B[46m");
        put("WHITE_BG", "\u001B[47m");
    }};
}