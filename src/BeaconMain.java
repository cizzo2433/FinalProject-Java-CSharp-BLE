import com.javonet.Javonet;
import com.javonet.JavonetException;
import com.javonet.JavonetFramework;
import helpers.Activity_Wheel;
import helpers.Constants;
import helpers.SynthesizerV2;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import weatherAPI.Geocoder;
import weatherAPI.Weather;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Class that handles the combined functions of determining the users' location and determining the temperature
 * and weather at that location. When a certain bluetooth signal is detected will use text to speech to say
 * the weather data and suggest an activity based on that data.
 */
public class BeaconMain extends Activity_Wheel {

    private final JavaWatcher jw; // object that handles detection of BLE signals

    /**
     * Constructor, calls to Javonet API to initialize the use of C# class library
     *
     * @throws JavonetException
     */
    public BeaconMain() throws JavonetException {
        Javonet.activate(Constants.email, Constants.APIkey, JavonetFramework.v40);
        Javonet.addReference(Constants.filePath);
        this.jw = new JavaWatcher();
    }

    /**
     * Listens for a specified bluetooth signal, and when detected, speaks a message with the temperature,
     * weather conditions, and a suggested activity for the day
     *
     * @throws JavonetException
     * @throws FileNotFoundException
     */
    public void start() throws JavonetException, FileNotFoundException {
        boolean detected = false; // True once beacon signal detected

        //Weather.getWeather("19312");
        //System.out.println("Location Found"); // For testing

        // pulls coordinates directly from the devices location service
        Double[] coordinates = Geocoder.geocode();

        Weather.getWeather(coordinates[0], coordinates[1]);

        // continue searching for BLE signal until detected
        while (!detected) {
            detected = jw.checkForBeacon();

            if (detected) {

                String message = "The current temperature is " + Weather.currentTemp +
                        " degrees, " + formatMessage(Weather.currentCondition);
                String activity = generateActivity(Weather.currentCondition);


                // Better way to have messages read one after the other without using Thread.sleep()
                // since the time between messages can vary
                Runnable r = () -> {
                    textToSpeech(message).run();
                    textToSpeech(activity).run();
                };
                new Thread(r).start();
            }
        }
    }

    /**
     * Formats message to be grammatically correct. Based off possible return values of the OpenWeatherMap API
     *
     * @param condition the condition returned from the API
     * @return a formatted message containing the current weather conditions
     */
    private String formatMessage(String condition) {

        switch (condition) {
            case "Clear":
                return "and conditions are clear.";
            case "Clouds":
                return "and conditions are cloudy.";
            case "Rain":
                return "and conditions are rainy";
            case "Drizzle":
                return "and there is light rain.";
            case "Thunderstorm":
                return "and conditions are stormy.";
            case "Snow":
                return "and conditions are snowy.";
            default:
                System.out.println("something went wrong");
                break;
        }
        return null;
    }

    /**
     * Converts a String message into an InputStream of MP3 data, which is returned in a Thread which will
     * play the MP3 using JLayer. The Thread will not start until specified elsewhere in the program.
     *
     * @param text a String message
     * @return a Thread
     */
    private Thread textToSpeech(String text) {

        SynthesizerV2 synthesizer = new SynthesizerV2(Constants.googleAPIKey);

        return new Thread(() -> {
            try {

                // Using JLayer to play MP3 data
                AdvancedPlayer player = new AdvancedPlayer(synthesizer.getMP3Data(text));
                player.play();

            } catch (IOException | JavaLayerException e) {
                e.printStackTrace();
            }
        });
    }
}
