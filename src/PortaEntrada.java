import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;

public class PortaEntrada extends Porta{

    String ID; //ID da porta
    int packageGenerationDelay; //Delay de geração de pacote
    int currentPackageId; //numero do ID do pacote atual
    int dropProbability; //probabilidade de descarte

    int size; //Tamanho máximo da fila
    LinkedList<String> filaEntrada; //fila de entreada dos pacotes

    File log_pacotes_criado_sucesso; //log de pacotes criados com sucesso
    File log_pacotes_descartados; //log de pacotes descartados (dropProbability)
    File log_pacotes_fila_cheia; //log de pacotes descartados (fila de entrada cheia)

    public PortaEntrada(String ID, int size, int packageGenerationDelay, int dropProbability){
        super(ID, size, packageGenerationDelay, dropProbability); //Super Porta
        this.ID = super.ID;
        this.size = super.size;
        this.dropProbability = super.p;
        this.packageGenerationDelay = super.t;
        this.filaEntrada = new LinkedList<String>();

        this.currentPackageId = 1; //Pacotes começam com o ID 1
    }

    public void criarPacote(){ //currentPackageID, dropProbability e packageGenerationDelay

        try {
            Thread.sleep(packageGenerationDelay); //Sleep delay de geação de pacote
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Cria o pacote (String ID + String currentPackageID + timestamp)
        String pacote = funcoesComuns.novoHorarioPacote(ID + String.valueOf(currentPackageId));
        currentPackageId++; //Proximo numero de id para o prox pacote

        //Teste de drop. Se passar no teste, pacote no log
        if( (double)Math.random() < (double)dropProbability/100.00 && !exit){
            log_pacotes_descartados = new File(ID + "log_pacotes_descartados.txt");
            funcoesComuns.escreveLog(log_pacotes_descartados, pacote);
            return;
        }

        // Se falhar, pacote criado e inserido no log
        log_pacotes_criado_sucesso = new File(ID + "log_pacotes_criado_sucesso.txt");
        funcoesComuns.escreveLog(log_pacotes_criado_sucesso, pacote);

        //Testar se fila ta cheia e inserir se não estiver
        if(filaEntrada.size() >= size && !exit){
            log_pacotes_fila_cheia = new File(ID + "log_pacotes_fila_cheia.txt");
            funcoesComuns.escreveLog(log_pacotes_fila_cheia, pacote);
            return;
        }

        //Se não solicidado para sair, inserir na fila
        if(!exit){
            inserirFila(pacote);
        }
        return;
    }

    //Inserir pacote na fila de entrada
    public void inserirFila(String pacote) { filaEntrada.add(pacote); }

    //Devolver fila entrada
    public Queue<String> getFilaEntrada(){
        return filaEntrada;
    }

    //Devolver primeiro pacote da fila entrada
    public String getFirstFilaEntrada(){
        return filaEntrada.poll();
    }


    public void run () {

        //Esperar todas as Threads .start()
        try {
            Router.barreira.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        //Loop verificando o estado de saída
        while (!exit) {
            criarPacote();
        }
        Thread.currentThread().interrupt(); //Desligar thread caso estado de saída true
    }
}
