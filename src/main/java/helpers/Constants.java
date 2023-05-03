package helpers;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Helper class for constants
 */
public class Constants {

    // To reference dll without using absolute file path
    private static final InputStream IS =
            Constants.class.getClassLoader().getResourceAsStream("QuickBlueToothLE.dll");
    private static final byte[] data;

    static {
        try {
            data = IOUtils.toByteArray(IS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String email = "izzochristian662@gmail.com", APIkey = "Zt6s-Do2w-Fo98-q7L2-Ei27",
            filePath = "QuickBlueToothLE.dll", googleAPIKey = "AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw",

    // We should all decide on a beacon name so we can use a constant
    beaconName = "";

    public static byte[] bArr = data;


}
