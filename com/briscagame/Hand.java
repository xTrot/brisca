package com.briscagame;

import java.util.ArrayList;

public class Hand {
    public static final int MAX_HAND_SIZE = 3;

    ArrayList<Card> cards;

    public Hand(){
        this.cards = new ArrayList<Card>();
    }

    public void pickUp(Card card) {
        if(cards.size() <= MAX_HAND_SIZE) {
            System.err.println("Picked up card " + card);
            cards.add(card);
        }
    }

    public Card play() {
        return cards.get(0);
    }

    public int count(){
        return cards.size();
    }

}
