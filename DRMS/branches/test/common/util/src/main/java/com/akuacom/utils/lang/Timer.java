package com.akuacom.utils.lang;

public class Timer extends Thread {
    public static interface Listener {
        void alarm();
    }
    protected int monitorPeriodMs = 100;

    /** Length of timeout */
    private int durationMs;

    /** Time elapsed */
    private int elapsedMs;
    
    private Listener listener;
    /**
     * Creates a timer of a specified length
     * 
     * @param length
     *            Length of time before timeout occurs
     */
    public Timer(Listener listener, int durationMs) {
        this.durationMs = durationMs;
        this.listener = listener;
        elapsedMs = 0;
    }

    /** Resets the timer back to zero */
    public synchronized void reset() {
        elapsedMs = 0;
    }

    /** Performs timer specific code */
    public void run() {
        while (true) {
            // Put the timer to sleep
            try {
                Thread.sleep(monitorPeriodMs);
            } catch (InterruptedException ioe) {
                continue;
            }

            elapsedMs += monitorPeriodMs;

            if (elapsedMs > durationMs) {
                if(listener != null) {
                    listener.alarm();
                } else {
                    throw new RuntimeException("timed out");
                }
            }

        }
    }

    // Override this to provide custom functionality
    public void alarm() {
        System.err.println("Network timeout occurred.... terminating");
        System.exit(1);
    }
}
