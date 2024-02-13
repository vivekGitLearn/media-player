package com.example.mediax;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    MediaPlayer player;

    private  SeekBar SeekProgress;
    private SeekBar seekVol;
    private   ArrayList<String> songs = new ArrayList<>();
    private int currentIndex=0;
    private TextView songName; // TextView for displaying song name
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songs.add("khaaban_ke_parinday");
        songs.add("bhula_dena");
        songs.add("chala_jaata_hoon");
        songName=findViewById(R.id.songName); // Initialize TextView
        songName.setText(songs.get(currentIndex)); // Set initial song name
        player=MediaPlayer.create(this,R.raw.khaabon_ke_parinday);

        audioManager =(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int maxVol=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVol=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekVol =findViewById(R.id.seek_vol);
        seekVol.setMax(maxVol);
        seekVol.setProgress(curVol);

        seekVol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        SeekProgress = findViewById(R.id.seekBarProg);
        SeekProgress.setMax(player.getDuration());

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SeekProgress.setProgress(player.getCurrentPosition());
            }
        },0,10000);

        SeekProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                player.seekTo(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    public  void next(View view)
    {
        if(currentIndex< songs.size()-1)
            currentIndex++;
        else
            currentIndex=0;
        switchSong();
    }

    public void prev(View view)
    {
        if(currentIndex>0)
            currentIndex--;
        else
            currentIndex= songs.size()-1;
        switchSong();
    }

    public  void replay(View view)
    {
        switchSong();
    }

    private void switchSong()
    {
        String currentSongName=songs.get(currentIndex); // Change variable name to avoid conflict
        int resId=getResources().getIdentifier(currentSongName,"raw",getPackageName());
        player.stop();
        player.release();
        player=MediaPlayer.create(this,resId);
        SeekProgress.setMax(player.getDuration());
        songName.setText(currentSongName); // Set text of TextView with current song name
    }

    public void play(View view)
    {
        player.start();
    }

    public void pause(View view)
    {
        player.pause();
    }

    public void stop(View view)
    {
        player.stop();
    }
}
