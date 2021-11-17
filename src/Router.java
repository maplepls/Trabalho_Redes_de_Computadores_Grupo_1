import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Router {

    String nomeArquivo;

    ThreadGroup portasEntrada = new ThreadGroup("PortasEntrada");
    ThreadGroup portasSaida = new ThreadGroup("PortasSaida");

    PortaEntrada pEntrada;
    PortaSaida pSaida;
    Comutador comutador;

    public Router(String arg){
        this.nomeArquivo = arg;
    }

    public void rodarRoteador() throws IOException {

       String caminhoArquivo = nomeArquivo;
       BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo));
       try {
           String line = br.readLine();
           while (line != null) {
               line = br.readLine();
               lerLinha(line);
           }
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           br.close();
       }

       //rodar comutador e portas

       /*PortaEntrada portaEntrada = new PortaEntrada("A", 5, 10, 2000);
       Thread thrd = new Thread(portaEntrada);
       thrd.start();*/  //Teste

       System.out.println("Pressionar qualquer tecla para encerrar programa");
       System.in.read();

       //portaEntrada.parar();
    }

    public void pararRoteador(){
        return;
    }


    public void lerLinha(String line){
        String[] palavra = line.split(" ");

        if (palavra[0].equals("switch-fabric:")) {
            comutador = new Comutador(Integer.parseInt(palavra[1]));
        } else if (palavra[0].equals("input:")) {
            pEntrada = new PortaEntrada(palavra[1], Integer.parseInt(palavra[2]), Integer.parseInt(palavra[3]), Integer.parseInt(palavra[4]));
            Thread thrdEntrada = new Thread(portasEntrada, pEntrada, palavra[1]);

        } else if (palavra[0].equals("output:")){
            pSaida = new PortaSaida(palavra[1], Integer.parseInt(palavra[2]), Integer.parseInt(palavra[3]), Integer.parseInt(palavra[4]), Integer.parseInt(palavra[5]));
            Thread thrdSaida = new Thread(portasSaida, pSaida, palavra[1]);
        }
        else{
            //throw invalidArgsException;
        }

    }

}
