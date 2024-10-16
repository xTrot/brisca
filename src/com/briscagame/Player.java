package com.briscagame;

import java.util.ArrayList;

import org.json.JSONObject;

public class Player {

    private String playerName;
    private ArrayList<Card> hand;
    private ArrayList<Card> scorePile;
    private Game game;
    private int seat;

    public Player(Game game, String playerName) {
        this.game = game;
        this.playerName = playerName;
        this.hand = new ArrayList<Card>();
        this.scorePile = new ArrayList<Card>();
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void pickUpCard(Card card) {
        hand.add(card);
    }

    public Card putDownCard(int index) {
        return hand.remove(index);
    }

    public void addScoreCard(Card scoreCard) {
        this.scorePile.add(scoreCard);
    }

    public int thinking() {
        return 0;
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

    public int getSeat() {
        return this.seat;
    }

    public Card yourTurn() {
        int index = this.thinking();
        Card cardPlayed = this.putDownCard(index);

        JSONObject payload = new JSONObject();
        payload.put("seat", this.seat);
        payload.put("index", index);
        payload.put("card", cardPlayed.toString());
        PlayAction action = new PlayAction(PlayAction.ActionType.CARD_PLAYED, payload);
        Game.registerAction(this.game, action);

        return cardPlayed;
    }

    public void sit(int seat) {
        this.seat = seat;
    }

}
