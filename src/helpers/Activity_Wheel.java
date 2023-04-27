package helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Activity_Wheel {

    private static String activityMessage; // may not need this anymore

    /**
     * Returns a String message containing an activity that is randomly selected from an ArrayList
     * of possible activities, which is filled based on the weather String passed to the method
     *
     * @param weather a String containing a type of weather condition obtained from the weatherAPI
     * @return a String message
     * @throws FileNotFoundException
     */
    public static String generateActivity(String weather) throws FileNotFoundException {
        
        Random dice = new Random();
        ArrayList<String> activities = null;

        switch (weather) {
            case "Clear":
                activities = generateList(new File("ref/Clear.txt"));
                break;
            case "Clouds":
                activities = generateList(new File("ref/Clouds.txt"));
                break;
            case "Rain", "Drizzle":
                activities = generateList(new File("ref/Rain.txt"));
                break;
            case "Thunderstorm":
                activities = generateList(new File("ref/Storm.txt"));
                break;
            case "Snow":
                activities = generateList(new File("ref/Snow.txt"));
                break;
            default:
                System.out.println("error");
                break;
        }
        return "You should " + activities.get(dice.nextInt(activities.size()));
    }

    /**
     * Fills an ArrayList of Strings with the lines of a txt file corresponding to a type of weather
     *
     * @param file the reference file
     * @return an ArrayList containing different activities
     * @throws FileNotFoundException
     */
    private static ArrayList<String> generateList(File file) throws FileNotFoundException {
        Scanner fileReader = new Scanner(file);
        ArrayList<String> activities = new ArrayList<>();

        while (fileReader.hasNext()) {
            activities.add(fileReader.nextLine());
        }
        return activities;
    }

    // Consolidated everything below into the 2 methods above

    public static String pickActivity(String weather) {

        //variable "dice" acts as the randomizer depending on the int (number) given (shown later on)
        Random dice = new Random();

        //runs through if and else if statements until a match is found
        if (weather.equals("Clear")) {
            sunny(dice);
        } else if (weather.equals("Clouds")) { // might want to incorporate temperature into these
            chilly(dice);
        } else if (weather.equals("Rain") || weather.equals("Drizzle")) {
            rainy(dice);
        } else if (weather.equals("Thunderstorm")) {
            stormy(dice);
        } else if (weather.equals("Snow")) {
            snowy(dice);
        } else {
            System.out.println("There is not a wheel created yet for this type of weather");
        }
        return activityMessage;
    }

    private static void sunny(Random dice) {

        //creates and adds items to ArrayList
        ArrayList<String> sun = new ArrayList<>();
        sun.add("Watch a movie or show");
        sun.add("Go on a walk (outdoors)");
        sun.add("Play volleyball");
        sun.add("Play football");
        sun.add("Go out to eat");
        sun.add("Go out to park");

        int sunSize = sun.size(); //finds array size number (as later on I can add code to add or remove an item)

        //selects a random number from 1 to sunSize (number of items in list shown earlier (6))
        int itemNumber = dice.nextInt(sunSize);

        //prints out the activity that was randomly selected
        sun.add(sun.size(), "You should " + sun.get(itemNumber));

        activityMessage = sun.get(sun.size()-1);
    }

    private static void chilly(Random dice) {
        ArrayList<String> chills = new ArrayList<String>();
        chills.add("Watch a movie/show");
        chills.add("Go on a walk (indoor track)");
        chills.add("Play chess");
        chills.add("Play a board game");
        chills.add("Play indoor hide and seek");
        chills.add("Go out to eat");
        chills.add("Order delivery");

        int chillsSize = chills.size();
        System.out.println("Here are some activities to enjoy during a cloudy day: ");

        for (int i = 0; i < chillsSize; i++) {

            System.out.println(i + 1 + ". " + chills.get(i));
        }

        int itemNumber = dice.nextInt(chillsSize);

        System.out.println("\n" + "The Randomized activity winner is: " + chills.get(itemNumber));
    }

    private static void rainy(Random dice) {
        ArrayList<String> rain = new ArrayList<String>();
        rain.add("Watch a movie/show");
        rain.add("Go on a walk (indoor track)");
        rain.add("Play chess");
        rain.add("Play a board game");
        rain.add("Play indoor hide and seek");
        rain.add("Go out to eat");
        rain.add("Order out to eat");
        rain.add("Dancing in the rain");

        int rainSize = rain.size();
        System.out.println("Here are some activities to enjoy during a rainy day: ");

        for (int i = 0; i < rainSize; i++) {

            System.out.println(i + 1 + ". " + rain.get(i));
        }

        int itemNumber = dice.nextInt(rainSize);

        System.out.println("\n" + "The Randomized activity winner is: " + rain.get(itemNumber));
    }

    private static void stormy(Random dice) {
        ArrayList<String> storm = new ArrayList<String>();
        storm.add("Watch a movie/show");
        storm.add("Go on a walk (indoor track)");
        storm.add("Play chess");
        storm.add("Play a board game");
        storm.add("Play indoor hide and seek");
        storm.add("Order delivery (if possible)");

        int rainSize = storm.size();
        System.out.println("Here are some activities to enjoy during a stormy day: ");

        for (int i = 0; i < rainSize; i++) {

            System.out.println(i + 1 + ". " + storm.get(i));
        }

        int itemNumber = dice.nextInt(rainSize);

        System.out.println("\n" + "The Randomized activity winner is: " + storm.get(itemNumber));
    }

    private static void snowy(Random dice) {
        ArrayList<String> snow = new ArrayList<String>();
        snow.add("Watch a movie/show");
        snow.add("Go on a walk (indoor track)");
        snow.add("Play chess");
        snow.add("Play a board game");
        snow.add("Play indoor hide and seek");
        snow.add("Go out to eat");
        snow.add("Order delivery");
        snow.add("Snow Tubing");

        int rainSize = snow.size();
        System.out.println("Here are some activities to enjoy during a snowy day: ");

        for (int i = 0; i < rainSize; i++) {

            System.out.println(i + 1 + ". " + snow.get(i));
        }

        int itemNumber = dice.nextInt(rainSize);

        System.out.println("\n" + "The Randomized activity winner is: " + snow.get(itemNumber));
    }
}