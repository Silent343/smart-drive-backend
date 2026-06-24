package pe.edu.upc.smartdrive.platform.advisor.infrastructure.gemini;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import pe.edu.upc.smartdrive.platform.advisor.domain.model.valueobjects.ChatTurn;
import pe.edu.upc.smartdrive.platform.advisor.domain.services.LanguageModelPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Gemini implementation of the {@link LanguageModelPort}.
 *
 * <p>Calls the Gemini {@code generateContent} REST endpoint using Spring's
 * {@link RestClient} (no extra dependency required). The API key and model are
 * read from configuration.</p>
 */
@Component
public class GeminiLanguageModelAdapter implements LanguageModelPort {

    private static final Logger log = LoggerFactory.getLogger(GeminiLanguageModelAdapter.class);

    private final RestClient restClient;
    private final String apiKey;
    private final String model;

    /**
     * @param apiKey  the Gemini API key ({@code gemini.api-key})
     * @param model   the Gemini model id ({@code gemini.model})
     * @param baseUrl the Gemini API base URL ({@code gemini.base-url})
     */
    public GeminiLanguageModelAdapter(
            @Value("${gemini.api-key:}") String apiKey,
            @Value("${gemini.model:gemini-2.0-flash}") String model,
            @Value("${gemini.base-url:https://generativelanguage.googleapis.com/v1beta}") String baseUrl) {
        this.apiKey = apiKey;
        this.model = model;
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        log.info("GeminiLanguageModelAdapter ready (model={}, apiKey={})",
                model, apiKey == null || apiKey.isBlank() ? "MISSING" : "present");
    }

    /**
     * {@inheritDoc}
     *
     * <p>Builds the Gemini request payload: the system instruction plus the
     * grounding context become a {@code systemInstruction}, and the history and
     * new question become the {@code contents} turns.</p>
     */
    @Override
    public String generate(
            String systemInstruction,
            String groundingContext,
            List<ChatTurn> history,
            String question) {

        if (apiKey == null || apiKey.isBlank()) {
            log.warn("Gemini API key is missing — advisor disabled.");
            return "El asesor de IA no está configurado (falta la clave de Gemini). "
                    + "Configura la variable GEMINI_API_KEY para activarlo.";
        }

        Map<String, Object> payload = buildPayload(
                systemInstruction, groundingContext, history, question);

        try {
            GeminiResponse response = restClient.post()
                    .uri("/models/{model}:generateContent?key={key}", model, apiKey)
                    .body(payload)
                    .retrieve()
                    .body(GeminiResponse.class);
            return extractText(response);
        } catch (RestClientResponseException ex) {
            // HTTP error from Gemini (400/403/404/429...). The body tells us EXACTLY what's wrong.
            log.error("Gemini HTTP {} for model '{}'. Response body: {}",
                    ex.getStatusCode(), model, ex.getResponseBodyAsString(), ex);
            return "Ocurrió un problema al consultar al asesor de IA. "
                    + "Inténtalo nuevamente en unos momentos.";
        } catch (Exception ex) {
            // Network / serialization / unexpected error.
            log.error("Unexpected error calling Gemini (model '{}')", model, ex);
            return "Ocurrió un problema al consultar al asesor de IA. "
                    + "Inténtalo nuevamente en unos momentos.";
        }
    }

    /**
     * Builds the Gemini request body.
     *
     * @param systemInstruction the persona/rules instruction
     * @param groundingContext  the loan figures
     * @param history           prior turns
     * @param question          the new question
     * @return a map representing the JSON request body
     */
    private Map<String, Object> buildPayload(
            String systemInstruction,
            String groundingContext,
            List<ChatTurn> history,
            String question) {

        // System instruction = persona + the grounding figures.
        Map<String, Object> systemPart = Map.of(
                "parts", List.of(Map.of(
                        "text", systemInstruction + "\n\n" + groundingContext)));

        List<Map<String, Object>> contents = new ArrayList<>();
        if (history != null) {
            for (ChatTurn turn : history) {
                // Gemini uses "model" for the assistant role.
                String role = "assistant".equals(turn.role()) ? "model" : "user";
                contents.add(Map.of(
                        "role", role,
                        "parts", List.of(Map.of("text", turn.content()))));
            }
        }
        contents.add(Map.of(
                "role", "user",
                "parts", List.of(Map.of("text", question))));

        return Map.of(
                "systemInstruction", systemPart,
                "contents", contents);
    }

    /**
     * Extracts the answer text from a Gemini response, guarding against empties.
     *
     * @param response the deserialized response
     * @return the answer text, or a fallback message
     */
    private String extractText(GeminiResponse response) {
        if (response == null || response.candidates() == null || response.candidates().isEmpty()) {
            log.warn("Gemini returned no candidates. Full response: {}", response);
            return "No recibí una respuesta del asesor. Inténtalo de nuevo.";
        }
        var candidate = response.candidates().get(0);
        if (candidate.content() == null || candidate.content().parts() == null
                || candidate.content().parts().isEmpty()) {
            log.warn("Gemini candidate had no content/parts. Candidate: {}", candidate);
            return "No recibí una respuesta del asesor. Inténtalo de nuevo.";
        }
        String text = candidate.content().parts().get(0).text();
        return text == null || text.isBlank()
                ? "No recibí una respuesta del asesor. Inténtalo de nuevo."
                : text.strip();
    }

    // ── Response DTOs (minimal projection of the Gemini schema) ──────────────

    /** Top-level Gemini response. */
    private record GeminiResponse(List<Candidate> candidates) {
    }

    /** One answer candidate. */
    private record Candidate(Content content) {
    }

    /** The content of a candidate. */
    private record Content(List<Part> parts) {
    }

    /** A text part. */
    private record Part(String text) {
    }
}