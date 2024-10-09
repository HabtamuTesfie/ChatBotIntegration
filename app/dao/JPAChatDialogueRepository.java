package dao;

import models.ChatDialogue;
import play.db.jpa.JPAApi;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;
import static java.util.concurrent.CompletableFuture.supplyAsync;

//--------------------------------------------------------------------------------
/**
 * Repository implementation for ChatDialogue entities using JPA.
 * Executes asynchronous database operations with JPAApi.
 */
//--------------------------------------------------------------------------------
public class JPAChatDialogueRepository implements ChatDialogueRepository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAChatDialogueRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    //--------------------------------------------------------------------------------
    /**
     * Saves a ChatDialogue asynchronously within a transaction.
     *
     * @param chatRequest The ChatDialogue to be saved.
     * @return A CompletionStage that resolves to the saved ChatDialogue.
     */
    //--------------------------------------------------------------------------------
    @Override
    public CompletionStage<ChatDialogue> saveChatRequest(ChatDialogue chatRequest) {
        return supplyAsync(() -> wrap(em -> insert(em, chatRequest)), executionContext);
    }

    //--------------------------------------------------------------------------------
    /**
     * Retrieves all chat dialogues for a specific user asynchronously.
     *
     * @param email the email of the user whose chat dialogues to retrieve.
     * @return A CompletionStage that contains a stream of ChatDialogues.
     */
    //--------------------------------------------------------------------------------
    @Override
    public CompletionStage<Stream<ChatDialogue>> getAllByUser(String email) {
        return supplyAsync(() -> wrap(em -> findAllByUser(em, email)), executionContext);
    }

    //--------------------------------------------------------------------------------
    /**
     * Wraps a database operation in a transaction and executes it.
     *
     * @param function The function to execute within a transaction.
     * @return The result of the function execution.
     */
    //--------------------------------------------------------------------------------
    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    //--------------------------------------------------------------------------------
    /**
     * Inserts a ChatDialogue entity into the database.
     *
     * @param em The EntityManager used to persist the entity.
     * @param chatRequest The ChatDialogue entity to be inserted.
     * @return The inserted ChatDialogue entity.
     */
    //--------------------------------------------------------------------------------
    private ChatDialogue insert(EntityManager em, ChatDialogue chatRequest) {
        em.persist(chatRequest);
        return chatRequest;
    }

    //--------------------------------------------------------------------------------
    /**
     * Finds all chat dialogues for a specific user.
     *
     * @param em The EntityManager used to execute the query.
     * @param email The email of the user whose chat dialogues to retrieve.
     * @return A stream of ChatDialogue objects.
     */
    //--------------------------------------------------------------------------------
    private Stream<ChatDialogue> findAllByUser(EntityManager em, String email) {
        TypedQuery<ChatDialogue> query = em.createQuery("SELECT c FROM ChatDialogue c WHERE c.email = :email", ChatDialogue.class);
        query.setParameter("email", email);
        List<ChatDialogue> chatRequests = query.getResultList();
        return chatRequests.stream();
    }
}
