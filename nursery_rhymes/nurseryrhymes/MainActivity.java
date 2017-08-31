package fiverr.habibqureshi.nursery_rhymes.nurseryrhymes;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fiverr.habibqureshi.nursery_rhymes.nurseryrhymes.Fragments.splash;

public class MainActivity extends AppCompatActivity {
    FragmentManager fm;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeSupportFragment(new splash(),false);
    }


    public void changeSupportFragment(android.support.v4.app.Fragment frag,
                                      Boolean addToStack) {

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.all_fram_layout, frag);
        if (addToStack){
            ft.addToBackStack(null);

        }else {

        }

        ft.commitAllowingStateLoss();
    }
}
