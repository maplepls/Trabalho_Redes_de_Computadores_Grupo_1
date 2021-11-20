import Exceptions.invalidArgException;
import Exceptions.invalidPackageFowardSum;

import java.io.IOException;

public class app {

    public static void main(String[] args) throws IOException, invalidArgException, invalidPackageFowardSum {
        if(args.length > 1 || args.length < 1){
            System.out.println("Quantidade de argumentos inválido");
        }else{
            Router router = new Router(args[0]);
            router.rodarRoteador();
        }
    }
}
