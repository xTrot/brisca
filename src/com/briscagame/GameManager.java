package com.briscagame;

import java.util.ArrayList;
import java.util.Random;

import com.briscagame.Card.SUIT;

public class GameManager {
    private static final int STARTING_HAND_SIZE = 3;


    private Deck deck;
    private ArrayList<Player> playerSeats;
    private int turn;
    private Game game;
    private Table table;
    private GameConfiguration gameConfiguration;

    private static Random rand = new Random();

    public GameManager(Game game, GameConfiguration gameConfiguration) {
        this.game = game;
        this.gameConfiguration = gameConfiguration;
        this.deck = new Deck(game);
        this.table = new Table(this.game, this.deck);
    }

    public void startSim() {
        setSimPlayers();
        if (playerSeats.size() == 4) {
            setTeamsRandomly();
        }
        setTheTable();
        while (rounds()) {
            judgeRound();
            awardCards();
            draw();
        }
        judgeGame();
    }

    public void startOnePlayer() {
        setSimPlayers();
        setOneUser();
        if (playerSeats.size() == 4) {
            setTeamsRandomly();
        }
        setTheTable();
        while (rounds()) {
            judgeRound();
            awardCards();
            draw();
        }
        judgeGame();
    }

    private void setOneUser() {
        int swapPlayer = rand.nextInt(playerSeats.size());
        User user = new User(this.table, "Net Player");
        user.sit(swapPlayer);
        playerSeats.set(swapPlayer, user);
    }

    private void setSimPlayers() {
        this.playerSeats = new ArrayList<Player>();
        for (int i = 0; i < gameConfiguration.maxPlayers; i++) {
            playerSeats.add(new Player(this.table, "Sim Player #" + (i + 1)));
            playerSeats.get(i).sit(i);
        }
    }

    private void setTeamsRandomly() {
        ArrayList<Player> team1 = new ArrayList<Player>();
        ArrayList<Player> team2 = new ArrayList<Player>();

        int remaingPlayers = playerSeats.size();
        for (int i = 0; remaingPlayers > 0; i++) {
            if (i % 2 == 0) {
                team1.add(playerSeats.remove(rand.nextInt(remaingPlayers)));
            } else {
                team2.add(playerSeats.remove(rand.nextInt(remaingPlayers)));
            }
            remaingPlayers = playerSeats.size();
        }

        while (team1.size() > 0 && team2.size() > 0) {
            playerSeats.add(team1.remove(0));
            playerSeats.add(team2.remove(0));
        }

        System.out.println("Team 1: " + playerSeats.get(0).getPlayerName() + " and " + playerSeats.get(2).getPlayerName());
        System.out.println("Team 2: " + playerSeats.get(1).getPlayerName() + " and " + playerSeats.get(3).getPlayerName());
    }

    private void setTheTable() {
        if (gameConfiguration.swapBottomCard) table.swapBottomCard = true;

        System.out.println("\n\nStarting a new game!.");
        System.out.println("This bottom card was picked " + table.bottomCard);
        System.out.println("This is the suit for the game " + table.bottomCard.getSuit());

        for (int i = 0; i < STARTING_HAND_SIZE; i++) {
            for (Player player : playerSeats) {
                player.draw();
            }
        }
        // System.out.println("Starting hands have been dealt to playerSeats.");

        turn = rand.nextInt(playerSeats.size());
        System.out.println(playerSeats.get(turn).getPlayerName() + " will start the game.\n");

    }

    private boolean rounds() {
        for (Player player : playerSeats) {
            if (player.getHandSize() == 0) {
                return false;
            }
            // Cannot combine for loops since it has to check
            // that everyone has cards before the first player
            // plays his card.
        }

        // System.out.println("New turn started.");

        int playersPlaying = playerSeats.size();
        int currentPlayerIndex;
        Player currentPlayer;
        for (int i = this.turn; i < this.turn + playersPlaying; i++) {
            currentPlayerIndex = i % playersPlaying; // Rotate around playerSeats.
            currentPlayer = playerSeats.get(currentPlayerIndex);
            currentPlayer.yourTurn();
        }

        return true;
    }

