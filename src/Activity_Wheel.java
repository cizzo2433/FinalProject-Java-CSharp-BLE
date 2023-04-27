import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;
public class Activity_Wheel {
    public static void pickActivity() {
        //variable "dice" acts as the randomizer depending on the int (number) given (shown later on)
        Random dice = new Random();

        //object that is later used so that the user can type a response to question
        Scanner keyboard = new Scanner(System.in);

        ArrayList<String> list = null;

        //this will be changed to get the information of the weather condition the beacon detects
        //raining is just used to show that the code runs and uses the "sunny method"
        //rest of notes are shown in the sunny method as side methods run similarly
        String weather = "sunny";

        //runs through if and else if statements until a match is found
        if (weather.equals("sunny")) {
            list = sunny();
        } else if (weather.equals("chilly")) {
            list = chilly();
        } else if (weather.equals("raining")) {
            list = rainy();
        } else if (weather.equals("stroming")) {
            list = stormy();
        } else if (weather.equals("snowing")) {
            list = snowy();
        } else {
            System.out.println("There is not a wheel created yet for this type of weather");
        }
        boolean repeat = false;
        while( repeat == false){
            System.out.println("The Current Activity in the list are: ");

            int listSize = list.size(); //finds array size number

            for (int i = 0; i < listSize; i++) {

                System.out.println(i + 1 + ". " + list.get(i));
            }

            System.out.println("\n To add an activity to the list type: 'add' \n To remove an activity to the list type: 'remove' \n To spin the wheel type: 'spin'");

            String eas = keyboard.nextLine();  // Read user input
            if (eas.equals("add")) {
                addList(dice, keyboard, list);
            }
            else if (eas.equals("remove")){
                removeList(dice, keyboard, list);
            }
            else if (eas.equals("spin")){
                repeat = true;
            }
            else{
                System.out.println("this is not a valid answer");
            }

        }

        int listSize = list.size(); //finds array size number (as later on I can add code to add or remove an item)
        //selects a random number from 1 to sunSize (number of items in list shown earlier (6))
        int itemNumber = dice.nextInt(listSize);

        //prints out the activity that was randomly selected
        System.out.println("\n" + "The randomized activity winner is: " + list.get(itemNumber));
    }

    public static ArrayList <String> sunny() {

        //creates and adds items to ArrayList
        ArrayList<String> list = new ArrayList<String>();
        list.add("Watch a movie/show");
        list.add("Go on a walk (outdoors)");
        list.add("Play volleyball");
        list.add("Play a football");
        list.add("Go out to eat");
        list.add("Go out to park");
        list.add("Go out swimming");
        return list;
    }

    public static ArrayList <String> chilly() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Watch a movie/show");
        list.add("Go on a walk (indoor track)");
        list.add("Play chess");
        list.add("Play a board game");
        list.add("Play indoor hide and seek");
        list.add("Go out to eat");
        list.add("Order delivery");
        return list;
    }
    public static ArrayList <String> rainy() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Watch a movie/show");
        list.add("Go on a walk (indoor track)");
        list.add("Play chess");
        list.add("Play a board game");
        list.add("Play indoor hide and seek");
        list.add("Go out to eat");
        list.add("Order out to eat");
        list.add("Dancing in the rain");
        return list;
    }
    public static ArrayList <String> stormy() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Watch a movie/show");
        list.add("Go on a walk (indoor track)");
        list.add("Play chess");
        list.add("Play a board game");
        list.add("Play indoor hide and seek");
        list.add("Order delivery (if possible)");
        return list;
    }

    public static ArrayList <String> snowy() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Watch a movie/show");
        list.add("Go on a walk (indoor track)");
        list.add("Play chess");
        list.add("Play a board game");
        list.add("Play indoor hide and seek");
        list.add("Go out to eat");
        list.add("Order delivery");
        list.add("Snow Tubing");
        return list;
    }
    public static ArrayList<String> addList(Random dice, Scanner keyboard, ArrayList<String> list) {
        System.out.println("Input an activity fo your choice into the list: ");
        String input = keyboard.nextLine();
        list.add(input);
        return list;
    }
    public static ArrayList<String> removeList(Random dice, Scanner keyboard, ArrayList<String> list) {
        System.out.println("Input an activity from the list to remove it (capitalization matters): ");
        String input = keyboard.nextLine();
        list.remove(input);
        return list;
    }
}
