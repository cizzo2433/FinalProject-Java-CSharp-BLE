import com.javonet.Javonet;
import com.javonet.JavonetException;
import com.javonet.JavonetFramework;
import helpers.Activity_Wheel;
import helpers.Constants;
import helpers.SynthesizerV2;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import weatherAPI.Weather;

import java.io.FileNotFoundException;
import java.io.IOException;

public class BeaconMain extends Activity_Wheel {

    private final JavaWatcher jw;

    public BeaconMain() throws JavonetException {
        Javonet.activate(Constants.email, Constants.APIkey, JavonetFramework.v40);
        Javonet.addReference(Constants.filePath);
        this.jw = new JavaWatcher();
    }

    public void start() throws JavonetException, FileNotFoundException {
        boolean detected = false; // True once beacon signal detected

        Weather.getWeather("19003");
        System.out.println("Location Found");

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
