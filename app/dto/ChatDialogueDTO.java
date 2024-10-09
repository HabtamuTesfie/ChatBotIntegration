package dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class ChatDialogueDTO {
    private String instruction;
    private String question;
    private String response;
    private Timestamp createdAt;
    private String email;
}
