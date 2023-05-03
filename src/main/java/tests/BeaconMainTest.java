package tests;

import bluetooth.BeaconMain;
import com.javonet.JavonetException;

import java.io.IOException;

public class BeaconMainTest {
    public static void main(String[] args) throws JavonetException, IOException {
       BeaconMain beaconMain = new BeaconMain();
       beaconMain.start();
    }
}
