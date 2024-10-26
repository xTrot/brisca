package com.briscagame;

import java.util.ArrayList;

import org.json.JSONObject;

public class Player {

    private String playerName;
    private ArrayList<Card> hand;
    private ArrayList<Card> scorePile;
    private int seat;
    private Table table;
    private boolean ready;

    public Player(Table table, String playerName) {
        this.table = table;
        this.playerName = playerName;
        this.hand = new ArrayList<Card>();
        this.scorePile = new ArrayList<Card>();
        this.ready = false;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void draw() {
        hand.add(table.deck.draw());
    }

    public Card playCard(int index) {
        Card playedCard = hand.remove(index);
        table.cardsInPlay.add(playedCard);
        return playedCard;
    }

    public void addScoreCard(Card scoreCard) {
        this.scorePile.add(scoreCard);
    }

    public int thinking() {
        if (table.swapBottomCard && !table.deck.empty()) {
            int size = hand.size();
            for (int i = 0; i < size; i++) {
                Card currentCard = hand.get(i);
                if (
                    currentCard.getSuit() == table.suitForThisGame 
                    && currentCard.getNumber() == Deck.THIS_CARD_NUMBER_CAN_SWAP
                ) {
                    Card bottomCard = table.deck.swapBottomCard(hand.remove(i));
                    hand.add(bottomCard);
                    System.out.println(this.playerName + " swapped " + currentCard + " for " + bottomCard);
                }
            }
        }
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
        for (int i = 0; i < hand.size(); i++) {
                scorePile.add(hand.remove(i));
    
        }
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

    public boolean isReady() {
        return this.ready;
    }

    public void ready() {
        this.ready = true;
    }

    public void setTable(Table table) {
        this.table = table;
    }

}
