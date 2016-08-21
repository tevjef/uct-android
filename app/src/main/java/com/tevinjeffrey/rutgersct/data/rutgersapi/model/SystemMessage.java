package com.tevinjeffrey.rutgersct.data.rutgersapi.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SystemMessage implements Parcelable {

    private String messageText;

    public SystemMessage() {
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    @Override
    public String toString() {
        return "SystemMessage{" +
                "messageText='" + messageText + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.messageText);
    }

    protected SystemMessage(Parcel in) {
        this.messageText = in.readString();
    }

    public static final Parcelable.Creator<SystemMessage> CREATOR = new Parcelable.Creator<SystemMessage>() {
        public SystemMessage createFromParcel(Parcel source) {
            return new SystemMessage(source);
        }

        public SystemMessage[] newArray(int size) {
            return new SystemMessage[size];
        }
    };
}
