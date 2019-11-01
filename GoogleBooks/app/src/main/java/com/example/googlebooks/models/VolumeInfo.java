package com.example.googlebooks.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.googlebooks.Utility;

public class VolumeInfo implements Parcelable {
    private String title;
    private String subtitle;
    private String authors;
    private String publisher;
    private String publishedDate;
    private String rating;
    private String img_Url;

    //getter
    public String getTitle() { //良好習慣 以防null亂跑
        return title != null ? title : "";
    }

    public String getSubtitle() { //良好習慣 以防null亂跑
        return subtitle != null ? subtitle : "";
    }

    public String getAuthors() {//良好習慣 以防null亂跑
        return authors != null ? authors : "";
    }

    public String getPublisher() {
        return publisher != null ? publisher : "";
    }

    public String getPublishedDate() {
        return publishedDate != null ? publishedDate : "";
    }

    public String getRating() { //良好習慣 以防null亂跑
        return authors != null ? authors : "";
    }

    public String getImg_Url() {
        return img_Url != null ? img_Url :"";
    }

    //setter
    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setImg_Url(String img_Url) {
        this.img_Url = img_Url;
    }


    public VolumeInfo(){}

    protected  VolumeInfo(Parcel source){
        title = source.readString();
        subtitle = source.readString();
        authors = source.readString();
        publisher = source.readString();
        publishedDate = source.readString();
        rating = source.readString();
        img_Url = source.readString();
    }
    public static final Creator<VolumeInfo> CREATOR = new Creator<VolumeInfo>() {
        @Override
        public VolumeInfo createFromParcel(Parcel source) {
            return new VolumeInfo(source);
        }

        @Override
        public VolumeInfo[] newArray(int size) {
            return new VolumeInfo[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(authors);
        dest.writeString(publisher);
        dest.writeString(publishedDate);
        dest.writeString(rating);
        dest.writeString(img_Url);
    }
}
