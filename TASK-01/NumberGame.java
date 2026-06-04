import java.util.Scanner;
import java.util.Random;
public class NumberGame{
    static final int min =1;
    static final int max=100;
    static final int max_attemps=7;
    Scanner sc=new Scanner(System.in);
    Random rand=new Random();

    static int total=0;
    static int round_won=0;
    static int roundNumber=0;


public static void main (String[] args){
    System.out.println("╔══════════════════════════════╗");
        System.out.println("║    NUMBER GUESSING GAME      ║");
        System.out.println("╚══════════════════════════════╝");
    System.out.println("Guss a number between " +min + "and " +max);
    System.out.println("You have " +max_attemps + " attempts to guess the number.");
    boolean playAgain=true;
    while(playAgain){
        playRound();
        playAgain=askPlayAgain();
    }
    printFinalScore();
    sc.close();
}
public static void playRound(){

}

}

