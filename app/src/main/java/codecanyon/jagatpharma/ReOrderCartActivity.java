package codecanyon.jagatpharma;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Config.BaseURL;
import adapter.Cart_adapter;
import adapter.My_order_detail_adapter;
import model.My_order_detail_model;
import util.CommonAsyTask;
import util.ConnectivityReceiver;
import util.DatabaseHandler;
import util.NameValuePair;
import util.Session_management;

public class ReOrderCartActivity extends CommonAppCompatActivity implements View.OnClickListener {

    private static String TAG = ReOrderCartActivity.class.getSimpleName();

    private RecyclerView rv_cart;
    private EditText et_voucher;
    private TextInputLayout ti_voucher;
    private TextView tv_subtotal, tv_total, tv_discount, tv_total_items, tv_voucher, tv_viewoffers;
    private Button btn_process;

    private DatabaseHandler dbcart;
    private Session_management sessionManagement;

    private String offer_coupon, offer_discount;
    private HashMap<String, String> map = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // intialize user session
        sessionManagement = new Session_management(this);
        // intialize local database
        dbcart = new DatabaseHandler(this);

        tv_subtotal = (TextView) findViewById(R.id.tv_cart_sub_total);
        tv_total_items = (TextView) findViewById(R.id.tv_cart_total_items);
        tv_total = (TextView) findViewById(R.id.tv_cart_total);
        tv_discount = (TextView) findViewById(R.id.tv_cart_discount);
        btn_process = (Button) findViewById(R.id.btn_cart_process);
        et_voucher = (EditText) findViewById(R.id.et_cart_voucher);
        ti_voucher = (TextInputLayout) findViewById(R.id.ti_cart_voucher);
        tv_voucher = (TextView) findViewById(R.id.tv_cart_check_voucher);
        tv_viewoffers = (TextView) findViewById(R.id.tv_cart_view_offers);
        rv_cart = (RecyclerView) findViewById(R.id.rv_cart);

        rv_cart.setLayoutManager(new LinearLayoutManager(this));

        // false recyclerview nested scrollin. so user can scroll wall screen
        rv_cart.setNestedScrollingEnabled(false);

        //Get Item Data from Server
        final String sale_id = getIntent().getStringExtra("sale_id");
        if (ConnectivityReceiver.isConnected()) {
            makeGetMyOrderDetail(sale_id);
        } else {
            // display snackbar in activity
            ConnectivityReceiver.showSnackbar(this);
        }





