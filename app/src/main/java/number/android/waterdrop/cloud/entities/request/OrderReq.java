package number.android.waterdrop.cloud.entities.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class OrderReq {

    @SerializedName("user_id")
    @Expose
    public Integer userId;

    @SerializedName("vendor_id")
    @Expose
    public Integer vendorId;

    @SerializedName("quantity")
    @Expose
    public Integer quantity;

    @SerializedName("product_id")
    @Expose
    public Integer productId;

    /**
     * No args constructor for use in serialization
     *
     */
    public OrderReq() {
    }

    /**
     *
     * @param userId
     * @param quantity
     * @param vendorId
     * @param productId
     */
    public OrderReq(Integer userId, Integer vendorId, Integer quantity, Integer productId) {
        this.userId = userId;
        this.vendorId = vendorId;
        this.quantity = quantity;
        this.productId = productId;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}