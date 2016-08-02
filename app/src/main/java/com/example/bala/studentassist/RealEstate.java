package com.example.bala.studentassist;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bala on 7/28/16.
 */
public class RealEstate implements Serializable,Parcelable{
    String id;
    double lat;
    double lng;
    String name;
    String email;
    String phoneNumber;
    String rent;
    String unitNumber;
    private Map<String,MyPlace> nearbyPlacesMap;

    protected RealEstate(Parcel in) {
        id = in.readString();
        /*
        lat = in.readDouble();
        lng = in.readDouble();
        name = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        rating = in.readDouble();
        unitNumber = in.readString();
        */
    }

    public static final Creator<RealEstate> CREATOR = new Creator<RealEstate>() {
        @Override
        public RealEstate createFromParcel(Parcel in) {
            return new RealEstate(in);
        }

        @Override
        public RealEstate[] newArray(int size) {
            return new RealEstate[size];
        }
    };

    public Map<String, MyPlace> getNearbyPlacesMap() {
        return nearbyPlacesMap;
    }

    public void setNearbyPlacesMap(Map<String, MyPlace> nearbyPlacesMap) {
        this.nearbyPlacesMap = nearbyPlacesMap;
    }

    public RealEstate()
    {
        nearbyPlacesMap = new HashMap<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
    }
}
