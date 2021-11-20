import Exceptions.invalidArgException;
import Exceptions.invalidPackageFowardSum;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;;

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

    public void rodarRoteador() throws IOException, invalidArgException, invalidPackageFowardSum {

       String caminhoArquivo = nomeArquivo;
       BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo));
       try {

           String line = br.readLine();
           while (line != null) {
               linhasBNF.add(line);
               contadorLinhasBNF++;
               line = br.readLine();

           }
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           br.close();
       }

       inicializarThreads();

        comutador = new Comutador(switchDelayComutador, portasEntrada, portasSaida);
        thrdComutador = new Thread(comutador);
        thrdComutador.start();

       System.out.println("Pressionar qualquer tecla para encerrar programa");
       System.in.read();
       pararRoteador();
       return;
    }

    public void pararRoteador(){
        Porta.exit = true;
        Comutador.exit = true;
        return;
    }

    //Verificar somatorio da PackageFowardProbability se é 100 antes de rodar :D

    public void inicializarThreads() throws invalidArgException, invalidPackageFowardSum {
        barreira = new CyclicBarrier(linhasBNF.size()
        );
        int somaProbsFoward = 0;

        //Ainda falta colocar os gate.await() nas threads filhas...

        for(int i = 0; i < linhasBNF.size(); i++){
            String line = linhasBNF.get(i);
            String[] palavra = line.split(" ");

            if (palavra[0].equals("switch-fabric:")) {
                switchDelayComutador = Integer.parseInt(palavra[1]);
            } else if (palavra[0].equals("input:")) {
                pEntrada = new PortaEntrada(palavra[1], Integer.parseInt(palavra[2]), Integer.parseInt(palavra[3]), Integer.parseInt(palavra[4]));
                Thread thrdEntrada = new Thread(thrdGpPortasEntrada, pEntrada, palavra[1]);

                portasEntrada.add(pEntrada);
                thrdEntrada.start();

            } else if (palavra[0].equals("output:")){
                pSaida = new PortaSaida(palavra[1], Integer.parseInt(palavra[2]), Integer.parseInt(palavra[3]), Integer.parseInt(palavra[4]), Integer.parseInt(palavra[5]));
                Thread thrdSaida = new Thread(thrdGpPortasSaida, pSaida, palavra[1]);

                portasSaida.add(pSaida);
                thrdSaida.start();

                somaProbsFoward += Integer.parseInt(palavra[3]);
                if(somaProbsFoward > 100 ){
                    throw new invalidPackageFowardSum(String.valueOf(somaProbsFoward));
                }

            }
            else{
                throw new invalidArgException(palavra[0]);
            }
        }

        //Verifica se ficou menor que 100 as probs
        if(somaProbsFoward < 100){
            throw new invalidPackageFowardSum(String.valueOf(somaProbsFoward));
        }

    }
}


