package br.com.alura.screenmatch.config;

public class OpenAiConfig {
/*
* API_KEY CONFIGURADA NA VARIÁVEL DE AMBIENTE
* */
    private static final String API_KEY = System.getenv("OPENAI_API_KEY");

    public static String getApiKey() {
        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new IllegalStateException("Chave da API da OpenAI não configurada. Configure a variável de ambiente OPENAI_API_KEY.");
        }
        return API_KEY;
    }
}
