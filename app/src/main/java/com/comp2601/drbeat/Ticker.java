package com.comp2601.drbeat;

/**
 * (c) 2018 L.D. Nel
 */
import android.support.annotation.MainThread;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class Ticker {

    private  String TAG = this.getClass().getSimpleName();

    private int startX;
    private int startY;


    // Time Signature Beats per Measure
    private static Ticker instance = null; //globally accessible instance
    public static Ticker getInstance(){return instance;}

    // Running
    private boolean running; //true when ticker is running

    // Constructor
    public Ticker(int aNumberOfBeatsPerMeasure) {
        instance = this;
        running = false;
    }


    // Start the ticker
    public void start() {
        Log.i(TAG, "ticker starting");
        if(!running) {
            running = true;
            new Thread(new Beat()).start();
        }
    }

    // Stop the ticker
    public void stop() {
        Log.i(TAG, "ticker stopping");
        running = false;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }


    // Beat
    private class Beat implements Runnable {


        // Run
        public void run() {
            // Init
            //completeMap(0,0);
            //condition for checking if maze was solved
            if(completeMap(startX,startY)) {
                MainActivity.resetWhichBlock();
                Log.i("TEST", "FOUND");
                MainActivity.getInstance().getHandler().post(new Runnable() {
                    public void run() {
                        MainActivity.getInstance().updateSubmit();
                        MainActivity.getInstance().enableTypeMap(true);
                        Toast.makeText(MainActivity.getInstance(), "FOUND", Toast.LENGTH_LONG).show();

                    }
                });

            }
            //condition if max was not solved
            else {
                MainActivity.resetWhichBlock();
                Log.i("TEST", "NOT FOUND");
                MainActivity.getInstance().getHandler().post(new Runnable() {
                    public void run() {
                        MainActivity.getInstance().updateSubmit();
                        MainActivity.getInstance().enableTypeMap(true);
                        Toast.makeText(MainActivity.getInstance(), "NOT FOUND", Toast.LENGTH_LONG).show();

                    }
                });
            }
            /*
            if(!isTick) {
                playAccent();
            }
            else {
                SoundManager.getInstance().playTickOne(beatSkip);
            } */



            // Thread Loop
          /*  while (running) {
                current = Calendar.getInstance();
                //get current UI beatsPerMinute tempo setting
                bpm = MainActivity.getInstance().getBPM();


                // Check BPM
                if (bpm < MIN_BPM_TEMPO || bpm > MAX_BPM_TEMPO) {
                    continue; //do nothing, tempo is out of range
                }

               // delay = 60000 / bpm; //milliseconds per beat
                delay = 1000;

                elapsed = (int) (current.getTimeInMillis() - previous
                        .getTimeInMillis());


/*
                // Time Elapsed
                if (elapsed >= delay) {
                    beatCount = beatCount % mBeatsPerMeasure + 1;
                    MainActivity.getInstance().getHandler().post(new Runnable() {
                        public void run() {
                            //MainActivity.getInstance().tester();
                            //MainActivity.getInstance().setBeatCount(beatCount);
                        }
                    });
                    previous = current;
                    Log.i(TAG, "Play Sound");


                } */


           // }  //end while

        } //end run

    }

    //method that solves the maze recursively
    boolean completeMap(int x, int y) {

       // if (running) {
            if (MainActivity.getInstance().getMap()[x][y] == 2) {
                return true;
            }
            if (MainActivity.getInstance().getMap()[x][y] == 1 || MainActivity.getInstance().getMap()[x][y] == 3) {
                return false;
            }

            MainActivity.getInstance().getMap()[x][y] = 1;
            MainActivity.getInstance().updateMap();
            //printMap();
            //else {
            if (y != 10 - 1) { // Checks if not on bottom edge

                if (completeMap(x, y + 1)) { // Recalls method one down
                    //map[x][y] = 1;
                    return true;
                }
            }
            if (x != 8 - 1) { // Checks if not on right edge
                if (completeMap(x + 1, y)) { // Recalls method one to the right
                    //map[x][y] = 1;
                    return true;
                }
            }
            if (x != 0) {// Checks if not on left edge
                if (completeMap(x - 1, y)) { // Recalls method one to the left
                    //map[x][y] = 1; // Sets that path value to true;
                    return true;
                }
            }

            if (y != 0) {// Checks if not on top edge
                if (completeMap(x, y - 1)) { // Recalls method one up
                    //map[x][y] = 1;
                    return true;
                }
            }

            //}


            return false;
       // }
        //return false;
    }


}
