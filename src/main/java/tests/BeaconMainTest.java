package tests;

import audio.SynthesizerV2;
import bluetooth.BeaconMain;
import com.javonet.JavonetException;
import helpers.Constants;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.IOException;

public class BeaconMainTest {
    public static void main(String[] args) throws JavonetException, IOException, InterruptedException, JavaLayerException {
        String welcomeMessage = "Welcome to the program. Press the button on your beacon to initiate " +
                "location detection and I will provide you with the current weather";
        SynthesizerV2 synth = new SynthesizerV2(Constants.googleAPIKey);
        AdvancedPlayer player = new AdvancedPlayer(synth.getMP3Data(welcomeMessage));
        player.play();

        BeaconMain beaconMain = new BeaconMain();
        beaconMain.start();
    }
}
