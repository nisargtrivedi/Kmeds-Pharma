package codecanyon.jagatpharma;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Config.BaseURL;
import adapter.Cart_adapter;
import model.My_order_detail_model;
import util.DatabaseHandler;
import util.Session_management;

public class Confirm_detailActivity extends CommonAppCompatActivity implements View.OnClickListener {

    private static String TAG = Confirm_detailActivity.class.getSimpleName();

    private TextView tv_subtotal, tv_shipping_charge, tv_total, tv_discount, tv_total_items, tv_edit_order,
            tv_edit_billing, tv_net_amount, tv_coupon_price;
    private TextView tv_name, tv_email, tv_add_address, tv_address_detail, tv_billing_detail;
    private RecyclerView rv_order;
    private Button btn_confirm;
    private CheckBox chk_agree,chk_upload_prescription;

    private SharedPreferences prefs_address;

    private DatabaseHandler dbcart;

    private String offer_coupon, offer_discount, total_amount, net_amount;

    private List<My_order_detail_model> my_order_detail_modelList = new ArrayList<>();
    public final int GALLERY_REQUEST_CODE = 1000;
    public final int CAMERA_REQUEST_CODE = 1001;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_detail);

        prefs_address = getSharedPreferences("Delivery_address", 0);

        dbcart = new DatabaseHandler(this);

        offer_coupon = getIntent().getStringExtra("offer_coupon");
        offer_discount = getIntent().getStringExtra("offer_discount");

        tv_add_address = (TextView) findViewById(R.id.tv_confirm_address_edit);
        tv_address_detail = (TextView) findViewById(R.id.tv_confirm_address_detail);
        tv_name = (TextView) findViewById(R.id.tv_confirm_address_fname);
        tv_email = (TextView) findViewById(R.id.tv_confirm_address_email);
        tv_billing_detail = (TextView) findViewById(R.id.tv_confirm_address_bill_detail);
        chk_upload_prescription=findViewById(R.id.chk_upload_prescription);
        tv_subtotal = (TextView) findViewById(R.id.tv_cart_sub_total);
        tv_total_items = (TextView) findViewById(R.id.tv_cart_total_items);
        tv_shipping_charge = (TextView) findViewById(R.id.tv_cart_charge);
        tv_total = (TextView) findViewById(R.id.tv_cart_total);
        //tv_discount = (TextView) findViewById(R.id.tv_cart_discount);
        tv_edit_order = (TextView) findViewById(R.id.tv_confirm_address_order_edit);
        tv_edit_billing = (TextView) findViewById(R.id.tv_confirm_billing_edit);
        btn_confirm = (Button) findViewById(R.id.btn_confirm_process);
        rv_order = (RecyclerView) findViewById(R.id.rv_confirm_items);
        LinearLayout ll_coupon = (LinearLayout) findViewById(R.id.ll_cart_coupon);
        LinearLayout ll_net_amount = (LinearLayout) findViewById(R.id.ll_net_amount);
        tv_net_amount = (TextView) findViewById(R.id.tv_confirm_order_net_amount);
        TextView tv_coupon = (TextView) findViewById(R.id.tv_confirm_order_coupon_discount);
        tv_coupon_price = (TextView) findViewById(R.id.tv_cart_discount_coupon_price);
        chk_agree = (CheckBox) findViewById(R.id.chk_config_agree);
        img=findViewById(R.id.img);
        rv_order.setLayoutManager(new LinearLayoutManager(this));
        // enabled nested scrolling in recyclerview
        rv_order.setNestedScrollingEnabled(false);

        if (offer_coupon == null) {
            ll_coupon.setVisibility(View.GONE);
            ll_net_amount.setVisibility(View.GONE);
        } else {
            ll_coupon.setVisibility(View.VISIBLE);
            ll_net_amount.setVisibility(View.VISIBLE);
            tv_coupon.setText(getResources().getString(R.string.offer_code) + "(" + offer_coupon + " " + offer_discount + "%)");
        }

        tv_add_address.setOnClickListener(this);
        tv_edit_order.setOnClickListener(this);
        tv_edit_billing.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        chk_upload_prescription.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                        selectOption();
                }
            }
        });

        //updateUI();
        updateData();

    }

    private void launchCameraIntent()
    {
        Intent intent = new Intent(Confirm_detailActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(Confirm_detailActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }
    private void selectOption(){
        ImagePickerActivity.showImagePickerOptions(Confirm_detailActivity.this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }
    public void termsClick(View view) {
        Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(BaseURL.GET_TERMS_OF_SALE_URL));
        startActivity(browseIntent);
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File obj = new File(
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/jagatpharma/");
        // have the object build the directory structure, if needed.
        if (!obj.exists()) {
            obj.mkdirs();
        }

        try {
            File f = new File(obj, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(Confirm_detailActivity.this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private String onCaptureImageResult(Intent data) {
        //profilepic.setImageBitmap(null);

        Uri fpath = data.getParcelableExtra("path");
//        String fpath = mTempCameraPhotoFile.getPath();
//        Bitmap thumbnail;
//        String path = saveImage(thumbnail);
        // Log.i("CAMERA FILE",fpath);
        String path="";
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(Confirm_detailActivity.this.getContentResolver(), fpath);
            path  = saveImage(bitmap);
            if (!path.isEmpty()) {
                File file = new File(path);

                //      imagefile1 = file;
                img.setImageURI(Uri.fromFile(file));

                //   String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
                // new Edit_profileActivity.updateProfile(user_id, file.getAbsolutePath()).execute();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //new ImageCompression().execute(fpath.toString());
        //img.setImageURI(Uri.parse(cameraFilePath));

        return path;
    }

    private String onSelectFromGalleryResult(Intent data) {
        String path="";
        try {
            Uri fpath = data.getParcelableExtra("path");
            Uri ImageSelect = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(Confirm_detailActivity.this.getContentResolver(), fpath);
                path  = saveImage(bitmap);
                if (!path.isEmpty()) {
                    File file = new File(path);

                    img.setImageURI(Uri.fromFile(file));

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //new ImageCompression().execute(fpath.toString());
            //img.setImageURI(Uri.parse(cameraFilePath));





        }catch (Exception e){
            Log.e("Error...",e.getMessage());
            Toast.makeText(Confirm_detailActivity.this,"Try Again...",Toast.LENGTH_LONG).show();
        }
        return path;
    }
    private void updateUI() {
        Session_management sessionManagement = new Session_management(this);

        String getname = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
        String getemail = sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL);
        String getcity = sessionManagement.getUserDetails().get(BaseURL.KEY_CITY);
        String getaddress = sessionManagement.getUserDetails().get(BaseURL.KEY_ADDRESS);
        String getmobile = sessionManagement.getUserDetails().get(BaseURL.KEY_MOBILE);

        StringBuilder sb = new StringBuilder();
        /*sb.append(getname+"\n");
        sb.append(getemail+"\n");*/
        sb.append(getmobile + "\n");
        if (getaddress.isEmpty()) {
            tv_edit_billing.setText(getResources().getString(R.string.add));
            sb.append(getResources().getString(R.string.no_address_found));
        } else {
            tv_edit_billing.setText(getResources().getString(R.string.edit));
            sb.append(getcity + "\n");
            sb.append(getaddress);
        }

        tv_name.setText(getname);
        tv_email.setText(getemail);
        tv_billing_detail.setText(sb.toString());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent i = null;

        if (id == R.id.tv_confirm_address_order_edit) {
            i = new Intent(Confirm_detailActivity.this, CartActivity.class);
        } else if (id == R.id.tv_confirm_address_edit) {
            i = new Intent(Confirm_detailActivity.this, Delivery_addressActivity.class);
            i.putExtra("select", "true");
        } else if (id == R.id.tv_confirm_billing_edit) {
            i = new Intent(Confirm_detailActivity.this, Edit_profileActivity.class);
        } else if (id == R.id.btn_confirm_process) {
            if (prefs_address.getString("delivery_id", null) == null) {
                CommonAppCompatActivity.showToast(Confirm_detailActivity.this, getResources().getString(R.string.please_select_delivery_address));
            } else if (!chk_agree.isChecked()) {
                CommonAppCompatActivity.showToast(Confirm_detailActivity.this, getResources().getString(R.string.please_agree_with_terms));
            } else if(chk_upload_prescription.isChecked()) {
                //CommonAppCompatActivity.showToast(Confirm_detailActivity.this, getResources().getString(R.string.please_upload_prescription));
                selectOption();
            }else
             {
                if (offer_coupon == null) {
                    total_amount = tv_total.getText().toString();
                } else {
                    total_amount = net_amount;
                }
                // go to payment detail screen so user can submit his order
                i = new Intent(Confirm_detailActivity.this, Payment_detailActivity.class);
                i.putExtra("total_price", total_amount);
                i.putExtra("delivery_id", prefs_address.getString("delivery_id", null));
                i.putExtra("offer_coupon", offer_coupon);
            }
        }

        // if intent not null then start new activity
        if (i != null) {
            startActivity(i);
        }
    }

    // update UI
    private void updateData() {
        if (dbcart.getCartCount() > 0) {
            tv_subtotal.setText(dbcart.getTotalDiscountAmount());
            tv_total_items.setText("" + dbcart.getCartCount());
            tv_total.setText(dbcart.getTotalDiscountAmount());
            //Double total_save = Double.parseDouble(dbcart.getTotalAmount()) - Double.parseDouble(dbcart.getTotalDiscountAmount());
            //tv_discount.setText(String.format("%.2f", total_save));

            CommonAppCompatActivity commonAppCompatActivity = new CommonAppCompatActivity();
            commonAppCompatActivity.updateCounter(this);
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateUI();

        showCartData();

    }

    private void showCartData() {
        ArrayList<HashMap<String, String>> map = dbcart.getCartAll();

        Cart_adapter adapter = new Cart_adapter(this, map, true);
        rv_order.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (prefs_address.getString("delivery_id", null) != null) {

            StringBuilder sb = new StringBuilder();

            //sb.append(prefs_address.getString("delivery_id", null) + "\n");
            //sb.append(prefs_address.getString("delivery_user_id", null) + "\n");
            sb.append(prefs_address.getString("delivery_fullname", null) + "\n");
            sb.append(prefs_address.getString("delivery_city", null) + "\n");
            sb.append(prefs_address.getString("delivery_landmark", null) + "\n");
            sb.append(prefs_address.getString("delivery_zipcode", null) + "\n");
            sb.append(prefs_address.getString("delivery_address", null) + "\n");
            sb.append(prefs_address.getString("delivery_mobilenumber", null) + "\n");

            tv_shipping_charge.setText(prefs_address.getString("delivery_charge", null));
            Double total_amount = Double.parseDouble(dbcart.getTotalDiscountAmount()) + Double.parseDouble(prefs_address.getString("delivery_charge", null));
            DecimalFormat f = new DecimalFormat("##.00");

            tv_total.setText("" +f.format(Math.round(total_amount*100)/100));
            tv_address_detail.setText(sb);
        }

        if (offer_coupon != null) {
            String total_price = tv_total.getText().toString();
            String coupon_pirce = String.format("%.2f", getDiscountPrice(offer_discount, total_price, false));
            net_amount = String.format("%.2f", Math.round(getDiscountPrice(offer_discount, total_price, true)));
            tv_coupon_price.setText(coupon_pirce);
            tv_net_amount.setText(total_price + " - " + coupon_pirce + " = " + net_amount);
        }
    }

    // get discount by price and discount amount
    private Double getDiscountPrice(String discount, String price, boolean getEffectedprice) {
        Double discount1 = Double.parseDouble(discount);
        Double price1 = Double.parseDouble(price);
        Double discount_amount = discount1 * price1 / 100;

        if (getEffectedprice) {
            Double effected_price = price1 - discount_amount;
            return effected_price;
        } else {
            return discount_amount;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_REQUEST_CODE){
            try {
                //SELECTED_PATH = mTakePicture.onActivityResult(requestCode, resultCode, data);
                try {
                    onCaptureImageResult(data);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(requestCode==GALLERY_REQUEST_CODE){
            try {
                //SELECTED_PATH = mTakePicture.onActivityResult(requestCode, resultCode, data);
                try {
                    onSelectFromGalleryResult(data);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
