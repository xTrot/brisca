package com.briscagame;

import java.util.ArrayList;
import java.util.Scanner;

public class GamePlan {

    private GameConfiguration gameConfiguration;
    private Scanner scan = new Scanner(System.in);

    public GamePlan() {
        int maxPlayers = askPositiveInt("How many players will join this game?", 2, 4);
        boolean swapBottomCard = askBoolean("Will players be able to Swap the Bottom card with the 2 of the same suit?");
        ArrayList<String> options = new ArrayList<String>();
        options.add("solo");
        options.add("public");
        String gameType = askOptions("Which game type?", options);

        this.gameConfiguration = new GameConfiguration();
        gameConfiguration.maxPlayers = maxPlayers;
        gameConfiguration.swapBottomCard = swapBottomCard;
        gameConfiguration.gameType = gameType;
    }

    private String askOptions(String question, ArrayList<String> options) {
        String response = "";
        while ( !options.contains(response) ) {
            System.out.println(question + options);
            response = scan.nextLine();
        }
        return response;
    }

    private int askPositiveInt(String question, int min, int max) {
        int response = -1;
        while (response < min || response > max) {
            System.out.println(question);
            response = scan.nextInt();
        }
        return response;
    }

    private boolean askBoolean(String question) {
        String response = "";
        while ( !(response.equals("y") || response.equals("n")) ) {
            System.out.println(question + "(y/n)");
            response = scan.nextLine();
        }
        return response.equals("y");
    }

    public GameConfiguration getGameConfiguration() {
        return gameConfiguration;
    }
}
