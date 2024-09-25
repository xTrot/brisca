package com.briscagame;

import java.util.ArrayList;

public class Hand {
    public static final int MAX_HAND_SIZE = 3;

    ArrayList<Card> cards;

    public Hand(){
        this.cards = new ArrayList<Card>();
    }

    public void pickUp(Card card) {
        cards.add(card);
    }

    public Card play() {
        return cards.remove(0);
    }

    public Card putDownCard(int index) {
        return cards.remove(index);
    }

    public int count(){
        return cards.size();
    }

    public String toString() {
        String rtn = "{ ";
        for (Card card : cards) {
            rtn += card + " ";
        }
        rtn += "}";
        return rtn;
    }

}
