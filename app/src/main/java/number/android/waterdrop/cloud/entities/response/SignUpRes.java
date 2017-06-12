package number.android.waterdrop.cloud.entities.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by user on 10/9/2016.
 */
public class SignUpRes {
    @SerializedName("user_id")
    @Expose
    public Integer userId;

    @SerializedName("user_code")
    @Expose
    public String userCode;

    /**
     * No args constructor for use in serialization
     *
     */
    public SignUpRes() {
    }

    /**
     *
     * @param userCode
     * @param userId
     */
    public SignUpRes(Integer userId, String userCode) {
        this.userId = userId;
        this.userCode = userCode;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
