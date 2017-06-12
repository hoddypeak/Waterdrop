package number.android.waterdrop.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import number.android.waterdrop.fragments.OrderFragment;
import number.android.waterdrop.fragments.OrdersHistoryFragment;
import number.android.waterdrop.fragments.PreferencesFragment;
import number.android.waterdrop.fragments.subfragments.OrderHistoryDOFragment;
import number.android.waterdrop.fragments.subfragments.OrderHistoryROFragment;

/**
 * Created by user on 8/26/2016.
 */
public class OrderHistoryPagesAdapter extends FragmentStatePagerAdapter {

    int tabCount;

    public OrderHistoryPagesAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                OrderHistoryROFragment orderHistoryROFragment = new OrderHistoryROFragment();
                return orderHistoryROFragment;
            case 1:
                 OrderHistoryDOFragment OrderHistoryDOFragment = new OrderHistoryDOFragment();
                return OrderHistoryDOFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
