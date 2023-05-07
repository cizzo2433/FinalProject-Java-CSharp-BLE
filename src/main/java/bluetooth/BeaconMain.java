package bluetooth;

import com.javonet.Javonet;
import com.javonet.JavonetException;
import com.javonet.JavonetFramework;
import com.javonet.api.NObject;
import helpers.*;
import weatherAPI.Geocoder;
import weatherAPI.Weather;
import java.io.*;


/**
 * Class that handles the combined functions of determining the users' location and determining the temperature
 * and weather at that location. When a certain bluetooth signal is detected will use text to speech and GPT-3.5
 * language model to generate and activity based on the weather.
 */
public class BeaconMain {

    private final JavaWatcher jw;

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
        jw = new JavaWatcher();
    }


    /**
     * Listens for a specified bluetooth signal, and when detected, speaks a message with the temperature,
     * weather conditions, and will suggest an activity for the day when prompted. Will also allow chat with
     * AI language model.
     *
     * @throws JavonetException
     * @throws FileNotFoundException
     */
    public void start() throws JavonetException, IOException {
        boolean detected = false; // True once beacon signal detected

        // pulls coordinates directly from the devices location service
        Double[] coordinates = Geocoder.geocode();
        Weather.getWeather(coordinates[0], coordinates[1]);

        Roxanne roxanne = new Roxanne(coordinates);

        String weather = "The current temperature is " + Weather.currentTemp +
                " degrees, " + formatMessage(Weather.currentCondition);
        roxanne.generateActivity(weather);

        // continue searching for BLE signal until detected
        while (!detected) {
            detected = jw.checkForBeacon();

            if (detected) {
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

    /**
     * BLE watcher class using C# wrapper to access the .NET BLE library
     */
    private static final class JavaWatcher extends NObject {

        /**
         * Constructor, with call to C# super class
         *
         * @throws JavonetException
         */
        private JavaWatcher() throws JavonetException {
            super("Program");
        }

        /**
         * Searches for beacon signal based on the assigned beacon name
         *
         * @return true if signal found, false otherwise
         * @throws JavonetException
         */
        private boolean checkForBeacon() throws JavonetException {
            return this.invoke("CheckForBlueCharm");
        }
    }
}
