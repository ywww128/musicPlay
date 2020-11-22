package com.example.musicplayer.bean;

/**
 * 歌曲评论信息各项数据，评论者昵称、评论时间、评论内容
 * @author czc
 */
public class SongCommentItemBean {
    private int avatar;
    private String name;
    private String time;
    private String content;

    public SongCommentItemBean(int avatar, String name, String time, String content) {
        this.avatar = avatar;
        this.name = name;
        this.time = time;
        this.content = content;
    }

    public int getAvatar(){
        return avatar;
    }
    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }
}
