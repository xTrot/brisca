package com.briscagame;

import java.util.Collections;
import java.util.Stack;

public class Deck {
    public static final int FULL_DECK_SIZE = Card.SUITS_SIZE * Card.CARD_NUMBERS_WITH_SKIP.length;

    private static final int THIS_CARD_NUMBER_CAN_SWAP = 2;

    private Stack<Card> deck;

    public Deck() {
        this.deck = new Stack<Card>();
        for (Card.SUIT suit : Card.SUITS) {
            for (int i = 0; i < Card.CARD_NUMBERS_WITH_SKIP.length; i++) {
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

    @Override
    public String toString() {
        String str = "";
        int count = 1;
        for (Card brisca : deck) {
            str += brisca + ", ";
            if (count++ % 10 == 0) {
                str += "\n";
            }
        }
        return str;
    }

}
