package my.gmap.googlemapv2;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by admin on 11/20/14.
 */
public class URLDrawable extends BitmapDrawable {
    protected Drawable drawable;

    @Override
    public void draw(Canvas canvas){
        // override the draw to facilitate refresh function later
        if(drawable != null){
            drawable.draw(canvas);
        }
    }
}