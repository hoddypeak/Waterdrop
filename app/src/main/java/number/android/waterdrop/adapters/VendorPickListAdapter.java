package number.android.waterdrop.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import number.android.waterdrop.R;
import number.android.waterdrop.adapters.entities.Vendor;

/**
 * Created by user on 8/25/2016.
 */
public class VendorPickListAdapter extends BaseAdapter {

    Context context;
    List<Vendor> vendors;

    private int mSelectedPosition = -1;
    private RadioButton mSelectedRB;

    public VendorPickListAdapter(Context context, List<Vendor> vendors) {
        this.context = context;
        this.vendors = vendors;
    }

    @Override
    public int getCount() {
        return vendors.size();
    }

    @Override
    public Vendor getItem(int position) {
        return vendors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        ViewHolder viewHolder = null;
        Vendor vendor = getItem(position);
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.vendorpick_list_item, null);
            viewHolder = new ViewHolder(view);
            viewHolder.name.setText(vendor.getName());
            viewHolder.mobile_number.setText(vendor.getMobile_number());
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.option.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(position != mSelectedPosition && mSelectedRB != null){
                    mSelectedRB.setChecked(false);
                }
                mSelectedPosition = position;
                mSelectedRB = (RadioButton)v;
            }
        });


        if(mSelectedPosition != position){
            viewHolder.option.setChecked(false);
        } else {
            viewHolder.option.setChecked(true);
            if(mSelectedRB != null && viewHolder.option != mSelectedRB){
                mSelectedRB = viewHolder.option;
            }
        }

        //viewHolder.name.setText(getItem(position));

        Log.i("id", String.valueOf(vendor.getId()));
        Log.i("position", String.valueOf(position));

        return view;
    }

    public Vendor getmSelectedVendor() {
        return vendors.get(mSelectedPosition);
    }

    private class ViewHolder {

        TextView name,mobile_number;
        RadioButton option;

        ViewHolder(View row) {
            this.option = (RadioButton) row.findViewById(R.id.option);
            this.name = (TextView) row.findViewById(R.id.vendor_name);
            this.mobile_number = (TextView) row.findViewById(R.id.vendor_mobile_number);
        }
    }
}
