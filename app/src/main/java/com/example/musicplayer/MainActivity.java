package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.musicplayer.entity.Artist;
import com.example.musicplayer.entity.Song;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RoundButton btnJumpToSongPlaying = findViewById(R.id.btn_jump_to_song_playing);
        btnJumpToSongPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Song song = new Song();
                song.name = "See You Again";
                song.id = 123;
                song.rUrl = "http://210.34.129.220:9999/m801.music.126.net/20201122132143/aeda636288d7b6cffc27fe218013ee7f/jdymusic/obj/wo3DlMOGwrbDjj7DisKw/4592761947/48ba/6d4c/e9fc/31803f1c0a99df20ab41a886ed11c41b.mp3";
                ArrayList<Artist> artists = new ArrayList<>();
                Artist artist = new Artist();
                artist.name = "zjw";
                artists.add(artist);
                artist = new Artist();
                artist.name = "ywwwww";
                artists.add(artist);
                song.artists = artists;
                Intent intent = new Intent(MainActivity.this, SongPlayingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("song", song);
                intent.putExtra("newSong", bundle);
                startActivity(intent);
            }
        });

        RoundButton btnJumpToSongPlaying2 = findViewById(R.id.btn_jump_to_song_playing2);
        btnJumpToSongPlaying2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Song song = new Song();
                song.name = "温泉";
                song.id = 456;
                song.rUrl = "http://m7.music.126.net/20201122140419/74a464814d1d8237a0466796f3fcff89/ymusic/5f85/6204/e7dd/db0a3e79d134101a577c1329845c5055.mp3";
                ArrayList<Artist> artists = new ArrayList<>();
                Artist artist = new Artist();
                artist.name = "czc";
                artists.add(artist);
                artist = new Artist();
                artist.name = "wxj";
                artists.add(artist);
                song.artists = artists;
                Intent intent = new Intent(MainActivity.this, SongPlayingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("song", song);
                intent.putExtra("newSong", bundle);
                startActivity(intent);
            }
        });

    }
}