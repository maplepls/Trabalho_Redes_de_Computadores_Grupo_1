import java.io.File;
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

    public static void escreveLog(File arquivo, String pacote){
        try {

            FileWriter arquivoEscrita = new FileWriter("./registros/" + arquivo, true);
            PrintWriter p = new PrintWriter(arquivoEscrita);
            p.println(pacote);
            arquivoEscrita.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
