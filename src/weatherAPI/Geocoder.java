package weatherAPI;

import com.google.gson.Gson;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Utilizes the OpenStreetMap Nominatim API to convert a given location to
 * latitude and longitude. Queries can be passed either as City,State or Zip Code
 */
public class Geocoder {

    private static double lat;
    private static double lon;

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
}
