package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xuexiang.xui.widget.button.roundbutton.RoundButton;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RoundButton btnJumpToSongPlaying = findViewById(R.id.btn_jump_to_song_playing);
        btnJumpToSongPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SongPlayingActivity.class);
                startActivity(intent);
            }
        });
    }
}