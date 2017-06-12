package number.android.waterdrop.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;
import number.android.waterdrop.R;
import number.android.waterdrop.activities.entities.UserSession;
import number.android.waterdrop.adapters.entities.Order;
import number.android.waterdrop.cloud.entities.request.UpdateOrderStatusReq;
import number.android.waterdrop.cloud.entities.response.UpdateOrderStatusRes;
import number.android.waterdrop.cloud.rest.Callbacks;
import number.android.waterdrop.cloud.rest.Client;
import number.android.waterdrop.fragments.subfragments.OrderHistoryROFragment;
import number.android.waterdrop.utilities.Icons;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by user on 8/25/2016.
 */
public class RecentOrdersListAdapter extends BaseAdapter {

    Context context;
    ArrayList<Order> recentOrders;

    Icons icons;

    public RecentOrdersListAdapter(Context context, ArrayList<Order> recentOrders) {
        this.context = context;
        this.recentOrders = recentOrders;
        icons = new Icons(context);
    }

    @Override
    public int getCount() {
        return recentOrders.size();
    }

    @Override
    public Order getItem(int position) {
        return recentOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.recent_order_list_item, null);
        }

        ViewHolder viewHolder = new ViewHolder(view);

        final Order recentOrder = getItem(position);

        String quantity = String.format("%d Cans",recentOrder.getQuantity());
        viewHolder.quantity.setText(quantity);
        viewHolder.date.setText(recentOrder.getDate());
        if(recentOrder.getStatus() == 1){
            viewHolder.confirm_delivery.setEnabled(false);
        } else {
            viewHolder.confirm_delivery.setEnabled(true);
        }
        viewHolder.confirm_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrderStatus(recentOrder.getId(),position);
            }
        });

        return view;
    }

    private class ViewHolder {

        TextView quantity,date;
        FancyButton confirm_delivery;

        ViewHolder(View row) {
            this.quantity = (TextView) row.findViewById(R.id.quantity);
            this.date = (TextView) row.findViewById(R.id.date);
            this.confirm_delivery = (FancyButton) row.findViewById(R.id.confirm_delivery);
            this.confirm_delivery.setIconResource(icons.tickIcon());
        }
    }

    public void updateOrderStatus(int order_id, final int position){

        UpdateOrderStatusReq updateOrderStatusReq = new UpdateOrderStatusReq();
        updateOrderStatusReq.setUserId(UserSession.getInstance().getUser_id());
        updateOrderStatusReq.setOrderId(order_id);
        updateOrderStatusReq.setStatus(3);

        Toast.makeText(context,"Updating order status...",Toast.LENGTH_LONG).show();

        Client client = new Client();
        Call<UpdateOrderStatusRes> call = client.get().UpdateOrderStatus(updateOrderStatusReq);
        call.enqueue(new Callbacks<UpdateOrderStatusRes>() {
            @Override
            public void onResponse(Call<UpdateOrderStatusRes> call, Response<UpdateOrderStatusRes> orderResResponse) {
                if(orderResResponse.code() == 200) {
                    processResponse(orderResResponse.body().getMessage(),position);
                } else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void processResponse(String message,int position){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
        recentOrders.get(position).setStatus(3);
        recentOrders.remove(position);
        this.notifyDataSetChanged();
    }
}
