import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;

public class Router {

    String nomeArquivo;

    ThreadGroup thrdGpPortasEntrada = new ThreadGroup("portasEntrada");
    ThreadGroup thrdGpPortasSaida = new ThreadGroup("portasSaida");
    Thread thrdComutador;

    PortaEntrada pEntrada;
    PortaSaida pSaida;
    Comutador comutador;

    ArrayList<PortaEntrada> portasEntrada = new ArrayList<PortaEntrada>();
    ArrayList<PortaSaida> portasSaida = new ArrayList<PortaSaida>();

    ArrayList<String> linhasBNF = new ArrayList<String>();
    int contadorLinhasBNF = 0;

    public static CyclicBarrier barreira;

    int switchDelayComutador;

    public Router(String arg){
        this.nomeArquivo = arg;
    }

    public void rodarRoteador() throws IOException {

       String caminhoArquivo = nomeArquivo;
       BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo));
       try {
           String line = br.readLine();
           while (line != null) {
               linhasBNF.add(br.readLine());
               contadorLinhasBNF++;
           }
           //CyclicBarrier adasdas = new CyclicBarrier(arrListString.size()+1)
           //
           comutador = new Comutador(switchDelayComutador, portasEntrada, portasSaida, barreira);
           thrdComutador = new Thread(comutador);

       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           br.close();
       }

       //rodar comutador e portas

       thrdComutador.start();

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

    //Verificar somatorio da PackageFowardProbability se é 100 antes de rodar :D

    public void inicializarThreads(){
        barreira = new CyclicBarrier(linhasBNF.size() + 1);

        //Ainda falta colocar os gate.await() nas threads filhas...

        for(int i = 0; i < linhasBNF.size(); i++){
            String line = linhasBNF.get(i);
            String[] palavra = line.split(" ");

            if (palavra[0].equals("switch-fabric:")) {
                switchDelayComutador = Integer.parseInt(palavra[1]);
                //comutador = new Comutador(Integer.parseInt(palavra[1]));
                //thrdComutador = new Thread(comutador);
            } else if (palavra[0].equals("input:")) {
                pEntrada = new PortaEntrada(palavra[1], Integer.parseInt(palavra[2]), Integer.parseInt(palavra[3]), Integer.parseInt(palavra[4]), barreira);
                Thread thrdEntrada = new Thread(thrdGpPortasEntrada, pEntrada, palavra[1]);

                portasEntrada.add(pEntrada);
                thrdEntrada.start();

            } else if (palavra[0].equals("output:")){
                pSaida = new PortaSaida(palavra[1], Integer.parseInt(palavra[2]), Integer.parseInt(palavra[3]), Integer.parseInt(palavra[4]), Integer.parseInt(palavra[5]), barreira);
                Thread thrdSaida = new Thread(thrdGpPortasSaida, pSaida, palavra[1]);

                portasSaida.add(pSaida);
                thrdSaida.start();
            }
            else{
                //throw invalidArgsException;
            }
        }
    }
}
