package com.example.thread;

import android.widget.ProgressBar;

import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadTest {

    private ProgressBar pb_main_progressionTH;
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private Thread monThread;


    public ThreadTest(ProgressBar pb){
        pb_main_progressionTH = pb;
    }

    public void Go(){
        pb_main_progressionTH.setProgress(0);

        monThread = new Thread(new Runnable() {
            @Override
            public void run() {

                for(int i=0; i<20 && isRunning.get(); i++){
                    pb_main_progressionTH.incrementProgressBy(5);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        isRunning.set(true);
        monThread.start();
    }

    public void Stop(){
        isRunning.set(false);
        monThread.interrupt();
        pb_main_progressionTH.setProgress(0);
    }
}
