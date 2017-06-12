package number.android.waterdrop.cloud.entities.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class UpdateOrderStatusReq {

    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("order_id")
    @Expose
    public Integer orderId;
    @SerializedName("status")
    @Expose
    public Integer status;

    /**
     * No args constructor for use in serialization
     *
     */
    public UpdateOrderStatusReq() {
    }

    /**
     *
     * @param status
     * @param userId
     * @param orderId
     */
    public UpdateOrderStatusReq(Integer userId, Integer orderId, Integer status) {
        this.userId = userId;
        this.orderId = orderId;
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}