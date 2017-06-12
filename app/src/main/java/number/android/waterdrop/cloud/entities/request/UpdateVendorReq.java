package number.android.waterdrop.cloud.entities.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class UpdateVendorReq {

    @SerializedName("user_id")
    @Expose
    public Integer userId;

    @SerializedName("vendor_id")
    @Expose
    public Integer vendorId;

    /**
     * No args constructor for use in serialization
     *
     */
    public UpdateVendorReq() {
    }

    /**
     *
     * @param userId
     * @param vendorId
     */
    public UpdateVendorReq(Integer userId, Integer vendorId) {
        this.userId = userId;
        this.vendorId = vendorId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
