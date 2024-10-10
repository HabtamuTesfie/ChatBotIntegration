package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import dao.ChatDialogueRepository;
import dto.ChatDialogueDTO;
import dto.ChatQueryDTO;
import lombok.extern.slf4j.Slf4j;
import models.ChatDialogue;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Timestamp;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

//--------------------------------------------------------------------------------
/**
 * ChatServiceImpl is an implementation of the ChatService interface that handles
 * processing
 * chat requests via the ChatGPT API and retrieving user chat history.
 */
//--------------------------------------------------------------------------------
@Singleton
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatDialogueRepository chatRequestRepository;
    private final WSClient wsClient;
    private final String openAiApiUrl;
    private final String openAiApiKey;

    @Inject
    public ChatServiceImpl(ChatDialogueRepository chatRequestRepository, WSClient wsClient, Config config) {
        this.chatRequestRepository = chatRequestRepository;
        this.wsClient = wsClient;
        this.openAiApiUrl = config.getString("openai.api.url");
        this.openAiApiKey = config.getString("openai.api.key");
    }

    //--------------------------------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    //--------------------------------------------------------------------------------
    @Override
    public CompletionStage<ChatDialogueDTO> processChatRequest(ChatQueryDTO chatQueryDTO, String email) {
        return callChatGPT(chatQueryDTO.getInstruction(), chatQueryDTO.getQuestion()).thenApply(chatGptResponse -> {
            System.out.println("chatgpt response: " + chatGptResponse);

            if (chatGptResponse.startsWith("Error:")) {
                log.error("ChatGPT returned an error: {}", chatGptResponse);
                chatGptResponse = getMockChatGptResponse(chatQueryDTO.getQuestion());
            }

            return saveChatAndConvertToDTO(chatQueryDTO, email, chatGptResponse);
        }).exceptionally(ex -> {
            log.error("Failed to process chat request", ex);
            return saveChatAndConvertToDTO(chatQueryDTO, email, getMockChatGptResponse(chatQueryDTO.getQuestion()));
        });
    }

    //--------------------------------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    //--------------------------------------------------------------------------------
    @Override
    public CompletionStage<Stream<ChatDialogueDTO>> getAllQueriesByUser(String email) {
        return chatRequestRepository.getAllByUser(email)
            .thenApply(chatDialogueStream -> chatDialogueStream.map(this::convertToDTO));
    }

    //--------------------------------------------------------------------------------
    /**
     * Saves the chat request and converts it to ChatDialogueDTO.
     *
     * @param chatQueryDTO     The chat query DTO containing the instruction
     *                         and question.
     * @param email            The email of the user making the request.
     * @param chatGptResponse  The response from the ChatGPT API or a mock response.
     * @return The ChatDialogueDTO representing the chat dialogue.
     */
    //--------------------------------------------------------------------------------
    private ChatDialogueDTO saveChatAndConvertToDTO(ChatQueryDTO chatQueryDTO, String email, String chatGptResponse) {
        ChatDialogue chatDialogue = new ChatDialogue();
        chatDialogue.setInstruction(chatQueryDTO.getInstruction());
        chatDialogue.setQuestion(chatQueryDTO.getQuestion());
        chatDialogue.setResponse(chatGptResponse);
        chatDialogue.setEmail(email);
        chatDialogue.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        chatRequestRepository.saveChatDialogue(chatDialogue);

        return convertToDTO(chatDialogue);
    }

    //--------------------------------------------------------------------------------
    /**
     * Converts a ChatDialogue entity to a ChatDialogueDTO.
     *
     * @param chatDialogue The ChatDialogue entity to be converted.
     * @return The corresponding ChatDialogueDTO.
     */
    //--------------------------------------------------------------------------------
    private ChatDialogueDTO convertToDTO(ChatDialogue chatDialogue) {
        return new ChatDialogueDTO(
            chatDialogue.getInstruction(),
            chatDialogue.getQuestion(),
            chatDialogue.getResponse(),
            chatDialogue.getCreatedAt(),
            chatDialogue.getEmail()
        );
    }

    //--------------------------------------------------------------------------------
    /**
     * Calls the ChatGPT API to process the given instruction and question.
     *
     * @param instruction The instruction for the system role.
     * @param question    The user's question.
     * @return A CompletionStage containing the API response as a string.
     */
    //--------------------------------------------------------------------------------
    private CompletionStage<String> callChatGPT(String instruction, String question) {
        String body = createRequestBody(instruction, question);

        WSRequest request = wsClient.url(openAiApiUrl)
            .addHeader("Authorization", "Bearer " + openAiApiKey)
            .addHeader("Content-Type", "application/json");

        return request.post(body).thenApply(WSResponse::asJson)
            .thenApply(this::extractChatGptResponse)
            .exceptionally(ex -> {
                log.error("Failed to call ChatGPT API", ex);
                return "Error: Unable to communicate with ChatGPT API";
            });
    }

    //--------------------------------------------------------------------------------
    /**
     * Creates the request body for the ChatGPT API request.
     *
     * @param instruction The system role instruction.
     * @param question    The user's question.
     * @return The formatted JSON request body.
     */
    //--------------------------------------------------------------------------------
    private String createRequestBody(String instruction, String question) {
        return "{\n" +
            "  \"model\": \"gpt-3.5-turbo\", \n" +
            "  \"messages\": [{\"role\": \"system\", \"content\": \"" + instruction + "\"}, \n" +
            "                {\"role\": \"user\", \"content\": \"" + question + "\"}]\n" +
            "}";
    }

    //--------------------------------------------------------------------------------
    /**
     * Extracts the ChatGPT response from the API's JSON response.
     *
     * @param json The JSON response from the ChatGPT API.
     * @return The content of the response as a string.
     */
    //--------------------------------------------------------------------------------
    private String extractChatGptResponse(JsonNode json) {
        System.out.println("json: " + json);
        try {
            if (json.has("error")) {
                JsonNode errorNode = json.get("error");
                String errorMessage = errorNode.has("message") ? errorNode.get("message").asText() : "Unknown error";
                String errorType = errorNode.has("type") ? errorNode.get("type").asText() : "Unknown type";
                String errorParam = errorNode.has("param") && !errorNode.get("param").isNull() ? errorNode.get("param").asText() : "None";
                String errorCode = errorNode.has("code") ? errorNode.get("code").asText() : "Unknown code";

                String formattedError = String.format(
                    "API Error:\n- Message: %s\n- Type: %s\n- Param: %s\n- Code: %s",
                    errorMessage, errorType, errorParam, errorCode
                );
                throw new IllegalStateException(formattedError);
            }

            JsonNode choicesNode = json.path("choices");
            if (choicesNode.isArray() && !choicesNode.isEmpty()) {
                JsonNode messageNode = choicesNode.get(0).path("message");
                if (messageNode.has("content")) {
                    return messageNode.get("content").asText();
                }
            }

            throw new IllegalStateException("Unexpected response format: " + json);
        } catch (Exception e) {
            log.error("Failed to parse ChatGPT response", e);
            return "Error: Unable to parse response from ChatGPT";
        }
    }

    //--------------------------------------------------------------------------------
    /**
     * Provides a mock response for a chat query.
     *
     * @param question The question for which a mock response is needed.
     * @return A mock response string.
     */
    //--------------------------------------------------------------------------------
    private String getMockChatGptResponse(String question) {
        return "This is a mock response for the question: " + question;
    }
}
