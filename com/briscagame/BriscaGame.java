package com.briscagame;

public class BriscaGame {

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
        Hand hand1 = new Hand();
        Hand hand2 = new Hand();
        
        while (Hand.MAX_HAND_SIZE > hand1.count()) {
            System.err.print("Hand1 ");
            hand1.pickUp(deck.draw());
        }

        while (Hand.MAX_HAND_SIZE > hand2.count()) {
            System.err.print("Hand2 ");
            hand2.pickUp(deck.draw());
        }
        
        brisca1 = hand1.play();
        brisca2 = hand2.play();
        brisca1.beats(brisca2);

        System.out.println(deck);

        deck = new Deck();
        System.out.println(deck);

        Table table = new Table(deck);
        System.out.println("This bottom card was picked " + table.bottomCard);
        System.out.println(table.deck);

        hand1 = new Hand();
        hand2 = new Hand();
        Hand hand3 = new Hand();
        Hand hand4 = new Hand();
        
        while (Hand.MAX_HAND_SIZE > hand1.count()) {
            System.err.print("Hand1 ");
            hand1.pickUp(deck.draw());
            System.err.print("Hand2 ");
            hand2.pickUp(deck.draw());
            System.err.print("Hand3 ");
            hand3.pickUp(deck.draw());
            System.err.print("Hand4 ");
            hand4.pickUp(deck.draw());
        }

        System.out.println(table.deck);

        table.cardsInPlay.add(hand1.play());
        table.cardsInPlay.add(hand2.play());
        table.cardsInPlay.add(hand3.play());
        table.cardsInPlay.add(hand4.play());

        table.judge();

        System.out.println(hand1);
        System.out.println(hand2);
        System.out.println(hand3);
        System.out.println(hand4);

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

    }

}