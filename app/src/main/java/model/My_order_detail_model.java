package model;

import java.io.Serializable;

/**
 * Created by Rajesh on 2017-09-19.
 */

public class My_order_detail_model implements Serializable {

    private String sale_item_id;
    private String sale_id;
    private String product_id;
    private String product_name;
    private String qty;
    private String unit;
    private String unit_value;
    private String price;
    private String product_image;
    private String discount;

    public String getSale_item_id() {
        return sale_item_id;
    }

    public String getSale_id() {
        return sale_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getQty() {
        return qty;
    }

    public String getUnit() {
        return unit;
    }

    public String getUnit_value() {
        return unit_value;
    }

    public String getPrice() {
        return price;
    }

    public String getProduct_image() {
        return product_image;
    }

    public String getDiscount() {
        return discount;
    }
}
