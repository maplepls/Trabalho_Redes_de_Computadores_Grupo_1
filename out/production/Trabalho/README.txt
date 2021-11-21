1. O usuário executa o arquivo "Exec.jar" por linha de comando "java -jar Exec.jar <path_arquivo_BNF>";

2. É criado uma pasta registros para armazenar os registros gerados durante a execução do programa;

3. Clicando 'Enter' no teclado, o programa é encerrado;


// Exceções:
invalidArgException: surge quando há no arquivo BNF uma declaração de componente inválida.
invalidComponentQuantity: surge quando no arquivo BNF não há um comutador/porta de entrada/porta de saída e/ou há mais de um comutador.
invalidPackageFowardSum: surge quando a soma das packageFowardProbability não resulta em 100%.