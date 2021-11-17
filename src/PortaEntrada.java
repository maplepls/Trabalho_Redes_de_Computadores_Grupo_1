import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.Random;

public class PortaEntrada extends Porta{

    String ID;
    int size;
    int packageGenerationDelay;
    int currentPackageId;
    int dropProbability;
    Queue<String> filaEntrada;

    FileWriter log_pacotes_criado_sucesso;
    FileWriter log_pacotes_descartados;
    FileWriter log_pacotes_na_fila;

    public PortaEntrada(String ID, int size, int dropProbability, int packageGenerationDelay){
        super(ID, size, dropProbability, packageGenerationDelay);
        this.ID = super.ID;
        this.size = super.size;
        this.dropProbability = super.p;
        this.packageGenerationDelay = super.t;
        this.filaEntrada = super.filaPacotes;

        this.currentPackageId = 1;
    }

    public void criarPacote(){ //currentPackageID, dropProbability e packageGenerationDelay
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        Date date = new Date();
        String pacote = ID + "_" + currentPackageId + " " + formatter.format(date);
        currentPackageId++;

        if( (double)Math.random() < (double)dropProbability/100.00){ //Teste de drop
            //bota no log de drop
            return;
        }

        //bota no log de criado na fila

        //Se pacote foi criado, testar se fila ta cheia
        if(filaPacotes.size() >= size){
            //kill Pacote
            //sair da funcao
            return;
        }

        //bota no log de inseridos na fila
        inserirFila(pacote);
    }

    public void run (){
        while(true) {
            try {
                Thread.sleep(packageGenerationDelay);
                criarPacote();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /* cod pra criar file e append
    try (FileWriter f = new FileWriter("filename_Letra.txt", true);

        PrintWriter p = new PrintWriter(f);) {
        p.println("append")

    } catch (IOException i) {
        i.printStackTrace();
    }

    } else  {
        throw error;
    }
     */

}
