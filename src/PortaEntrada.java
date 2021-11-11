import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PortaEntrada extends Porta{

    String ID;
    int size;
    int packageGenerationDelay;
    int currentPackageId;
    int dropProbability;
    String[] filaEntrada;

    String pacote;

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

    }

    /*public void inserirFila(){ //pacote e dropProbability

    }

    public void removerFila(){ //pacote e depende do comutador

    }*/

    public void run (){

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

    /* cod para pegar data e hora atual ja no formato correto
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
    Date date = new Date();
    System.out.println(formatter.format(date));
     */

}
