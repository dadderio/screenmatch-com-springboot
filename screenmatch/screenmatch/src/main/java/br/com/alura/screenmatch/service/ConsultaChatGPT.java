package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.config.OpenAiConfig;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;


public class ConsultaChatGPT {

    public static String obterTraducao(String texto) {

        //CONFIGURADA COM VARIÁVEL DE AMBIENTE + OpenAiConfig:

        String apiKey = OpenAiConfig.getApiKey();
        OpenAiService service = new OpenAiService(apiKey);


//    public static String obterTraducao(String texto) {
//        OpenAiService service = new OpenAiService("COLOQUE A API KEY");


        CompletionRequest requisicao = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt("traduza para o português o texto: " + texto)
                .maxTokens(1000)
                .temperature(0.7)
                .build();


        var resposta = service.createCompletion(requisicao);
        return resposta.getChoices().get(0).getText();
    }
}

