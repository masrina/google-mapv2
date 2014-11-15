package my.gmap.googlemapv2;

/**
 * Created by admin on 11/15/14.
 */

import com.google.android.gms.maps.model.LatLng;

public class MyMarker {
    private String mLabel;
    private Integer mIcon;
    private Double mLatitude;
    private Double mLongitude;

    public MyMarker(String label, Integer icon, Double latitude, Double longitude){
        this.mLabel = label;
        this.mIcon = icon;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public String getmLabel(){
        return mLabel;
    }

    public void setmLabel(String mLabel){
        this.mLabel = mLabel;
    }

    public Integer getmIcon(){
        return mIcon;
    }

    public void setmIcon(Integer mIcon){
        this.mIcon = mIcon;
    }

    public Double getmLatitude(){
        return mLatitude;
    }

    public void setmLatitude(Double mLatitude){
        this.mLatitude = mLatitude;
    }

    public Double getmLongitude(){
        return mLongitude;
    }

    public void setmLongitude(Double mLongitude){
        this.mLongitude = mLongitude;
    }
}
