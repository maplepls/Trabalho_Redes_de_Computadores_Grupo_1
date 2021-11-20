import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.io.FileWriter;
import java.util.Date;

public class funcoesComuns {

    public static String novoHorarioPacote(String pacote){
        pacote = pacote.split(" ")[0];
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        Date date = new Date();
        return pacote + " " + formatter.format(date);
    }

    public static void escreveLog(String nomeArquivo, String pacote){

        try {
            FileWriter arquivo = new FileWriter(nomeArquivo + ".txt", true);
            PrintWriter p = new PrintWriter(arquivo);
            p.println(pacote);
            arquivo.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
