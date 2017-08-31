package fiverr.habibqureshi.nursery_rhymes.nurseryrhymes;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.youtube.player.YouTubePlayer;

/**
 * Created by HabibQureshi on 8/13/2017.
 */
public final class touchEffect implements View.OnTouchListener {

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_BUTTON_PRESS:
            case MotionEvent.ACTION_DOWN: {
                ImageView view = (ImageView) v;
                //overlay is black with transparency of 0x77 (119)
                view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                view.invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                ImageView view = (ImageView) v;
                //clear the overlay
                view.getDrawable().clearColorFilter();
                view.invalidate();
                // Log.v("onlick", "onclick");

                break;
            }
        }

        return false;
    }
}