import java.io.FileWriter;

public class PortaSaida extends Porta{

    String ID;
    int size;
    int packageTransmissionDelay;
    int packageFowardProbability;
    int retransmissionProbability;
    String[] filaSaida;

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
    }

    public void retransmission(){} //retransmissionProbability, usar inserirFila


    /*public void inserirFila(){ //pacote e dropProbability

    }

    public void removerFila(){ //pacote e depende do comutador

    }*/

}
