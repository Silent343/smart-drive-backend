package pe.edu.upc.smartdrive.platform.advisor.domain.model.valueobjects;

/**
 * A single prior turn of the advisor conversation, supplied by the client so
 * the assistant has context for follow-up questions.
 *
 * @param role    who authored the turn: {@code "user"} or {@code "assistant"}
 * @param content the message text
 */
public record ChatTurn(String role, String content) {
}
