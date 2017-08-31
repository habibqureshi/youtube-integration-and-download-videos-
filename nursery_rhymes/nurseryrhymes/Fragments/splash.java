package fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.Fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.MainActivity;
import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.R;
import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.touchEffect;

/**
 * Created by HabibQureshi on 8/4/2017.
 */

public class splash extends Fragment {
    View fragment_view;
    MainActivity activity;
    ImageView play;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragment_view = inflater.inflate(R.layout.splash_layout_fragment,
                container, false);
        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }
        findViewsById();
        init();
        return fragment_view;
    }

    private void findViewsById(){
        play = (ImageView) fragment_view.findViewById(R.id.splash_play);
    }
    private void init() {
       play.setOnTouchListener( new touchEffect());
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.changeSupportFragment(new mainscreen(),true);

            }
        });
    }
}
