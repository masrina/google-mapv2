package my.gmap.googlemapv2.activity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import my.gmap.googlemapv2.AlertDialogManager;
import my.gmap.googlemapv2.ConnectionDetector;
import my.gmap.googlemapv2.GPSTracker;
import my.gmap.googlemapv2.MyMarker;
import my.gmap.googlemapv2.NearbyPropertyParams;
import my.gmap.googlemapv2.PlaceList;
import my.gmap.googlemapv2.R;
import my.gmap.googlemapv2.lib.view.TransparentPanel;

public class MapsActivity extends FragmentActivity {

    public static final String nearby_url = "http://api4.iproperty.com/v1/property/nearby";
    private String url;
    boolean isInternetPresent = false;
    ConnectionDetector mDetector;
    AlertDialogManager mAlertDialogManager = new AlertDialogManager();
    //GooglePlaces mGooglePlaces;
    PlaceList mPlaceList;
    GPSTracker gps;
    public Animation animShow, animHide;
    NearbyPropertyParams params;
    private static final String url_nearby = "http://api4.iproperty.com/v1/property/nearby";
    ArrayList<Double> latitude = new ArrayList<Double>();
    ArrayList<Double> longitude = new ArrayList<Double>();
    ArrayList<String> title = new ArrayList<String>();
    ArrayList<String> bedrooms = new ArrayList<String>();
    ArrayList<String> bathrooms = new ArrayList<String>();
    ArrayList<String> photo = new ArrayList<String>();
    ArrayList<String> price = new ArrayList<String>();

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

        new HttpAsyncTask().execute(url_nearby);
        initPopUp();
        //setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
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

            mMap.animateCamera(CameraUpdateFactory
                    .newLatLngZoom(new LatLng(gps.getLatitude(), gps.getLongitude()), 13));

            mMarkerHashMap = new HashMap<Marker, MyMarker>();

            for (int i=0; i<latitude.size(); i++){
                mMyMarkerArrayList.add(new MyMarker(title.get(i), price.get(i), photo.get(i), latitude.get(i), longitude.get(i)));
            }

