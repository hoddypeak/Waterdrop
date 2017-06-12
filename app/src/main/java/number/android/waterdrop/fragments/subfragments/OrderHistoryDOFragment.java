package number.android.waterdrop.fragments.subfragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import number.android.waterdrop.R;
import number.android.waterdrop.activities.entities.UserSession;
import number.android.waterdrop.adapters.DeliveredOrdersListAdapter;
import number.android.waterdrop.adapters.entities.Order;
import number.android.waterdrop.cloud.entities.request.OrdersReq;
import number.android.waterdrop.cloud.entities.response.OrdersRes;
import number.android.waterdrop.cloud.rest.Callbacks;
import number.android.waterdrop.cloud.rest.Client;
import number.android.waterdrop.utilities.DateTime;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Delivered orders list
 */
public class OrderHistoryDOFragment extends Fragment {

    View view;

    ListView delivered_orders;

    Client client;

    ArrayList<Order> orders = new ArrayList<>();

    DeliveredOrdersListAdapter deliveredOrdersListAdapter;
    View emptyView;
    WaveSwipeRefreshLayout mWSRefresh;

    public OrderHistoryDOFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_history_do, container, false);
        initView();
        return view;
    }

    public void initView(){
        mWSRefresh = (WaveSwipeRefreshLayout) view.findViewById(R.id.refresh);
        mWSRefresh.setWaveColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        mWSRefresh.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.white));
        mWSRefresh.setShadowRadius(2);

        delivered_orders = (ListView) view.findViewById(R.id.delivered_orders);
        emptyView = view.findViewById(android.R.id.empty);
        delivered_orders.setEmptyView(emptyView);

        bindAdapter();
        setWaveSwipeRefreshLayout();
    }

    public void bindAdapter(){
        loadOrdersFromCloud();
        deliveredOrdersListAdapter = new DeliveredOrdersListAdapter(getActivity(), orders);
        delivered_orders.setAdapter(deliveredOrdersListAdapter);
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



    void loadOrdersFromCloud(){
        OrdersReq ordersReq = new OrdersReq();
        ordersReq.setUserId(UserSession.getInstance().getUser_id());
        ordersReq.setLastUpdatedDatetime("");

        client = new Client();
        Call<OrdersRes> call = client.get().OrderList(ordersReq);
        call.enqueue(new Callbacks<OrdersRes>() {
            @Override
            public void onResponse(Call<OrdersRes> call, Response<OrdersRes> ordersRes) {

                if (ordersRes.code() == 200) {
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
        orders.clear();
        for (OrdersRes.Order order : ordersRes.getOrders()) {
            if(order.getStatus() == 3) {
                Order listItem = new Order();
                listItem.setId(order.getId());
                listItem.setVendor_id(order.getVendorId());
                listItem.setQuantity(order.getQuantity());
                listItem.setStatus(order.getStatus());
                listItem.setDate(DateTime.ServerFormatToCustom(order.getLastUpdatedDatetime(),"d MMM"));
                orders.add(listItem);
            }
        }
        // update vendor list
        deliveredOrdersListAdapter.notifyDataSetChanged();


    }



}
