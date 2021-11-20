import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Comutador implements Runnable{
    int switchDelay;

    FileWriter log_pacotes_encaminhado_sucesso;
    FileWriter log_pacotes_fila_cheia;
    FileWriter log_pacotes_nao_tratados_comutacao;

    ArrayList<PortaEntrada> portasEntrada;
    ArrayList<PortaSaida> portasSaida;

    CyclicBarrier barreira;

    String pacote;


    private static boolean exit = false;

    public Comutador(int switchDelay, ArrayList<PortaEntrada> portasEntrada, ArrayList<PortaSaida> portasSaida, CyclicBarrier barreira){
        this.barreira = barreira;
        this.switchDelay = switchDelay;
        this.portasEntrada = portasEntrada;
        this.portasSaida = portasSaida;
    }

    public void run(){
        try {
            barreira.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        int i = 0;
        PortaEntrada portaEntradaAtual;
        PortaSaida portaSaidaAtual;

        while(!exit){
            portaEntradaAtual = portasEntrada.get(i);
            pacote = portaEntradaAtual.getFirstFilaEntrada();
            if(pacote != null && !exit){

                try {
                    Thread.sleep(switchDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //ajeitar pelo package_foward_probability
                PortaSaida portaEscolhida = escolherPortaSaida();

                //mudar horário
                //log pacote encaminhados sucesso

                portaSaidaAtual = null;
                portaEntradaAtual = null;
                pacote = " ";
            }

            i = (i + 1) % portasEntrada.size();
        }
        //getFilaEntrada para as portas de entrada
        //logs de fila de entrada
    }

    public PortaSaida escolherPortaSaida(){
        int[] probs = new int[portasSaida.size()];
        double chutePortaSaida = (double) Math.random()*100;
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
            }else if(i == portasSaida.size() - 1){
                    portaEscolhida = portasSaida.get(i);
            }else{
                if(chutePortaSaida <= probs[i] && chutePortaSaida >= probs[i+1]){
                    portaEscolhida = portasSaida.get(i);
                }
            }
        }

        return portaEscolhida;
    }

    public String novoHorarioPacote(String pacote){
        pacote = pacote.split(" ")[0];
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        Date date = new Date();
        return pacote + " " + formatter.format(date);
    }


    public void parar(){
        exit = true;
    }
}
