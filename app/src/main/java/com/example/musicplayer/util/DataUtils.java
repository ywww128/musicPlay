package com.example.musicplayer.util;

import com.example.musicplayer.bean.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 14548
 */
public class DataUtils {
    public static List<Post> posts = new ArrayList<>();

    public static void setPosts(List<Post> posts) {
        DataUtils.posts = posts;
    }

}
