import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;

public class PortaEntrada extends Porta{

    String ID;
    int size;
    int packageGenerationDelay;
    int currentPackageId;
    int dropProbability;
    Queue<String> filaEntrada;

    String log_pacotes_criado_sucesso;
    String log_pacotes_descartados;
    String log_pacotes_fila_cheia;

    public PortaEntrada(String ID, int size, int packageGenerationDelay, int dropProbability){
        super(ID, size, packageGenerationDelay, dropProbability);
        this.ID = super.ID;
        this.size = super.size;
        this.dropProbability = super.p;
        this.packageGenerationDelay = super.t;
        this.filaEntrada = super.filaPacotes;

        this.currentPackageId = 1;

        log_pacotes_criado_sucesso = ID + "log_pacotes_criado_sucesso";
        log_pacotes_descartados = ID + "log_pacotes_descartados";
        log_pacotes_fila_cheia = ID + "log_pacotes_fila_cheia";
    }

    public void criarPacote(){ //currentPackageID, dropProbability e packageGenerationDelay
        String pacote = funcoesComuns.novoHorarioPacote(ID + String.valueOf(currentPackageId));
        currentPackageId++;

        if( (double)Math.random() < (double)dropProbability/100.00 && !exit){ //Teste de drop
            funcoesComuns.escreveLog(log_pacotes_descartados, pacote);
            return;
        }

        funcoesComuns.escreveLog(log_pacotes_criado_sucesso, pacote);

        //Se pacote foi criado, testar se fila ta cheia
        if(filaPacotes.size() >= size && !exit){
            funcoesComuns.escreveLog(log_pacotes_fila_cheia, funcoesComuns.novoHorarioPacote(pacote));
            return;
        }

        if(!exit){
            inserirFila(pacote);
        }

    }

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
            try {
                Thread.sleep(packageGenerationDelay);
                if (!exit) {
                    criarPacote();
                    //System.out.println("oi da Porta de entrada " + ID);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Thread.currentThread().interrupt();
    }
}
