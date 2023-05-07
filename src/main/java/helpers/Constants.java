package helpers;

import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Helper class for constants
 */
public class Constants {

    public static String email = "izzochristian662@gmail.com", APIkey = "Zt6s-Do2w-Fo98-q7L2-Ei27",
            filePath = "QuickBlueToothLE.dll", googleAPIKey = "AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw";

    // loads the C# dll as an InputStream, which is converted to a byte array. Used for Javonet API activation
    private static final InputStream IS =
            Constants.class.getClassLoader().getResourceAsStream(filePath);
    private static final byte[] data;

    static {
        try {
            data = IOUtils.toByteArray(IS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reference to properties file to pull OpenAI API key. Will need to create own properties file named
     * application.properties in the resources folder and add one line with the format:
     * OpenAIKey=your_API_key_goes_here
     * This is to prevent API key deactivation from being shared on GitHub
      */
    public static InputStream in = Constants.class.getClassLoader().getResourceAsStream("application.properties");
    public static Properties properties = new Properties();

    public static byte[] bArr = data;
}
