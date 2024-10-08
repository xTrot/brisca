package com.briscagame;

import java.util.EventListener;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class User extends Player implements EventListener {
    private static final long WAIT_TENTH_SEC = 100;
    private static final int WAIT_ONE_MIN_COUNT = 600; // 600 count * tenths

    private boolean thinking = false;
    private AtomicInteger indexIdea;

    public User(Game game, String playerName) {
        super(game, playerName);
        this.indexIdea = new AtomicInteger(-1);
        SimpleHttpServer.getPlayCardHandler().addListener(this);
    }

    public int startThinking() {
        this.thinking = true;
        int count = WAIT_ONE_MIN_COUNT;
        while (thinking) {
            try {
                TimeUnit.MILLISECONDS.sleep(WAIT_TENTH_SEC);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(count == 0) {
                return 0; //Timeout
            }
            count--;
        }
        return indexIdea.get();
    }

    public void doneThinking(CardPlayedEvent event) {
        this.indexIdea.set(event.getIndex());
        this.thinking = false;
    }

    @Override
    public int thinking(){
        System.out.println(this);
        return this.startThinking();
    }
    
}
