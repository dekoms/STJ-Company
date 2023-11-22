package com.example.petme;

public class FeedItem {
    private int mediaResourceId;
    private String caption;

    public FeedItem(int mediaResourceId, String caption) {
        this.mediaResourceId = mediaResourceId;
        this.caption = caption;
    }

    //getter setter 추가

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getMediaResourceId() {
        return mediaResourceId;
    }
}