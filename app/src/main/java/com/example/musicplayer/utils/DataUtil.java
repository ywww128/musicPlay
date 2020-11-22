package com.example.musicplayer.utils;

import com.example.musicplayer.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.xuexiang.xui.utils.ResUtils.getResources;

/**
 * 存放用户发布的动态数据和评论数据
 * @author czc
 */
public class DataUtil {
    public static List<String> situation = null;
    public static List<String> comment_community = null;
    public static List<String> comment_song = null;
    static{
        situation = readTxt(R.raw.situation);
        comment_community = readTxt(R.raw.comment_comunity);
        comment_song = readTxt(R.raw.comment_song);
    }
    private static List<String> readTxt(int filePath){
        InputStream is = getResources().openRawResource(filePath);
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(reader);
        String line;
        List<String> list = new ArrayList<>();
        try {
            while ((line = br.readLine()) != null) {
                list.add(line);
            }

        }catch (IOException e){
            e.printStackTrace();
        }finally{
            try {
                br.close();
                reader.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
