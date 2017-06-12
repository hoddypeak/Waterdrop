package number.android.waterdrop.cloud.entities.request;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class OrdersReq {

    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("last_updated_datetime")
    @Expose
    public String lastUpdatedDatetime;

    /**
     * No args constructor for use in serialization
     *
     */
    public OrdersReq() {
    }

    /**
     *
     * @param userId
     * @param lastUpdatedDatetime
     */
    public OrdersReq(Integer userId, String lastUpdatedDatetime) {
        this.userId = userId;
        this.lastUpdatedDatetime = lastUpdatedDatetime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLastUpdatedDatetime() {
        return lastUpdatedDatetime;
    }

    public void setLastUpdatedDatetime(String lastUpdatedDatetime) {
        this.lastUpdatedDatetime = lastUpdatedDatetime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
