package sh.brisca.gameServer;

import java.util.ArrayList;

import sh.brisca.common.GameConfiguration;
import sh.brisca.gameServer.Card.SUIT;

public class Table {

    public static final int MAX_PLAYERS = 4;

    // private static final Logger logger = LoggerFactory.getLogger(User.class);

    Game game;
    Deck deck;
    Card bottomCard;
    SUIT suitForThisGame;
    ArrayList<Card> cardsInPlay;
    boolean swapBottomCard;

    public Table(Game game, Deck deck, GameConfiguration gameConfiguration) {
        this.game = game;
        this.deck = deck;
        this.swapBottomCard = gameConfiguration.getSwapBottomCard();
        if (gameConfiguration.getMaxPlayers() == 3) {
            this.bottomCard = deck.getBottomCard3Player();
        } else {
            this.bottomCard = deck.draw();
        }
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

    public boolean getSwapBottomCard() {
        return this.swapBottomCard;
    }

}
