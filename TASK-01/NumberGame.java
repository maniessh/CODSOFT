
import java.util.Scanner;
import java.util.Random;

public class NumberGuessingGame {

    static final int MIN = 1;
    static final int MAX = 100;
    static final int MAX_ATTEMPTS = 7;

    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();

    // Score & round tracking
    static int totalScore = 0;
    static int roundsWon = 0;
    static int roundNumber = 0;

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════╗");
        System.out.println("║    NUMBER GUESSING GAME      ║");
        System.out.println("╚══════════════════════════════╝");
        System.out.println("Guess a number between " + MIN + " and " + MAX);
        System.out.println("You have " + MAX_ATTEMPTS + " attempts per round.\n");

        boolean playAgain = true;

        while (playAgain) {
            playRound();
            playAgain = askPlayAgain();
        }

        printFinalScore();
        scanner.close();
    }

    static void playRound() {
        roundNumber++;
        int secret = random.nextInt(MAX - MIN + 1) + MIN; // range: 1–100
        int attemptsUsed = 0;
        boolean guessedCorrect = false;

        System.out.println("\n─── Round " + roundNumber + " ───");
        System.out.println("A new number has been generated. Start guessing!\n");

        while (attemptsUsed < MAX_ATTEMPTS) {
            int attemptsLeft = MAX_ATTEMPTS - attemptsUsed;
            System.out.print("Attempt " + (attemptsUsed + 1) + "/" + MAX_ATTEMPTS
                    + " (" + attemptsLeft + " left) — Enter guess: ");

            int guess = readInt();

            // Validate range
            if (guess < MIN || guess > MAX) {
                System.out.println("  ⚠  Please enter a number between " + MIN + " and " + MAX + ".\n");
                continue; // don't count invalid input as an attempt
            }

            attemptsUsed++;

            if (guess == secret) {
                guessedCorrect = true;
                int pts = calculateScore(attemptsUsed);
                totalScore += pts;
                roundsWon++;
                System.out.println("\n  ✓  Correct! The number was " + secret + ".");
                System.out.println("  Solved in " + attemptsUsed + " attempt(s). +" + pts + " points!\n");
                break;

            } else if (guess > secret) {
                System.out.println("  ↓  Too high! Go lower.\n");
            } else {
                System.out.println("  ↑  Too low! Go higher.\n");
            }
        }

        if (!guessedCorrect) {
            System.out.println("\n  ✗  Out of attempts! The number was " + secret + ".\n");
        }

        printRoundScore();
    }

    // Score = more points for fewer attempts
    static int calculateScore(int attemptsUsed) {
        return Math.max(10, 80 - (attemptsUsed - 1) * 10);
        // Attempt 1 → 80 pts, Attempt 2 → 70 pts ... Attempt 7 → 20 pts
    }

    static boolean askPlayAgain() {

    }
    static void printRoundScore(){
         System.out.println("  Rounds played : " + roundNumber);
        System.out.println("  Rounds won    : " + roundsWon);
        System.out.println("  Total score   : " + totalScore);

    }
    static void printFinalScore() {
 System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║         FINAL SCORE          ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.printf( "║  Rounds played : %-11d ║%n", roundNumber);
        System.out.printf( "║  Rounds won    : %-11d ║%n", roundsWon);
        System.out.printf( "║  Total score   : %-11d ║%n", totalScore);
        System.out.println("╚══════════════════════════════╝");
        System.out.println("Thanks for playing!");
    }
    static int readInt(){

    }
}
