package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.ConverterDados;
import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverterDados conversor = new ConverterDados();
    private final String ENDERECO = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=b8daf779";

    public void exibirMenu(){
        System.out.println("*****************************************************************");
        System.out.println("Digite sua série preferida: ");
        System.out.println("*****************************************************************");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ","+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);


        List<DadosTemporada> temporadas = new ArrayList<>();

        for(int i=1; i <= dados.totalTemporadas(); i++){
            json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ","+")+ "&season="+ i + API_KEY);
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



    }
}
