package tests;

import bluetooth.BeaconMain;
import com.javonet.JavonetException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class BeaconMainTest {
    public static void main(String[] args) throws JavonetException, IOException, InterruptedException {
       BeaconMain beaconMain = new BeaconMain();
       beaconMain.start();
    }
}
