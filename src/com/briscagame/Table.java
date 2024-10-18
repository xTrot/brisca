package com.briscagame;

import java.util.ArrayList;
import com.briscagame.Card.SUIT;

public class Table {

    public static final int MAX_PLAYERS = 4;

    public Game game;
    public Deck deck;
    public Card bottomCard;
    public SUIT suitForThisGame;
    public ArrayList<Card> cardsInPlay;

    public Table(Game game, Deck deck) {
        this.game = game;
        this.deck = deck;
        this.bottomCard = deck.draw();
        this.suitForThisGame = this.bottomCard.getSuit();
        deck.putBottomCardBack(bottomCard);
        this.cardsInPlay = new ArrayList<Card>();
    }

    public Game getGame() {
        return this.game;
    }

    public Deck getDeck() {
        return this.deck;
    }

    public Card getBottomCard() {
        return this.bottomCard;
    }

    public SUIT getSuitForThisGame() {
        return this.suitForThisGame;
    }

    public ArrayList<Card> getCardsInPlay() {
        return this.cardsInPlay;
    }

    public void addToCardsInPlay(Card playedCard) {
        this.cardsInPlay.add(playedCard);
    }

}
