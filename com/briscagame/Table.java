package com.briscagame;

import java.util.ArrayList;

public class Table {

    public Deck deck;
    public Card bottomCard;
    public ArrayList<Card> cardsInPlay;

    public Table(Deck deck) {
        this.deck = deck;
        this.bottomCard = deck.draw();
        deck.putBottomCardBack(bottomCard);
        this.cardsInPlay = new ArrayList<Card>();
    }

    public int judge() {
        int rtn = -1;
        int bestMatchingCardIndex = -1;
        
        int bestCardIndex = 0;
        Card bestCard = cardsInPlay.get(0);
        if (bottomCard.getSuit() == bestCard.getSuit()) {
            bestMatchingCardIndex = 0;
        }

        Card currentCard;
        for (int i = 1; i < cardsInPlay.size(); i++) {
            currentCard = cardsInPlay.get(i);
            if (bestMatchingCardIndex > 0) {
                if (bottomCard.getSuit() == currentCard.getSuit()) {
                    if (Card.VALUE[currentCard.getNumber()] > Card.VALUE[bestCard.getNumber()]) {
                        bestCard = currentCard;
                        bestMatchingCardIndex = i;
                    }
                }
            } else {
                if (bottomCard.getSuit() == currentCard.getSuit()) {
                    if (Card.VALUE[currentCard.getNumber()] > Card.VALUE[bestCard.getNumber()]) {
                        bestCard = currentCard;
                        bestMatchingCardIndex = i;
                    }
                } else {
                    if (Card.VALUE[currentCard.getNumber()] > Card.VALUE[bestCard.getNumber()]) {
                        bestCard = currentCard;
                        bestCardIndex = i;
                    }
                }
            }
        }

        if (bestMatchingCardIndex > 0) {
            rtn = bestMatchingCardIndex;
            System.out.println("Round won by player " + (bestMatchingCardIndex + 1));
        } else {
            rtn = bestCardIndex;
            System.out.println("Round won by player " + (bestCardIndex + 1));
        }

        return rtn;
    }

    public void awardCards (Player player) {
        while (cardsInPlay.size() > 0) {
            player.addScoreCard(cardsInPlay.remove(0));
        }
    }

}
