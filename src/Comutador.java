import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;

public class Comutador implements Runnable{
    int switchDelay;

    String log_pacotes_encaminhado_sucesso = "log_pacotes_encaminhado_sucesso";
    String log_pacotes_fila_cheia = "log_pacotes_encaminhado_sucesso";
    String log_pacotes_nao_tratados_comutacao = "log_pacotes_nao_tratados_comutacao";

    ArrayList<PortaEntrada> portasEntrada;
    ArrayList<PortaSaida> portasSaida;

    String pacote;

    public static boolean exit;

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

        int i = 0;
        PortaEntrada portaEntradaAtual;
        PortaSaida portaSaidaAtual;

        while(!exit){
            portaEntradaAtual = portasEntrada.get(i);
            pacote = portaEntradaAtual.getFirstFilaEntrada();
            if(pacote != null){

                try {
                    Thread.sleep(switchDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //escolhe porta saida com packageFowardProbability
                portaSaidaAtual = escolherPortaSaida();
                if(!exit){
                    portaSaidaAtual = escolherPortaSaida();
                    if(!portaSaidaAtual.filaCheia()){
                        portaSaidaAtual.inserirFila(pacote);
                        funcoesComuns.escreveLog(log_pacotes_encaminhado_sucesso, funcoesComuns.novoHorarioPacote(pacote));
                    }else{
                        funcoesComuns.escreveLog(log_pacotes_fila_cheia, funcoesComuns.novoHorarioPacote(pacote));
                    }
                }else{
                    funcoesComuns.escreveLog(log_pacotes_nao_tratados_comutacao, funcoesComuns.novoHorarioPacote(pacote));
                }

                //resetando para próximo ciclo
                portaSaidaAtual = null;
                portaEntradaAtual = null;
                pacote = " ";
                i = (i + 1) % portasEntrada.size(); //iterando próxima porta
            }
        }

        for (i = 0; i < portasEntrada.size(); i++){
            String novoPacote;
            while((novoPacote = portasEntrada.get(i).getFilaEntrada().poll()) != null){
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
