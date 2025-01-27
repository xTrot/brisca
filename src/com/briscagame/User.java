package com.briscagame;

import java.util.EventListener;
import java.util.concurrent.TimeUnit;

import com.briscagame.httpHandlers.Session;

public class User extends Player implements EventListener {
    private static final long WAIT_TENTH_SEC = 100;
    private static final int WAIT_ONE_MIN_COUNT = 600; // 600 count * tenths
    private static final int WAIT_FIVE_SEC_COUNT = 50; // 600 count * tenths

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
                e.printStackTrace();
            }
            if(count == 0) {
                this.hasTimedOut = true;
                return 0; //Timeout
            }
            count--;
        }
        this.hasTimedOut = false;
        return indexIdea;
    }

    public boolean doneThinking(int index) {
        if (!this.thinking){
            return false;
        }
        this.indexIdea = index;
        this.thinking = false;
        return true;
    }

    @Override
    public int thinking(){
        // System.out.println(this);
        return this.startThinking();
    }

    public String getUuid() {
        return userId;
    }
    
}
