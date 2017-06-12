package number.android.waterdrop.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import number.android.waterdrop.fragments.OrderFragment;
import number.android.waterdrop.fragments.OrdersHistoryFragment;
import number.android.waterdrop.fragments.PreferencesFragment;

/**
 * Created by user on 8/26/2016.
 */
public class WaterDropPagesAdapter extends FragmentStatePagerAdapter {

    int tabCount;

    public WaterDropPagesAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                PreferencesFragment preferencesFragment = new PreferencesFragment();
                return preferencesFragment;
            case 1:
                OrderFragment orderFragment = new OrderFragment();
                return orderFragment;
            case 2:
                OrdersHistoryFragment ordersHistoryFragment = new OrdersHistoryFragment();
                return ordersHistoryFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

}
