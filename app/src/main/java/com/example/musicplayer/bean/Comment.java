package com.example.musicplayer.bean;

import java.util.Date;

/**
 * 每条动态对应的评论内容
 * @author czc
 */
public class Comment {
    private String commentId;
    private String commentUserId;
    private String commentText;
    private String commentTime;
    /**
     * 对应的动态id
     */
    private String commentPostId;

    public Comment(String commentId, String commentUserId, String commentText, String commentPostId, String commentTime) {
        this.commentId = commentId;
        this.commentUserId = commentUserId;
        this.commentText = commentText;
        this.commentPostId = commentPostId;
        this.commentTime = commentTime;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public void setCommentPostId(String commentPostId) {
        this.commentPostId = commentPostId;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public String getCommentText() {
        return commentText;
    }

    public String getCommentPostId() {
        return commentPostId;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getCommentTime() {
        return commentTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId='" + commentId + '\'' +
                ", commentUserId='" + commentUserId + '\'' +
                ", commentText='" + commentText + '\'' +
                ", commentTime='" + commentTime + '\'' +
                ", commentPostId='" + commentPostId + '\'' +
                '}';
    }
}
