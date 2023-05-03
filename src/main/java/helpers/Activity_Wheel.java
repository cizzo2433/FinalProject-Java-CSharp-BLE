package helpers;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.io.*;
import java.util.*;

public class Activity_Wheel {

    // Activation for OpenAI API
    private static final OpenAiService SERVICE
            = new OpenAiService("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");

    /**
     * Returns a String message containing an activity that is randomly selected from an ArrayList
     * of possible activities, which is filled based on the weather String passed to the method
     *
     * @param weather a String containing a type of weather condition obtained from the weatherAPI
     * @return a String message
     * @throws FileNotFoundException
     */
    public static String generateActivity(String weather) throws IOException {
        
        Random dice = new Random();
        ArrayList<String> activities = null;
        InputStream in = Activity_Wheel.class.getClassLoader().getResourceAsStream("Clear.txt");

        switch (weather) {
            case "Clear":
                activities = generateList(in);
                break;
            case "Clouds":
                activities = generateList(in);
                break;
            case "Drizzle":
            case "Rain":
                activities = generateList(in);
                break;
            case "Thunderstorm":
                activities = generateList(in);
                break;
            case "Snow":
                activities = generateList(in);
                break;
            default:
                System.out.println("error");
                break;
        }
        return "You should " + activities.get(dice.nextInt(activities.size()));
    }

    /**
     * Returns a suggested activity based on the weather, calling the OpenAI API to generate the suggestion
     *
     * @param temperature the temperature
     * @param weather the weather conditions
     * @return an activity suggestion as a String
     */
    public static String generateActivity(double temperature, String weather) {

        // Request to send to the API. So far this has given the best results but can be edited
        String request = String.format("The temperature is %.2f degrees and conditions are %s. What is one" +
                "activity I can do today? Give only 1 suggestion.", temperature, weather);

        // Creates the actual query to be sent, setting the role of the AI to assistant for response formatting
        // (other options are user and system)
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemMessage = new ChatMessage(ChatMessageRole.ASSISTANT.value(), request);
        messages.add(systemMessage);

        // Parameters for the AI
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .temperature(1.0)       // edit this to change the randomness of the response (0.0 least, 2.0 most)
                .messages(messages)
                .n(1)
                .build();

        List<ChatCompletionChoice> completion = SERVICE.createChatCompletion(chatCompletionRequest).getChoices();

        // Parsing response to only include necessary content
        String message = completion.get(0).toString();
        String[] sp = message.split("content=");
        sp = sp[1].split("finishReason=stop");

        SERVICE.shutdownExecutor();

        return sp[0];
    }

    /**
     * Fills an ArrayList of Strings with the lines of a txt file corresponding to a type of weather
     *
     * @param file the reference file
     * @return an ArrayList containing different activities
     * @throws FileNotFoundException
     */
    private static ArrayList<String> generateList(File file) throws FileNotFoundException {
        Scanner fileReader = new Scanner(file);
        ArrayList<String> activities = new ArrayList<>();

        while (fileReader.hasNext()) {
            activities.add(fileReader.nextLine());
        }
        return activities;
    }

    /**
     * Overloaded method to read from InputStream, used when deploying to server
     *
     * @param in the InputStream to be read
     * @return an ArrayList containing different activities
     * @throws IOException
     */
    private static ArrayList<String> generateList(InputStream in) throws IOException {
        Scanner fileReader = new Scanner(in);
        ArrayList<String> activities = new ArrayList<>();

        while (fileReader.hasNext()) {
            activities.add(fileReader.nextLine());
        }
        in.close();
        return activities;
    }





    // Consolidated everything below into the 2 methods above
}