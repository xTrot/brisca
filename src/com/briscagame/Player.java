package com.briscagame;

import java.util.ArrayList;

import org.json.JSONObject;

public class Player {

    private String playerName;
    private ArrayList<Card> hand;
    private ArrayList<Card> scorePile;
    private int seat;
    private Table table;

    public Player(Table table, String playerName) {
        this.table = table;
        this.playerName = playerName;
        this.hand = new ArrayList<Card>();
        this.scorePile = new ArrayList<Card>();
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void draw() {
        hand.add(table.deck.draw());
    }

    public Card playCard(int index) {
        Card playedCard = hand.remove(index);
        table.addToCardsInPlay(playedCard);
        return playedCard;
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

    public void yourTurn() {
        int index = this.thinking();
        Card cardPlayed = this.playCard(index);

        JSONObject payload = new JSONObject();
        payload.put("seat", this.seat);
        payload.put("index", index);
        payload.put("card", cardPlayed.toString());
        PlayAction action = new PlayAction(PlayAction.ActionType.CARD_PLAYED, payload);
        Game.registerAction(this.table.game, action);
    }

    public void sit(int seat) {
        this.seat = seat;
    }

}
