package weatherAPI;

import com.google.gson.Gson;
import com.javonet.JavonetException;
import com.javonet.api.NObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Utilizes the OpenStreetMap Nominatim API to convert a given location to
 * latitude and longitude. Queries can be passed either as City,State or Zip Code
 */
public final class Geocoder {

    static double lat;
    static double lon;
    private static final GeoWatcher geoWatcher;

    static {
        try {
            geoWatcher = new GeoWatcher();
        } catch (JavonetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructor declared private to prevent instantiation
     */
    private Geocoder() {
    }

    /**
     * Overloaded method using GeoWatcher to get coordinates using device location services.
     * Uses Double instead of primitive type to account for translation from C#
     *
     * @return a Double array of coordinates
     * @throws JavonetException
     */
    public static Double[] geocode() throws JavonetException {
        Double[] coordinates = geoWatcher.getLocation();
        lat = coordinates[0];
        lon = coordinates[1];
        return coordinates;
    }

    public static double[] geocode(String location) {
        try {
            URL url = new URL("https://nominatim.openstreetmap.org/search?format=json&q=" + URLEncoder.encode(location, "UTF-8"));
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            BufferedReader geoReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder geoResponse = new StringBuilder();
            String line;
            while ((line = geoReader.readLine()) != null) {
                geoResponse.append(line);
            }
            geoReader.close();

            Gson gson = new Gson();
            LocationData[] locations = gson.fromJson(geoResponse.toString(), LocationData[].class);

            double[] coordinates = new double[]{locations[0].lat, locations[0].lon};
            lat = locations[0].lat;
            lon = locations[0].lon;
            return coordinates;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while retrieving location information.");
        }
        return null;
    }

    /**
     * Nested class to access device location services using C# wrapper.
     * Including within Geocoder as none of the methods that use Javonet can
     * be invoked statically
     */
    private static class GeoWatcher extends NObject {

        /**
         * Constructor, with call to C# super class
         *
         * @throws JavonetException
         */
        private GeoWatcher() throws JavonetException {
            super("Program");
        }

        /**
         * Uses the devices location service to find the approximate latitude and longitude of the device
         *
         * @return a Double array with latitude in index 0 and longitude in index 1
         * @throws JavonetException
         */
        private Double[] getLocation() throws JavonetException {
            return this.invoke("GetLocationProperty");
        }

        /**
         * Prints status of watcher as it searches for the current location.
         * Will specify if location services have been disabled.
         *
         * @throws JavonetException
         */
        private void showStatusUpdate() throws JavonetException {
            this.invoke("ShowStatusUpdates");
        }
    }
}
