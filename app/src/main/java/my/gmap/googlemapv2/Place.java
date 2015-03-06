package my.gmap.googlemapv2;

import com.google.api.client.util.Key;

import java.io.Serializable;

/**
 * Created by admin on 11/15/14.
 * Make every place as an object to make it reusable components
 */
public class Place implements Serializable {
    @Key
    public String id;
    @Key
    public String name;
    @Key
    public String reference;
    @Key
    public String icon;
    @Key
    public String vicinity;
    @Key
    public Geometry mGeometry;
    @Key
    public String formatted_address;
    @Key
    public String formatted_phone_numbers;

    @Override
    public String toString(){
        return name + " - " + id + " - " + reference;
    }

    public static class Geometry implements Serializable{
        @Key
        public Location mLocation;
    }

    public static class Location implements Serializable{
        @Key
        public double lat;
        @Key
        public double lon;
    }
}
