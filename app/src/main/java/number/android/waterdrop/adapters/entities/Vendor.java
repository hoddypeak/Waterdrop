package number.android.waterdrop.adapters.entities;

import java.math.BigInteger;

/**
 * Created by user on 8/25/2016.
 */
public class Vendor {
    private int id;
    private String name;
    private String mobile_number;

    public Vendor() {
    }

    public Vendor(int id, String name, String mobile_number) {
        this.id = id;
        this.name = name;
        this.mobile_number = mobile_number;
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
}
