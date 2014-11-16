package my.gmap.googlemapv2;

/**
 * Created by admin on 11/15/14.
 */

public class MyMarker {
    private String mPropertyTitle;
    private String mPropertyPrice;
    private Integer mIcon;
    private Double mLatitude;
    private Double mLongitude;

    public MyMarker(String label, String price, Integer icon, Double latitude, Double longitude){
        this.mPropertyTitle = label;
        this.mPropertyPrice = price;
        this.mIcon = icon;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public String getmLabel(){
        return mPropertyTitle;
    }

    public void setmLabel(String mLabel){
        this.mPropertyTitle = mLabel;
    }

    public String getmPrice(){return mPropertyPrice;}

    public void setmPropertyPrice(String mPropertyPrice) {this.mPropertyPrice = mPropertyPrice;}

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
