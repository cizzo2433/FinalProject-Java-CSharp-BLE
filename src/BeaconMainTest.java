import com.javonet.JavonetException;

import java.io.FileNotFoundException;

public class BeaconMainTest {
    public static void main(String[] args) throws JavonetException, FileNotFoundException {
        BeaconMain beaconMain = new BeaconMain();
        beaconMain.start();
    }
}
