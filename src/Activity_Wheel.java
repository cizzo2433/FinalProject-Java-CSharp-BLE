import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;
public class Activity_Wheel {
    public static void pickActivity() {
        //variable "dice" acts as the randomizer depending on the int (number) given (shown later on)
        Random dice = new Random();

        //this will be changed to get the information of the weather condition the beacon detects
        //raining is just used to show that the code runs and uses the "sunny method"
        //rest of notes are shown in the sunny method as side methods run similarly
        String weather = "sunny";

        //runs through if and else if statements until a match is found
        if (weather.equals("sunny")) {
            sunny(dice);
        } else if (weather.equals("chilly")) {
            chilly(dice);
        } else if (weather.equals("raining")) {
            rainy(dice);
        } else if (weather.equals("stroming")) {
            stormy(dice);
        } else if (weather.equals("snowing")) {
            snowy(dice);
        } else {
            System.out.println("There is not a wheel created yet for this type of weather");
        }
    }

    public static void sunny(Random dice) {

        //creates and adds items to ArrayList
        ArrayList<String> sun = new ArrayList<String>();
        sun.add("Watch a movie/show");
        sun.add("Go on a walk (outdoors)");
        sun.add("Play vollyball");
        sun.add("Play a football");
        sun.add("Go out to eat");
        sun.add("Go out to park");

        int sunSize = sun.size(); //finds array size number (as later on I can add code to add or remove an item)

        //prints the list of items/activities in ArrayList
        System.out.println("Here are some activities to enjoy during a sunny day: ");
        for (int i = 0; i < sunSize; i++) {

            System.out.println(i + 1 + ". " + sun.get(i));
        }

        //selects a random number from 1 to sunSize (number of items in list shown earlier (6))
        int itemNumber = dice.nextInt(sunSize);

        //prints out the activity that was randomly selected
        System.out.println("\n" + "The randomized activity winner is: " + sun.get(itemNumber));
    }

    public static void chilly(Random dice) {
        ArrayList<String> chills = new ArrayList<String>();
        chills.add("Watch a movie/show");
        chills.add("Go on a walk (indoor track)");
        chills.add("Play chess");
        chills.add("Play a board game");
        chills.add("Play indoor hide and seek");
        chills.add("Go out to eat");
        chills.add("Order delivery");

        int chillsSize = chills.size();
        System.out.println("Here are some activities to enjoy during a chilly day: ");

        for (int i = 0; i < chillsSize; i++) {

            System.out.println(i + 1 + ". " + chills.get(i));
        }

        int itemNumber = dice.nextInt(chillsSize);

        System.out.println("\n" + "The Randomized activity winner is: " + chills.get(itemNumber));
    }

    public static void rainy(Random dice) {
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

    public static void stormy(Random dice) {
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

    public static void snowy(Random dice) {
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