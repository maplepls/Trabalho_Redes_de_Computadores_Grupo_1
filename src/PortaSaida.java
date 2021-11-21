import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.BrokenBarrierException;

public class PortaSaida extends Porta{

    String ID;
    int packageTransmissionDelay; //delay de transmissão
    int packageFowardProbability; //probabilidade de a porta ser escolhida
    int retransmissionProbability; //probabilidaded de retransmissao

    int size; //tamanho da fila de saida
    LinkedList<String> filaSaida; //fila de saida dos pacotes

    File log_pacotes_transmitido_sucesso; //log de pacotes transmitidos com sucesso
    File log_pacotes_retransmitidos; //log de pacotes que tiveram que ser retransmitidos
    File log_pacotes_nao_tratados_saida; //log de pacotes que estavam em processo de serem, ou não foram tratados

    public PortaSaida(String ID, int size, int packageFowardProbability, int packageTransmissionDelay, int retransmissionProbability){
        super(ID, size, packageTransmissionDelay, retransmissionProbability); //Super Porta
        this.ID = super.ID;
        this.size = super.size;
        this.retransmissionProbability = super.p;
        this.packageTransmissionDelay = super.t;
        this.filaSaida = new LinkedList<String>();
        this.packageFowardProbability = packageFowardProbability;
    }

    public void transmitirPacote(){ //Testar a retransmission e simular tempos de armazenamento

        //Puxar da fila
        String pacote = filaSaida.poll();

        //caso não haja pacotes na fila, fica no reinicia a função e espera para verificar se há novos pacotes.
        if (pacote == null || exit){
            try {
                Thread.sleep(packageTransmissionDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        pacote = funcoesComuns.novoHorarioPacote(pacote); //Atribuir horario em que pacote foi puxado

        int thisRetProb = retransmissionProbability;
        int p = 100 - thisRetProb; //probabilidade de TRANSMITIR pacote

        try {
            Thread.sleep(packageTransmissionDelay); //delay de transmissão
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //teste de transmissão. Se falhar, delay e tenta retransmitir
        while(((double) Math.random() > (double) p / 100.00) && !exit) {

            log_pacotes_retransmitidos  = new File(ID + "log_pacotes_retransmitidos.txt");
            funcoesComuns.escreveLog(log_pacotes_retransmitidos, pacote);

            thisRetProb = thisRetProb/2; //a retransmission probability diminui pela metade com o tempo
            p = 100 - thisRetProb; // portanto p chega a 100 depois ded um tempo

            try {
                Thread.sleep(packageTransmissionDelay); //delay de (re)transmissao
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //se em estado de saída, colocar no log de não tratados. Se não em estado de saída, em log de transmitidos
        if(!exit){
            log_pacotes_transmitido_sucesso = new File(ID + "log_pacotes_transmitido_sucesso.txt");
            funcoesComuns.escreveLog(log_pacotes_transmitido_sucesso, pacote);

        } else{ //log de pacotes que não foram tratados pela porta de saida (log estava no processo de ser tratado)
            log_pacotes_nao_tratados_saida = new File(ID + "log_pacotes_nao_tratados_saida.txt");
            funcoesComuns.escreveLog(log_pacotes_nao_tratados_saida, pacote);
        }

    }

    //pegar tamanho da fila de saida
    public boolean filaCheia(){
        return filaSaida.size() == size;
    }

    //pegar probabilidade de a porta ser escolhida
    public int getPackageFowardProbability() { return packageFowardProbability; }

    //inserir pacote na fila
    public void inserirFila(String pacote) { filaSaida.add(pacote); }

    public void run(){

        //Esperar todas as Threads .start()
        try {
            //
            Router.barreira.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();

        }

        //Loop verificando o estado de saída
        while(!exit){
            transmitirPacote();
        }

        //log de pacotes não tratados pela porta de saída (que ainda estão na fila)
        String novoPacote;
        while((novoPacote = filaSaida.poll()) != null){
            log_pacotes_nao_tratados_saida = new File(ID + "log_pacotes_nao_tratados_saida.txt");
            funcoesComuns.escreveLog(log_pacotes_nao_tratados_saida, novoPacote);
        }

        Thread.currentThread().interrupt(); //Desligar thread caso estado de saída true
    }
}
