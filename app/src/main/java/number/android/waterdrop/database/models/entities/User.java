package number.android.waterdrop.database.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 9/17/2016.
 */
public class User {
    private int id;
    private String name;
    private String mobile_number;
    private String address;
    private String landmark;
    private int rmt_address_id;
    private UserLocation userLocation;


    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public int getRmt_address_id() {
        return rmt_address_id;
    }

    public void setRmt_address_id(int rmt_address_id) {
        this.rmt_address_id = rmt_address_id;
    }

    public UserLocation getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(UserLocation userLocation) {
        this.userLocation = userLocation;
    }

    public static class UserLocation {

        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("street")
        @Expose
        private String street;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("postal_code")
        @Expose
        private String postalCode;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("landmark")
        @Expose
        private String landmark;

        /**
         * No args constructor for use in serialization
         */
        public UserLocation() {
        }

        /**
         * @param landmark
         * @param postalCode
         * @param address
         * @param street
         * @param state
         * @param longitude
         * @param latitude
         * @param country
         * @param city
         */
        public UserLocation(String latitude, String longitude, String street, String city, String state, String country, String postalCode, String address, String landmark) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.street = street;
            this.city = city;
            this.state = state;
            this.country = country;
            this.postalCode = postalCode;
            this.address = address;
            this.landmark = landmark;
        }

        /**
         * @return The latitude
         */
        public String getLatitude() {
            return latitude;
        }

        /**
         * @param latitude The latitude
         */
        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        /**
         * @return The longitude
         */
        public String getLongitude() {
            return longitude;
        }

        /**
         * @param longitude The longitude
         */
        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        /**
         * @return The street
         */
        public String getStreet() {
            return street;
        }

        /**
         * @param street The street
         */
        public void setStreet(String street) {
            this.street = street;
        }

        /**
         * @return The city
         */
        public String getCity() {
            return city;
        }

        /**
         * @param city The city
         */
        public void setCity(String city) {
            this.city = city;
        }

        /**
         * @return The state
         */
        public String getState() {
            return state;
        }

        /**
         * @param state The state
         */
        public void setState(String state) {
            this.state = state;
        }

        /**
         * @return The country
         */
        public String getCountry() {
            return country;
        }

        /**
         * @param country The country
         */
        public void setCountry(String country) {
            this.country = country;
        }

        /**
         * @return The postalCode
         */
        public String getPostalCode() {
            return postalCode;
        }

        /**
         * @param postalCode The postal_code
         */
        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        /**
         * @return The address
         */
        public String getAddress() {
            return address;
        }

        /**
         * @param address The address
         */
        public void setAddress(String address) {
            this.address = address;
        }

        /**
         * @return The landmark
         */
        public String getLandmark() {
            return landmark;
        }

        /**
         * @param landmark The landmark
         */
        public void setLandmark(String landmark) {
            this.landmark = landmark;
        }

    }

}
