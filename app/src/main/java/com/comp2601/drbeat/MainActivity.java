package com.comp2601.drbeat;

//TODO: NEED TO ADD CLEAR BUTTON, WHICH CLEARS MAP, NEED TO ADD ABILITY TO PLACE START POINT, END POINT, AND WALL, AND IF WALL IS PICKED IT SHOULD
//TODO: DISPLACE IT

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static MainActivity instance; //globally accessible instance

    private Handler handler;

    private int width = 10;
    private int height = 8;

    //variable to determine which button user is placing, 1 for start, 2 for end, 3 for walls
    private static int whichBlock = 1;


    private static int[][] map = new int[][] {
                                {0,0,0,0,0,0, 0, 0, 0, 0},
                                {0,0,0,0,0,0, 0, 0, 0, 0},
                                {0,0,0,0,0,0, 0, 0, 0, 0},
                                {0,0,0,0,0,0, 0, 0, 0, 0},
                                {0,0,0,0,0,0, 0, 0, 0, 0},
                                {0,0,0,0,0,0, 0, 0, 0, 0},
                                {0,0,0,0,0,0, 0, 0, 0, 0},
                                {0,0,0,0,0,0, 0, 0, 0, 0}};
    private Button[][] buttonMap = new Button[height][width];

    private Button submitBtn;


    private Ticker ticker; //time keeper to generate beat ticks

    //provide global access to main activity instance
    public static MainActivity getInstance(){return instance;}



    //reenables the submit button
    public void updateSubmit() {
        submitBtn.setEnabled(true);
    }
    //resets the variable that is linked to which button the user is laying down
    public static void resetWhichBlock() {
        whichBlock = 1;
    }

    public static int[][] getMap() {
        return map;
    }

    //method that updates the visual of maze being completed
    public void updateMap() {
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                if(map[i][j] == 1) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(buttonMap[i][j] != null) {
                        buttonMap[i][j].setBackgroundColor(Color.parseColor("#f99e8e"));
                        //buttonMap[i][j].setBackgroundColor(Color.RED);
                    }
                }
            }
        }
    }
    public Handler getHandler() {
        return handler;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int[] tmpArray = new int[height*width];
        int counter = 0;
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                tmpArray[counter] = map[i][j];
                counter++;
            }
        }
        outState.putIntArray("KEY", tmpArray);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        instance = this; //set globally accessible instance
        setContentView(R.layout.activity_main); //set and inflate UI
        if(savedInstanceState!= null) {
            int[] tmpArray = savedInstanceState.getIntArray("KEY");
            int counter = 0;
            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    map[i][j] =  tmpArray[counter];
                   // buttonMap[i][j] = findViewById(idArray[counter]);
                    buttonMap[i][j].setBackgroundColor(Color.WHITE);
                    if(map[i][j] == 1) {
                        buttonMap[i][j].setBackgroundColor(Color.parseColor("#f99e8e"));
                        //buttonMap[i][j].setBackgroundColor(Color.RED);
                    }
                    counter++;
                }
            }
        }
        handler = new Handler();
        //if existing ticker instance is running use it
        if(Ticker.getInstance() != null) ticker = Ticker.getInstance();
        else ticker = new Ticker(0);

        //initializing layout of maze grid
        TableLayout gameLayout = findViewById(R.id.gameTable);
        for(int i = 0; i < height; i++) {
            TableRow tableRow = new TableRow(MainActivity.this);
            for(int j = 0; j < width; j++) {
                Button button = new Button(MainActivity.this);
                TableRow.LayoutParams trParams = new TableRow.LayoutParams(100, 110); button.setLayoutParams(trParams);
                buttonMap[i][j] = button;
                buttonMap[i][j].setBackgroundColor(Color.WHITE);
                tableRow.addView(button);

            }
            gameLayout.addView(tableRow);
        }
        gameLayout.setPadding(20,0,0,0);

        submitBtn = findViewById(R.id.submit);

        /*
        int totalCounter = 0;
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {

                //buttonMap[i][j] = findViewById(idArray[totalCounter]);
                totalCounter++;
                if(map[i][j] == 2) {
                    buttonMap[i][j].setBackgroundColor(Color.BLUE);
                }
               if(map[i][j] == 0) {
                   buttonMap[i][j].setBackgroundColor(Color.WHITE);
               }
               if (map[i][j] == 3) {
                    buttonMap[i][j].setBackgroundColor(Color.BLACK);
               }
            }
        } */

        //button listener for submit
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitBtn.setEnabled(false);
                ticker.stop();
                //loops through map array and resets board
                for(int i = 0; i < height; i++) {
                    for(int j = 0; j < width; j++) {
                        if(map[i][j]==1) {
                            map[i][j] = 0;
                            buttonMap[i][j].setBackgroundColor(Color.WHITE);
                        }
                        /*
                        if(map[i][j] == 2) {
                            buttonMap[i][j].setBackgroundColor(Color.BLUE);
                        }
                        if(map[i][j] == 3) {
                            buttonMap[i][j].setBackgroundColor(Color.BLACK);
                        }
                        if(map[i][j] == 0) {
                            buttonMap[i][j].setBackgroundColor(Color.WHITE);
                        } */

                    }
                }
                //setting the map buttons clickable to false
                enableTypeMap(false);
                ticker.start();
            }
        });
        //MAZE BUTTON LISTENERS, NEED TO ADD CHECKERS FOR WHEN ADDING STARTING POINT, END POINT, AND WALL
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                final Button b = buttonMap[i][j];
                final int iTmp = i;
                final int jTmp = j;
                buttonMap[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(map[iTmp][jTmp] == 3) {
                            map[iTmp][jTmp] = 0;
                            b.setBackgroundColor(Color.WHITE);
                        }
                        //that means placing start point
                        else if(whichBlock == 1) {
                            resetMap();
                            ticker.setStartX(iTmp);
                            ticker.setStartY(jTmp);
                            map[iTmp][jTmp] = 1;
                            b.setBackgroundColor(Color.parseColor("#f99e8e"));
                            //b.setBackgroundColor(Color.RED);
                            whichBlock++;
                        }
                        //that means placing end spot
                        else if(whichBlock == 2) {
                            map[iTmp][jTmp] = 2;
                            b.setBackgroundColor(Color.parseColor("#8fbaf7"));
                            whichBlock++;
                        }
                        else if(whichBlock == 3) {
                            map[iTmp][jTmp] = 3;
                            b.setBackgroundColor(Color.BLACK);
                        }
                    }
                });
            }
        }




    }

    //method that resets the map setting map array and buttons to default
    private void resetMap() {
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                map[i][j] = 0;
                buttonMap[i][j].setBackgroundColor(Color.WHITE);
            }
        }
    }
    //method that sets the map buttons to clickable or not accordingly
    public void enableTypeMap(boolean isEnabled) {
        for(int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                buttonMap[i][j].setClickable(isEnabled);
            }
        }
    }


}
