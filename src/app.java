import java.io.IOException;

public class app {

    public static void main(String[] args) throws IOException {
        if(args.length > 1 || args.length < 1){
            System.out.println("Quantidade de argumentos inválido");
        }else{
            Router router = new Router(args[0]);
            /*-->*/ router.rodarRoteador();

            /*PortaEntrada portaEntrada = new PortaEntrada("A", 5, 10, 2000);
            Thread thrd = new Thread(portaEntrada);
            thrd.start();*/
        }
    }
}
