package my.gmap.googlemapv2;

import com.google.api.client.util.Key;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 11/15/14.
 * List of places
 */
public class PlaceList implements Serializable {
    @Key
    public String status;
    @Key
    public List<Place> results;
}
