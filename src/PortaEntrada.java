import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;

public class PortaEntrada extends Porta{

    String ID;
    int size;
    int packageGenerationDelay;
    int currentPackageId;
    int dropProbability;
    LinkedList<String> filaEntrada;

    File log_pacotes_criado_sucesso;
    File log_pacotes_descartados;
    File log_pacotes_fila_cheia;

    public PortaEntrada(String ID, int size, int packageGenerationDelay, int dropProbability){
        super(ID, size, packageGenerationDelay, dropProbability);
        this.ID = super.ID;
        this.size = super.size;
        this.dropProbability = super.p;
        this.packageGenerationDelay = super.t;
        this.filaEntrada = new LinkedList<String>();

        this.currentPackageId = 1;
    }

    public void criarPacote(){ //currentPackageID, dropProbability e packageGenerationDelay
        String pacote = funcoesComuns.novoHorarioPacote(ID + String.valueOf(currentPackageId));
        currentPackageId++;

        if( (double)Math.random() < (double)dropProbability/100.00 && !exit){ //Teste de drop
            log_pacotes_descartados = new File(ID + "log_pacotes_descartados.txt");
            funcoesComuns.escreveLog(log_pacotes_descartados, pacote);
            return;
        }

        log_pacotes_criado_sucesso = new File(ID + "log_pacotes_criado_sucesso.txt");
        funcoesComuns.escreveLog(log_pacotes_criado_sucesso, pacote);

        //Se pacote foi criado, testar se fila ta cheia
        if(filaEntrada.size() >= size && !exit){
            log_pacotes_fila_cheia = new File(ID + "log_pacotes_fila_cheia.txt");
            funcoesComuns.escreveLog(log_pacotes_fila_cheia, pacote);
            return;
        }

        if(!exit){
            inserirFila(pacote);
        }
        return;
    }

    public void inserirFila(String pacote) { filaEntrada.add(pacote); }

    public Queue<String> getFilaEntrada(){
        return filaEntrada;
    }

    public String getFirstFilaEntrada(){
        return filaEntrada.poll();
    }


    public void run () {

        try {
            Router.barreira.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        while (!exit) {
            criarPacote();
        }
        Thread.currentThread().interrupt();
    }
}
