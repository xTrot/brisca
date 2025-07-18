package sh.brisca.gameServer;

import java.util.EventListener;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sh.brisca.common.Session;

public class User extends Player implements EventListener {
    private static final long WAIT_TENTH_SEC = 100;
    private static final int WAIT_ONE_MIN_COUNT = 600; // 600 count * tenths
    private static final int WAIT_FIVE_SEC_COUNT = 50; // 50 count * tenths

    private static final Logger logger = LoggerFactory.getLogger(User.class);

    private boolean thinking = false;
    private boolean hasTimedOut = false;
    private int indexIdea;
    private String userId;

    public User(Table table, String playerName) {
        super(table, playerName);
    }

    public User(Session userSession) {
        this(null, userSession.getUsername());
        this.userId = userSession.getUserId();
    }

    public int startThinking() {
        this.thinking = true;
        int count = !this.hasTimedOut ? WAIT_ONE_MIN_COUNT : WAIT_FIVE_SEC_COUNT;
        while (thinking) {
            try {
                TimeUnit.MILLISECONDS.sleep(WAIT_TENTH_SEC);
            } catch (InterruptedException e) {
                logger.error("{}", e);
            }
            if (count == 0) {
                this.setTimedOut();
                return 0; // Timeout
            }
            count--;
        }
        this.unsetTimedOut();
        return indexIdea;
    }

    public boolean doneThinking(int index) {
        if (!this.thinking) {
            return false;
        }
        this.indexIdea = index;
        this.thinking = false;
        return true;
    }

    @Override
    public int thinking() {
        logger.trace("Thinking: {}", this);
        return this.startThinking();
    }

    public String getUuid() {
        return userId;
    }

    private void setTimedOut() {
        if (!this.hasTimedOut) {
            this.hasTimedOut = true;
            new PlayAction(((Player) this).table.game, PlayAction.ActionType.SEAT_AFK,
                    new JSONObject().put("seat", this.getSeat()));
        }
    }

    private void unsetTimedOut() {
        if (this.hasTimedOut) {
            this.hasTimedOut = false;
            new PlayAction(((Player) this).table.game, PlayAction.ActionType.SEAT_NOT_AFK,
                    new JSONObject().put("seat", this.getSeat()));
        }
    }

}
