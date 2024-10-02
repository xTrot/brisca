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

    public static final int CARD_NUMBER_INDEX = 0;
    public static final int CARD_VALUE_INDEX = 1;
    public static final int CARD_SCORE_INDEX = 2;
    public static final int[][] CARDS_WITH_SKIP = {
        { 1, 12, 11 },
        { 2, 1, 0   },
        { 3, 11, 10 },
        { 4, 2, 0   },
        { 5, 3, 0   },
        { 6, 4, 0   },
        { 7, 5, 0   },
        { 10, 8, 2  },
        { 11, 9, 3  },
        { 12, 10, 4 }
    };

    public static final int[][] CARDS_WITHOUT_SKIP = {
        { 1, 12, 11 },
        { 2, 1, 0   },
        { 3, 11, 10 },
        { 4, 2, 0   },
        { 5, 3, 0   },
        { 6, 4, 0   },
        { 7, 5, 0   },
        { 8, 6, 0   },
        { 9, 7, 0   },
        { 10, 8, 2  },
        { 11, 9, 3  },
        { 12, 10, 4 }
    };

    private static Random rand = new Random();

    private SUIT suit;
    private int number;
    private int value;
    private int score;

    public Card() {
        this.number = CARDS_WITH_SKIP[rand.nextInt(CARDS_WITH_SKIP.length)][CARD_NUMBER_INDEX];
        this.value = CARDS_WITH_SKIP[rand.nextInt(CARDS_WITH_SKIP.length)][CARD_VALUE_INDEX];
        this.score = CARDS_WITH_SKIP[rand.nextInt(CARDS_WITH_SKIP.length)][CARD_SCORE_INDEX];
        this.suit = SUITS.get(rand.nextInt(SUITS_SIZE));

        System.out.println("Created random brisca " + this);
    }

    public Card(SUIT suit, int index) {
        this.suit = suit;
        this.number = CARDS_WITH_SKIP[index][CARD_NUMBER_INDEX];
        this.value = CARDS_WITH_SKIP[index][CARD_VALUE_INDEX];
        this.score = CARDS_WITH_SKIP[index][CARD_SCORE_INDEX];
        
        // System.out.println("Created brisca " + this);
    }

    public void beats(Card that) {
        Card winner;

        if (this.value == that.value){
            System.out.println(this + " vs " + that + ", draw.");
            return;
        } else if (this.value > that.value) {
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
        return score;
    }

    public int getValue () {
        return value;
    }

    @Override
    public String toString() {
        return this.suit + ":" + this.number;
    }

}
