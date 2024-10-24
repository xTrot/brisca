package com.briscagame;

import java.util.EventObject;

public class CardPlayedEvent extends EventObject{
    private int index = -1;
    private String uuid;

    public CardPlayedEvent(Object source, int index, String uuid) {
        super(source);
        this.index = index;
        this.uuid = uuid;
    }

    public int getIndex() {
        return index;
    }

    public String getUuid() {
        return uuid;
    }

    
}