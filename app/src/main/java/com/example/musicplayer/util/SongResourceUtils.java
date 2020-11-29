package com.example.musicplayer.util;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.musicplayer.bean.LocalSong;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ywww
 * @date 2020-11-29 18:22
 */
public class SongResourceUtils {
    private final static long minDuration = 30000;
    public static List<LocalSong> getSongList(Context context){
        ContentResolver contentResolver = context.getContentResolver();
        /**
         * 第一个参数为查询的表的Uri
         * 第二个参数为返回的列,null就全部返回
         * 第三个参数为条件,where
         * 第四个参数是为where中的占位符提供具体的值
         * 第五个参数为排序方式
         */
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        List<LocalSong> songList = new ArrayList<>();
        // 开始遍历参数
        if(cursor.moveToNext()){
            LocalSong localSong = new LocalSong();
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            // 歌的绝对路径
            String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            // 专辑
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            long album_id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            int ismusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            // 判断是否符合是音乐的条件
            if(ismusic != 0 && duration>=minDuration){
                localSong.setId(id);
                localSong.setName(name);
                localSong.setArtist_name(artist_name);
                localSong.setDuration(duration);
                localSong.setSize(size);
                localSong.setUrl(url);
                localSong.setAlbum(album);
                localSong.setAlbum_id(album_id);
                localSong.setIsMusic(ismusic);
                songList.add(localSong);
            }
        }
        cursor.close();
        return songList;
    }
}
