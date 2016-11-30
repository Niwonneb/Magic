package com.magic_app.magic.util;

public class Debounce {
    int minCallDelay;
    private long lastCall;

    public Debounce(int minCallDelayInms) {
        this.minCallDelay = minCallDelayInms;
    }

    public boolean calledRecently() {
        long delay = System.currentTimeMillis() - lastCall;
        if (delay < minCallDelay) {
            return true;
        } else {
            lastCall = System.currentTimeMillis();
            return false;
        }
    }
}
