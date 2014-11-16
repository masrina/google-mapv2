package my.gmap.googlemapv2;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by admin on 11/16/14.
 */
public class ListingDetailActivity extends Activity {
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing_detail);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
