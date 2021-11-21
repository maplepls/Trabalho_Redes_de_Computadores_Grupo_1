import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;

public class Comutador implements Runnable{
    int switchDelay;

    File log_pacotes_encaminhado_sucesso;
    File log_pacotes_fila_cheia;
    File log_pacotes_nao_tratados_comutacao;

    ArrayList<PortaEntrada> portasEntrada;
    ArrayList<PortaSaida> portasSaida;

    String pacote;

    public static boolean exit;

    public int iteradorPortaEntrada = 0; //indica qual porta de entrada será checada

    public Comutador(int switchDelay, ArrayList<PortaEntrada> portasEntrada, ArrayList<PortaSaida> portasSaida){
        this.switchDelay = switchDelay;
        this.portasEntrada = portasEntrada;
        this.portasSaida = portasSaida;
        this.exit = false;
    }

    public void run(){

        try {
            Router.barreira.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        //Loop verificando o estado de saída
        while (!exit) {
            encaminharPacote();
        }

        for (i = 0; i < portasEntrada.size(); i++){
            String novoPacote;
            while((novoPacote = portasEntrada.get(i).getFilaEntrada().poll()) != null){
                log_pacotes_nao_tratados_comutacao = new File("log_pacotes_nao_tratados_comutacao.txt");
                funcoesComuns.escreveLog(log_pacotes_nao_tratados_comutacao, novoPacote);
            }
        }

        Thread.currentThread().interrupt();
    }

    public PortaSaida escolherPortaSaida(){
        int[] probs = new int[portasSaida.size()];
        double chutePortaSaida = (double) Math.random() * 100;
        PortaSaida portaEscolhida = null;

        for(int i = 0;i < portasSaida.size(); i++){
            probs[i] = 0;
            for(int j = i; j >= 0; j--){
                probs[i] += portasSaida.get(j).getPackageFowardProbability();
            }
        }

        for(int i = 0; i < portasSaida.size();i++) {
            if (i == 0) {
                if (chutePortaSaida <= probs[i]) {
                    portaEscolhida = portasSaida.get(i);
                }
            }else{
                if(chutePortaSaida <= probs[i] && chutePortaSaida >= probs[i-1]){
                    portaEscolhida = portasSaida.get(i);
                }
            }
        }
        return portaEscolhida;
    }
}
