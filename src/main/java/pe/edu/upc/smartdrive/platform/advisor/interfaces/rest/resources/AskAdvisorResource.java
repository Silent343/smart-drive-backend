package pe.edu.upc.smartdrive.platform.advisor.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Request body for {@code POST /advisor/ask}. Field names are snake_case to
 * match the frontend assembler, via explicit {@link JsonProperty} mappings.
 *
 * @param loanId   the id of the loan the question is about
 * @param question the user's question
 * @param history  prior conversation turns
 */
public record AskAdvisorResource(
        @JsonProperty("loan_id") Long loanId,
        @JsonProperty("question") String question,
        @JsonProperty("history") List<HistoryItemResource> history) {

    /**
     * A prior conversation turn.
     *
     * @param role    "user" or "assistant"
     * @param content the message text
     */
    public record HistoryItemResource(
            @JsonProperty("role") String role,
            @JsonProperty("content") String content) {
    }
}
