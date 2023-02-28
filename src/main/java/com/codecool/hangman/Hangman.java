package com.codecool.hangman;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class Hangman {

    public static void main(String[] args) {

        //  play("Codecool", 6);

        stateMachine();

    }

    private static void stateMachine() {


        //global változók
        boolean stop = false; //while ciklus stoppoló
        String[] user_input = new String[1]; //ezzel az array stringgel passzolkadjuk a userinputot a statek között
        ArrayList<String> used_letters = new ArrayList<>(); //ez a lista gyüjti a használt betüket
        used_letters.add("1"); //ez csak ezért van hogy ne legyen üres  a lista
        String blankWord = ""; //legelsőalkalommal kirajzolt alúlvonások
        String[] blank = new String[1]; //a játék során ebben frissül a már kitalált betük
        ArrayList<String> goodLetter = new ArrayList<>(); // jó betüket gyüjti
        goodLetter.add(" ");
        ArrayList<String> countryLetters =new ArrayList<>();
        countryLetters.add("");
        int lives = 10;

        enum State {
            INI, IMPORT, CREATE, PLAY, DRAW, CHECKING, END, USED, SHOW
        }

        State activeState = State.INI;
        while (!stop) {
            switch (activeState) {
                case INI -> {
                    user_input[0]=welcome();
                    if(user_input[0].equals("quit")){
                        activeState = State.END;
                    } else if (user_input[0].equals("wrong")) {
                        System.out.println("Wrong input");
                    }else {
                        activeState = State.IMPORT;
                    }

                }
                case IMPORT -> {
                    used_letters = selectWordsIntoList(user_input[0]);
                    activeState = State.CREATE;
                }
                case CREATE -> {
                    lives = setLives(user_input[0]);
                    user_input[0] = choseWord(used_letters);
                    countryLetters = create(user_input[0]);
                    blank[0] = blank(countryLetters, blankWord);
                    System.out.println(blank[0]);
                    used_letters.clear();
                    activeState = State.DRAW;
                }
                case PLAY -> {
                    System.out.println("Lives: " + lives);
                    System.out.println("Wrong letters: "+wrongLettes(used_letters,goodLetter));
                    user_input[0] = (playGame());

                    //ha a player azt írta be hogy quit akkor leáll a játék különben megnézi a karater angol abc-e
                    if (user_input[0].equals("quit")) {
                        activeState = State.END;
                    } else if (user_input[0].equals("wrong")) {
                        System.out.println("Wrong character!");
                    } else {
                        activeState = State.USED;
                    }
                }
                case DRAW -> {
                    System.out.println(Doodles.getDoodle(lives));
                    if (lives <= 0) {
                        System.out.println("You Lose.");
                        String word = String.join("", countryLetters);
                        System.out.println("Word was: " + word);
                        activeState = State.END;
                    }
                    else if (!blank[0].contains("_")) {
                        System.out.println("You Win!");
                        activeState = State.END;
                    }
                    else
                    {activeState = State.PLAY;}
                }


                case CHECKING -> {
                    //megnézi hogy a user betűje szerepel-e a szóban ha nem levon egy életet és kiírja hogy a betú nincs benne
                    if (checking(user_input[0], countryLetters)) {
                        goodLetter.add(user_input[0]);
                        activeState = State.SHOW; //ha szerepelt akkot átdobja a show stage-be
                    } else {
                        //levon egy életet
                        System.out.printf("The letter %s not exits in the word\n", user_input[0]);
                        lives--;
                        activeState = State.SHOW;
                    }
                }

                case END -> {
                    System.out.println("GAME END");
                    stop = true;
                }
                case USED -> {
                    //ha angol abc volt akkor megnézi nem-e használta-e

                    if (usedLetter(user_input[0], used_letters)) {
                        System.out.println("Used letter please give a new one");
                        activeState = State.PLAY;
                    } else {
                        used_letters.add(user_input[0]);

                        activeState = State.CHECKING;
                    }
                }

                case SHOW -> {
                    blank[0] = showLetter(goodLetter, countryLetters);
                    System.out.println(blank[0]);
                    activeState = State.DRAW;
                }
            }
        }
    }

    private static int setLives(String s) {
        int difficulty_number = Integer.parseInt(s);
        return switch (difficulty_number) {
            case 1 -> 10; //Easy
            case 2 -> 7; //Normal
            case 3 -> 5; //Hard
            default -> 10;
        };

    }

    //Sziku methodjai:
    //blank: legenerálja a üres alsó aláhúzásokat a kiválasztott szó alapján
    public static String blank(ArrayList<String> choosenCountryLetters, String blank) {

        for (int i = 0; i < choosenCountryLetters.size(); i++) {
            if (choosenCountryLetters.get(i).equals(" ")) {
                blank += " ";
            }
            else {
                blank = blank + "_ ";
            }
        }
        return blank;
    }

    //playgame: a játéksotól vár egy betüt és leelenörzi hogy tényleg betőt-e adott meg vagy ki akakr lépni
    public static String playGame() {

        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.next();
        char c = userInput.charAt(0);
        //kiakarlépni?
        if (userInput.equals("quit")) {
            userInput = "quit";
        }
        //ellenőrzi angol betű-e amit beírt a user
        else if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
            userInput = "wrong";
        }
        else if (userInput.length()>1){
            userInput = "wrong";
        }

        return userInput;

    }

    //usedLetter: megnézi nem-e volt-e már ez a betű
    public static Boolean usedLetter(String letter, ArrayList<String> usedlist) {

        boolean exits = false;
        for (String x : usedlist) {
            if (x.toLowerCase().equals(letter.toLowerCase())) {
                exits = true;
            }
        }

        return exits;

    }

    //chechking: megnézi hogy a user betúje helyes-e
    public static Boolean checking(String letter, ArrayList<String> playlist) {



        boolean exits = false;
        for (String x : playlist) {
            if (x.toLowerCase().equals(letter.toLowerCase())) {
                exits = true;
                break;
            }
        }

        return exits;

    }

    //ha helyes a user betűje akkor memgutja a szóban hol vannak azok a kis és nagy betűt is
    public static String showLetter(ArrayList<String> goodLetter, ArrayList<String> playlist) {
        String notblank = "";
        for (String x : playlist) {
            for (int i = 0; i < goodLetter.size(); i++) {
                if (x.equalsIgnoreCase(goodLetter.get(i).toLowerCase())) {
                    notblank = notblank + x + " ";
                    break;
                } else if (i == goodLetter.size() - 1) {
                    notblank = notblank + "_ ";

                }
            }
        }

        return notblank;
    }

    public static ArrayList<String> wrongLettes(ArrayList<String> used, ArrayList<String> good){
        ArrayList<String> wrong = new ArrayList<>();
        for (String x: used){
            for (int i=0; i< good.size(); i++){
                if (x.equalsIgnoreCase(good.get(i))){
                    break;
                } else if (i==good.size()-1) {
                    wrong.add(x);
                }
            }
        }
        return wrong;
    }

