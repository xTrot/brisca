package com.briscagame;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Card {
    public static enum SUIT {
        BASTO,
        ESPADA,
        ORO,
        COPA
    }
    public static final List<SUIT> SUITS =
    Collections.unmodifiableList(Arrays.asList(SUIT.values()));
    public static final int SUITS_SIZE = SUITS.size();
    public static final int[] CARD_NUMBERS_WITH_SKIP = { 1, 2, 3, 4, 5, 6, 7, 10, 11, 12 };
    public static final int[] CARD_SCORE_WITH_SKIP = { 11, 0, 10, 0, 0, 0, 0, 2, 3, 4 };

    static final int[] VALUE = {0, 12, 1, 11, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static Random rand = new Random();

    private SUIT suit;
    private int number;

    public Card() {
        this.number = CARD_NUMBERS_WITH_SKIP[rand.nextInt(CARD_NUMBERS_WITH_SKIP.length)];
        this.suit = SUITS.get(rand.nextInt(SUITS_SIZE));

        System.out.println("Created random brisca " + this);
    }

    public Card(SUIT suit, int index) {
        this.suit = suit;
        this.number = CARD_NUMBERS_WITH_SKIP[index];
        
        // System.out.println("Created brisca " + this);
    }

    public void beats(Card that) {
        Card winner;

        if (VALUE[this.number] == VALUE[that.number]){
            System.out.println(this + " vs " + that + ", draw.");
            return;
        } else if (VALUE[this.number] > VALUE[that.number]) {
            winner = this;
        } else {
            winner = that;
        }

        System.out.println(this + " vs " + that + ", " + winner + " wins.");
    }

    public SUIT getSuit() {
        return suit;
    }

    public int getNumber() {
        return number;
    }

    public int getScore() {
        return CARD_SCORE_WITH_SKIP[this.number];
    }

    @Override
    public String toString() {
        return this.suit + ":" + this.number;
    }

}
