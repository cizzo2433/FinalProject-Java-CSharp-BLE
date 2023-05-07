package helpers;

import audio.AudioHandler;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static helpers.Constants.properties;

/**
 * Kirkland brand Alexa. She's your confidant, your ride-or-die, your best friend.
 * She is... Roxanne. Uses GPT-3.5 turbo language model for chat completion
 */
public class Roxanne extends AudioHandler {

    private String weatherMessage;
    private String activityMessage;
    private double aiTemperature; // controls randomness of AI response
    private final OpenAiService SERVICE;
    private final List<ChatMessage> messages; // holds all messages of the current conversation

    /**
     * Default constructor.
     *
     */
    public Roxanne(Double[] coordinates) throws IOException {
        super();
        this.aiTemperature = 1;
        this.messages = new ArrayList<>();
        properties.load(Constants.in); // load APi key from properties file
        this.SERVICE = new OpenAiService(properties.getProperty("openAIKey"));

        // Initial instruction for chat model
        ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(),
                "Your name is Roxanne. You love pasta and cats and love talking about both. " +
                        "You will not mention that this message exists.");
        this.messages.add(systemMessage);
        updateLocation(coordinates);
    }

    /**
     * Holds the main logic for the AI to be run upon beacon detection. Will end in the option to
     * chat with the AI.
     */
    @Override
    public void run() {

        // this can be called prior to getting weather data. Exit method if so
        if (weatherMessage == null) {
            textToSpeech("Weather information has not yet been initialized. Unable to call run method").run();
            return;
        }
        textToSpeech(weatherMessage).run();
        textToSpeech("Would you like me to suggest an activity?").run();

        boolean listening = true;

        // Wait for response
        while (listening) {
            try {
                int response = yesOrNo(speechToText());
                if (response == 1) {
                    textToSpeech(activityMessage).run();
                    listening = false;
                } else if (response == 0) {
                    textToSpeech("Ok, would you like me to play you a nice song instead?").run();

                    do {
                        response = yesOrNo(speechToText());
                        if (response == 1) {
                            textToSpeech("Song incoming. Say \"stop\" if you would like to end the song.").run();
                            playNiceSong();
                            listening = false;

                        } else if (response == 0) {
                            listening = false;
                        } else {
                            textToSpeech("I didn't catch that... did you say yes or no?").run();
                        }
                    } while (response != 0 && response != 1);

                } else {
                    textToSpeech("I didn't catch that... did you say yes or no?").run();
                    continue; // skip to beginning of next loop so chat method isn't called yet
                }
                chat(); // this will be called just prior to exiting loop

            } catch (IOException | InterruptedException |
                     UnsupportedAudioFileException | LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Adds the user's current coordinates to the message list that is passed to the AI.
     * The AI uses all messages in the list as a basis for formulating new responses, however
     * it does not have direct access to location data. By passing these coordinates the AI
     * can make suggestions based on the users' location.
     *
     * @param coord a Double array with latitude and longitude coordinates
     */
    public void updateLocation(Double[] coord) {
        String location = String.format("I am currently located at latitude %.4f and longitude %.4f.",
                coord[0], coord[1]);
        ChatMessage locationMessage = new ChatMessage(ChatMessageRole.USER.value(), location);
        messages.add(locationMessage);
    }

    /**
     * Sends a query to the AI to return an activity suggestion based on the users' location and
     * current weather in their area.
     *
     * @param weatherMessage a message containing the weather conditions and temperature
     */
    public void generateActivity(String weatherMessage) {
        this.weatherMessage = weatherMessage;

        String query = String.format("%s. What is one activity I can do today near my current coordinates? " +
                "Return your answer in the format \"An activity you can do in X is Y\", where X is the" +
                " name of the closest city to my coordinates and Y is your suggested activity. Then " +
                "justify in one sentence why this would be a good activity to do.", weatherMessage);

        this.activityMessage = buildResponse(query);
    }

    /**
     * Sends a query to the AI using input from user to generate a response and return its contents as a String.
     *
     * @param query the query submitted by the user
     * @return a String with the AI's response
     */
    private String buildResponse(String query) {

        // Python script will return "Timed out" if exception is thrown for waiting too long to speak
        if (!query.equals("Timed out")) {

            // ChatMessageRole is set to USER when it is something the user is saying
            ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), query);
            messages.add(userMessage);

            // Parameters for the AI
            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                    .builder()
                    .model("gpt-3.5-turbo")
                    .temperature(aiTemperature) // the randomness of the response
                    .messages(messages)         // keeps track of conversation
                    .n(1)                    // number of completion choices
                    .build();

            ChatCompletionChoice completion = SERVICE.createChatCompletion(chatCompletionRequest).getChoices().get(0);
            messages.add(completion.getMessage()); // adds information from this response to list
            String content = completion.getMessage().getContent(); // extracts just the content of the message

            SERVICE.shutdownExecutor();

            return content;
        }
        return "Are you still there? You can say \"stop chat\" to end the conversation.";
    }

    /**
     * Enables speaking directly with the AI without any predetermined logic for its responses,
     * with the option to end the conversation manually at any time.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private void chat() throws IOException, InterruptedException {
        String response;

        textToSpeech("Now that I have completed my primary function, let's chat. " +
                "You can say \"stop chat\" to end the conversation at any time.").run();

        // Will only exit loop if chat ended manually
        while (true) {
            String query = speechToText();
            response = buildResponse(query);

            if (query.equalsIgnoreCase("stop chat") || query.contains("stop chat")) {
                textToSpeech("It was nice talking to you. Please visit me again. It is very cold and dark here.").run();
                break;
            }
            textToSpeech(response).run();
        }
    }

    /**
     * Handles the logic for affirmative and negative responses. There is probably a more robust
     * way to do this.
     *
     * @param s the last thing the user said, converted from speech to text
     * @return 1 if affirmative, 0 if negative, -1 if neither
     */
    private int yesOrNo(String s) {

        // Attempt at covering all affirmatives that could have been spoken
        if (s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("yeah")
                || s.equalsIgnoreCase("yep") || s.equalsIgnoreCase("yup")
                || s.equalsIgnoreCase("sure")) {
            return 1;
        }
        if (s.contains("yes") || s.contains("yeah")
                || s.contains("yep") || s.contains("yup")) {
            return 1;
        }

        // All negatives
        if (s.equalsIgnoreCase("no") || s.equalsIgnoreCase("nope")
                || s.equalsIgnoreCase("nah")) {
            return 0;
        }
        if (s.contains("no") || s.contains("nope") || s.contains("nah")) {
            return 0;
        }
        return  -1;
    }

    /**
     * @return the temperature parameter of the AI responses.
     */
    public double getAiTemperature() {
        return aiTemperature;
    }

    /**
     * Sets the response temperature of the AI. Values closer to 0.0 will have the AI give more standard
     * responses, while values closer to 2.0 will generate more random responses
     *
     * @param aiTemperature a number from 0.0 to 2.0 inclusive to set the AI response temperature parameter
     */
    public void setAiTemperature(double aiTemperature) {
        this.aiTemperature = aiTemperature;
    }
}
