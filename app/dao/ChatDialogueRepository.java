package dao;

import com.google.inject.ImplementedBy;
import models.ChatDialogue;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

//--------------------------------------------------------------------------------
/**
 * Provides an asynchronous API for operations on ChatDialogue entities.
 * Supports saving and retrieving chat dialogues by user.
 */
//--------------------------------------------------------------------------------
@ImplementedBy(JPAChatDialogueRepository.class)
public interface ChatDialogueRepository {

    //--------------------------------------------------------------------------------
    /**
     * Saves a ChatDialogue asynchronously within a transaction.
     *
     * @param chatRequest The ChatDialogue to be saved.
     * @return A CompletionStage that resolves when the save completes.
     */
    //--------------------------------------------------------------------------------
    CompletionStage<ChatDialogue> saveChatRequest(ChatDialogue chatRequest);

    //--------------------------------------------------------------------------------
    /**
     * Retrieves all chat dialogues for a specific user asynchronously.
     *
     * @param email the email of the user whose chat dialogues to retrieve.
     * @return A CompletionStage that contains a stream of ChatDialogues.
     */
    //--------------------------------------------------------------------------------
    CompletionStage<Stream<ChatDialogue>> getAllByUser(String email);
}
