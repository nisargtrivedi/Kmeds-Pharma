package Config;

/**
 * Created by Rajesh on 2017-09-04.
 */

public class BaseURL {

    //hear you can change paypal currency
    public static final String PAYPAL_CURRENCY = "INR";

    //hear you can change app url
    // Note Please keep Slash "/" at end url.
    public static String BASE_URL = "http://kmedspharma.co.in/Admin_Panel/";


    public static String REGISTER_URL = BASE_URL + "index.php/rest/user/register";

    public static String LOGIN_URL = BASE_URL + "index.php/rest/user/login";

    public static String GET_SLIDER_URL = BASE_URL + "index.php/rest/slider/list";

    public static String GET_MEDICAL_CATEGORIES_URL = BASE_URL + "index.php/rest/categories/list_medical_categories";

    public static String GET_PRESCRIPTIONS_URL = BASE_URL + "index.php/rest/categories/list_prescriptions_post";

    public static String GET_PRODUCT_URL = BASE_URL + "index.php/rest/Product/list";

    public static String FORGOT_PASSWORD_URL = BASE_URL + "index.php/rest/user/forgotpassword";

    public static String CHANGE_PASSWORD_URL = BASE_URL + "index.php/rest/user/changepass";

    public static String EDIT_PROFILE_IMG_URL = BASE_URL + "index.php/rest/user/updatepicture";

    public static String EDIT_PROFILE_URL = BASE_URL + "index.php/rest/user/updateprofile";

    public static String CHECK_ZIPCODE_URL = BASE_URL + "index.php/rest/zipcode/zipcode";

    public static String ADD_DELIVERY_ADDRESS_URL = BASE_URL + "index.php/rest/user/add_delivery_address";

    public static String GET_DELIVERY_ADDRESS_URL = BASE_URL + "index.php/rest/user/user_delivery_address";

    public static String EDIT_DELIVERY_ADDRESS_URL = BASE_URL + "index.php/rest/user/edit_delivery_address";

    public static String DELETE_DELIVERY_ADDRESS_URL = BASE_URL + "index.php/rest/user/delete_delivery_address";

    public static String GET_PRESCRIPTIONS_CATEGORY_URL = BASE_URL + "index.php/rest/categories/list_prescriptions_categories";

    public static String GET_OFFERS_URL = BASE_URL + "index.php/rest/offer/list";

    public static String CHECK_OFFERS_URL = BASE_URL + "index.php/rest/offer/offer_check";

    public static String SEND_ORDER_URL = BASE_URL + "index.php/rest/order/send_order";

    public static String GET_MY_ORDER_URL = BASE_URL + "index.php/rest/order/my_orders";

    public static String REPEAT_MY_ORDER_URL = BASE_URL + "api/repeat_order.php";

    public static String PLACE_ENQUIRY = BASE_URL + "api/place_enquiry.php";

    public static String LIST_ENQUIRY = BASE_URL + "api/list_enquiry.php";

    public static String GET_ORDER_DETAIL_URL = BASE_URL + "index.php/rest/order/order_details";

    public static String CANCEL_ORDER_URL = BASE_URL + "index.php/rest/order/cancel_order";

    public static String SEND_ORDER_PRESCRIPTION_URL = BASE_URL + "index.php/rest/order/send_order_prescription";

    public static String GET_MY_PRESCRIPTION_URL = BASE_URL + "index.php/rest/order/my_prescription";

    public static String GET_SUGGEST_URL = BASE_URL + "index.php/rest/product/suggest";

    public static String GET_SUGGEST_DETAILS_URL = BASE_URL + "index.php/rest/product/suggest_details_page";

    public static String GET_PAYPAL_STATUS_URL = BASE_URL + "index.php/rest/paypal/paypal_status";

    public static String GET_PRESCRIPTION_ITEMS_URL = BASE_URL + "index.php/rest/order/prescription_items";

    public static String REGISTER_FCM_URL = BASE_URL + "index.php/rest/user/register_fcm";

    public static String GET_NOTIFICATION_URL = BASE_URL + "index.php/rest/notification/list";

    public static String GET_TERMS_OF_SALE_URL = BASE_URL + "terms_of_sale.html";

    public static String ACCEPT_PRESCRIPTION_ORDER_URL = BASE_URL + "index.php/rest/order/accept_prescription_order";


    public static String IMG_SLIDER_URL = BASE_URL + "uploads/slider/";

    public static String IMG_PROFILE_URL = BASE_URL + "uploads/profile/";

    public static String IMG_CATEGORY_URL = BASE_URL + "uploads/category/";

    public static String IMG_PRODUCT_URL = BASE_URL + "uploads/products/";

    public static String IMG_PRESCRIPTION_URL = BASE_URL + "uploads/prescription/";

    public static String IMG_NOTIFICATION_URL = BASE_URL + "uploads/notification/";


    public static final String PREFS_NAME = "GetPillsLoginPrefs";

    public static final String IS_LOGIN = "isLogin";

    public static final String KEY_NAME = "user_fullname";

    public static final String KEY_EMAIL = "user_email";

    public static final String KEY_ID = "user_id";

    public static final String KEY_TYPE_ID = "user_type_id";

    public static final String KEY_MOBILE = "user_phone";

    public static final String KEY_BDATE = "user_bdate";

    public static final String KEY_IMAGE = "user_image";

    public static final String KEY_ADDRESS = "user_address";

    public static final String KEY_GENDER = "user_gender";

    public static final String KEY_CITY = "user_city";


}
