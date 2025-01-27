package com.briscagame;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Player {
    public static final List<String> TEAM_TYPES = List.of("A", "B", "S"); // S=Spectator

    private String playerName;
    private ArrayList<Card> hand;
    private ArrayList<Card> scorePile;
    private int seat;
    private Table table;
    private boolean ready;
    private int team = 0;

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
                    new PlayAction(this.table.game, PlayAction.ActionType.SWAP_BOTTOM_CARD);
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
        for (Card card : scorePile) {
            score += card.getScore();
        }
        return score;
    }

    public int getFinalScore() {
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

    public int getTeam() {
        return team;
    }

    public String getHand() {
        JSONArray jsonArray = new JSONArray();
        for (Card card : this.hand) {
            jsonArray.put(card);
        }
        return jsonArray.toString();
    }

    public void yourTurn() {
        int index = this.thinking();
        Card cardPlayed = this.playCard(index);

        JSONObject payload = new JSONObject();
        payload.put("seat", this.seat);
        payload.put("index", index);
        payload.put("card", cardPlayed.toString());
        new PlayAction(this.table.game, PlayAction.ActionType.CARD_PLAYED, payload);
    }

    public void sit(int seat) {
        this.seat = seat;
    }

    public boolean isReady() {
        return this.ready;
    }

    public void readyToggle() {
        this.ready = !this.ready;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public boolean setTeam(String team) {
        if (team == null) {
            // set to the other team
            if (this.team == TEAM_TYPES.indexOf("team1")){
                this.team = TEAM_TYPES.indexOf("teamA");
                return true;
            } else if (this.team == TEAM_TYPES.indexOf("teamA")) {
                this.team = TEAM_TYPES.indexOf("team1");
                return true;
            }
            throw new NullPointerException("This Null has to be handled.");
        }
        if (!TEAM_TYPES.contains(team)) {
            return false;
        }
        this.team = TEAM_TYPES.indexOf(team);
        return true;
    }

}
