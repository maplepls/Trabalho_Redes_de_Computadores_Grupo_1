import Exceptions.invalidArgException;
import Exceptions.invalidPackageFowardSum;

import java.io.File;
import java.io.IOException;

public class app {

    public static void main(String[] args) throws IOException, invalidArgException, invalidPackageFowardSum {
        if(args.length != 1){
            System.out.println("Quantidade de argumentos inválido");
        }else{
            File theDir = new File("./registros");
            if(theDir.exists()){
                apagarArquivos(theDir);
            }
            theDir.mkdirs();

            Router router = new Router(args[0]);
            router.rodarRoteador();
        }
    }

    private static void apagarArquivos(File dir) {
        File[] listaConteudo = dir.listFiles();
        if(listaConteudo != null){
            for(File arq : listaConteudo){
                apagarArquivos(arq);
            }
        }
        dir.delete();
    }
}
