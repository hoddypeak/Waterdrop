
package number.android.waterdrop.cloud.entities.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpReq {

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("user_location")
    @Expose
    private UserLocation userLocation;
    @SerializedName("user_device")
    @Expose
    private UserDevice userDevice;

    /**
     * No args constructor for use in serialization
     */
    public SignUpReq() {
    }

    /**
     * @param userDevice
     * @param userLocation
     * @param user
     */
    public SignUpReq(User user, UserLocation userLocation, UserDevice userDevice) {
        this.user = user;
        this.userLocation = userLocation;
        this.userDevice = userDevice;
    }

    /**
     * @return The user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user The user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return The userLocation
     */
    public UserLocation getUserLocation() {
        return userLocation;
    }

    /**
     * @param userLocation The user_location
     */
    public void setUserLocation(UserLocation userLocation) {
        this.userLocation = userLocation;
    }

    /**
     * @return The userDevice
     */
    public UserDevice getUserDevice() {
        return userDevice;
    }

    /**
     * @param userDevice The user_device
     */
    public void setUserDevice(UserDevice userDevice) {
        this.userDevice = userDevice;
    }

//    @Override
//    public String toString() {
//        return ToStringBuilder.reflectionToString(this);
//    }


    public static class User {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("mobile_number")
        @Expose
        private String mobileNumber;

        /**
         * No args constructor for use in serialization
         */
        public User() {
        }

        /**
         * @param name
         * @param mobileNumber
         */
        public User(String name, String mobileNumber) {
            this.name = name;
            this.mobileNumber = mobileNumber;
        }

        /**
         * @return The name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name The name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return The mobileNumber
         */
        public String getMobileNumber() {
            return mobileNumber;
        }

        /**
         * @param mobileNumber The mobile_number
         */
        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

//    @Override
//    public String toString() {
//        return ToStringBuilder.reflectionToString(this);
//    }

    }

    public static class UserDevice {

        @SerializedName("device_token")
        @Expose
        private String deviceToken;
        @SerializedName("device_os")
        @Expose
        private String deviceOs;
        @SerializedName("device_os_version")
        @Expose
        private String deviceOsVersion;

        /**
         * No args constructor for use in serialization
         */
        public UserDevice() {
        }

        /**
         * @param deviceOsVersion
         * @param deviceToken
         * @param deviceOs
         */
        public UserDevice(String deviceToken, String deviceOs, String deviceOsVersion) {
            this.deviceToken = deviceToken;
            this.deviceOs = deviceOs;
            this.deviceOsVersion = deviceOsVersion;
        }

        /**
         * @return The deviceToken
         */
        public String getDeviceToken() {
            return deviceToken;
        }

        /**
         * @param deviceToken The device_token
         */
        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        /**
         * @return The deviceOs
         */
        public String getDeviceOs() {
            return deviceOs;
        }

        /**
         * @param deviceOs The device_os
         */
        public void setDeviceOs(String deviceOs) {
            this.deviceOs = deviceOs;
        }

        /**
         * @return The deviceOsVersion
         */
        public String getDeviceOsVersion() {
            return deviceOsVersion;
        }

        /**
         * @param deviceOsVersion The device_os_version
         */
        public void setDeviceOsVersion(String deviceOsVersion) {
            this.deviceOsVersion = deviceOsVersion;
        }

//    @Override
//    public String toString() {
//        return ToStringBuilder.reflectionToString(this);
//    }

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