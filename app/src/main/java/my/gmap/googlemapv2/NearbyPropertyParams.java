package my.gmap.googlemapv2;

/**
 * Created by admin on 11/20/14.
 */
public class NearbyPropertyParams {
    private Integer listingType;
    private String propGroupType;
    private String Lat;
    private String Lon;
    private Integer pageSize;
    private Integer distance;

    public void setListingType(Integer listingType){
        this.listingType = listingType;
    }

    public Integer getListingType(){
        return listingType;
    }

    public void setPropGroupType(String propGroupType){
        this.propGroupType = propGroupType;
    }

    public String getPropGroupType(){return propGroupType;}

    public void setLat(String lat){
        this.Lat = lat;
    }

    public String getLat(){return Lat;}

    public void setLon(String lon){
        this.Lon = lon;
    }

    public String getLon(){return Lon;}

    public void setPageSize(Integer pageSize){
        this.pageSize = pageSize;
    }

    public Integer getPageSize(){
        return pageSize;
    }

    public void setDistance(Integer distance){
        this.distance = distance;
    }
    public Integer getDistance(){
        return distance;
    }
}
