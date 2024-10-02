package com.briscagame;

import java.util.EventObject;

public class CardPlayedEvent extends EventObject{
    private int index = -1;

    public CardPlayedEvent(Object source, int index) {
        super(source);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    
}