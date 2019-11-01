package com.example.googlebooks.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SaleInfo implements Parcelable  {
    private String buyLink;

    public String getBuyLink() {//良好習慣 以防null亂跑
        return buyLink != null ? buyLink :"";
    }

    public void setBuyLink(String buyLink) {
        this.buyLink = buyLink;
    }

    public SaleInfo(){}

    protected  SaleInfo(Parcel source){
        buyLink = source.readString();

    }
    public static final Parcelable.Creator<SaleInfo> CREATOR = new Parcelable.Creator<SaleInfo>() {
        @Override
        public SaleInfo createFromParcel(Parcel source) {
            return new SaleInfo(source);
        }

        @Override
        public SaleInfo[] newArray(int size) {
            return new SaleInfo[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(buyLink);
    }
}
