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
                        log_pacotes_encaminhado_sucesso = new File("log_pacotes_encaminhado_sucesso.txt");
                        funcoesComuns.escreveLog(log_pacotes_encaminhado_sucesso, pacote);
                    }else{
                        log_pacotes_fila_cheia = new File("log_pacotes_fila_cheia.txt");
                        funcoesComuns.escreveLog(log_pacotes_fila_cheia, pacote);
                    }
                }else{
                    log_pacotes_nao_tratados_comutacao = new File("log_pacotes_nao_tratados_comutacao.txt");
                    funcoesComuns.escreveLog(log_pacotes_nao_tratados_comutacao, pacote);
                }

                //resetando para próximo ciclo
                portaSaidaAtual = null;
                portaEntradaAtual = null;
                pacote = null;
            }
            i = (i + 1) % portasEntrada.size(); //iterando próxima porta
        }

        for (i = 0; i < portasEntrada.size(); i++){
            String novoPacote;
            while((novoPacote = portasEntrada.get(i).getFilaEntrada().poll()) != null){
                log_pacotes_nao_tratados_comutacao = new File("log_pacotes_nao_tratados_comutacao.txt");
                //System.out.println(log_pacotes_nao_tratados_comutacao.exists());
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
