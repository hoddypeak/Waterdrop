package number.android.waterdrop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import number.android.waterdrop.R;
import number.android.waterdrop.adapters.entities.Order;

/**
 * Created by user on 8/25/2016.
 */
public class DeliveredOrdersListAdapter extends BaseAdapter {

    Context context;
    ArrayList<Order> orders;

    public DeliveredOrdersListAdapter(Context context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Order getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.delivered_order_list_item, null);
        }

        ViewHolder viewHolder = new ViewHolder(view);

        Order deliveredOrder = getItem(position);

        String quantity = String.format("%d Cans", deliveredOrder.getQuantity());
        viewHolder.quantity.setText(quantity);
        viewHolder.date.setText(deliveredOrder.getDate());


        return view;
    }

    private class ViewHolder {

        TextView quantity,date;

        ViewHolder(View row) {
            this.quantity = (TextView) row.findViewById(R.id.quantity);
            this.date = (TextView) row.findViewById(R.id.date);
        }
    }
}
