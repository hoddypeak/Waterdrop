package number.android.waterdrop.cloud.entities.response;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;


public class VendorsRes {

    @SerializedName("vendors")
    @Expose
    public List<Vendor> vendors = new ArrayList<Vendor>();

    /**
     * No args constructor for use in serialization
     */
    public VendorsRes() {
    }

    /**
     *
     * @param vendors
     */
    public VendorsRes(List<Vendor> vendors) {
        this.vendors = vendors;
    }

    public List<Vendor> getVendors() {
        return vendors;
    }

    public void setVendors(List<Vendor> vendors) {
        this.vendors = vendors;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


    public static class Vendor {

        @SerializedName("id")
        @Expose
        public Integer id;

        @SerializedName("name")
        @Expose
        public String name;

        @SerializedName("mobile")
        @Expose
        public String mobile;

        @SerializedName("address")
        @Expose
        public String address;

        /**
         * No args constructor for use in serialization
         *
         */
        public Vendor() {
        }

        /**
         *
         * @param id
         * @param name
         * @param mobile
         * @param address
         */
        public Vendor(Integer id, String name, String mobile, String address) {
            this.id = id;
            this.name = name;
            this.mobile = mobile;
            this.address = address;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

    }

}