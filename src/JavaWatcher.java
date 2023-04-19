import com.javonet.JavonetException;
import com.javonet.api.NObject;

/**
 * BLE watcher class using C# wrapper to access the .NET BLE API. Probably won't use this
 */
public class JavaWatcher extends NObject {

    public JavaWatcher() throws JavonetException {
        super("MyWatcher");
    }

    public void run() throws JavonetException {
        this.invoke("Run");
    }
}
