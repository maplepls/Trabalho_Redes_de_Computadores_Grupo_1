package Exceptions;

public class invalidComponentQuantity extends Exception {
    public invalidComponentQuantity(){
        super("A quantidade de algum componente está errado, favor checar arquivo de entrada e arquivo de instrução ");
    }
}
