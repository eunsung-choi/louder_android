package com.example.louder2;

import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

public class Noti {
    public int soundID;
    public double latitude;
    public double longitude;
    public String created_at;
    public String address;

    public int icon;
    public String name;
    public Noti(int soundID, double latitude, double longitude, String created_at, String address){
        this.soundID = soundID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.created_at = created_at;
        this.address = address;

        switch(this.soundID){ //아이콘 설정
            case 1: //살려주세요
                this.name="살려주세요";
                this.icon = R.drawable.paranoia2;
                break;
            case 2:
                this.name="도와주세요";
                this.icon = R.drawable.help;
                break;
            case 3:
                this.name="울음 소리";
                this.icon = R.drawable.baby;
                break;

        }
    }
    public Noti(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
