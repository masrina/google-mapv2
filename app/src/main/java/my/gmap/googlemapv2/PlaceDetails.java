package my.gmap.googlemapv2;

import com.google.api.client.util.Key;

import java.io.Serializable;

/**
 * Created by admin on 11/15/14.
 */
public class PlaceDetails implements Serializable{
    @Key
    public String status;
    @Key
    public String results;
    @Override
    public String toString(){
        if (results != null){
            return results.toString();
        }
        return super.toString();
    }
}
