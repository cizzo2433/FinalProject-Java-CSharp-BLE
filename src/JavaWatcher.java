import com.javonet.Javonet;
import com.javonet.JavonetException;
import com.javonet.api.NObject;

/**
 * BLE watcher class using C# wrapper to access the .NET BLE API. Probably won't use this
 */
public class JavaWatcher extends NObject {

    public JavaWatcher() throws JavonetException {
        super("Program");
    }

    public boolean checkForBeacon() throws JavonetException {
       return this.invoke("CheckForBlueCharm");
    }
}
