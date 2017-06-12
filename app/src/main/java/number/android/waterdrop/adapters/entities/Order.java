package number.android.waterdrop.adapters.entities;

/***
 * Created by user on 9/4/2016.
 ***/
public class Order {
    private int id;
    private long order_id;
    private String vendor;
    private long vendor_id;
    private String date;
    private int quantity;
    private int status;

    public Order() {
    }

    public Order(int id, long order_id, String vendor, long vendor_id, String date, int quantity, int status) {
        this.id = id;
        this.order_id = order_id;
        this.vendor = vendor;
        this.vendor_id = vendor_id;
        this.date = date;
        this.quantity = quantity;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public long getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(long vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
