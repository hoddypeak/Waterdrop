package number.android.waterdrop.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import number.android.waterdrop.R;
import number.android.waterdrop.activities.entities.UserSession;
import number.android.waterdrop.adapters.WaterDropPagesAdapter;
import number.android.waterdrop.database.WDSharedPreferences;
import number.android.waterdrop.utilities.Icons;
import number.android.waterdrop.utilities.Session;

public class WaterDropActivity extends AppCompatActivity {

    private TabLayout tabs;
    private ViewPager pages;
    int default_tab = 1;

    Icons icons;

    WaterDropPagesAdapter waterDropPagesAdapter;
    WDSharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_drop);
        new Session(this).start();
        preferences = new WDSharedPreferences(this);

        try {
            default_tab = getIntent().getIntExtra("tab",1);
            int request_type = getIntent().getIntExtra("request_type",0);
            switch (request_type){
                case 4:
                    int status = getIntent().getIntExtra("status",0);
                    if(status == 2){
                        preferences.setVendorStatus(true);
                        UserSession.getInstance().setVendor_status(true);
                    } else {
                        preferences.setVendorStatus(false);
                        UserSession.getInstance().setVendor_status(false);
                    }
                    break;
            }
        } catch ( Exception e){
            default_tab = 1;
        }

        icons = new Icons(this);
        initView();
    }

    private void initView(){

        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setIcon(icons.settingsIcon()));
        tabs.addTab(tabs.newTab().setIcon(icons.waterDropActiveIcon()));
        tabs.addTab(tabs.newTab().setIcon(icons.receiptIcon()));
        tabs.addOnTabSelectedListener(new OnTabsSelected());

        pages = (ViewPager) findViewById(R.id.pages);
        waterDropPagesAdapter = new WaterDropPagesAdapter(getSupportFragmentManager(),tabs.getTabCount());
        pages.setAdapter(waterDropPagesAdapter);
        pages.addOnPageChangeListener(new OnPageChange());


        tabs.getTabAt(default_tab).select();

    }

    private class OnTabsSelected implements TabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            pages.setCurrentItem(tab.getPosition(),true);
            OrderTabStateChangeIndicator(tab.getPosition());
            waterDropPagesAdapter.notifyDataSetChanged();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }

    private class OnPageChange implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            tabs.getTabAt(position).select();
            OrderTabStateChangeIndicator(position);
            waterDropPagesAdapter.notifyDataSetChanged();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public void OrderTabStateChangeIndicator(int position){
        if(position == 1) {
            tabs.getTabAt(1).setIcon(icons.waterDropActiveIcon());
        } else {
            tabs.getTabAt(1).setIcon(icons.waterDropInActiveIcon());
        }
    }

}