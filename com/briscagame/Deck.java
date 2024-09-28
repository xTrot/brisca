package com.briscagame;

import java.util.Collections;
import java.util.Stack;

public class Deck {
    public static final int FULL_DECK_SIZE = Card.SUITS_SIZE * Card.CARDS_WITH_SKIP.length;

    private static final int THIS_CARD_NUMBER_CAN_SWAP = 2;

    private Stack<Card> deck;

    public Deck() {
        this.deck = new Stack<Card>();
        for (Card.SUIT suit : Card.SUITS) {
            for (int i = 0; i < Card.CARDS_WITH_SKIP.length; i++) {
                deck.add(new Card(suit, i));
            }
        }
        Collections.shuffle(deck);
    }

    public Card draw(){
        return deck.pop();
    }

    public void putBottomCardBack(Card card) {
        deck.add(0, card);
    }

    public Card swapBottomCard(Card card) {
        if(
            this.deck.elementAt(0).getSuit() != card.getSuit() ||
            THIS_CARD_NUMBER_CAN_SWAP == card.getNumber()
        ){
            return card;
        }
        Card bottomCard = deck.remove(0);
        deck.add(0, card);
        return bottomCard;
    }

    public int getDeckSize() {
        return this.deck.size();
    }

    @Override
    public String toString() {
        return this.deck.toString();
    }

}
