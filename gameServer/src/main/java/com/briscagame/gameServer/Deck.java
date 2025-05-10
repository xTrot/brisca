package com.briscagame.gameServer;

import java.util.Collections;
import java.util.Stack;

public class Deck {
    static final int FULL_DECK_SIZE = Card.SUITS_SIZE * Card.CARDS_WITH_SKIP.length;

    static final int THIS_CARD_NUMBER_CAN_SWAP = 2;

    private Stack<Card> deck;

    public Deck(Game game) {
        this.deck = new Stack<Card>();
        for (Card.SUIT suit : Card.SUITS) {
            for (int i = 0; i < Card.CARDS_WITH_SKIP.length; i++) {
                deck.add(new Card(suit, i));
            }
        }
        Collections.shuffle(deck);
    }

    public Card draw() {
        return deck.pop();
    }

    public void putBottomCardBack(Card card) {
        deck.add(0, card);
    }

    public Card swapBottomCard(Card card) {
        if (this.deck.elementAt(0).getSuit() != card.getSuit() ||
                THIS_CARD_NUMBER_CAN_SWAP != card.getNumber()) {
            return card;
        }
        Card bottomCard = deck.remove(0);
        deck.add(0, card);
        return bottomCard;
    }

    public int getDeckSize() {
        return this.deck.size();
    }

    public boolean empty() {
        return this.deck.empty();
    }

    @Override
    public String toString() {
        return "Deck: " + this.deck;
    }

    public Card getBottomCard3Player() {
        Card bottomCard;
        bottomCard = this.draw();
        if (bottomCard.getNumber() == THIS_CARD_NUMBER_CAN_SWAP) {
            int index = 0;
            for (int i = 0; i < this.deck.size(); i++) {
                if (this.deck.get(i).getSuit() == bottomCard.getSuit()) {
                    index = i;
                    break;
                }
            }
            bottomCard = this.deck.remove(index);
        } else {
            int index = 0;
            for (int i = 0; i < this.deck.size(); i++) {
                if (this.deck.get(i).getSuit() == bottomCard.getSuit() &&
                        this.deck.get(i).getNumber() == THIS_CARD_NUMBER_CAN_SWAP) {
                    index = i;
                    break;
                }
            }
            this.deck.remove(index);
        }
        return bottomCard;
    }

}
