package my.gmap.googlemapv2.activity;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

import java.util.List;

import my.gmap.googlemapv2.R;

/**
 * Created by admin on 11/19/14.
 */
public class ChatActivity extends Activity {
    SharedPreferences prefs;
    List<NameValuePair> params;
    EditText chat_msg;
    Button send_btn;
    Bundle bundle;
    TableLayout tab;

    @Override protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

    }
}
