package com.example.googlebooks.models;


import android.os.Parcel;
import android.os.Parcelable;

public class BookModel implements Parcelable {
    private String kind;
    private String eTag;
    private String selfLink;
    private VolumeInfo volumeInfo;
    private SaleInfo saleInfo;

    public BookModel(){
        volumeInfo = new VolumeInfo();
        saleInfo = new SaleInfo();
    }
    protected BookModel(Parcel source){
        kind = source.readString();
        eTag = source.readString();
        selfLink = source.readString();
        volumeInfo = source.readParcelable(VolumeInfo.class.getClassLoader());
        saleInfo = source.readParcelable(SaleInfo.class.getClassLoader());
    }
    public static final Creator<BookModel> CREATOR = new Creator<BookModel>() {
        @Override
        public BookModel createFromParcel(Parcel source) {
            return new BookModel(source);
        }

        @Override
        public BookModel[] newArray(int size) {
            return new BookModel[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(kind);
        dest.writeString(eTag);
        dest.writeString(selfLink);
        dest.writeParcelable(volumeInfo,flags);
        dest.writeParcelable(saleInfo,flags);
    }



    public String getKind() {
        return kind != null ? kind : "";
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String geteTag() {
        return eTag != null ? eTag : "";
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }

    public String getSelfLink() {
        return selfLink != null ? selfLink : "";
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(VolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }

    public SaleInfo getSaleInfo() {
        return saleInfo;
    }

    public void setSaleInfo(SaleInfo saleInfo) {
        this.saleInfo = saleInfo;
    }

}
