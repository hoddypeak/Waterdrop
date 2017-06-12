package number.android.waterdrop.cloud.entities.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;


public class SearchVendorReq {
    @SerializedName("mobile")
    @Expose
    public String mobile;

    /**
     * No args constructor for use in serialization
     *
     */
    public SearchVendorReq() {
    }

    /**
     *
     * @param mobile
     */
    public SearchVendorReq(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
