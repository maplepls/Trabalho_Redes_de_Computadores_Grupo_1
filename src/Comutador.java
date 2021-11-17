import java.io.FileWriter;

public class Comutador implements Runnable{
    int switchDelay;

    FileWriter log_pacotes_encaminhado_sucesso;
    FileWriter log_pacotes_fila_cheia;
    FileWriter log_pacotes_nao_tratados_comutacao;

    private static boolean exit = false;

    public Comutador(int switchDelay){
        this.switchDelay = switchDelay;
    }

    public void run(){

    }

    public void parar(){
        exit = true;
    }
}

//Semaforo binario == mutex???
