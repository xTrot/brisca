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

    }

}