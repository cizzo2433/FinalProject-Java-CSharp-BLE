package tests;

import com.javonet.Javonet;
import com.javonet.JavonetException;
import com.javonet.JavonetFramework;
import helpers.Constants;
import helpers.GoogleTranslate;
import helpers.SynthesizerV2;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.json.simple.parser.ParseException;

import java.io.IOException;

/**
 * A bit on how this works. The dll file in the .idea/csharp folder is the C# code compiled into the equivalent of a
 * Java class library. Using the Javonet API, Java can translate the dll into something it can actually read and
 * use. To use the methods you just specify the C# class with Javonet.getType, and then call the method with
 * Javonet.invoke. We should only really need the one method for this, but if needed it should be easy going forward
 * to use any code written in C#. I wrote the code in .NET core which is platform independent, so if you have a Mac
 * there shouldn't be any need to even download Visual Studio, the dll file has everything in it.
 */
public class Test {
    public static void main(String[] args) throws JavonetException, IOException, ParseException {

        // This is needed once at the beginning of the program to translate the C# library
        // Not needed for any consecutive calls to C# functions
        Javonet.activate(Constants.email, Constants.APIkey, JavonetFramework.v40);

        boolean detected = false;

        // Run until signal is detected
        while (!detected) {
            detected = checkForBeacon();
            if (detected) {
                textToSpeech("Beacon signal detected");

                // This part below is just for fun. We can translate to any language by generating a URL to send to
                // Google Translate, parsing the result, and then sending it to the textToSpeech method. First param
                // for translate method is an ISO 639 language code, link below if you guys want to mess around
                // https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes
                try {

                    // So they don't talk over each-other, increase time for longer messages
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                textToSpeech(GoogleTranslate.translate("es","Beacon signal detected"));
            }
        }
    }

    /**
     * tests.Test check for BlueCharm beacon BLE signal. Currently, is set to search for the beacon name "MyBlueCharm",
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
    public static void textToSpeech(String text) {
        SynthesizerV2 synthesizer = new SynthesizerV2(Constants.googleAPIKey);
        Thread thread = new Thread(() -> {
            try {

                // Using JLayer to play MP3 data
                AdvancedPlayer player = new AdvancedPlayer(synthesizer.getMP3Data(text));
                player.play();

            } catch (IOException  | JavaLayerException e) {
                e.printStackTrace();
            }
        });

        // We don't want the application to terminate before this Thread terminates
        thread.setDaemon(false);
        thread.start();
    }
}
