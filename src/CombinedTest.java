import com.javonet.Javonet;
import com.javonet.JavonetException;
import com.javonet.JavonetFramework;
import helpers.SynthesizerV2;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import weatherAPI.Geocoder;
import weatherAPI.Weather;

import java.io.IOException;

/**
 * Test combining the weather API, activity randomizer, Bluetooth functionality, and text to speech capabilities.
 * Our magnum opus
 */
public class CombinedTest {

    public static void main(String[] args) throws JavonetException {

        // This is needed once at the beginning of the program to translate the C# library
        // Not needed for any consecutive calls to C# functions
        Javonet.activate(Constants.email, Constants.APIkey, JavonetFramework.v40);

        boolean detected = false; // True once beacon signal detected

        double[] location = Geocoder.geocode("19312");

        assert location != null;
        Weather.getWeather(location[0], location[1]);

        while (!detected) {
            detected = checkForBeacon();
            if (detected) {

                String message = "The current temperature is " + Weather.getCurrentTemp() +
                        " degrees, and conditions are " + Weather.getCurrentCondition();

                String activity = Activity_Wheel.pickActivity(Weather.getCurrentCondition());

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
     * Test check for BlueCharm beacon BLE signal. Currently, is set to search for the beacon name "MyBlueCharm",
     * but the dll can be reconfigured to use another identifier.
     *
     * @return a boolean
     * @throws JavonetException if any exception happens on the .NET side
     */
    public static boolean checkForBeacon() throws JavonetException {
        Javonet.addReference(Constants.filePath);

        // Will print message to console when signal is detected, returns a boolean
        return Javonet.getType("Program").invoke("CheckForBlueCharm");
    }

    /**
     * Tester method for text to speech using Google's text to speech API
     *
     * @param text The text that will be spoken
     */
    public static Thread textToSpeech(String text) {

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