    private void judgeRound() {
        int winningCardIndex = this.judge();
        int winningPlayer = (this.turn + winningCardIndex) % playerSeats.size();
        this.turn = winningPlayer;
        // System.out.println(playerSeats.get(winningPlayer).getPlayerName() + " won the round.\n");
    }

    private void draw() {
        int playersPlaying = playerSeats.size();
        int currentPlayerIndex;
        for (int i = this.turn; i < this.turn + playersPlaying; i++) {
            currentPlayerIndex = i % playersPlaying; // Rotate around playerSeats.
            if (deck.getDeckSize() > 0){
                playerSeats.get(currentPlayerIndex).draw();
            } else {
                break;
            }
        }
    }

    private void judgeGame() {
        if (playerSeats.size() == 4) {
            judgeTeamGame();
        } else {
            judgeFreeForAllGame();
        }
    }

    private void judgeTeamGame() {
        int team1Score = playerSeats.get(0).getScore() + playerSeats.get(2).getScore();
        int team2Score = playerSeats.get(1).getScore() + playerSeats.get(3).getScore();

        if (team1Score > team2Score) {
            System.out.println("Team 1 won.");
        } else if (team2Score > team1Score) {
            System.out.println("Team 2 won.");
        } else {
            System.out.println("Its a draw.");
        }

        System.out.println("Scores:" + team1Score + ", " + team2Score);
        for (Player player : playerSeats) {
            System.out.println(player);
        }

    }

    private void judgeFreeForAllGame() {
        int[] scores = new int[playerSeats.size()];
        int winner = 0;
        int maxScore = playerSeats.get(winner).getScore();
        scores[0] = maxScore;
        

        int currentScore;
        for (int i = 1; i < playerSeats.size(); i++) {
            currentScore = playerSeats.get(i).getScore();
            scores[i] = currentScore;
            if (currentScore > maxScore) {
                maxScore = currentScore;
                winner = i;
            }
        }

        for (int i = 0; i < scores.length; i++) {
            if (scores[i] == maxScore && i != winner) {
                System.out.println("Draw between " + playerSeats.get(i).getPlayerName() + " and "
                    + playerSeats.get(winner).getPlayerName());
                for (Player player : playerSeats) {
                    System.out.println(player);
                }
                return;
            }
        }

        System.out.println("\nThe winner is " + playerSeats.get(winner).getPlayerName());
        for (Player player : playerSeats) {
            System.out.println(player);
        }

    }

    public int judge() {
        
        int bestCardIndex = 0;
        Card bestCard = table.cardsInPlay.get(0);
        SUIT suitForThisPlay = bestCard.getSuit();
        int bestCardValue = bestCard.getValue();

        Card currentCard;
        int currentCardValue;
        SUIT currentCardSuit;
        for (int i = 1; i < table.cardsInPlay.size(); i++) {
            currentCard = table.cardsInPlay.get(i);
            currentCardValue = currentCard.getValue();
            currentCardSuit = currentCard.getSuit();

            if (currentCardSuit == table.suitForThisGame && suitForThisPlay != table.suitForThisGame){
                // System.out.println("Card was bested by suit, " + bestCard + " beaten by " + currentCard);
                    bestCardIndex = i;
                    bestCard = currentCard;
                    suitForThisPlay = currentCardSuit;
                    bestCardValue = currentCardValue;
            }
            if (currentCardSuit == suitForThisPlay && currentCardValue > bestCardValue){
                // System.out.println("Card was bested by value, " + bestCard + " beaten by " + currentCard);
                    bestCardIndex = i;
                    bestCard = currentCard;
                    bestCardValue = currentCardValue;
            }

        }

        return bestCardIndex;
    }

    private void awardCards() {
        Player player = playerSeats.get(turn);
        while (table.cardsInPlay.size() > 0) {
            player.addScoreCard(table.cardsInPlay.remove(0));
        }
    }

}
