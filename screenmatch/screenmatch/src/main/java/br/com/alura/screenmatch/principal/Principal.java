package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.service.ConsumoApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverterDados conversor = new ConverterDados();
    private final String ENDERECO = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=b8daf779";

    public void exibirMenu() {
        System.out.println("*****************************************************************");
        System.out.println("Digite sua série preferida: ");
        System.out.println("*****************************************************************");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);


        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        //temporadas.forEach(t -> System.out.println(t));
        //reduzindo o código acima, selecionando as temporadas:
        temporadas.forEach(System.out::println);


//        for(int i=0; i<dados.totalTemporadas(); i++){
//            List<DadosEpisodio> episodioTemporada = temporadas.get(i).episodios();
//            for(int j = 0; j< dados.totalTemporadas(); j++){
//                System.out.println(episodioTemporada.get(j).titulo());
//            }
//        }

        System.out.println("=====================================================");

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        System.out.println("=====================================================");

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());//consegue acrescentar novos itens
        //.toList();//lista imutável
//        dadosEpisodios.add(new DadosEpisodio("teste", 3, "10", "2020-11-20"));
//        dadosEpisodios.forEach(System.out::println);
        System.out.println("**********************************************************************");
        System.out.println("USANDO O 'PEEK' - UMA OLHADINHA EM CADA LINHA DE STREAMS - DEPURAÇÃO");
        System.out.println("**********************************************************************");

        System.out.println("\n Top 5 episódios: ");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
               // .peek(e -> System.out.println("Primeiro filtro (N/A) " + e))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
               // .peek(e -> System.out.println("Ordenação "+ e))
                .limit(5)
               // .peek(e -> System.out.println("Limite " + e))
                .map(e -> e.titulo().toUpperCase())
               // .peek(e -> System.out.println("Mapeamento " + e))
                .forEach(System.out::println);
        System.out.println("=====================================================");


        //método flatMap() primeiro mapeia cada elemento usando uma função de mapeamento e, em seguida, nivela o resultado em um novo array
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());
        episodios.forEach(System.out::println);
        System.out.println("=====================================================");
        System.out.println("DIGITE UM TRECHO DO TÍTULO DO EPISÓDIO: ");
        System.out.println("=====================================================");
        var trechoTitulo = leitura.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                        .findFirst();
        if(episodioBuscado.isPresent()) {
            System.out.println("Episódio encontrado! ");
            //Optional é um container que está guardando o episódio caso ele exista e coloca um .get() para pegar "o episódio" que está dentro e depois um .getTemporada():
            System.out.println("Temporada: " + episodioBuscado.get().getTemporada()+" Episódio: "+ episodioBuscado.get().getTitulo());
        }else{
            System.out.println("Episódio não encontrado");
        }

        System.out.println("=====================================================");

        System.out.println("A partir de que ano você deseja ver os episódios? ");

        var ano = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        //Declarando o formatador para usar abaixo:
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                " Episódio: " + e.getTitulo() +
                                " Data de lançamento: " + e.getDataLancamento().format(formatador)
                ));
        System.out.println("=====================================================");
        System.out.println("Utilizando o map para avaliar cada temporada com a média das avaliações dos episódios");

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacoesPorTemporada);

        System.out.println("=====================================================");
        System.out.println("Classe DoubleSummaryStatistics");
//é uma classe fornecida no pacote java.util do Java que faz parte da API de streams introduzida no Java 8.
//Ela é frequentemente usada em conjunto com as operações de stream para calcular estatísticas resumidas para valores do tipo double.
// Essa classe faz parte do pacote java.util e está contida no mesmo pacote que a classe Optional
        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                //coletor para calcular as estatísticas resumidas para um fluxo de valores double
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        //System.out.println(est);
        //DoubleSummaryStatistics{count=68, sum=593,100000, min=4,000000, average=8,722059, max=9,900000}
        System.out.println("Média: " + est.getAverage());
        System.out.println("Melhor episódio: " + est.getMax());
        System.out.println("Pior episódio: " + est.getMin());
        System.out.println("Quantidade: " + est.getCount());

    }
}
