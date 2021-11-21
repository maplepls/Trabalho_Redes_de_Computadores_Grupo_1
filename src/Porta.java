import java.util.*;

public class Porta implements Runnable{

    String ID;
    int size; //tamanho da fila de pacotes
    int p; //probabilidade de drop do pacote ou de escolha de porta de saída
    int t; //tempo de criação ou guardar na fila

    public static boolean exit; //Estado de saida. Caso true, o sistema começará a desligar

    public Porta(String ID, int size, int t, int p) {
        this.ID = ID;
        this.size  = size;
        this.p = p;
        this.t = t;
        this.exit = false;
    }
    public void run(){};

}
