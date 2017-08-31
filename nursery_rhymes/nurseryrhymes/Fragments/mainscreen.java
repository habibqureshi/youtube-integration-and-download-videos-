package fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.youtube.player.YouTubePlayer;

import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.MainActivity;
import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.R;
import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.YoutubeApiImp;
import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.touchEffect;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by HabibQureshi on 8/4/2017.
 */

public class mainscreen extends Fragment {
    View fragment_view;
    MainActivity activity;
    LinearLayout l,l1,l2,l3,l4;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragment_view = inflater.inflate(R.layout.main_layout_fragment,
                container, false);
        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }
        l=(LinearLayout)fragment_view.findViewById(R.id.l);
        l1=(LinearLayout)fragment_view.findViewById(R.id.l2);
        l2=(LinearLayout)fragment_view.findViewById(R.id.l3);
        l3=(LinearLayout)fragment_view.findViewById(R.id.l4);
        l4=(LinearLayout)fragment_view.findViewById(R.id.l5);
        l.setOnClickListener(click);
        l1.setOnClickListener(click);
        l2.setOnClickListener(click);
        l3.setOnClickListener(click);
        l4.setOnClickListener(click);






        return fragment_view;
    }
    View.OnClickListener click= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LinearLayout l=(LinearLayout)v;
            Intent intent = new Intent(getContext(),YoutubeApiImp.class);
            intent.putExtra("PlayListID",l.getTag().toString());
            startActivity(intent);

        }
    };
}
