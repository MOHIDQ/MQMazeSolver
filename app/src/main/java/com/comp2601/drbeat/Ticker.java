package com.comp2601.drbeat;

import android.support.annotation.MainThread;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class Ticker {

    private  String TAG = this.getClass().getSimpleName();

    private int startX;
    private int startY;

    private int height;
    private int width;


    // Time Signature Beats per Measure
    private static Ticker instance = null; //globally accessible instance
    public static Ticker getInstance(){return instance;}

    // Running
    private boolean running; //true when ticker is running

    // Constructor
    public Ticker(int h, int w) {
        height = h;
        width = w;
        instance = this;
        running = false;
    }


    // Start the ticker
    public void start() {
        Log.i(TAG, "ticker starting");
        if(!running) {
            running = true;
            new Thread(new Solve()).start();
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
    private class Solve implements Runnable {


        // Run
        public void run() {
            // Init
            //completeMap(0,0);
            //condition for checking if maze was solved
            if(completeMap(startX,startY)) {
                MainActivity.resetWhichBlock();
                Log.i("TEST", "FOUND");
                //allows thread to update ui thread
                MainActivity.getInstance().getHandler().post(new Runnable() {
                    public void run() {
                        MainActivity.getInstance().updateSubmit();
                        MainActivity.getInstance().enableTypeMap(true);
                        Toast.makeText(MainActivity.getInstance(), R.string.found_label, Toast.LENGTH_LONG).show();

                    }
                });

            }
            //condition if maze was not solved
            else {
                MainActivity.resetWhichBlock();
                Log.i("TEST", "NOT FOUND");
                //allows thread to update ui thread
                MainActivity.getInstance().getHandler().post(new Runnable() {
                    public void run() {
                        MainActivity.getInstance().updateSubmit();
                        MainActivity.getInstance().enableTypeMap(true);
                        Toast.makeText(MainActivity.getInstance(), R.string.not_found_label, Toast.LENGTH_LONG).show();

                    }
                });
            }


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

            //TODO: add code where if current pos is adjacent to end position it would automatically be found
            //conditions that checks adjacent positions of current position to see if end point is there
            //reduces total amount of searches for when around end point
            if(x+1 == MainActivity.getInstance().getEndX() && y == MainActivity.getInstance().getEndY()) {
                return true;
            }
            if(x == MainActivity.getInstance().getEndX() && y+1 == MainActivity.getInstance().getEndY()) {
                return true;
            }
            if(x == MainActivity.getInstance().getEndX() && y-1 == MainActivity.getInstance().getEndY()) {
                return true;
            }
            if(x-1 == MainActivity.getInstance().getEndX() && y == MainActivity.getInstance().getEndY()) {
                return true;
            }

            //condition for checking current position is to the left of the end position
            if(y < MainActivity.getInstance().getEndY()) {
                //condition to check if current position is above of the end position
                if(x < MainActivity.getInstance().getEndX()) {
                    Log.i("TEST", "LEFT:ABOVE");
                    if (y != width - 1) { // Checks if not on bottom edge

                        if (completeMap(x, y + 1)) { // Recalls method one down
                            //map[x][y] = 1;
                            return true;
                        }
                    }
                    if (x != height - 1) { // Checks if not on right edge
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
                }
                //condition for if current position is below the end position
                else {
                    if (y != width - 1) { // Checks if not on bottom edge

                        if (completeMap(x, y + 1)) { // Recalls method one down
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
                    if (x != height - 1) { // Checks if not on right edge
                        if (completeMap(x + 1, y)) { // Recalls method one to the right
                            //map[x][y] = 1;
                            return true;
                        }
                    }

                    if (y != 0) {// Checks if not on top edge
                        if (completeMap(x, y - 1)) { // Recalls method one up
                            //map[x][y] = 1;
                            return true;
                        }
                    }
                }



            }
            //condition for checking if current pos is to the right of the end position
            else {
                Log.i("TEST", "RIGHT:ABOVE");
                //condition for checking if current position is above the end position
                if(x < MainActivity.getInstance().getEndX()) {
                    if (y != 0) {// Checks if not on top edge
                        if (completeMap(x, y - 1)) { // Recalls method one up
                            //map[x][y] = 1;
                            return true;
                        }
                    }
                    if (x != height - 1) { // Checks if not on right edge
                        if (completeMap(x + 1, y)) { // Recalls method one to the right
                            //map[x][y] = 1;
                            return true;
                        }
                    }
                    if (y != width - 1) { // Checks if not on bottom edge

                        if (completeMap(x, y + 1)) { // Recalls method one down
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
                }
                //condition for checking if current position is below the end position
                else {
                    if (y != 0) {// Checks if not on top edge
                        if (completeMap(x, y - 1)) { // Recalls method one up
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
                    if (y != width - 1) { // Checks if not on bottom edge

                        if (completeMap(x, y + 1)) { // Recalls method one down
                            //map[x][y] = 1;
                            return true;
                        }
                    }
                    if (x != height - 1) { // Checks if not on right edge
                        if (completeMap(x + 1, y)) { // Recalls method one to the right
                            //map[x][y] = 1;
                            return true;
                        }
                    }


                }



            }

            //printMap();
            //else {


            //}


            return false;
       // }
        //return false;
    }


}
