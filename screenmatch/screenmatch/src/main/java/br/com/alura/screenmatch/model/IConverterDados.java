package br.com.alura.screenmatch.model;

public interface IConverterDados {
    //recebe uma string (json) e devolve algo genérico (que não sabemos)
    <T> T obterDados(String json, Class<T> classe);
}