            plotMarkers(mMyMarkerArrayList);

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
                    @Override
                public boolean onMarkerClick(Marker marker){
                        initPropertyInfo();
                        //marker.showInfoWindow();
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

                markerOptions.icon(
                        BitmapDescriptorFactory.fromResource(R.drawable.map_marker_green));

                Marker currentMarker = mMap.addMarker(markerOptions);
                dropPinEffect(currentMarker, 1);
                mMarkerHashMap.put(currentMarker, myMarker);

                //mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
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
            ImageView markerIcon = (ImageView) view.findViewById(R.id.marker_icon1);
            TextView markerLabel = (TextView) view.findViewById(R.id.property_title);
            TextView price = (TextView) view.findViewById(R.id.property_price);
            View like = (View) view.findViewById(R.id.call_agent);
            View chat = (View) view.findViewById(R.id.chatbtn);
            if (myMarker.getmLabel().equals("You're Here")) {
                markerIcon.setImageResource(0);
            }
            //}else {
            //    markerIcon.setImageResource(R.drawable.photourl);
            //}
            new DownloadImageTask((ImageView) findViewById(R.id.marker_icon1))
                    .execute(myMarker.getmIcon());
            markerLabel.setText(myMarker.getmLabel());
            price.setText(myMarker.getmPrice());
            like.setVisibility(View.GONE);
            chat.setVisibility(View.GONE);
            return view;
        }
    }

    private void dropPinEffect(final Marker marker, int choice){
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();


        final android.view.animation.Interpolator interpolator = new BounceInterpolator();

        switch (choice){
            case 1:{
                handler.postDelayed(new Runnable() {
                    @Override public void run() {
                        long elapsed = SystemClock.uptimeMillis() - start;
                        float t = Math.max(1 - interpolator.getInterpolation((float) elapsed / 2500), 0);
                        marker.setAnchor(0.5f, 1.0f + 14 * t);
                        if (t > 0.0){
                            handler.postDelayed(this, 20);
                        }else{
                            //marker.showInfoWindow();
                        }
                    }
                }, 1000);
            }
            break;
            case 2:{
                handler.post(new Runnable() {
                    @Override public void run() {
                        long elapsed = SystemClock.uptimeMillis() - start;
                        float t = Math
                                .max(1 - interpolator.getInterpolation((float) elapsed / 1500),
                                        0);
                        marker.setAnchor(0.5f, 1.0f + 14 * t);
                        if (t > 0.0) {
                            handler.postDelayed(this, 15);
                        } else {
                            //marker.showInfoWindow();
                        }
                    }
                });
            }


        }

    }

    private void initPopUp(){
        final TransparentPanel popup = (TransparentPanel) findViewById(R.id.popup_window);
        final TransparentPanel propertyinfo = (TransparentPanel) findViewById(R.id.property_info);

        popup.setVisibility(View.GONE);
        propertyinfo.setVisibility(View.GONE);

        animShow = AnimationUtils.loadAnimation(this, R.anim.popup_show);
        animHide = AnimationUtils.loadAnimation(this, R.anim.popup_hide);

        final View filter = (View) findViewById(R.id.filter_btn);
        final View close_filter = (View) findViewById(R.id.apply);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                popup.setVisibility(View.VISIBLE);
                popup.startAnimation(animShow);
                close_filter.setEnabled(true);
                if (propertyinfo.getVisibility() == View.VISIBLE){
                    propertyinfo.setVisibility(View.GONE);
                }
            }
        });

        close_filter.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                popup.startAnimation(animHide);
                close_filter.setEnabled(true);
                popup.setVisibility(View.GONE);
            }
        });

        final Spinner propertyType = (Spinner) findViewById(R.id.property_type);
        propertyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position,
                    long id) {

                popup.startAnimation(animHide);
                close_filter.setEnabled(true);
                popup.setVisibility(View.GONE);

                //if (parent.getItemAtPosition(position).toString().equals("All Residential")) {
                //    setPropGroupType("All Residential");
                //
                //} else if (parent.getItemAtPosition(position).toString().equals("All Commercial")) {
                //    setPropGroupType("All Commercial");
                //
                //} else {
                //    setPropGroupType("All Industrial");
                //
                //}

                Toast.makeText(parent.getContext(),
                        "Filter Property Type : " + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {
                popup.startAnimation(animHide);
                close_filter.setEnabled(true);
                popup.setVisibility(View.GONE);

                Toast.makeText(parent.getContext(),
                        "No Filter changes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initPropertyInfo(){
        final TransparentPanel filter = (TransparentPanel) findViewById(R.id.popup_window);
        final TransparentPanel propertyInfo = (TransparentPanel) findViewById(R.id.property_info);
        propertyInfo.setVisibility(View.GONE);
        filter.setVisibility(View.GONE);

        animShow = AnimationUtils.loadAnimation(this, R.anim.popup_show);
        animHide = AnimationUtils.loadAnimation(this, R.anim.popup_hide);

        final ImageView markerIcon = (ImageView) findViewById(R.id.marker_icon1);
        final TextView markerLabel = (TextView) findViewById(R.id.property_title);
        final TextView price = (TextView) findViewById(R.id.property_price);
        final View like = (View) findViewById(R.id.like);
        final View chat = (View) findViewById(R.id.chatbtn);


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override public boolean onMarkerClick(Marker marker) {
                MyMarker myMarker = mMarkerHashMap.get(marker);
                dropPinEffect(marker, 2);
                if (filter.getVisibility() == View.VISIBLE){
                    filter.setVisibility(View.GONE);
                }

                if (!myMarker.getmLabel().equals("You're Here")){
                    propertyInfo.setVisibility(View.VISIBLE);
                    propertyInfo.startAnimation(animShow);
                    new DownloadImageTask(markerIcon).execute(myMarker.getmIcon());
                    //markerIcon.setImageResource(myMarker.getmIcon());
                }
                markerLabel.setText(myMarker.getmLabel());
                price.setText(myMarker.getmPrice());
                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override public void onMapClick(LatLng latLng) {
                if (propertyInfo.getVisibility() == View.VISIBLE){
                    propertyInfo.setVisibility(View.GONE);
                }
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                new SweetAlertDialog(MapsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Great!")
                        .setContentText("Thanks For the Thumbs Up!")
                        .show();
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent i;
                PackageManager manager = getPackageManager();
                try {
                    i = manager.getLaunchIntentForPackage("iproperty.gcmchat");
                    if (i == null)
                        throw new PackageManager.NameNotFoundException();
                    i.addCategory(Intent.CATEGORY_LAUNCHER);
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public static String POST(String url, NearbyPropertyParams nearby){
        InputStream inputStream = null;
        String result = "";

        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("ListingType", nearby.getListingType());
            jsonObject.accumulate("PropertyGroupType", nearby.getPropGroupType());
            jsonObject.accumulate("Lat", nearby.getLat());
            jsonObject.accumulate("Lon", nearby.getLon());
            jsonObject.accumulate("PageSize", nearby.getPageSize());
            jsonObject.accumulate("Distance", nearby.getDistance());

            json = jsonObject.toString();

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Authorization", "Basic aXBhZF92Ml9teXM6bW9iMTIzNA==");
            httpPost.setHeader("Country", "MYS");
            httpPost.setHeader("Content-Type", "application/json");

            HttpResponse httpResponse = httpClient.execute(httpPost);

            inputStream = httpResponse.getEntity().getContent();

            if (inputStream != null){
                result = convertInputStreamToString(inputStream);
            }else {
                result = "Error";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
    String propgrouptype;
    private String getPgt(){
        return propgrouptype;
    }

    private void setPropGroupType(String pgt){

        if(pgt.equals("All Residential")){
            propgrouptype = "AR";
        }else if(pgt.equals("All Commercial")){
            propgrouptype = "AC";
        }else{
            propgrouptype = "AI";
        }
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            params = new NearbyPropertyParams();
            params.setListingType(1);
            params.setPropGroupType("AR");
            params.setDistance(20);
            params.setLat(String.valueOf(gps.getLatitude()));
            params.setLon(String.valueOf(gps.getLongitude()));
            params.setPageSize(20);

            return POST(urls[0], params);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = new JSONObject(result);
                JSONObject response = json.getJSONObject("response");
                JSONArray results = response.getJSONArray("results");
                Log.i("Length of result: ", String.valueOf(results.length()));
                Log.i("LatitudeResult", results.getJSONObject(0).getString("lat"));
                Log.i("LongitudeResult", results.getJSONObject(0).getString("lon"));

                for (int i=0; i<results.length(); i++){
                    latitude.add(i, Double.valueOf(results.getJSONObject(i).getString("lat")));
                    longitude.add(i, Double.valueOf(results.getJSONObject(i).getString("lon")));
                    title.add(i, results.getJSONObject(i).getString("title"));
                    //bedrooms.add(i, results.getJSONObject(i).getString("bedrooms"));
                    //bathrooms.add(i, results.getJSONObject(i).getString("bathrooms"));
                    photo.add(i, results.getJSONObject(i).getString("photo"));
                    price.add(i, results.getJSONObject(i).getString("price"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            setUpMapIfNeeded();
            //Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}