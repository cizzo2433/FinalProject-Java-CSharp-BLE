import com.javonet.Javonet;
import com.javonet.JavonetException;
import com.javonet.JavonetFramework;

/**
 * A bit on how this works. The dll file in the .idea/csharp folder is the C# code compiled into the equivalent of a
 * Java class library. Using the Javonet API, Java can translate the dll into something it can actually read and
 * use. To use the methods you just specify the C# class with Javonet.getType, and then call the method with
 * Javonet.invoke. We should only really need the one method for this, but if needed it should be easy going forward
 * to use any code written in C#. I wrote the code in .NET core which is platform independent, so if you have a Mac
 * there shouldn't be any need to even download Visual Studio, the dll file has everything in it.
 */
public class Test {
    public static void main(String[] args) throws JavonetException {
        Javonet.activate(Constants.email, Constants.APIkey, JavonetFramework.v40);
        Javonet.addReference(Constants.filePath);

        // Will print message to console when signal is detected
        Javonet.getType("Program").invoke("CheckForBlueCharm");
        //changes


    }
}
