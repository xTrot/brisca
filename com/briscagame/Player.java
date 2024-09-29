package com.briscagame;

import java.util.ArrayList;

public class Player {

    private String playerName;
    private ArrayList<Card> hand;
    private ArrayList<Card> scorePile;

    public Player(String playerName) {
        this.playerName = playerName;
        this.hand = new ArrayList<Card>();
        this.scorePile = new ArrayList<Card>();
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void pickUpCard(Card card) {
        hand.add(card);
        // System.out.println(this.playerName + " picked up card " + card + ", Hand " + this.hand);
    }

    public Card putDownCard(int index) {
        return hand.remove(index);
    }

    public void addScoreCard(Card scoreCard) {
        this.scorePile.add(scoreCard);
    }

    @Override
    public String toString() {
        String rtn = "User: ";
        rtn += this.playerName + "\n Hand: ";
        rtn += this.hand + "\n Score Pile: ";
        rtn += this.scorePile + "\n Score: ";
        rtn += this.getScore() + "\n";
        return rtn;
    }

    public int getHandSize() {
        return this.hand.size();
    }

    public int getScore() {
        int score = 0;
        for (Card card : scorePile) {
            score += card.getScore();
        }
        return score;
    }

}
