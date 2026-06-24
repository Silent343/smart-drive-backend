package pe.edu.upc.smartdrive.platform.advisor.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response body for {@code POST /advisor/ask}. Field names are snake_case to
 * match the frontend, via explicit {@link JsonProperty} mappings.
 *
 * @param id          a generated id for the answer
 * @param answer      the natural-language answer
 * @param usedFigures labels of the loan figures used to ground the answer
 */
public record AdvisorAnswerResource(
        @JsonProperty("id") String id,
        @JsonProperty("answer") String answer,
        @JsonProperty("used_figures") List<String> usedFigures) {
}