//Peti metódjai:
public static String welcome(){
    System.out.println("Welcome to the game!");
    System.out.println("If you want to play, type play or you can quit the game with typing quit.");
    Scanner scan = new Scanner((System.in));
    String playOrQuit = scan.next();

    if(playOrQuit.equals("quit")) {
        return "quit";
    }
    else if (playOrQuit.equals("play")) {

        return difficulty();
    }
    else {
        return "wrong";
    }
}

    public static String difficulty(){
        System.out.println("You can choose from three different difficulties: ");
        System.out.println("Beginner(press: 1) - shorter words (Lives: 10)");
        System.out.println("Normal(press: 2) - medium length words (Lives: 7)");
        System.out.println("Hard(press: 3) - longer words (Lives:5)");
        Scanner scan1 = new Scanner(System.in);
        int diff = scan1.nextInt();

        switch (diff) {
            case 1 : {
                System.out.println("You choose: Beginner");
                return "1";
            }
            case 2 : {
                System.out.println("You choose: Normal");
                return "2";
            }
            case 3 : {
                System.out.println("You choose: Hard");
                return "3";
            }
            default:{
                System.out.println("Choose a valid number!");
                return "wrong";
            }
        }
    }

    public static ArrayList<String> selectWordsIntoList(String number){
        ArrayList<String> allCountries = new ArrayList<>(Countries.getAllCountries());
        ArrayList<String> playList=new ArrayList<>();
        int difficultyNumber = Integer.parseInt(number);

        for (int i=0;i<allCountries.size();i++)
        {
            if (allCountries.get(i).length()<=5 && difficultyNumber==1){

                playList.add(allCountries.get(i));

            } else if (allCountries.get(i).length()<8 && allCountries.get(i).length()>5 && difficultyNumber==2) {

                playList.add(allCountries.get(i));

            }else if (allCountries.get(i).length()>8 && difficultyNumber==3){
                playList.add(allCountries.get(i));
            }

        }

        return playList;
    }

    public static String choseWord(ArrayList<String> array){
        int max=array.size();
        int min=0;
        int range = max - min + 1;
        int randomNumber=(int)(Math.random() * range)+min;
        return array.get(randomNumber);
    }
    private static ArrayList<String> create(String word) {
        ArrayList<String> countryLetters= new ArrayList<>();
        List<Character> chars = new ArrayList<>();
        for (char ch : word.toCharArray()) {

            chars.add(ch);
        }
        for (Character x : chars){
            countryLetters.add(x.toString());
        }
        return countryLetters;
    }

}

