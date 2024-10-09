package controllers;

import dto.ChatQueryDTO;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.ChatService;
import services.ChatServiceImpl;

import javax.inject.Inject;
import java.util.Map;
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
    public ChatController(ChatServiceImpl chatService, FormFactory formFactory) {
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
        String email = getEmailFromSession(request);

        return chatService.processChatRequest(chatQueryDTO, email)
            .thenApply(chatRequest -> ok(Json.toJson(chatRequest)))
            .exceptionally(e ->  internalServerError("Error processing request: " + e.getMessage()));
    }

    //--------------------------------------------------------------------------------
    /**
     * Fetches all chat queries for the logged-in user.
     * @param request the HTTP request.
     * @return a CompletionStage containing the Result with the list of queries.
     */
    //--------------------------------------------------------------------------------
    public CompletionStage<Result> getAllQueries(Http.Request request) {
        String email = getEmailFromSession(request);

        return chatService.getAllQueriesByUser(email)
            .thenApply(queries -> ok(Json.toJson(queries)))
            .exceptionally(e -> internalServerError("Error fetching queries: " + e.getMessage()));
    }

    //--------------------------------------------------------------------------------
    /**
     * Handles user login by storing the email in the session.
     * @param request the HTTP request.
     * @return a redirect to the home page with the email added to the session.
     * @throws IllegalArgumentException if the email is missing.
     */
    //--------------------------------------------------------------------------------
    public Result login(Http.Request request) {
        Map<String, String[]> formParams = request.body().asFormUrlEncoded();

        if (formParams == null || !formParams.containsKey("email") || formParams.get("email").length == 0) {
            throw new IllegalArgumentException("Email is required for login.");
        }

        String email = formParams.get("email")[0];
        return redirect("/home").addingToSession(request, "email", email);
    }

    //--------------------------------------------------------------------------------
    /**
     * Renders the home page with the email from the session.
     * @param request the HTTP request.
     * @return the rendered home page.
     */
    //--------------------------------------------------------------------------------
    public Result home(Http.Request request) {
        String email = request.session().get("email").orElse("No email in session");
        return ok(views.html.home.render(email));
    }

    //--------------------------------------------------------------------------------
    /**
     * Helper method to fetch email from the session.
     * @param request the HTTP request.
     * @return the email if found, otherwise throws an exception.
     */
    //--------------------------------------------------------------------------------
    private String getEmailFromSession(Http.Request request) {
        return request.session().get("email").orElseThrow(() ->
            new IllegalArgumentException("No email in session, user must be logged in."));
    }
}

