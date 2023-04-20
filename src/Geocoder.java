import com.google.gson.Gson;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class Geocoder {

    private double lat;
    private double lon;

    protected static double[] geocode (String location) {
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
            return coordinates;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while retrieving location information.");
        }
        return null;
    }
}
