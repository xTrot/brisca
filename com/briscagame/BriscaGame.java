package com.briscagame;

import java.util.Scanner;

public class BriscaGame {

    static Scanner stdin = new Scanner(System.in);

    public static int askCardIndex(String question) {
        int response = -1;
        System.out.println(question);
        while (response <= 0 || response >=4) {
            response = stdin.nextInt();
        }
        return response;
    }

    public static void main(String[] args) {
        Card brisca1 = new Card();
        Card brisca2 = new Card();
        brisca1.beats(brisca2);

        Deck deck = new Deck();
        System.out.println(deck);

        brisca1 = deck.draw();
        brisca2 = deck.draw();
        brisca1.beats(brisca2);

        System.out.println(deck);
        brisca1.beats(brisca2);

        System.out.println(deck);

        deck = new Deck();
        System.out.println(deck);

        Table table = new Table(deck);
        System.out.println("This bottom card was picked " + table.bottomCard);
        System.out.println(table.deck);

        System.out.println(table.deck);

        Player player1 = new Player("Player #1");
        Player player2 = new Player("Player #2");

        deck = new Deck();
        System.out.println(deck);

        table = new Table(deck);
        System.out.println("This bottom card was picked " + table.bottomCard);
        System.out.println(table.deck);

        for (int i = 3; i > 0 ; i--) {
            player1.pickUpCard(deck.draw());
            player2.pickUpCard(deck.draw());
        }

        int turnIndex = 0;
        
        while (player1.getHandSize() > 0) {

            if (turnIndex == 0){
                table.cardsInPlay.add(player1.putDownCard(0));
                table.cardsInPlay.add(player2.putDownCard(0));
            } else {
                table.cardsInPlay.add(player2.putDownCard(0));
                table.cardsInPlay.add(player1.putDownCard(0));
            }

            turnIndex = table.judge();

            if (turnIndex == 0) {
                table.awardCards(player1);
            } else {
                table.awardCards(player2);
            }

            if (deck.getDeckSize() > 0) {
                if (turnIndex > 0) {
                    player1.pickUpCard(deck.draw());
                    player2.pickUpCard(deck.draw());
                } else {
                    player2.pickUpCard(deck.draw());
                    player1.pickUpCard(deck.draw());
                }
            }
        }

        int player1Score = player1.getScore();
        int player2Score = player2.getScore();

        if (player1Score > player2Score) {
            System.out.println(player1.getPlayerName() + " won.");
        } else if (player2Score > player1Score) {
            System.out.println(player2.getPlayerName() + " won.");
        } else {
            System.out.println("Its a draw.");
        }

        System.out.println("Scores:" + player1Score + ", " + player2Score);

        player1 = new Player("Player #1");
        player2 = new Player("Player #2");

        deck = new Deck();
        System.out.println(deck);

        table = new Table(deck);
        System.out.println("\nNew game has started!");
        System.out.println("This will be the suit for the game " + table.bottomCard);
        // System.out.println(table.deck);

        for (int i = 3; i > 0 ; i--) {
            player1.pickUpCard(deck.draw());
            player2.pickUpCard(deck.draw());
        }

        turnIndex = 0;
        int cardIndex = -1;
        Card oponentCard;
        
        while (player1.getHandSize() > 0) {

            if (turnIndex == 0){
                System.out.println("Your turn, " + player1);
                cardIndex = askCardIndex("Which card do you want to play? (1,2,3)") - 1;
                table.cardsInPlay.add(player1.putDownCard(cardIndex));

                oponentCard = player2.putDownCard(0);
                System.out.println(player2 + " played " + oponentCard);
                table.cardsInPlay.add(oponentCard);
            } else {
                oponentCard = player2.putDownCard(0);
                System.out.println(player2 + " played " + oponentCard);
                table.cardsInPlay.add(oponentCard);

                System.out.println("Your turn, " + player1);
                cardIndex = askCardIndex("Which card do you want to play? (1,2,3)") - 1;
                table.cardsInPlay.add(player1.putDownCard(cardIndex));
            }

            turnIndex = table.judge();

            if (turnIndex == 0) {
                table.awardCards(player1);
            } else {
                table.awardCards(player2);
            }

            if (deck.getDeckSize() > 0) {
                if (turnIndex > 0) {
                    player1.pickUpCard(deck.draw());
                    player2.pickUpCard(deck.draw());
                } else {
                    player2.pickUpCard(deck.draw());
                    player1.pickUpCard(deck.draw());
                }
            }
        }

        player1Score = player1.getScore();
        player2Score = player2.getScore();

        if (player1Score > player2Score) {
            System.out.println(player1.getPlayerName() + " won.");
        } else if (player2Score > player1Score) {
            System.out.println(player2.getPlayerName() + " won.");
        } else {
            System.out.println("Its a draw.");
        }

        System.out.println("Scores:" + player1Score + ", " + player2Score);

    }

}