import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;

public class Comutador implements Runnable {
    int switchDelay;

    File log_pacotes_encaminhado_sucesso;
    File log_pacotes_fila_cheia;
    File log_pacotes_nao_tratados_comutacao;

    ArrayList<PortaEntrada> portasEntrada;
    ArrayList<PortaSaida> portasSaida;

    String pacote;

    public static boolean exit;

    public int iteradorPortaEntrada = 0; //indica qual porta de entrada será checada

    public Comutador(int switchDelay, ArrayList<PortaEntrada> portasEntrada, ArrayList<PortaSaida> portasSaida) {
        this.switchDelay = switchDelay;
        this.portasEntrada = portasEntrada;
        this.portasSaida = portasSaida;
        this.exit = false;
    }

    //função que itera por portas de entrada, verifica se há pacote, e escolhe qual porta de saída receberá o pacote, caso haja.
    public void encaminharPacote() {
        PortaEntrada portaEntradaAtual;
        PortaSaida portaSaidaAtual;

        portaEntradaAtual = portasEntrada.get(iteradorPortaEntrada);
        pacote = portaEntradaAtual.getFirstFilaEntrada();
        if (pacote != null) {
            pacote = funcoesComuns.novoHorarioPacote(pacote);
            try {
                Thread.sleep(switchDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //escolhe porta saida com packageFowardProbability
            portaSaidaAtual = escolherPortaSaida();
            if (!exit) {
                if (!portaSaidaAtual.filaCheia()) { //caso a fila da porta de saida não esteja cheia..
                    portaSaidaAtual.inserirFila(pacote); //inserir em sua fila
                    log_pacotes_encaminhado_sucesso = new File("log_pacotes_encaminhado_sucesso.txt"); //e registra no log
                    funcoesComuns.escreveLog(log_pacotes_encaminhado_sucesso, pacote);

                } else { //caso esteja cheia, registra no respectivo log
                    log_pacotes_fila_cheia = new File("log_pacotes_fila_cheia.txt");
                    funcoesComuns.escreveLog(log_pacotes_fila_cheia, pacote);
                }

            } else { //log de pacotes que não foram tratados pelo comutador (log estava no processo de ser tratado)
                log_pacotes_nao_tratados_comutacao = new File("log_pacotes_nao_tratados_comutacao.txt");
                funcoesComuns.escreveLog(log_pacotes_nao_tratados_comutacao, pacote);
            }

            //resetando para próximo ciclo
            pacote = null;
        }
        iteradorPortaEntrada = (iteradorPortaEntrada + 1) % portasEntrada.size(); //iterando próxima porta
    }

    //metodo para escolher qual porta de saida sera enviada o pacote atraves de sua packageFowardProbability
    //a "probabilidade na prática" de cada porta de saída será a soma de sua PackageFowardProbability com as das portas anteriores
    //assim, a chance de escolher uma porta será a chance de um chute aleatório ser entre
    // a sua "probabilidade na prática" e a "probabilidade na prática" das porta anterior (ou 0, caso seja a primeira porta.)
    public PortaSaida escolherPortaSaida() {
        int[] probs = new int[portasSaida.size()];
        double chutePortaSaida = (double) Math.random() * 100;
        PortaSaida portaEscolhida = null;

        for (int i = 0; i < portasSaida.size(); i++) {
            probs[i] = 0;
            for (int j = i; j >= 0; j--) {
                probs[i] += portasSaida.get(j).getPackageFowardProbability();
            }
        }

        for (int i = 0; i < portasSaida.size(); i++) {
            if (i == 0) {
                if (chutePortaSaida <= probs[i]) {
                    portaEscolhida = portasSaida.get(i);
                }
            } else {
                if (chutePortaSaida <= probs[i] && chutePortaSaida >= probs[i - 1]) {
                    portaEscolhida = portasSaida.get(i);
                }
            }
        }
        return portaEscolhida;
    }


    public void run() {

        //Esperar todas as Threads .start()
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

        //log de pacotes não tratados pelo comutador
        for (int i = 0; i < portasEntrada.size(); i++) {
            String novoPacote;
            while ((novoPacote = portasEntrada.get(i).getFilaEntrada().poll()) != null) {
                log_pacotes_nao_tratados_comutacao = new File("log_pacotes_nao_tratados_comutacao.txt");
                funcoesComuns.escreveLog(log_pacotes_nao_tratados_comutacao, novoPacote);
            }
        }
        Thread.currentThread().interrupt();
    }
}
