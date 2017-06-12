package number.android.waterdrop.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import number.android.waterdrop.R;
import number.android.waterdrop.activities.UserProfileActivity;
import number.android.waterdrop.activities.VendorPickActivity;
import number.android.waterdrop.activities.entities.UserSession;
import number.android.waterdrop.adapters.PreferencesListAdapter;
import number.android.waterdrop.adapters.VendorPickListAdapter;
import number.android.waterdrop.adapters.entities.Preference;
import number.android.waterdrop.adapters.entities.Vendor;
import number.android.waterdrop.database.DatabaseConfig;
import number.android.waterdrop.database.models.CustomerModel;
import number.android.waterdrop.utilities.Icons;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreferencesFragment extends Fragment {

    ListView preferences;
    PreferencesListAdapter preferencesListAdapter;

    ArrayList<Preference> preferenceList = new ArrayList<Preference>();

    View view;
    Icons icons;

    public PreferencesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_preferences, container, false);
        icons = new Icons(getActivity());
        initView(view);
        return view;
    }

    private void initView(View view){

        Preference edit_profile = new Preference(1,icons.personIcon(),"Edit profile");
        preferenceList.add(edit_profile);

        Preference change_vendor = new Preference(2,icons.personChangeIcon(), "Change vendor");
        preferenceList.add(change_vendor);

        Preference share = new Preference(3,icons.shareIcon(), "Share app");
        preferenceList.add(share);

        Preference deactivate_account = new Preference(4,icons.trashIcon(), "Deactivate account");
        preferenceList.add(deactivate_account);

        preferences = (ListView) view.findViewById(R.id.preferences);

        preferencesListAdapter = new PreferencesListAdapter(getActivity(),preferenceList);
        preferences.setAdapter(preferencesListAdapter);
        preferences.setOnItemClickListener(new OnPreferenceClickListener());
    }

    class OnPreferenceClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Preference preference = preferencesListAdapter.getItem(position);
            toScreen(preference.getId());
        }
    }

    void toScreen(int id){
        switch (id) {
            case 1:
                Intent intentEditProfile = new Intent(getActivity(), UserProfileActivity.class);
                startActivity(intentEditProfile);
                break;
            case 2:
                Intent intentChangeVendor = new Intent(getActivity(), VendorPickActivity.class);
                startActivity(intentChangeVendor);
                break;
            case 3:
                Intent shareVia=new Intent(android.content.Intent.ACTION_SEND);
                shareVia.setType("text/plain");
                shareVia.putExtra(android.content.Intent.EXTRA_SUBJECT,"Waterdrop");
                shareVia.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=number.android.waterdrop");
                startActivity(Intent.createChooser(shareVia,"Share via"));
                break;
        }
    }

    public boolean getUser(){
        DatabaseConfig databaseConfig = new DatabaseConfig(getActivity());
        CustomerModel customerModel = new CustomerModel(databaseConfig);
        if(customerModel.getUserById(UserSession.getInstance().getUser_id()).getId() != 0) {
            return true;
        } else {
            return false;
        }
    }


}
