package com.chivesoft.ewavi;

import android.graphics.Bitmap;

import com.google.android.gms.ads.AdView;

public class ListDataModel {

    private Bitmap picture = null;
    private String title = null;
    private String description = null;
    private String price = null;
    private Bitmap marketplace = null;
    private String url = null;
    private String imgUrl = null;
    private AdView adView = null;

    public ListDataModel(Bitmap picture, String title, String description, String price, Bitmap marketplace, String url, String imgUrl) {
        this.picture = picture;
        this.title = title;
        this.description = description;
        this.price = price;
        this.marketplace = marketplace;
        this.url = url;
        this.imgUrl = imgUrl;
    }
    public ListDataModel(AdView adView) {
        this.adView = adView;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public Bitmap getMarketplace() {
        return marketplace;
    }

    public String getUrl() {
        return url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public AdView getView() {
        return adView;
    }

}