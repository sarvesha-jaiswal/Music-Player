package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.musicplayer.R;

import java.io.File;
import java.util.ArrayList;

public class playerActivity extends AppCompatActivity {

    Button btn_next,btn_previous,btn_pause;
    TextView songTextLable;
    SeekBar songSeekbar;
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;
    Thread updateseekBar;
    String sname;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        btn_next=(Button)findViewById(R.id.next);
        btn_previous=(Button)findViewById(R.id.previous);
        btn_pause=(Button)findViewById(R.id.pause);
        songTextLable=(TextView)findViewById(R.id.songLable);
        songSeekbar=(SeekBar)findViewById(R.id.seekBar);
        getSupportActionBar().setTitle("Now Playing ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        updateseekBar=new Thread(){

            @Override
            public void run() {
                int totalDuration=mediaPlayer.getDuration();
                int currentPosition=0;
                while (currentPosition<totalDuration){
                    try {
                        sleep(500);
                        currentPosition=mediaPlayer.getCurrentPosition();
                        songSeekbar.setProgress(currentPosition);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }


            }
        };
    if(mediaPlayer!=null){
        mediaPlayer.stop();
        mediaPlayer.release();
    }
        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        mySongs=(ArrayList)bundle.getParcelableArrayList("songs");
        sname=mySongs.get(position).getName().toString();
        String songName =i.getStringExtra("songname");
        songTextLable.setText(songName);
        songTextLable.setSelected(true);
        position= bundle.getInt("pos",0);

        Uri u=Uri.parse(mySongs.get(position).toString());
        mediaPlayer=MediaPlayer.create(getApplicationContext(),u);
        mediaPlayer.start();
        songSeekbar.setMax(mediaPlayer.getDuration());
        updateseekBar.start();
        songSeekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.MULTIPLY);
        songSeekbar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);

        songSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songSeekbar.setMax(mediaPlayer.getDuration());
                if(mediaPlayer.isPlaying()){
                    btn_pause.setBackgroundResource(R.drawable.icon_play);
                    mediaPlayer.pause();
                }
                else{
                    btn_pause.setBackgroundResource(R.drawable.icon_pause);
                    mediaPlayer.start();
                }
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position=((position+1)%mySongs.size());
                Uri u=Uri.parse(mySongs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                sname=mySongs.get(position).getName().toString();
                songTextLable.setText(sname);
                mediaPlayer.start();


            }
        });
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position=((position-1)<0)?(mySongs.size()-1):(position-1);
                Uri u=Uri.parse(mySongs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                sname=mySongs.get(position).getName().toString();
                songTextLable.setText(sname);
                mediaPlayer.start();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }


        return super.onOptionsItemSelected(item);
    }
}