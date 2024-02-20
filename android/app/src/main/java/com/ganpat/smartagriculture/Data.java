package com.ganpat.smartagriculture;

public class Data {
    private int id;
    private String title;
    private String shortdesc;
    private int image;


    private String sdata;



    public Data(int id, String title, String shortdesc, String sdata, int image) {
        this.id = id;
        this.title = title;
        this.shortdesc = shortdesc;
        this.image = image;
        this.sdata = sdata;
    }


    public String getSdata() {
        return sdata;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getShortdesc() {
        return shortdesc;
    }


    public int getImage() {
        return image;
    }
}