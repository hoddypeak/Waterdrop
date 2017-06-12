package number.android.waterdrop.cloud.entities.response;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class OrdersRes {

    @SerializedName("orders")
    @Expose
    public List<Order> orders = new ArrayList<Order>();

    /**
     * No args constructor for use in serialization
     *
     */
    public OrdersRes() {
    }

    /**
     *
     * @param orders
     */
    public OrdersRes(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static class Order {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("vendor_id")
        @Expose
        public Integer vendorId;
        @SerializedName("quantity")
        @Expose
        public Integer quantity;
        @SerializedName("created_on")
        @Expose
        public String createdOn;
        @SerializedName("last_updated_datetime")
        @Expose
        public String lastUpdatedDatetime;
        @SerializedName("status")
        @Expose
        public Integer status;

        /**
         * No args constructor for use in serialization
         *
         */
        public Order() {
        }

        /**
         *
         * @param createdOn
         * @param id
         * @param status
         * @param lastUpdatedDatetime
         * @param quantity
         * @param vendorId
         */
        public Order(Integer id, Integer vendorId, Integer quantity, String createdOn, String lastUpdatedDatetime, Integer status) {
            this.id = id;
            this.vendorId = vendorId;
            this.quantity = quantity;
            this.createdOn = createdOn;
            this.lastUpdatedDatetime = lastUpdatedDatetime;
            this.status = status;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
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

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }

        public String getLastUpdatedDatetime() {
            return lastUpdatedDatetime;
        }

        public void setLastUpdatedDatetime(String lastUpdatedDatetime) {
            this.lastUpdatedDatetime = lastUpdatedDatetime;
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

}