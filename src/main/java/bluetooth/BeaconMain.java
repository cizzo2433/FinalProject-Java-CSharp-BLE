package bluetooth;

import com.javonet.Javonet;
import com.javonet.JavonetException;
import com.javonet.JavonetFramework;
import helpers.*;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import weatherAPI.Geocoder;
import weatherAPI.Weather;
import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;
import java.util.concurrent.CountDownLatch;


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
        if (!Javonet.isActivated()) {
            Javonet.activate(Constants.email, Constants.APIkey, JavonetFramework.v45);
        }
        Javonet.addReference(Constants.filePath, Constants.bArr);
        this.jw = new JavaWatcher();
    }


    /**
     * Listens for a specified bluetooth signal, and when detected, speaks a message with the temperature,
     * weather conditions, and a suggested activity for the day
     *
     * @throws JavonetException
     * @throws FileNotFoundException
     */
    public void start() throws JavonetException, IOException, InterruptedException {
        boolean detected = false; // True once beacon signal detected

        // pulls coordinates directly from the devices location service
        Double[] coordinates = Geocoder.geocode();

        Weather.getWeather(coordinates[0], coordinates[1]);

        String message = "The current temperature is " + Weather.currentTemp +
                " degrees, " + formatMessage(Weather.currentCondition);
        String activity = generateActivity(Weather.currentTemp, Weather.currentCondition);

        // continue searching for BLE signal until detected
        while (!detected) {
            detected = jw.checkForBeacon();

            if (detected) {
                Roxanne roxanne = new Roxanne(message, activity);
                roxanne.updateLocation(coordinates[0], coordinates[1]);
                roxanne.run();
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

}
