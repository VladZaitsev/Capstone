package com.baikaleg.v3.cookingaid.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Step implements Parcelable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("shortDescription")
    @Expose
    private String shortDescription;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("videoURL")
    @Expose
    private String videoURL = null;
    @SerializedName("thumbnailURL")
    @Expose
    private String thumbnailURL;
    @SerializedName("stepTime")
    @Expose
    private int stepTime;

    public Step(int id, String shortDescription, String description,String videoURL, String thumbnailURL, int stepTime) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL=videoURL;
        this.thumbnailURL = thumbnailURL;
        this.stepTime = stepTime;
    }

    protected Step(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL=in.readString();
        thumbnailURL = in.readString();
        stepTime=in.readInt();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
        dest.writeInt(stepTime);
    }

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public int getStepTime() {
        return stepTime;
    }

    public String getVideoURL() {
        return videoURL;
    }
}