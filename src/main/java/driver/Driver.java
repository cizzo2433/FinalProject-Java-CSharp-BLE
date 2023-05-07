package driver;

import audio.SynthesizerV2;
import bluetooth.BeaconMain;
import com.javonet.JavonetException;
import helpers.Constants;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import java.io.IOException;

/**
 * Program entry-point
 */
public class Driver {
    public static void main(String[] args) throws JavonetException, IOException, JavaLayerException {

        String welcomeMessage = "Welcome to the program. Press the button on your beacon to initiate " +
                "location detection and I will provide you with the current weather";
        SynthesizerV2 synth = new SynthesizerV2(Constants.googleAPIKey);
        AdvancedPlayer player = new AdvancedPlayer(synth.getMP3Data(welcomeMessage));
        player.play();

        BeaconMain beaconMain = new BeaconMain();
        beaconMain.start();
    }
}
