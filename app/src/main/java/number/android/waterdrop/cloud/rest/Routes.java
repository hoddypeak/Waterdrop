package number.android.waterdrop.cloud.rest;


import number.android.waterdrop.cloud.entities.request.OrderReq;
import number.android.waterdrop.cloud.entities.request.OrdersReq;
import number.android.waterdrop.cloud.entities.request.SearchVendorReq;
import number.android.waterdrop.cloud.entities.request.SignUpReq;
import number.android.waterdrop.cloud.entities.request.UpdateOrderStatusReq;
import number.android.waterdrop.cloud.entities.request.UpdateUserReq;
import number.android.waterdrop.cloud.entities.request.UpdateVendorReq;
import number.android.waterdrop.cloud.entities.request.VendorsReq;
import number.android.waterdrop.cloud.entities.response.OrderRes;
import number.android.waterdrop.cloud.entities.response.OrdersRes;
import number.android.waterdrop.cloud.entities.response.SignUpRes;
import number.android.waterdrop.cloud.entities.response.UpdateOrderStatusRes;
import number.android.waterdrop.cloud.entities.response.UpdateUserRes;
import number.android.waterdrop.cloud.entities.response.UpdateVendorRes;
import number.android.waterdrop.cloud.entities.response.VendorsRes;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Routes {

    @POST("user/signup")
    Call<SignUpRes> SignUp(@Body SignUpReq signUpReq);

    @POST("vendor/vendorlist")
    Call<VendorsRes> Vendors(@Body VendorsReq vendorsReq);

    @POST("order/placeorder")
    Call<OrderRes> Order(@Body OrderReq orderReq);

    @POST("order/orderlist")
    Call<OrdersRes> OrderList(@Body OrdersReq ordersReq);

    @POST("user/changevendor")
    Call<UpdateVendorRes> UpdateVendor(@Body UpdateVendorReq updateVendorReq);

    @POST("vendor/vendorsearch")
    Call<VendorsRes> SearchVendor(@Body SearchVendorReq searchVendorReq);

    @POST("order/orderstatus")
    Call<UpdateOrderStatusRes> UpdateOrderStatus(@Body UpdateOrderStatusReq updateOrderStatusReq);

    @POST("user/update")
    Call<UpdateUserRes> UpdateUserDetails(@Body UpdateUserReq updateUserReq);

}
