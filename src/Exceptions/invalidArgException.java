package Exceptions;

public class invalidArgException extends Exception {
    public invalidArgException(String errorMessage){
        super("Argumento inválido: " + errorMessage);
    }
}
