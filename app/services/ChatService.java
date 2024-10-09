package services;

import dto.ChatDialogueDTO;
import dto.ChatQueryDTO;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

//--------------------------------------------------------------------------------
/**
 * ChatService defines the contract for processing chat requests and retrieving
 * chat history.
 */
//--------------------------------------------------------------------------------
public interface ChatService {

    //--------------------------------------------------------------------------------
    /**
     * Processes a chat request by sending it to the ChatGPT API and returning the
     * response.
     *
     * @param chatQueryDTO Contains the instruction and question for the chat request.
     * @param email        The email of the user making the request.
     * @return A CompletionStage containing the ChatDialogueDTO with the chat response.
     */
    //--------------------------------------------------------------------------------
    CompletionStage<ChatDialogueDTO> processChatRequest(ChatQueryDTO chatQueryDTO, String email);

    //--------------------------------------------------------------------------------
    /**
     * Retrieves all chat queries made by a user.
     *
     * @param email The email of the user whose chat queries are to be retrieved.
     * @return A CompletionStage containing a stream of ChatDialogue objects.
     */
    //--------------------------------------------------------------------------------
    CompletionStage<Stream<ChatDialogueDTO>> getAllQueriesByUser(String email);
}
