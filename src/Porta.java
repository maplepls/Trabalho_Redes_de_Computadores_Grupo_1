public class Porta implements Runnable{

    String ID;
    int size; //tamanho da fila de pacotes
    int p; //probabilidade de drop do pacote ou de escolha de porta de saída
    int t; //tempo de criação ou guardar na fila
    String[] filaPacotes; //fila de pacotes (entrada ou saída)

    public Porta(String ID, int size, int p, int t) {
        this.ID = ID;
        this.size  = size;
        this.p = p;
        this.t = t;
        filaPacotes = new String[size];
    }

    public void inserirFila(){ //pacote e dropProbability ou retransmissionProbability

    }

    public void removerFila(){ //retirar pacote dependendo do comutador no caso do de Entrada

    }

    public void run(){}

}
