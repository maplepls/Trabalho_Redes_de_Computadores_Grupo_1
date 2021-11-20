import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.BrokenBarrierException;

public class PortaSaida extends Porta{

    String ID;
    int size;
    int packageTransmissionDelay;
    int packageFowardProbability;
    int retransmissionProbability;
    LinkedList<String> filaSaida;

    File log_pacotes_transmitido_sucesso;
    File log_pacotes_retransmitidos;
    File log_pacotes_nao_tratados_saida;

    public PortaSaida(String ID, int size, int packageFowardProbability, int packageTransmissionDelay, int retransmissionProbability){
        super(ID, size, packageTransmissionDelay, retransmissionProbability);
        this.ID = super.ID;
        this.size = super.size;
        this.retransmissionProbability = super.p;
        this.packageTransmissionDelay = super.t;
        this.filaSaida = new LinkedList<String>();
        this.packageFowardProbability = packageFowardProbability;
    }

    public void transmitirPacote(){ //Testar a retransmission e simular tempos de armazenamento

        String pacote = filaSaida.poll();

        if (pacote == null || exit){ //caso não tenha recebido nada do comutador, fica no aguardo.
            return;
        }

        int thisRetProb = retransmissionProbability;
        int p = 100 - thisRetProb; //probabilidade de TRANSMITIR

        try {
            Thread.sleep(packageTransmissionDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while(((double) Math.random() > (double) p / 100.00) && !exit) {

            log_pacotes_retransmitidos  = new File(ID + "log_pacotes_retransmitidos.txt");
            funcoesComuns.escreveLog(log_pacotes_retransmitidos, pacote);

            thisRetProb = thisRetProb/2;
            p = 100 - thisRetProb;

            try {
                Thread.sleep(packageTransmissionDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(!exit){
            log_pacotes_transmitido_sucesso = new File(ID + "log_pacotes_transmitido_sucesso.txt");
            funcoesComuns.escreveLog(log_pacotes_transmitido_sucesso, pacote);
        } else{
            log_pacotes_nao_tratados_saida = new File(ID + "log_pacotes_nao_tratados_saida.txt");
            funcoesComuns.escreveLog(log_pacotes_nao_tratados_saida, pacote);
        }

    }

    public boolean filaCheia(){
        return filaSaida.size() == size;
    }

    public int getPackageFowardProbability(){
        return packageFowardProbability;
    }

    public void inserirFila(String pacote) { filaSaida.add(pacote); }

    public void run(){

        try {
            Router.barreira.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();

        }

        while(!exit){
            try {
                Thread.sleep(packageTransmissionDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            transmitirPacote();
        }

        String novoPacote;
        while((novoPacote = filaSaida.poll()) != null){
            log_pacotes_nao_tratados_saida = new File(ID + "log_pacotes_nao_tratados_saida.txt");
            funcoesComuns.escreveLog(log_pacotes_nao_tratados_saida, novoPacote);
        }

        Thread.currentThread().interrupt();
    }
}
