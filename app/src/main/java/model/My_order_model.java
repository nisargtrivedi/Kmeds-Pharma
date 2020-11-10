package model;

/**
 * Created by Rajesh on 2017-09-18.
 */

public class My_order_model {

    private String sale_id;
    private String user_id;
    private String on_date;
    private String status;
    private String note;
    private String payment_type;
    private String payment_ref;
    private String payment_paypal_amount;
    private String total_amount;
    private String total_items;
    private String zipcode_id;
    private String delivery_address;
    private String delivery_id;
    private String delivery_charge;
    private String offer_coupon;
    private String offer_discount;


    public String getSale_id() {
        return sale_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getOn_date() {
        return on_date;
    }

    public String getStatus() {
        return status;
    }

    public String getNote() {
        return note;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public String getPayment_ref() {
        return payment_ref;
    }

    public String getPayment_paypal_amount() {
        return payment_paypal_amount;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public String getTotal_items() {
        return total_items;
    }

    public String getZipcode_id() {
        return zipcode_id;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public String getDelivery_id() {
        return delivery_id;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public String getOffer_coupon() {
        return offer_coupon;
    }

    public String getOffer_discount() {
        return offer_discount;
    }

}
