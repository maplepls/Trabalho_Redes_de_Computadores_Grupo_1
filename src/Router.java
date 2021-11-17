public class Router {
    public static void main(String[] args){
         PortaEntrada portaEntrada = new PortaEntrada("A", 5, 10, 2000);
         Thread thrd = new Thread(portaEntrada);
         thrd.start();
    }
}
