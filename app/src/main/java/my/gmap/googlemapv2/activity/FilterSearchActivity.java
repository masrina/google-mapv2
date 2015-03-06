package my.gmap.googlemapv2.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import my.gmap.googlemapv2.R;

/**
 * Created by admin on 11/17/14.
 */
public class FilterSearchActivity extends Activity {
    private Spinner propertyType;
    private Button filter;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_filter);
        addListenerOnSpinnerItemSelection();
    }

    public void addListenerOnSpinnerItemSelection() {
        propertyType = (Spinner) findViewById(R.id.property_type);
        filter = (Button) findViewById(R.id.button_filter);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

            }
        });
        propertyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position,
                    long id) {
                Toast.makeText(parent.getContext(),
                        "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
