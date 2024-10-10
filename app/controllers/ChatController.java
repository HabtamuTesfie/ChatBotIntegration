package controllers;

import dto.ChatQueryDTO;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.ChatService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

//--------------------------------------------------------------------------------
/**
 * Controller for handling chat-related actions such as submitting queries,
 * retrieving all queries for the logged-in user, and managing user sessions.
 * It interacts with the ChatService to process requests and ensures proper
 * session management and form validation.
 */
//--------------------------------------------------------------------------------
public class ChatController extends Controller {

    private final ChatService chatService;
    private final FormFactory formFactory;

    @Inject
    public ChatController(ChatService chatService, FormFactory formFactory) {
        this.chatService = chatService;
        this.formFactory = formFactory;
    }

    //--------------------------------------------------------------------------------
    /**
     * Handles submitting a chat query.
     * @param request the HTTP request containing the query.
     * @return a CompletionStage containing the Result of the chat request processing.
     * @throws IllegalArgumentException if the form validation fails or email is
     * missing.
     */
    //--------------------------------------------------------------------------------
    public CompletionStage<Result> submitQuery(Http.Request request) {
        Form<ChatQueryDTO> boundForm = formFactory.form(ChatQueryDTO.class).bindFromRequest(request);

        if (boundForm.hasErrors()) {
            throw new IllegalArgumentException("Form validation failed: " + boundForm.errorsAsJson());
        }

        ChatQueryDTO chatQueryDTO = boundForm.get();
        Http.Cookie emailCookie = request.cookies().get("email").orElse(null);
        String email = (emailCookie != null) ? emailCookie.value() : "anonymous";

        return chatService.processChatRequest(chatQueryDTO, email)
            .thenApply(chatRequest -> ok(Json.toJson(chatRequest)))
            .exceptionally(e ->  internalServerError("Error processing request: " + e.getMessage()));
    }

    //--------------------------------------------------------------------------------
    /**
     * Fetches all chat queries for the logged-in user. This method expects an
     * email cookie to be present in the request, which identifies the user.
     * If the email cookie is missing or empty, the method will return a
     * bad request response with an error message.
     *
     * @param request the HTTP request containing cookies.
     * @return a CompletionStage containing the Result with the list of queries
     *         if the email is valid, or a bad request response if the email
     *         is missing.
     */
    //--------------------------------------------------------------------------------
    public CompletionStage<Result> getAllQueries(Http.Request request) {
        Http.Cookie emailCookie = request.cookies().get("email").orElse(null);

        if (emailCookie == null || emailCookie.value().trim().isEmpty()) {
            return CompletableFuture.completedFuture(badRequest("Error: Email is required and missing in the cookies."));
        }

        String email = emailCookie.value();

        return chatService.getAllQueriesByUser(email)
            .thenApply(queries -> ok(Json.toJson(queries)))
            .exceptionally(e -> internalServerError("Error fetching queries: " + e.getMessage()));
    }

}
