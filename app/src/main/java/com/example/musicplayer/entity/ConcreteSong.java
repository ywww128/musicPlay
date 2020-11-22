package com.example.musicplayer.entity;

/**
 * @author ywww
 * @date 2020-11-22 0:46
 */
public class ConcreteSong {
    public int id;
    public String url;
    public int br;
    public int size;
    public String md5;
    public int code;
    public int expi;
    public String type;
    private int gain;
    private int fee;

    @Override
    public String toString() {
        return "ConcreteSong{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", br=" + br +
                ", size=" + size +
                ", md5='" + md5 + '\'' +
                ", code=" + code +
                ", expi=" + expi +
                ", type='" + type + '\'' +
                ", gain=" + gain +
                ", fee=" + fee +
                ", uf='" + uf + '\'' +
                ", payed=" + payed +
                ", flag=" + flag +
                ", canExtend=" + canExtend +
                ", freeTrialInfo='" + freeTrialInfo + '\'' +
                ", level='" + level + '\'' +
                ", encodeType='" + encodeType + '\'' +
                ", freeTrialPrivilege=" + freeTrialPrivilege +
                ", urlSource=" + urlSource +
                '}';
    }

    private String uf;
    private int payed;
    private int flag;
    private boolean canExtend;
    private FreeTrialInfo freeTrialInfo;
    private String level;
    private String encodeType;
    private FreeTrialPrivilege freeTrialPrivilege;
    public int urlSource;


}
