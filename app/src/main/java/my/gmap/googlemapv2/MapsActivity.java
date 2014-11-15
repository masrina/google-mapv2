package my.gmap.googlemapv2;

import android.graphics.Interpolator;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity {

    boolean isInternetPresent = false;
    ConnectionDetector mDetector;
    AlertDialogManager mAlertDialogManager = new AlertDialogManager();
    //GooglePlaces mGooglePlaces;
    PlaceList mPlaceList;
    GPSTracker gps;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private HashMap<Marker, MyMarker> mMarkerHashMap; // Will contain all markers
    private ArrayList<MyMarker> mMyMarkerArrayList = new ArrayList<MyMarker>(); // will carry MyMarker objects

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mDetector = new ConnectionDetector(getApplicationContext());

        gps = new GPSTracker(this);
        if (gps.canGetLocation()){
            Log.d("Your Location", "latitude: " + gps.getLatitude() + " longitude: " + gps.getLongitude());
        }else{
            mAlertDialogManager.showAlertDialog(MapsActivity.this, "GPS Status",
                    "Couldn't get location information. Please enable GPS", false);
            return;
        }

        mMarkerHashMap = new HashMap<Marker, MyMarker>();

        mMyMarkerArrayList.add(new MyMarker("Brasil", R.drawable.map_marker_green, Double.parseDouble("-28.5971788"), Double.parseDouble("-52.7309824")));
        mMyMarkerArrayList.add(new MyMarker("United States", R.drawable.map_marker_green, Double.parseDouble("33.7266622"), Double.parseDouble("-87.1469829")));
        mMyMarkerArrayList.add(new MyMarker("Canada", R.drawable.map_marker_green, Double.parseDouble("51.8917773"), Double.parseDouble("-86.0922954")));
        mMyMarkerArrayList.add(new MyMarker("England", R.drawable.map_marker_green, Double.parseDouble("52.4435047"), Double.parseDouble("-3.4199249")));
        mMyMarkerArrayList.add(new MyMarker("España", R.drawable.map_marker_green, Double.parseDouble("41.8728262"),
                Double.parseDouble("-0.2375882")));
        mMyMarkerArrayList.add(new MyMarker("Portugal", R.drawable.map_marker_green, Double.parseDouble("40.8316649"),
                Double.parseDouble("-4.936009")));
        mMyMarkerArrayList.add(new MyMarker("Deutschland", R.drawable.map_marker_green, Double.parseDouble("51.1642292"), Double.parseDouble("10.4541194")));
        mMyMarkerArrayList.add(new MyMarker("Atlantic Ocean", R.drawable.map_marker_green, Double.parseDouble("-13.1294607"), Double.parseDouble("-19.9602353")));

        setUpMapIfNeeded();

        plotMarkers(mMyMarkerArrayList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            //mMap.addMarker(new MarkerOptions().position(new LatLng(gps.getLatitude(), gps.getLongitude()))
            //        .title("You're Here")
            //        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_green))
            //        .alpha(0.7f));
            //dropPinEffect(marker);
            //mMap.setMyLocationEnabled(true);
            //
            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gps.getLatitude(), gps.getLongitude()), 13));
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
                    @Override
                public boolean onMarkerClick(Marker marker){
                        marker.showInfoWindow();
                        return true;
                    }
                });
                //setUpMap();
            }else{
                Toast.makeText(getApplicationContext(), "Unable to create Maps", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private void plotMarkers(ArrayList<MyMarker> markers){
        if (markers.size() > 0){
            for (MyMarker myMarker : markers){
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude()));
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_green));

                Marker currentMarker = mMap.addMarker(markerOptions);
                dropPinEffect(currentMarker);
                mMarkerHashMap.put(currentMarker, myMarker);

                mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            }
        }
    }

    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        @Override public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override public View getInfoContents(Marker marker) {
            View view = getLayoutInflater().inflate(R.layout.infowindow_layout, null);
            MyMarker myMarker = mMarkerHashMap.get(marker);
            ImageView markerIcon = (ImageView) view.findViewById(R.id.marker_icon);
            TextView markerLabel = (TextView) view.findViewById(R.id.marker_label);
            markerIcon.setImageResource(R.drawable.default_img);
            markerLabel.setText(myMarker.getmLabel());
            return view;
        }
    }

    private void dropPinEffect(final Marker marker){
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 2500;

        final android.view.animation.Interpolator interpolator = new BounceInterpolator();

        handler.post(new Runnable() {
            @Override public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(1 - interpolator.getInterpolation((float) elapsed / duration), 0);
                marker.setAnchor(0.5f, 1.0f + 14 * t);
                if (t > 0.0){
                    handler.postDelayed(this, 20);
                }else{
                    //marker.showInfoWindow();
                }
            }
        });
    }
}


