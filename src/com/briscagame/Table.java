package com.briscagame;

import java.util.ArrayList;
import com.briscagame.Card.SUIT;

public class Table {

    public static final int MAX_PLAYERS = 4;

    private Deck deck;
    private Card bottomCard;
    private SUIT suitForThisGame;
    private ArrayList<Card> cardsInPlay;

    public Table(Game game, Deck deck) {
        this.deck = deck;
        this.bottomCard = deck.draw();
        this.suitForThisGame = this.bottomCard.getSuit();
        deck.putBottomCardBack(bottomCard);
        this.cardsInPlay = new ArrayList<Card>();
    }

    public int judge() {
        
        int bestCardIndex = 0;
        Card bestCard = cardsInPlay.get(0);
        SUIT suitForThisPlay = bestCard.getSuit();
        int bestCardValue = bestCard.getValue();

        Card currentCard;
        int currentCardValue;
        SUIT currentCardSuit;
        for (int i = 1; i < cardsInPlay.size(); i++) {
            currentCard = cardsInPlay.get(i);
            currentCardValue = currentCard.getValue();
            currentCardSuit = currentCard.getSuit();

            if (currentCardSuit == suitForThisGame && suitForThisPlay != suitForThisGame){
                System.out.println("Card was bested by suit, " + bestCard + " beaten by " + currentCard);
                    bestCardIndex = i;
                    bestCard = currentCard;
                    suitForThisPlay = currentCardSuit;
                    bestCardValue = currentCardValue;
            }
            if (currentCardSuit == suitForThisPlay && currentCardValue > bestCardValue){
                System.out.println("Card was bested by value, " + bestCard + " beaten by " + currentCard);
                    bestCardIndex = i;
                    bestCard = currentCard;
                    bestCardValue = currentCardValue;
            }

        }

        // System.out.println("Round won by player " + (bestCardIndex + 1));

        return bestCardIndex;
    }

    public void awardCards (Player player) {
        while (cardsInPlay.size() > 0) {
            player.addScoreCard(cardsInPlay.remove(0));
        }
    }

    public Deck getDeck() {
        return this.deck;
    }

    public Card getBottomCard() {
        return this.bottomCard;
    }

    public void addToCardsInPlay(Card putDownCard) {
        this.cardsInPlay.add(putDownCard);
    }

}
