package model;

import java.io.Serializable;

/**
 * Created by Rajesh on 2017-09-22.
 */

public class My_prescription_model implements Serializable {

    private String pres_id;
    private String sale_id;
    private String user_id;
    private String on_date;
    private String on_time;
    private String prescription_img1;
    private String prescription_img2;
    private String prescription_img3;
    private String status;
    private String zipcode_id;
    private String delivery_address;
    private String delivery_id;
    private String delivery_user_id;
    private String delivery_zipcode;
    private String delivery_landmark;
    private String delivery_fullname;
    private String delivery_mobilenumber;
    private String delivery_city;
    private String delivery_charge;


    public String getPres_id() {
        return pres_id;
    }

    public String getSale_id() {
        return sale_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getOn_date() {
        return on_date;
    }

    public String getOn_time() {
        return on_time;
    }

    public String getPrescription_img1() {
        return prescription_img1;
    }

    public String getPrescription_img2() {
        return prescription_img2;
    }

    public String getPrescription_img3() {
        return prescription_img3;
    }

    public String getStatus() {
        return status;
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

    public String getDelivery_user_id() {
        return delivery_user_id;
    }

    public String getDelivery_zipcode() {
        return delivery_zipcode;
    }

    public String getDelivery_landmark() {
        return delivery_landmark;
    }

    public String getDelivery_fullname() {
        return delivery_fullname;
    }

    public String getDelivery_mobilenumber() {
        return delivery_mobilenumber;
    }

    public String getDelivery_city() {
        return delivery_city;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

}
