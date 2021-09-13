package com.app.utilities.utilitiesActivity._ARMeasure;

import android.os.Parcel;
import android.os.Parcelable;

public class Arl_saved implements Parcelable {
    public static final Creator<Arl_saved> CREATOR = new Creator<Arl_saved>() {
        @Override
        public Arl_saved createFromParcel(Parcel in) {
            return new Arl_saved(in);
        }

        @Override
        public Arl_saved[] newArray(int size) {
            return new Arl_saved[size];
        }
    };
    private String arl_saved;

    public Arl_saved(String arl_saved) {
        this.arl_saved = arl_saved;
    }

    protected Arl_saved(Parcel in) {
        arl_saved = in.readString();
    }

    public String getArl_saved() {
        return arl_saved;
    }

    @SuppressWarnings("unused")
    public void setArl_saved(String arl_saved) {
        this.arl_saved = arl_saved;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(arl_saved);
    }
}
