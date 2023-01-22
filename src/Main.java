package rockpaperscissors;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.random.RandomGenerator;
class Scorer {
    String username;
    long score;
    String scoreFilePath;

    public Scorer(String username, String scoreFilePath) {
        this.username = username;
        this.scoreFilePath = scoreFilePath;
        fetchUser();
    }
    public long getScore() {
        return score;
    }
    public void updateScore(int winner) {
        int drawPoints = 50;
        int winPoints = 100;
        switch(winner) {
            case 1:
                break;
            case 2:
                score += drawPoints;
                break;
            case 3:
                score += winPoints;
                break;
            default:
                System.out.println("Unknown value for winner!");
                break;
        }
    }
    public void fetchUser() {
        score = 0;
        File ratingFile = new File(scoreFilePath);
        try(Scanner scanner = new Scanner(ratingFile)) {
            while(scanner.hasNextLine()) {
                String[] currentLine = scanner.nextLine().split(" ");
                if(currentLine[0].equals(username)) {
                    score = Long.parseLong(currentLine[1]);
                    break;
                }
            }
        } catch (FileNotFoundException e){
            System.out.println("Error: The file 'rating.txt' is not found!");
            score = 0;
        }

    }
}
public class Main {
    public static void main(String[] args) {
        // write your code here
        String userChoice;
        String username;
        String optionsList;
        Scanner readInput = new Scanner(System.in);

        System.out.print("Enter your name: > ");
        username = readInput.nextLine();
        System.out.println("Hello, " + username);
        Scorer userScore = new Scorer(username, "rating.txt");
        System.out.print("> ");
        optionsList = readInput.nextLine();
        if(optionsList.trim().equals("")) {
            optionsList = "rock,paper,scissors";
        }
        System.out.println("Okay, let's start");
        do {
            System.out.print("> ");
            userChoice = readInput.nextLine();
            if (Arrays.asList(optionsList.split(",")).contains(userChoice)) {
                challengeUser(userChoice, userScore, optionsList);
            } else if ("!rating".equals(userChoice)) {
                System.out.println("Your rating: " + userScore.getScore());
            }
            else if ("!exit".equals(userChoice)) {
                System.out.println("Bye!");
            } else {
                System.out.println("Invalid input");
            }
        } while (!"!exit".equals(userChoice));

    }

    public static void challengeUser(String userChoice, Scorer userScore, String optionsList) {
        String[] responses = optionsList.split(",");
        Random randomResponse = new Random();
        String chosenOption = responses[randomResponse.nextInt(responses.length)];
        int winner = decideWinner(userChoice, chosenOption, responses);
        userScore.updateScore(winner);

        switch (winner) {
            case 1:
                System.out.println("Sorry, but the computer chose " + chosenOption);
                break;
            case 2:
                System.out.printf("Draw: There is a draw (%s)\n", chosenOption);
                break;
            case 3:
                System.out.println("Well done. The computer chose " + chosenOption + " and failed");
                break;
            default:
                System.out.println("Error in decision!");
                break;
        }
    }
    public static int decideWinner(String userChoice, String chosenOption, String[] responses) {
        int winner;
        int indexOfUserChoice = Arrays.asList(responses).indexOf(userChoice);
        if (userChoice.equals(chosenOption)) {
            return 2;
        }
        String[] ranksArray = new String[responses.length - 1];
        ranksArray = Arrays.copyOfRange(responses, indexOfUserChoice + 1, responses.length + indexOfUserChoice);
        int lengthOfSuccessorArray = responses.length - indexOfUserChoice - 1;
        for(int i = 0; i < indexOfUserChoice ; i++) {
            ranksArray[lengthOfSuccessorArray + i] = responses[i];
        }

        winner = Arrays.asList(ranksArray).indexOf(chosenOption) < ranksArray.length / 2 ? 1 : 3;

        return winner;
    }
    public static void defeatUser(String userChoice) {
        String response;
        if (userChoice.equals("rock")) {
            response = "paper";
        } else if (userChoice.equals("paper")) {
            response = "scissors";
        } else {
            response = "rock";
        }
        System.out.println("Sorry, but the computer chose " + response);
    }
}
