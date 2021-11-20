import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class PortaSaida extends Porta{

    String ID;
    int size;
    int packageTransmissionDelay;
    int packageFowardProbability;
    int retransmissionProbability;
    CyclicBarrier barreira;
    Queue<String> filaSaida;

    FileWriter log_pacotes_transmitido_sucesso;
    FileWriter log_pacotes_retransmitidos;
    FileWriter log_pacotes_nao_tratados_saida;

    public PortaSaida(String ID, int size, int retransmissionProbability, int packageTransmissionDelay, int packageFowardProbability, CyclicBarrier barreira){
        super(ID, size, retransmissionProbability, packageTransmissionDelay, barreira);
        this.barreira = super.barreira;
        this.ID = super.ID;
        this.size = super.size;
        this.retransmissionProbability = super.p;
        this.packageTransmissionDelay = super.t;
        this.filaSaida = super.filaPacotes;

        this.packageFowardProbability = packageFowardProbability;
    }

    public void guardarPacote(){ //Testar a retransmission e simular tempos de armazenamento

        String pacote = filaPacotes.poll();

        if (pacote == null){ //caso não tenha recebido nada do comutador, fica no aguardo.
            return;
        }

        int thisRetProb = retransmissionProbability;
        int p = 100 - thisRetProb;

        try {
            Thread.sleep(packageTransmissionDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while(((double) Math.random() < (double) p / 100.00) && !exit) {

            //bota no log de retransmissao

            p = 100 - thisRetProb;
            thisRetProb = thisRetProb/2;

            try {
                Thread.sleep(packageTransmissionDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(!exit){
            //bota no log de pacotes inseridos com sucesso
        }

        String[] nomeCortado = pacote.split(" ");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        Date date = new Date();
        //inserirFila(nomeCortado[0] + " " + formatter.format(date));

    }

    public boolean filaCheia(){
        return filaSaida.size() == size;
    }

    public int getPackageFowardProbability(){
        return packageFowardProbability;
    }


    public void run(){
        try {
            barreira.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        while(!exit){
            guardarPacote();
        }
    }

}
