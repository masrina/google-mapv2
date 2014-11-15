package my.gmap.googlemapv2;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by admin on 11/15/14.
 */
public class GPSTracker extends Service implements LocationListener {
    private final Context mContext;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location mLocation = null;
    double latitude;
    double longitude;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;

    //The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    protected LocationManager mLocationManager;

    public GPSTracker(Context context){
        this.mContext = context;
        getLocation();
    }

    public Location getLocation(){
        try{
            mLocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled){
                Toast.makeText(getApplicationContext(),"GPS & Network is not enabled", Toast.LENGTH_SHORT).show();
            }else{
                this.canGetLocation = true;
                if (isNetworkEnabled){
                    mLocationManager.requestLocationUpdates(mLocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network Enabled");
                    if (mLocationManager != null){
                        mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (mLocation != null){
                            latitude = mLocation.getLatitude();
                            longitude = mLocation.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled){
                    if (mLocation == null){
                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS", "GPS Enabled");
                        if (mLocationManager != null){
                            mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (mLocation != null){
                                latitude = mLocation.getLatitude();
                                longitude = mLocation.getLongitude();
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return mLocation;
    }

    public double getLatitude(){
        if (mLocation != null){
            latitude = mLocation.getLatitude();
        }
        return latitude;
    }

    public double getLongitude(){
        if (mLocation != null){
            longitude = mLocation.getLongitude();
        }
        return longitude;
    }

    public void stopUsingGPS(){
        if (mLocationManager != null){
            mLocationManager.removeUpdates(this);
        }
    }

    public boolean canGetLocation(){
        return this.canGetLocation;
    }

    public void showSettingAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("GPS is Settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings?");

        // Press Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // Press Cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }
    @Override public void onLocationChanged(Location location) {

    }

    @Override public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override public void onProviderEnabled(String provider) {

    }

    @Override public void onProviderDisabled(String provider) {

    }

    @Override public IBinder onBind(Intent intent) {
        return null;
    }
}
