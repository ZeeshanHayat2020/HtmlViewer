package com.zapps.html.xml.viewer.file.reader.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelMainCards implements Parcelable {
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public ModelMainCards(String name) {
        this.name = name;

    }

    protected ModelMainCards(Parcel in) {
        name = in.readString();
    }

    public static final Creator<ModelMainCards> CREATOR = new Creator<ModelMainCards>() {
        @Override
        public ModelMainCards createFromParcel(Parcel in) {
            return new ModelMainCards(in);
        }

        @Override
        public ModelMainCards[] newArray(int size) {
            return new ModelMainCards[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}