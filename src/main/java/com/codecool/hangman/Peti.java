/*
package com.codecool.hangman;
import java.util.ArrayList;
import java.util.Scanner;
public class Peti {


    public class Main {


        public static void main(String[] args) {

            welcome();
        }


        public static String welcome(){
            System.out.println("Welcome to the game!");
            System.out.println("If you want to play, type play or you can quit the game with typing quit.");
            Scanner scan = new Scanner((System.in));
            String playOrQuit = scan.next();

            if(playOrQuit=="quit") {
                return "quit";
            }
            else if (playOrQuit=="play") {
               return difficulty();
            }
            else {
                return "wrong";
            }
        }

        public static String difficulty(){
            System.out.println("You can choose from three different difficulties: ");
            System.out.println("Beginner(press: 1) - shorter words");
            System.out.println("Normal(press: 2) - medium length words");
            System.out.println("Hard(press: 3) - longer words");
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
        public static void chooseWord(ArrayList<String> countries ,int number){


            selectWordsIntoList(countries , number );
        }
        public static String[] selectWordsIntoList(ArrayList<String> List, int number){

            ArrayList<String> playListEasy=new ArrayList<>();
            ArrayList<String> playListMedium=new ArrayList<>();
            ArrayList<String> playListHard=new ArrayList<>();

            for (int i=0;i<List.size();i++)
            {


                if (List.get(i).length()<=5 && number==1){

                    playListEasy.add(List.get(i));

                    return listToArray(playListEasy);

                }
                else if (List.get(i).length()<8 && List.get(i).length()>5 && number==2){

                    playListMedium.add(List.get(i));
                    return listToArray(playListMedium);
                }
                else {

                    playListHard.add(List.get(i));
                    return listToArray(playListHard);
                }
            }
            return listToArray();
        }
        public static String[] listToArray (ArrayList<String> List){


            String[] array = new String [List.size()];
            array = List.toArray(array);
            return array;
        }
        public static String chosenWord(String[] array){
            int max=array.length;
            int min=0;
            int randomNumber=(int)Math.random() * (max - min+1)+min;
            return array[randomNumber];
        }

    }

}
*/
