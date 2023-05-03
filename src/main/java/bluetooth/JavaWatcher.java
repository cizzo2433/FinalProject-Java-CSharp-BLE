package bluetooth;

import com.javonet.JavonetException;
import com.javonet.api.NObject;

/**
 * BLE watcher class using C# wrapper to access the .NET BLE API
 */
public class JavaWatcher extends NObject {

    /**
     * Constructor, with call to C# super class
     *
     * @throws JavonetException
     */
    public JavaWatcher() throws JavonetException {
        super("Program");
    }

    /**
     * Searches for beacon signal based on the assigned beacon name
     *
     * @return true if signal found, false otherwise
     * @throws JavonetException
     */
    public boolean checkForBeacon() throws JavonetException {
       return this.invoke("CheckForBlueCharm");
    }
}
