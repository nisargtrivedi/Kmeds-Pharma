package codecanyon.jagatpharma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Config.BaseURL;
import util.ConnectivityReceiver;
import util.JSONParser;
import util.NameValuePair;
import util.Session_management;

public class Upload_prescriptionActivity extends CommonAppCompatActivity implements View.OnClickListener {

    private static String TAG = Upload_prescriptionActivity.class.getSimpleName();

    private List<Map<String, String>> image_list = new ArrayList<>();

    private ImageView iv_img_1, iv_img_2, iv_img_3;
    private TextView tv_name, tv_email, tv_add_address, tv_address_detail, tv_bill_name, tv_bill_email, tv_bill_detail, tv_add_billing;
    private Button btn_upload;

    private String filePath1 = "";
    private String filePath2 = "";
    private String filePath3 = "";
    private static final int GALLERY_REQUEST_CODE1 = 201;
    private static final int GALLERY_REQUEST_CODE2 = 202;
    private static final int GALLERY_REQUEST_CODE3 = 203;
    private Bitmap bitmap;
    private Uri imageuri;

    private SharedPreferences prefs_address;

    public final int GALLERY_REQUEST_CODE = 1000;
    public final int CAMERA_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_prescription);

        prefs_address = getSharedPreferences("Delivery_address", 0);

        iv_img_1 = (ImageView) findViewById(R.id.iv_upload_pres_img_1);
        iv_img_2 = (ImageView) findViewById(R.id.iv_upload_pres_img_2);
        iv_img_3 = (ImageView) findViewById(R.id.iv_upload_pres_img_3);

        tv_name = (TextView) findViewById(R.id.tv_upload_pres_name);
        tv_email = (TextView) findViewById(R.id.tv_upload_pres_email);
        tv_add_address = (TextView) findViewById(R.id.tv_confirm_address_edit);
        tv_address_detail = (TextView) findViewById(R.id.tv_confirm_address_detail);
        tv_bill_name = (TextView) findViewById(R.id.tv_confirm_address_fname);
        tv_bill_email = (TextView) findViewById(R.id.tv_confirm_address_email);
        tv_bill_detail = (TextView) findViewById(R.id.tv_confirm_address_bill_detail);
        tv_add_billing = (TextView) findViewById(R.id.tv_confirm_billing_edit);

        btn_upload = (Button) findViewById(R.id.btn_upload);

        iv_img_1.setOnClickListener(this);
        iv_img_2.setOnClickListener(this);
        iv_img_3.setOnClickListener(this);
        tv_add_address.setOnClickListener(this);
        tv_add_billing.setOnClickListener(this);
        btn_upload.setOnClickListener(this);

        upadateUI();

    }
    private void launchCameraIntent() {
        Intent intent = new Intent(Upload_prescriptionActivity.this, ImagePickerActivity.class);
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
        Intent intent = new Intent(Upload_prescriptionActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }
    private void selectOption(){
        ImagePickerActivity.showImagePickerOptions(Upload_prescriptionActivity.this, new ImagePickerActivity.PickerOptionListener() {
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


    private void upadateUI() {
        Session_management sessionManagement = new Session_management(this);

        String getname = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
        String getemail = sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL);
        String getcity = sessionManagement.getUserDetails().get(BaseURL.KEY_CITY);
        String getaddress = sessionManagement.getUserDetails().get(BaseURL.KEY_ADDRESS);
        String getmobile = sessionManagement.getUserDetails().get(BaseURL.KEY_MOBILE);

        StringBuilder sb = new StringBuilder();
        sb.append(getmobile + "\n");
        if (getaddress.isEmpty()) {
            tv_add_billing.setText(getResources().getString(R.string.add));
            sb.append(getResources().getString(R.string.no_address_found));
        } else {
            tv_add_billing.setText(getResources().getString(R.string.edit));
            sb.append(getcity + "\n");
            sb.append(getaddress);
        }

        tv_name.setText(getname);
        tv_email.setText(getemail);

        tv_bill_name.setText(getname);
        tv_bill_email.setText(getemail);
        tv_bill_detail.setText(sb.toString());
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
            MediaScannerConnection.scanFile(Upload_prescriptionActivity.this,
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
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(Upload_prescriptionActivity.this.getContentResolver(), fpath);
            path  = saveImage(bitmap);
            if (!path.isEmpty()) {
                File file = new File(path);



                if(filePath1.isEmpty()) {
                    iv_img_1.setImageBitmap(bitmap);
                    filePath1 = file.getAbsolutePath();
                    Map<String, String> map = new HashMap<>();
                    map.put("file_type", "image/png");
                    map.put("filepath", filePath1);
                    map.put("imagename", "prescription_img1");
                    image_list.add(map);
                }
                else if(filePath2.isEmpty()) {
                    iv_img_2.setImageBitmap(bitmap);
                    filePath2 = file.getAbsolutePath();
                    Map<String, String> map = new HashMap<>();
                    map.put("file_type", "image/png");
                    map.put("filepath", filePath2);
                    map.put("imagename", "prescription_img2");
                    image_list.add(map);
                }else if(filePath3.isEmpty()) {
                    iv_img_3.setImageBitmap(bitmap);
                    filePath3 = file.getAbsolutePath();
                    Map<String, String> map = new HashMap<>();
                    map.put("file_type", "image/png");
                    map.put("filepath", filePath3);
                    map.put("imagename", "prescription_img3");
                    image_list.add(map);
                }

          //      imagefile1 = file;
            //    iv_profile.setImageURI(Uri.fromFile(imagefile1));

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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(Upload_prescriptionActivity.this.getContentResolver(), fpath);
                path  = saveImage(bitmap);
                if (!path.isEmpty()) {
                    File file = new File(path);
                    if(filePath1.isEmpty()) {
                        iv_img_1.setImageBitmap(bitmap);
                        filePath1 = file.getAbsolutePath();
                        Map<String, String> map = new HashMap<>();
                        map.put("file_type", "image/png");
                        map.put("filepath", filePath1);
                        map.put("imagename", "prescription_img1");
                        image_list.add(map);
                    }
                    else if(filePath2.isEmpty()) {
                        iv_img_2.setImageBitmap(bitmap);
                        filePath2 = file.getAbsolutePath();
                        Map<String, String> map = new HashMap<>();
                        map.put("file_type", "image/png");
                        map.put("filepath", filePath2);
                        map.put("imagename", "prescription_img2");
                        image_list.add(map);
                    }else if(filePath3.isEmpty()) {
                        iv_img_3.setImageBitmap(bitmap);
                        filePath3 = file.getAbsolutePath();
                        Map<String, String> map = new HashMap<>();
                        map.put("file_type", "image/png");
                        map.put("filepath", filePath3);
                        map.put("imagename", "prescription_img3");
                        image_list.add(map);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //new ImageCompression().execute(fpath.toString());
            //img.setImageURI(Uri.parse(cameraFilePath));





        }catch (Exception e){
            Log.e("Error...",e.getMessage());
            Toast.makeText(Upload_prescriptionActivity.this,"Try Again...",Toast.LENGTH_LONG).show();
        }
        return path;
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();

        Intent i = null;

        if (id == R.id.iv_upload_pres_img_1) {
//            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            // Start the Intent
//            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE1);
            selectOption();
        } else if (id == R.id.iv_upload_pres_img_2) {
//            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            // Start the Intent
//            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE2);
            selectOption();
        } else if (id == R.id.iv_upload_pres_img_3) {
//            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            // Start the Intent
//            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE3);
            selectOption();
        } else if (id == R.id.tv_confirm_address_edit) {
            i = new Intent(Upload_prescriptionActivity.this, Delivery_addressActivity.class);
            i.putExtra("select", "true");
        } else if (id == R.id.tv_confirm_billing_edit) {
            i = new Intent(Upload_prescriptionActivity.this, Edit_profileActivity.class);
        } else if (id == R.id.btn_upload) {
            // check internet connection available or not
            if (ConnectivityReceiver.isConnected()) {
                if (prefs_address.getString("delivery_id", null) != null) {
                    String delivery_id = prefs_address.getString("delivery_id", null);
                    Session_management sessionManagement = new Session_management(this);
                    String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
                    new uploadPrescription(user_id, delivery_id).execute();
                } else {
                    CommonAppCompatActivity.showToast(this, "Please select delivery address");
                }
            } else {
                // display snackbar
                ConnectivityReceiver.showSnackbar(this);
            }
        }

        if (i != null) {
            startActivity(i);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if the result is capturing Image

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
        else
            if (resultCode == RESULT_OK) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                //filePath = imgDecodableString;

                Bitmap b = BitmapFactory.decodeFile(imgDecodableString);
                Bitmap out = Bitmap.createScaledBitmap(b, 1200, 1024, false);

                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);


                File file = new File(imgDecodableString);
                //filePath = file.getAbsolutePath();
                FileOutputStream fOut;
                try {
                    fOut = new FileOutputStream(file);
                    out.compress(Bitmap.CompressFormat.JPEG, 80, fOut);
                    fOut.flush();
                    fOut.close();
                    //b.recycle();
                    //out.recycle();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (requestCode == GALLERY_REQUEST_CODE1) {
                    // Set the Image in ImageView after decoding the String
                    iv_img_1.setImageBitmap(bitmap);
                    filePath1 = file.getAbsolutePath();

                    Map<String, String> map = new HashMap<>();
                    map.put("file_type", "image/png");
                    map.put("filepath", filePath1);
                    map.put("imagename", "prescription_img1");
                    image_list.add(map);
                }
                if (requestCode == GALLERY_REQUEST_CODE2) {
                    // Set the Image in ImageView after decoding the String
                    iv_img_2.setImageBitmap(bitmap);
                    filePath2 = file.getAbsolutePath();

                    Map<String, String> map = new HashMap<>();
                    map.put("file_type", "image/png");
                    map.put("filepath", filePath2);
                    map.put("imagename", "prescription_img2");
                    image_list.add(map);
                }
                if (requestCode == GALLERY_REQUEST_CODE3) {
                    // Set the Image in ImageView after decoding the String
                    iv_img_3.setImageBitmap(bitmap);
                    filePath3 = file.getAbsolutePath();

                    Map<String, String> map = new HashMap<>();
                    map.put("file_type", "image/png");
                    map.put("filepath", filePath3);
                    map.put("imagename", "prescription_img3");
                    image_list.add(map);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class uploadPrescription extends AsyncTask<String, Integer, Void> {

        private ProgressDialog progressDialog;
        JSONParser jsonParser;
        ArrayList<NameValuePair> nameValuePairs;
        boolean response;
        String error_string, success_msg;
        String filePath = "";

        private uploadPrescription(String user_id, String delivery_id) {

            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new NameValuePair("user_id", user_id));
            nameValuePairs.add(new NameValuePair("delivery_id", delivery_id));

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(Upload_prescriptionActivity.this, R.style.AppCompatAlertDialogStyle);
            progressDialog.setMessage("Process with data..");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

            jsonParser = new JSONParser(Upload_prescriptionActivity.this);
        }

        protected Void doInBackground(String... urls) {

            String json_responce = null;
            try {
                // upload multiple images to server
                json_responce = jsonParser.execMultiPartPostScriptJSON(BaseURL.SEND_ORDER_PRESCRIPTION_URL,
                        nameValuePairs, image_list);
                Log.e(TAG, json_responce + "," + filePath);

                JSONObject jObj = new JSONObject(json_responce);
                if (jObj.getBoolean("responce")) {
                    response = true;
                    success_msg = jObj.getString("data");

                } else {
                    response = false;
                    error_string = jObj.getString("message");
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            if ((this.progressDialog != null) && this.progressDialog.isShowing()) {
                this.progressDialog.dismiss();
            }

            if (response) {
                // display toast message
                CommonAppCompatActivity.showToast(Upload_prescriptionActivity.this, String.valueOf(Html.fromHtml(success_msg)));
                finish();
            } else {
                // display toast message
                CommonAppCompatActivity.showToast(Upload_prescriptionActivity.this, error_string);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        upadateUI();

        if (prefs_address.getString("delivery_id", null) != null) {

            StringBuilder sb = new StringBuilder();

            sb.append(prefs_address.getString("delivery_fullname", null) + "\n");
            sb.append(prefs_address.getString("delivery_city", null) + "\n");
            sb.append(prefs_address.getString("delivery_landmark", null) + "\n");
            sb.append(prefs_address.getString("delivery_zipcode", null) + "\n");
            sb.append(prefs_address.getString("delivery_address", null) + "\n");
            sb.append(prefs_address.getString("delivery_mobilenumber", null) + "\n");

            tv_address_detail.setText(sb);
            tv_add_address.setText(getResources().getString(R.string.edit));
        }

    }

}
