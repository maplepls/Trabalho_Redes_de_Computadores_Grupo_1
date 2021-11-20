import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;

public class PortaSaida extends Porta{

    String ID;
    int size;
    int packageTransmissionDelay;
    int packageFowardProbability;
    int retransmissionProbability;
    LinkedList<String> filaSaida;

    String log_pacotes_transmitido_sucesso;
    String log_pacotes_retransmitidos;
    String log_pacotes_nao_tratados_saida;

    public PortaSaida(String ID, int size, int packageFowardProbability, int packageTransmissionDelay, int retransmissionProbability){
        super(ID, size, packageTransmissionDelay, retransmissionProbability);
        this.ID = super.ID;
        this.size = super.size;
        this.retransmissionProbability = super.p;
        this.packageTransmissionDelay = super.t;
        this.filaSaida = new LinkedList<String>();
        this.packageFowardProbability = packageFowardProbability;

        log_pacotes_transmitido_sucesso = ID + "log_pacotes_transmitido_sucesso";
        log_pacotes_retransmitidos  = ID + "log_pacotes_retransmitidos";
        log_pacotes_nao_tratados_saida = ID + "log_pacotes_nao_tratados_saida";

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

            funcoesComuns.escreveLog(log_pacotes_retransmitidos, funcoesComuns.novoHorarioPacote(pacote));

            thisRetProb = thisRetProb/2;
            p = 100 - thisRetProb;

            try {
                Thread.sleep(packageTransmissionDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(!exit){
            funcoesComuns.escreveLog(log_pacotes_transmitido_sucesso, funcoesComuns.novoHorarioPacote(pacote));
        } else{
            funcoesComuns.escreveLog(log_pacotes_nao_tratados_saida, funcoesComuns.novoHorarioPacote(pacote));
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
            funcoesComuns.escreveLog(log_pacotes_nao_tratados_saida, novoPacote);
        }

        Thread.currentThread().interrupt();
    }
}
