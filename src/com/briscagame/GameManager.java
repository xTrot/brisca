package com.briscagame;

import java.util.ArrayList;
import java.util.Random;

public class GameManager {

    private Table table;
    private Deck deck;
    private ArrayList<Player> players;
    private int maxPlayers;
    private int startingHandSize;
    private int turn;

    private static Random rand = new Random();

    public GameManager() {
        this.maxPlayers = 4;
        this.startingHandSize = 3;
    }

    public void startSim() {
        setSimPlayers();
        if (players.size() == 4) {
            setTeamsRandomly();
        }
        setTheTable();
        while (turns()) {
            judgeTurn();
            awardCards();
            draw();
        }
        judgeGame();
    }

    public void startOnePlayer() {
        setSimPlayers();
        setOneUser();
        if (players.size() == 4) {
            setTeamsRandomly();
        }
        setTheTable();
        while (turns()) {
            judgeTurn();
            awardCards();
            draw();
        }
        judgeGame();
    }

    private void setOneUser() {
        int swapPlayer = rand.nextInt(players.size());
        User user = new User("Net Player");
        players.set(swapPlayer, user);
    }

    private void setSimPlayers() {
        this.players = new ArrayList<Player>();
        for (int i = 0; i < maxPlayers; i++) {
            players.add(new Player("Sim Player #" + (i + 1)));
        }
    }

    private void setTeamsRandomly() {
        ArrayList<Player> team1 = new ArrayList<Player>();
        ArrayList<Player> team2 = new ArrayList<Player>();

        int remaingPlayers = players.size();
        for (int i = 0; remaingPlayers > 0; i++) {
            if (i % 2 == 0) {
                team1.add(players.remove(rand.nextInt(remaingPlayers)));
            } else {
                team2.add(players.remove(rand.nextInt(remaingPlayers)));
            }
            remaingPlayers = players.size();
        }

        while (team1.size() > 0 && team2.size() > 0) {
            players.add(team1.remove(0));
            players.add(team2.remove(0));
        }

        System.out.println("Team 1: " + players.get(0).getPlayerName() + " and " + players.get(2).getPlayerName());
        System.out.println("Team 2: " + players.get(1).getPlayerName() + " and " + players.get(3).getPlayerName());
    }

    private void setTheTable() {
        this.deck = new Deck();
        this.table = new Table(this.deck);
        Card bottomCard = table.getBottomCard();
        System.out.println("\n\nStarting a new game!.");
        System.out.println("This bottom card was picked " + bottomCard);
        System.out.println("This is the suit for the game " + bottomCard.getSuit());

        for (int i = 0; i < this.startingHandSize; i++) {
            for (Player player : players) {
                player.pickUpCard(deck.draw());
            }
        }
        System.out.println("Starting hands have been dealt to players.");

        turn = rand.nextInt(players.size());
        System.out.println(players.get(turn).getPlayerName() + " will start the game.\n");

    }

    private boolean turns() {
        for (Player player : players) {
            if (player.getHandSize() == 0) {
                return false;
            }
            // Cannot combine for loops since it has to check
            // that everyone has cards before the first player
            // plays his card.
        }

        int playersPlaying = players.size();
        int currentPlayer;
        Card currentCard;
        for (int i = this.turn; i < this.turn + playersPlaying; i++) {
            currentPlayer = i % playersPlaying; // Rotate around players.
            currentCard = players.get(currentPlayer).putDownCard(players.get(currentPlayer).thinking());
            table.addToCardsInPlay(currentCard);
            System.out.println(players.get(currentPlayer).getPlayerName() + " played card " + currentCard);
        }

        return true;
    }

    private void judgeTurn() {
        int winningCardIndex = table.judge();
        int winningPlayer = (this.turn + winningCardIndex) % players.size();
        this.turn = winningPlayer;
        System.out.println(players.get(winningPlayer).getPlayerName() + " won the round.\n");
    }

    private void awardCards() {
        table.awardCards(players.get(turn));
    }

    private void draw() {
        int playersPlaying = players.size();
        int currentPlayer;
        for (int i = this.turn; i < this.turn + playersPlaying; i++) {
            currentPlayer = i % playersPlaying; // Rotate around players.
            if (deck.getDeckSize() > 0){
                players.get(currentPlayer).pickUpCard(deck.draw());
            } else {
                break;
            }
            System.out.println(players.get(currentPlayer).getPlayerName() + " drew a card.");
        }
    }

    private void judgeGame() {
        if (players.size() == 4) {
            judgeTeamGame();
        } else {
            judgeFreeForAllGame();
        }
    }

    private void judgeTeamGame() {
        int team1Score = players.get(0).getScore() + players.get(2).getScore();
        int team2Score = players.get(1).getScore() + players.get(3).getScore();

        if (team1Score > team2Score) {
            System.out.println("Team 1 won.");
        } else if (team2Score > team1Score) {
            System.out.println("Team 2 won.");
        } else {
            System.out.println("Its a draw.");
        }

        System.out.println("Scores:" + team1Score + ", " + team2Score);
        for (Player player : players) {
            System.out.println(player);
        }

    }

    private void judgeFreeForAllGame() {
        int[] scores = new int[players.size()];
        int winner = 0;
        int maxScore = players.get(winner).getScore();
        scores[0] = maxScore;
        

        int currentScore;
        for (int i = 1; i < players.size(); i++) {
            currentScore = players.get(i).getScore();
            scores[i] = currentScore;
            if (currentScore > maxScore) {
                maxScore = currentScore;
                winner = i;
            }
        }

        for (int i = 0; i < scores.length; i++) {
            if (scores[i] == maxScore && i != winner) {
                System.out.println("Draw between " + players.get(i).getPlayerName() + " and "
                    + players.get(winner).getPlayerName());
                for (Player player : players) {
                    System.out.println(player);
                }
                return;
            }
        }

        System.out.println("\nThe winner is " + players.get(winner).getPlayerName());
        for (Player player : players) {
            System.out.println(player);
        }

    }

}
