package Exceptions;

public class invalidPackageFowardSum extends Exception {
    public invalidPackageFowardSum(String errorMessage){
        super("Soma inválida de Package Foward probability das portas de saída: " + errorMessage);
    }
}
