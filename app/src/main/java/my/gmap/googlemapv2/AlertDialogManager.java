package my.gmap.googlemapv2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by admin on 11/15/14.
 * Reusable alert class
 */
public class AlertDialogManager {
    public void showAlertDialog(Context context,String title, String message, Boolean status){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        if (status != null){
            alertDialog.setIcon((status) ? null : null);
        }
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }
}
