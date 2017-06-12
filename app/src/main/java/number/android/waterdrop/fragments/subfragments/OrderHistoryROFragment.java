package number.android.waterdrop.fragments.subfragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import number.android.waterdrop.R;
import number.android.waterdrop.activities.entities.UserSession;
import number.android.waterdrop.adapters.DeliveredOrdersListAdapter;
import number.android.waterdrop.adapters.RecentOrdersListAdapter;
import number.android.waterdrop.adapters.entities.Order;
import number.android.waterdrop.cloud.entities.request.OrdersReq;
import number.android.waterdrop.cloud.entities.request.UpdateOrderStatusReq;
import number.android.waterdrop.cloud.entities.response.OrderRes;
import number.android.waterdrop.cloud.entities.response.OrdersRes;
import number.android.waterdrop.cloud.entities.response.UpdateOrderStatusRes;
import number.android.waterdrop.cloud.rest.Callbacks;
import number.android.waterdrop.cloud.rest.Client;
import number.android.waterdrop.utilities.DateTime;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderHistoryROFragment extends Fragment implements View.OnClickListener {


    View view;

    ListView recent_orders;
    ArrayList<Order> resentOrders = new ArrayList<>();
    RecentOrdersListAdapter recentOrdersListAdapter;
    Client client;

    WaveSwipeRefreshLayout mWSRefresh;

    View emptyView;

    public OrderHistoryROFragment() {
        // Required empty public constructor
    }

    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_history_ro, container, false);
        activity = getActivity();
        initView();
        return view;
    }

    public void initView(){
        mWSRefresh = (WaveSwipeRefreshLayout) view.findViewById(R.id.refresh);
        mWSRefresh.setWaveColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        mWSRefresh.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.white));
        mWSRefresh.setShadowRadius(2);

        recent_orders = (ListView) view.findViewById(R.id.recent_orders);
        emptyView = view.findViewById(android.R.id.empty);
        recent_orders.setEmptyView(emptyView);
        bindAdapter();
        setWaveSwipeRefreshLayout();
    }

    public void bindAdapter(){
        loadOrdersFromCloud();

        recentOrdersListAdapter = new RecentOrdersListAdapter(getActivity(),resentOrders);
        recent_orders.setAdapter(recentOrdersListAdapter);
    }

    void setOnClickListenerForListButton(){
        int n = recent_orders.getLastVisiblePosition() - recent_orders.getFirstVisiblePosition();
        for(int i = 0; i < recent_orders.getChildCount(); i++){
            Button button = (Button) recent_orders.getChildAt(i).findViewById(R.id.confirm_delivery);
            button.setOnClickListener(this);
        }
    }

    void loadOrdersFromCloud(){
        OrdersReq ordersReq = new OrdersReq();
        ordersReq.setUserId(UserSession.getInstance().getUser_id());
        ordersReq.setLastUpdatedDatetime("");

        client = new Client();
        Call<OrdersRes> call = client.get().OrderList(ordersReq);
        call.enqueue(new Callbacks<OrdersRes>() {
            @Override
            public void onResponse(Call<OrdersRes> call, Response<OrdersRes> ordersRes) {
                if(ordersRes.code() == 200) {
                    Log.i("Response", ordersRes.body().toString());
                    processResponse(ordersRes.body());
                }
                stopRefresh();
            }

            @Override
            public void onFailure(Call<OrdersRes> call, Throwable t) {
                stopRefresh();
            }
        });
    }

    /**
     *
     * @param ordersRes
     */
    void processResponse(OrdersRes ordersRes){
        resentOrders.clear();
        for (OrdersRes.Order order : ordersRes.getOrders()) {
            if(order.getStatus() < 3) {
                Order listItem = new Order();
                listItem.setId(order.getId());
                listItem.setQuantity(order.getQuantity());
                listItem.setVendor_id(order.getVendorId());
                listItem.setStatus(order.getStatus());
                listItem.setDate(DateTime.ServerFormatToCustom(order.getCreatedOn(),"d MMM"));
                resentOrders.add(listItem);
            }
        }

        // update orders list
        recentOrdersListAdapter.notifyDataSetChanged();
        // open orders count
        UserSession.getInstance().setOpen_order_count( resentOrders.size() );
        //setOnClickListenerForListButton();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_delivery:
                Toast.makeText(getActivity(),"Clicked",Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void setWaveSwipeRefreshLayout(){
        mWSRefresh.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                loadOrdersFromCloud();
            }
        });
    }

    public void stopRefresh(){
        if(mWSRefresh.isRefreshing()){
            mWSRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mWSRefresh.setRefreshing(false);
                    //Toast.makeText(getActivity(),"Done",Toast.LENGTH_SHORT).show();
                }
            },1000);
        }
    }


}
