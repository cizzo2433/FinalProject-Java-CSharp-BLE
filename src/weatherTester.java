public class weatherTester {
    public static void main(String[] args) {
        double[] location = Geocoder.geocode("17551");
        Weather.getWeather(location[0], location[1]);

        System.out.println(String.format("Current Weather:\n Temperature: %.2f\n Condition: %s\n",
                Weather.currentTemp, Weather.currentCondition));
    }
}
