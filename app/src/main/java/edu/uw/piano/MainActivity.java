package edu.uw.piano;

import android.app.Activity;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;

public class MainActivity extends Activity {

    private static final String TAG = "Piano";
    private SoundPool soundPool;
    private int[] soundIDs = new int[12];
    private boolean[] soundBools = new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeSoundPool();
    }

    //helper method for setting up the sound pool
    @SuppressWarnings("deprecation")
    private void initializeSoundPool(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            //API >= 21
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(4)
                    .setAudioAttributes(new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build())
                    .build();
        }
        else {
            //API < 21
            soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        }

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    if (sampleId == soundIDs[0]) {
                        soundBools[0] = true;
                    } else if (sampleId == soundIDs[1]) {
                        soundBools[1] = true;
                    } else if (sampleId == soundIDs[2]) {
                        soundBools[2] = true;
                    } else if (sampleId == soundIDs[3]) {
                        soundBools[3] = true;
                    } else if (sampleId == soundIDs[4]) {
                        soundBools[4] = true;
                    } else if (sampleId == soundIDs[5]) {
                        soundBools[5] = true;
                    } else if (sampleId == soundIDs[6]) {
                        soundBools[6] = true;
                    } else if (sampleId == soundIDs[7]) {
                        soundBools[7] = true;
                    } else if (sampleId == soundIDs[8]) {
                        soundBools[8] = true;
                    } else if (sampleId == soundIDs[9]) {
                        soundBools[9] = true;
                    } else if (sampleId == soundIDs[10]) {
                        soundBools[10] = true;
                    } else if (sampleId == soundIDs[11]) {
                        soundBools[11] = true;
                    }
                }
            }
        });

        soundIDs[0] = soundPool.load(this, R.raw.piano_040, 1);
        soundIDs[1] = soundPool.load(this, R.raw.piano_041, 1);
        soundIDs[2] = soundPool.load(this, R.raw.piano_042, 1);
        soundIDs[3] = soundPool.load(this, R.raw.piano_043, 1);
        soundIDs[4] = soundPool.load(this, R.raw.piano_044, 1);
        soundIDs[5] = soundPool.load(this, R.raw.piano_045, 1);
        soundIDs[6] = soundPool.load(this, R.raw.piano_046, 1);
        soundIDs[7] = soundPool.load(this, R.raw.piano_047, 1);
        soundIDs[8] = soundPool.load(this, R.raw.piano_048, 1);
        soundIDs[9] = soundPool.load(this, R.raw.piano_049, 1);
        soundIDs[10] = soundPool.load(this, R.raw.piano_050, 1);
        soundIDs[11] = soundPool.load(this, R.raw.piano_051, 1);



    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: //for multiple fingers
            //case MotionEvent.ACTION_MOVE: //uncomment for swipes
                handleTap((int)event.getX(), (int)event.getY());
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }


    //helper method for handling tap logic
    public void handleTap(int x, int y){
        int key = getPianoKey(x,y); //call helper method to get key number
        Log.v(TAG, "Tapped key: "+KEY_NAMES[key]);

        //TODO: Play sound depending on key pressed!
        if (soundBools[key]) {
            soundPool.play(soundIDs[key], .5f, .5f, 0, 0, 1);
        }
    }


    //mapping between touchable rectangles and "key" number
    private final HashMap<Rect, Integer> BLACK_KEYS = new HashMap<Rect, Integer>();
    private final HashMap<Rect, Integer> WHITE_KEYS = new HashMap<Rect, Integer>();
    private static final String[] KEY_NAMES = {"C", "C\u266F", "D", "E\u266D", "E", "F", "F\u266F", "G", "G\u266F", "A", "B\u266D", "B"};

    //helper method to return which key was pressed
    //0 for middle C, then +1 for each half-step
    //-1 if not found
    public int getPianoKey(int x, int y){
        if(BLACK_KEYS.size() == 0){
            //assign key values to constants first time we click
            View view = findViewById(R.id.imgPiano);
            int width = view.getWidth();
            int height = view.getHeight();

            double ww = width/7.0; //white width - 7 keys on screen
            double bw = .52*ww; //black width - estimate at about half the width
            double bh = .575*height; //black height - estimate based on image

            BLACK_KEYS.put(new Rect( (int)(ww*1-.5*bw), 0,   (int)(ww*1+.5*bw), (int)bh), 1); //C#
            BLACK_KEYS.put(new Rect( (int)(ww*2-.5*bw), 0,   (int)(ww*2+.5*bw), (int)bh), 3); //Eb
            BLACK_KEYS.put(new Rect( (int)(ww*4-.5*bw), 0,   (int)(ww*4+.5*bw), (int)bh), 6); //F#
            BLACK_KEYS.put(new Rect( (int)(ww*5-.5*bw), 0,   (int)(ww*5+.5*bw), (int)bh), 8); //G#
            BLACK_KEYS.put(new Rect( (int)(ww*6-.5*bw), 0,   (int)(ww*6+.5*bw), (int)bh), 10); //Bb

            WHITE_KEYS.put(new Rect( (int)(ww*0), 0, (int)(ww*1), height ), 0); //C
            WHITE_KEYS.put(new Rect( (int)(ww*1), 0, (int)(ww*2), height ), 2); //D
            WHITE_KEYS.put(new Rect( (int)(ww*2), 0, (int)(ww*3), height ), 4); //E
            WHITE_KEYS.put(new Rect( (int)(ww*3), 0, (int)(ww*4), height ), 5); //F
            WHITE_KEYS.put(new Rect( (int)(ww*4), 0, (int)(ww*5), height ), 7); //G
            WHITE_KEYS.put(new Rect( (int)(ww*5), 0, (int)(ww*6), height ), 9); //A
            WHITE_KEYS.put(new Rect( (int)(ww*6), 0, (int)(ww*7), height ), 11); //B
        }

        for(Rect key : BLACK_KEYS.keySet()){
            if(key.contains(x,y)){
                return BLACK_KEYS.get(key);
            }
        }

        for(Rect key : WHITE_KEYS.keySet()){
            if(key.contains(x,y)){
                return WHITE_KEYS.get(key);
            }
        }

        return -1; //no key found
    }


    //for immersive full-screen (from API guide)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            final View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }
}
