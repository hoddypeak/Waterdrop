package number.android.waterdrop.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import number.android.waterdrop.R;
import number.android.waterdrop.adapters.OrderHistoryPagesAdapter;
import number.android.waterdrop.adapters.WaterDropPagesAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersHistoryFragment extends Fragment {

    TabLayout orders_segment_tabs;
    ViewPager orders_history_pages;
    View view;
    OrderHistoryPagesAdapter orderHistoryPagesAdapter;

    public OrdersHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_orders_history, container, false);
        initView(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void initView(View view) {

        orders_segment_tabs = (TabLayout) view.findViewById(R.id.orders_segment_tabs);
        orders_segment_tabs.addOnTabSelectedListener(new OnSegmentTabsSelected());

        orders_history_pages = (ViewPager) view.findViewById(R.id.orders_history_pages);
        orderHistoryPagesAdapter = new OrderHistoryPagesAdapter(getChildFragmentManager(),orders_segment_tabs.getTabCount());
        orders_history_pages.setAdapter(orderHistoryPagesAdapter);
        orders_history_pages.addOnPageChangeListener(new OnOrderHistoryPageChange());

    }

    private class OnSegmentTabsSelected implements TabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            orders_history_pages.setCurrentItem(tab.getPosition(),true);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }

    private class OnOrderHistoryPageChange implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            orders_segment_tabs.getTabAt(position).select();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
