package com.plracticalcoding.androidLibraries;

public class MessageEvent {

    public final String msg;

    MessageEvent(String msg) {
        this.msg = msg;
    }

    public static MessageEvent of(String msg) {
        return new MessageEvent(msg);
    }

    @Override
    public String toString() {
        return msg;
    }






}
