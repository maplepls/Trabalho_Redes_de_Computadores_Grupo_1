import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;

public class PortaSaida extends Porta{

    String ID;
    int size;
    int packageTransmissionDelay;
    int packageFowardProbability;
    int retransmissionProbability;
    Queue<String> filaSaida;

    String pacote;

    FileWriter log_pacotes_transmitido_sucesso;
    FileWriter log_pacotes_retransmitidos;
    FileWriter log_pacotes_nao_tratados_saida;

    public PortaSaida(String ID, int size, int retransmissionProbability, int packageTransmissionDelay, int packageFowardProbability){
        super(ID, size, retransmissionProbability, packageTransmissionDelay);
        this.ID = super.ID;
        this.size = super.size;
        this.retransmissionProbability = super.p;
        this.packageTransmissionDelay = super.t;
        this.filaSaida = super.filaPacotes;

        this.packageFowardProbability = packageFowardProbability;

        pacote = " ";
    }

    public void guardarPacote(){ //Testar a retransmission e simular tempos de armazenamento

        if (pacote.equals(" ")){ //caso não tenha recebido nada do comutador, fica no aguardo.
            return;
        }

        int thisRetProb = retransmissionProbability;
        int p;
        do {
            p = 100 - thisRetProb;
            thisRetProb = thisRetProb/2;

            try {
                Thread.sleep(packageTransmissionDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while((double) Math.random() < (double) p / 100.00);

        String[] nomeCortado = pacote.split(" ");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        Date date = new Date();
        inserirFila(nomeCortado[0] + " " + formatter.format(date));


        pacote = " ";

    }

    public void run(){
        while(true){
            guardarPacote();
        }
    }

}
