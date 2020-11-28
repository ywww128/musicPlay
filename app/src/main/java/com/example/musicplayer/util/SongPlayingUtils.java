package com.example.musicplayer.util;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.musicplayer.bean.Artist;
import com.example.musicplayer.bean.PlaySongData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author zjw
 *
 */

public class SongPlayingUtils {

    /**
     * 将毫秒转化为 “分钟：秒钟”形式
     * @param duration 毫秒数
     * @return 转化后的形式
     */
    @SuppressLint("DefaultLocale")
    public static String convertTime(int duration) {
        duration /= 1000;
        // 计算分钟数
        int minute = duration / 60;
        // 计算余下的秒数
        int second = duration % 60;
        return String.format("%02d:%02d", minute, second);
    }

    /**
     * 从lrc文件中获取歌词字符串
     * @param context 上下文环境
     * @param path 歌词文件存放路径
     * @return 歌词字符串
     */
    public static String getLrcStrFromLrcFile(Context context, String path) {
        StringBuilder lrcStr = new StringBuilder();
        String encoding = "GBK";
        // 使用try-with-resource，再执行完毕后可以直接close相应流
        try(InputStreamReader read = new InputStreamReader(context.getResources().getAssets().open(path), encoding);
            BufferedReader bufferedReader = new BufferedReader(read);) {
            String lineStr;
            // 逐行读取文件中的内容
            while ((lineStr = bufferedReader.readLine()) != null) {
                lrcStr.append(lineStr).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lrcStr.toString();
    }

    /**
     * 获取下一曲音乐
     * @param songs 音乐列表
     * @param currentSong 当前播放的音乐
     * @return 下一曲音乐
     */
    public static PlaySongData getNextSong(ArrayList<PlaySongData> songs, PlaySongData currentSong) {
        int len = songs.size();
        for(int i = 0; i < len; ++i) {
            if(currentSong.getId() == songs.get(i).getId()) {
                if(i == len - 1) {
                    return songs.get(0);
                } else {
                    return songs.get(i + 1);
                }
            }
        }
        return null;
    }

    /**
     * 获取上一曲音乐
     * @param songs 音乐列表
     * @param currentSong 当前播放的音乐
     * @return 上一曲音乐
     */
    public static PlaySongData getPreSong(ArrayList<PlaySongData> songs, PlaySongData currentSong) {
        int len = songs.size();
        for(int i = 0; i < len; ++i) {
            if(currentSong.getId() == songs.get(i).getId()) {
                if(i == 0) {
                    return songs.get(len - 1);
                } else {
                    return songs.get(i - 1);
                }
            }
        }
        return null;
    }

    /**
     * 获取播放列表中随机一首歌
     * @param songs 播放列表
     * @return 随机一首歌
     */
    public static PlaySongData getRandomSong(ArrayList<PlaySongData> songs) {
        Random random = new Random();
        return songs.get(random.nextInt(songs.size()));
    }

    /**
     * 判断新音乐是否已经在歌曲列表里面
     * @param songs 歌曲列表
     * @param newSong 新歌曲
     * @return true/false
     */
    public static boolean isNewSongInSongs(ArrayList<PlaySongData> songs, PlaySongData newSong) {
        int len = songs.size();
        for(int i = 0; i < len; ++i) {
            if(newSong.getId() == songs.get(i).getId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取歌曲信息中的 歌曲名字 和 歌曲作者名，将其转化为歌曲标题格式即：歌曲名字（歌曲作者名）
     * 多个作者名之间用&符号隔开
     * 例如：歌曲名：Last Dance
     *      歌曲作者：伍佰，China Blue
     *      转化后为：Last Dance（伍佰 & China Blue）
     * @param song 歌曲信息
     * @return 歌曲标题格式字符串
     */
    public static String getSongTitle(PlaySongData song) {
        StringBuilder str = new StringBuilder(song.getName() + "（");
        ArrayList<Artist> artists = song.getArtists();
        if(artists != null) {
            for(int i = 0; i < artists.size(); ++i) {
                str.append(artists.get(i).name);
                if(i != artists.size() - 1) {
                    str.append(" & ");
                }
            }
        }
        str.append("）");
        return str.toString();
    }

    /**
     * 将播放列表的所有歌曲转化为歌曲标题格式字符串数组。
     * 歌曲标题格式为：歌曲名字（歌曲作者名）
     * @param songs 播放列表的歌单
     * @return 歌曲信息字符串数组
     */
    public static String[] songArrayListToArray(ArrayList<PlaySongData> songs) {
        int len = songs.size();
        String[] songsInformation = new String[len];
        for(int i = 0; i < len; ++i) {
            songsInformation[i] = SongPlayingUtils.getSongTitle(songs.get(i));
        }
        return songsInformation;
    }
}
