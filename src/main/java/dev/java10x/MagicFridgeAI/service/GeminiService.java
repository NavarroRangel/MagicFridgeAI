package dev.java10x.MagicFridgeAI.service;

import dev.java10x.MagicFridgeAI.model.FoodItemModel;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GeminiService {

    private final WebClient webClient;
    private final String geminiKey = System.getenv("GEMINI_API_KEY");

    public GeminiService(WebClient webClient) {
        this.webClient = webClient;
    }

public Mono<String> generateRecipe(List<FoodItemModel> foodItemModels) {

        String alimentos = foodItemModels.stream()
                .map(item -> String.format("%s:  - Quantidade: %d, validade: %s",
                        item.getNome(),
                        item.getQuantidade(),
                        item.getValidade()))
                .collect(Collectors.joining("\n"));

    String prompt = String.format(
            "Aja como um chef de cozinha criativo e experiente. Sua tarefa é criar uma receita deliciosa usando apenas os ingredientes da lista abaixo. " +
                    "Priorize o uso dos itens que estão mais próximos da data de validade.\n\n" +
                    "Ingredientes Disponíveis:\n%s\n\n" +
                    "Por favor, apresente a receita no seguinte formato:\n" +
                    "**Nome da Receita:** (um nome criativo)\n" +
                    "**Ingredientes:**\n- (liste os ingredientes usados)\n" +
                    "**Modo de Preparo:**\n1. (passo 1)\n2. (passo 2)\n...",
            alimentos
    );;

    Map<String, Object> requestBody = Map.of(
            "contents", List.of(
                    Map.of("parts", List.of(
                            Map.of("text", prompt)
                    ))
            ),
            "generationConfig", Map.of(
                    "thinkingConfig", Map.of(
                            "thinkingBudget", 0
                    )
            )
    );


        return webClient.post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent")
                .header("x-goog-api-key", geminiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .doOnError(error -> {
                    System.err.println(">>>>>> ERRO DETALHADO DA API <<<<<<");
                    // Verifica se é um erro de resposta do cliente/servidor
                    if (error instanceof org.springframework.web.reactive.function.client.WebClientResponseException) {
                        org.springframework.web.reactive.function.client.WebClientResponseException ex = (org.springframework.web.reactive.function.client.WebClientResponseException) error;
                        System.err.println("Status Code: " + ex.getStatusCode());
                        System.err.println("Corpo da Resposta de Erro: " + ex.getResponseBodyAsString());
                    } else {

                        System.err.println("Mensagem de Erro Geral: " + error.getMessage());
                    }
                    System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                })
                .map(response -> {
                    try {
                        List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
                        Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                        List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                        return parts.get(0).get("text").toString();
                    } catch (Exception e) {
                        System.err.println("Error parsing Gemini response: " + response);
                        return "Não foi possível extrair a receita da resposta da IA.";
                    }
                })
                .onErrorReturn("Desculpe, ocorreu um erro ao se comunicar com a IA para gerar sua receita.");
    }

}

