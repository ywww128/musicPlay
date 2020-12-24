package com.example.musicplayer.bean;

/**
 * @author 14548
 */
public class Post {
    private String postId;
    private int postAgree;
    private String postCreateTime;
    private String postAuthorId;
    private String postText;
    private String postImage;

    public Post(String postId, int postAgree, String postCreateTime, String postAuthorId, String postText, String postImage) {
        this.postId = postId;
        this.postAgree = postAgree;
        this.postCreateTime = postCreateTime;
        this.postAuthorId = postAuthorId;
        this.postText = postText;
        this.postImage = postImage;
    }

    public String getPostId() {
        return postId;
    }

    public int getPostAgree() {
        return postAgree;
    }

    public String getPostCreateTime() {
        return postCreateTime;
    }

    public String getPostAuthorId() {
        return postAuthorId;
    }

    public String getPostText() {
        return postText;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setPostAgree(int postAgree) {
        this.postAgree = postAgree;
    }

    public void setPostCreateTime(String postCreateTime) {
        this.postCreateTime = postCreateTime;
    }

    public void setPostAuthorId(String postAuthorId) {
        this.postAuthorId = postAuthorId;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    @Override
    public String toString() {
        return "{" +
                "postId='" + postId + '\'' +
                ", postAgree=" + postAgree +
                ", postCreateTime='" + postCreateTime + '\'' +
                ", postAuthorId='" + postAuthorId + '\'' +
                ", postText='" + postText + '\'' +
                ", postImage='" + postImage + '\'' +
                '}';
    }
}
