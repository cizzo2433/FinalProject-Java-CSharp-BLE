package weatherAPI;

/**
 * This is a simple tester program to make sure that all classes in the WeatherAPI package
 * play nicely with one another. This will eventually be removed.
 */
public class weatherTester {
    public static void main(String[] args) {
        double[] location = Geocoder.geocode("17551");
        Weather.getWeather(location[0], location[1]);

        System.out.printf("Current weatherAPI.Weather:\n Temperature: %.2f\n Condition: %s\n%n",
                Weather.currentTemp, Weather.currentCondition);
    }
}
