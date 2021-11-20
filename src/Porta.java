import java.text.SimpleDateFormat;
import java.util.*;

public class Porta implements Runnable{

    String ID;
    int size; //tamanho da fila de pacotes
    int p; //probabilidade de drop do pacote ou de escolha de porta de saída
    int t; //tempo de criação ou guardar na fila
    Queue<String> filaPacotes; //fila de pacotes (entrada ou saída)
    CyclicBarrier barreira;

    public static boolean exit = false;

    public Porta(String ID, int size, int t, int p) {
        this.ID = ID;
        this.size  = size;
        this.p = p;
        this.t = t;
        filaPacotes = new LinkedList<String>();
    }

    public String novoHorarioPacote(String pacote){
        pacote = pacote.split(" ")[0];
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        Date date = new Date();
        return pacote + " " + formatter.format(date);
    }

    public void inserirFila(String idPacote){ //pacote e dropProbability ou retransmissionProbability
        filaPacotes.add(idPacote);
    }

    public void removerFila(){ //retirar pacote dependendo do comutador no caso do de Entrada
        filaPacotes.poll();
    }

    public void run(){}

    public void parar(){
        exit = true;
    }

}