        btn_process.setOnClickListener(this);
        tv_voucher.setOnClickListener(this);
        tv_viewoffers.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.tv_cart_check_voucher) {
            ti_voucher.setError(null);

            String getcode = et_voucher.getText().toString();

            if (sessionManagement.isLoggedIn()) {
                if (!TextUtils.isEmpty(getcode)) {

                    // check internet connection is available or not
                    if (ConnectivityReceiver.isConnected()) {
                        String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
                        makeCheckOffer(user_id, getcode);
                    } else {
                        // show snackbar in activity
                        ConnectivityReceiver.showSnackbar(ReOrderCartActivity.this);
                    }
                } else {
                    ti_voucher.setError(getResources().getString(R.string.please_enter_valid_voucher_code));
                }
            } else {
                Intent loginIntent = new Intent(ReOrderCartActivity.this, LoginActivity.class);
                loginIntent.putExtra("setfinish", "true");
                startActivity(loginIntent);
            }
        } else if (id == R.id.btn_cart_process) {
            if (sessionManagement.isLoggedIn()) {
                // start confirm detail activity
                Intent i = new Intent(ReOrderCartActivity.this, Confirm_detailActivity.class);
                i.putExtra("offer_coupon", offer_coupon);
                i.putExtra("offer_discount", offer_discount);
                startActivity(i);
            } else {
                Intent loginIntent = new Intent(ReOrderCartActivity.this, LoginActivity.class);
                loginIntent.putExtra("setfinish", "true");
                startActivity(loginIntent);
            }
        } else if (id == R.id.tv_cart_view_offers) {
            // start offers activity
            Intent i = new Intent(ReOrderCartActivity.this, OffersActivity.class);
            startActivity(i);
        }

        // Check if no view has focus:
        View view2 = this.getCurrentFocus();
        if (view2 != null) {
            // hide keyboard on view
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }
    }

    // update UI
    private void updateData() {
        // check local database has at list 1 record or not
        if (dbcart.getCartCount() > 0) {
            //tv_subtotal.setText(dbcart.getTotalAmount());
            tv_subtotal.setText(dbcart.getTotalDiscountAmount());
            tv_total_items.setText("" + dbcart.getCartCount());
            tv_total.setText(dbcart.getTotalDiscountAmount());
            Double total_save = Double.parseDouble(dbcart.getTotalAmount()) - Double.parseDouble(dbcart.getTotalDiscountAmount());

            tv_discount.setText(String.format("%.2f", total_save));

            CommonAppCompatActivity commonAppCompatActivity = new CommonAppCompatActivity();
            commonAppCompatActivity.updateCounter(this);
        } else {
            finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // unregister reciver
        unregisterReceiver(mCart);
    }

    @Override
    public void onResume() {
        super.onResume();
        // register reciver
        registerReceiver(mCart, new IntentFilter("GetPills_cart"));
    }

    // broadcast receiver for receive data
    private BroadcastReceiver mCart = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String type = intent.getStringExtra("type");

            if (type.contentEquals("update")) {
                updateData();
            }
        }
    };

    private void makeCheckOffer(String user_id, String offer_coupons) {

        // adding post values in arraylist
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new NameValuePair("user_id", user_id));
        params.add(new NameValuePair("offer_coupon", offer_coupons));

        // CommonAsyTask class for load data from api and manage response and api
        CommonAsyTask task = new CommonAsyTask(params,
                BaseURL.CHECK_OFFERS_URL, new CommonAsyTask.VJsonResponce() {
            @Override
            public void VResponce(String response) {
                Log.e(TAG, response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String offer_id = jsonObject.getString("offer_id");
                    String offer_title = jsonObject.getString("offer_title");
                    offer_coupon = jsonObject.getString("offer_coupon");
                    offer_discount = jsonObject.getString("offer_discount");

                    ti_voucher.setErrorTextAppearance(R.style.error_appearance_primey);
                    ti_voucher.setError(offer_title + ". " + offer_discount + "% Discount available");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void VError(String responce) {
                Log.e(TAG, responce);
                ti_voucher.setErrorTextAppearance(R.style.error_appearance_red);
                ti_voucher.setError(responce);
            }
        }, true, this);
        task.execute();
    }

    private void makeGetMyOrderDetail(String sale_id) {

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new NameValuePair("sale_id", sale_id));

        // CommonAsyTask class for load data from api and manage response and api
        CommonAsyTask task = new CommonAsyTask(params,
                BaseURL.GET_ORDER_DETAIL_URL, new CommonAsyTask.VJsonResponce() {
            @Override
            public void VResponce(String response) {
                Log.e(TAG, response);
                System.out.println("REORDER ----->"+response);

                List<My_order_detail_model> my_order_detail_modelList = new ArrayList<>();

                // gson library use for getting api response from api and store as over model
                Gson gson = new Gson();
                Type listType = new TypeToken<List<My_order_detail_model>>() {
                }.getType();

                // store gson values in list
                my_order_detail_modelList = gson.fromJson(response, listType);

                for(int i=0;i<my_order_detail_modelList.size();i++){
                    map.put("product_id", my_order_detail_modelList.get(i).getProduct_id());
                    map.put("product_name", my_order_detail_modelList.get(i).getProduct_name());
                    map.put("product_image", my_order_detail_modelList.get(i).getProduct_image());
                    map.put("category_id", "");
                    map.put("in_stock", "1");
                    map.put("price", my_order_detail_modelList.get(i).getPrice());
                    map.put("unit_value", my_order_detail_modelList.get(i).getUnit_value());
                    map.put("unit", "");
                    map.put("mfg_id", "");
                    map.put("isgeneric", "");
                    map.put("group_name", "");
                    map.put("discount", my_order_detail_modelList.get(i).getDiscount());
                    map.put("stock", "100");
                    map.put("title", my_order_detail_modelList.get(i).getProduct_name());
                    map.put("mfg_name", "");

                    Double items = Double.parseDouble(my_order_detail_modelList.get(i).getQty().toString());
                    Double price = Double.parseDouble(map.get("price"));
                    if (!map.get("discount").isEmpty() && !map.get("discount").equalsIgnoreCase("0")) {
//
                    dbcart.setCart(map, Float.valueOf(my_order_detail_modelList.get(i).getQty().toString()),
                            Double.parseDouble(getDiscountPrice(map.get("discount"), "" + price * items, false)),
                            Double.parseDouble(getDiscountPrice(map.get("discount"), "" + price * items, true)));
                } else {
                    dbcart.setCart(map, Float.valueOf(my_order_detail_modelList.get(i).getQty().toString()),
                            price * items,
                            price * items);
                }
                    map.clear();
                }
//
                updateData();

                // get all cart data from database and store in map list
                ArrayList<HashMap<String, String>> map = dbcart.getCartAll();

                Cart_adapter adapter = new Cart_adapter(ReOrderCartActivity.this, map, false);
                rv_cart.setAdapter(adapter);
                adapter.notifyDataSetChanged();



//                My_order_detail_adapter adapter = new My_order_detail_adapter(my_order_detail_modelList);
//                rv_order_detail.setAdapter(adapter);
//                adapter.notifyDataSetChanged();

                // display toast message
                CommonAppCompatActivity.showListToast(ReOrderCartActivity.this, my_order_detail_modelList.isEmpty());
            }

            @Override
            public void VError(String responce) {
                Log.e(TAG, responce);
                // display toast message
                CommonAppCompatActivity.showToast(ReOrderCartActivity.this, responce);
            }
        }, true, this);
        task.execute();
    }
    private String getDiscountPrice(String discount, String price, boolean getEffectedprice) {
        Double discount1 = Double.parseDouble(discount);
        Double price1 = Double.parseDouble(price);
        Double discount_amount = discount1 * price1 / 100;

        if (getEffectedprice) {
            Double effected_price = price1 - discount_amount;
            return effected_price.toString();
        } else {
            return discount_amount.toString();
        }
    }

}
