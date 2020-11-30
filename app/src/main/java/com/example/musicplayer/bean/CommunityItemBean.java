package com.example.musicplayer.bean;

import android.widget.SimpleAdapter;

/**
 * 社区界面ListView中Item内容
 * @author czc
 */
public class CommunityItemBean {
    /**
     * 头像
     */
    private int avatar;
    /**
     * 发信息的时间
     */
    private String time;
    /**
     * 用户名
     */
    private String name;
    /**
     * 发送内容
     */
    private String content;
    /**
     * 发布动态附带的图片
     */
    private int image_content;

    private SimpleAdapter commentAdapter;

    public CommunityItemBean(int avatar, String time, String name, String content, int image_content, SimpleAdapter commentAdapter) {
        this.avatar = avatar;
        this.time = time;
        this.name = name;
        this.content = content;
        this.image_content = image_content;
        this.commentAdapter = commentAdapter;
    }
    public CommunityItemBean(int avatar, String time, String name, String content, SimpleAdapter commentAdapter) {
        this.avatar = avatar;
        this.time = time;
        this.name = name;
        this.content = content;
        this.commentAdapter = commentAdapter;
    }


    public int getAvatar() {
        return avatar;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public int getImage_content(){
        return image_content;
    }
    public SimpleAdapter getCommentAdapter() {
        return commentAdapter;
    }
}
