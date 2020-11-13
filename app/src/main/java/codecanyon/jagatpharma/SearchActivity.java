package codecanyon.jagatpharma;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Config.BaseURL;
import adapter.Medical_product_list_adapter;
import model.Medical_category_list_model;
import util.CommonAsyTask;
import util.ConnectivityReceiver;
import util.NameValuePair;

public class SearchActivity extends CommonAppCompatActivity implements View.OnClickListener {

    private static String TAG = SearchActivity.class.getSimpleName();

    private List<Medical_category_list_model> medical_category_list_modelList = new ArrayList<>();

    private EditText et_search;
    private Spinner sp_search;
    private TextInputLayout ti_search;
    private Button btn_search;
    private RecyclerView rv_search;
    FloatingActionButton fab1, fab2,fab3;
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        et_search = (EditText) findViewById(R.id.et_search);
        ti_search = (TextInputLayout) findViewById(R.id.ti_search);
        btn_search = (Button) findViewById(R.id.btn_search);
        sp_search = (Spinner) findViewById(R.id.sp_search);
        rv_search = (RecyclerView) findViewById(R.id.rv_search);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rv_search.setLayoutManager(gridLayoutManager);

        List<String> items = new ArrayList<>();
        items.add(getResources().getString(R.string.prescription));
        items.add(getResources().getString(R.string.medical_product));

        // bind adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_spinner_text_white, R.id.tv_sp, items);
        sp_search.setAdapter(adapter);

        btn_search.setOnClickListener(this);

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    fab1.show();
                    fab2.show();

                    fab1.animate().translationY(-(fab2.getCustomSize()+fab3.getCustomSize()));
                    fab2.animate().translationY(-(fab3.getCustomSize()));


                    fab3.setImageResource(R.drawable.ic_baseline_close_24);
                    flag = false;

                }else {
                    fab1.hide();
                    fab2.hide();
                    fab1.animate().translationY(0);
                    fab2.animate().translationY(0);

                    fab3.setImageResource(R.drawable.ic_baseline_add_circle_24);
                    flag = true;

                }
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String contact = "+91 7041681684"; // use country code with your phone number
                String url = "https://api.whatsapp.com/send?phone=" + contact;
                try {
                    PackageManager pm = getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent in = new Intent(Intent.ACTION_VIEW);
                    in.setData(Uri.parse(url));
                    startActivity(in);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(SearchActivity.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    try{
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:"+"7041681684"));
                        startActivity(callIntent);
                    }catch (Exception ex){

                    }
            }
        });


        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String search = et_search.getText().toString();
                if (!TextUtils.isEmpty(search)) {
                    if (ConnectivityReceiver.isConnected()) {
                        makeGetProduct(search);
                    } else {
                        ConnectivityReceiver.showSnackbar(SearchActivity.this);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //Log.i(TAG,"Enter pressed");
                    ti_search.setError(null);

                    String search = et_search.getText().toString();

                    if (TextUtils.isEmpty(search)) {
                        ti_search.setError(getResources().getString(R.string.error_field_required));
                        et_search.requestFocus();
                    } else {
                        // check internet connection available or not
                        if (ConnectivityReceiver.isConnected()) {
                            makeGetProduct(search);
                        } else {
                            ConnectivityReceiver.showSnackbar(SearchActivity.this);
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {

        ti_search.setError(null);

        String search = et_search.getText().toString();

        if (TextUtils.isEmpty(search)) {
            ti_search.setError(getResources().getString(R.string.error_field_required));
            et_search.requestFocus();
        } else {
            // check internet connection available or not
            if (ConnectivityReceiver.isConnected()) {
                makeGetProduct(search);
            } else {
                ConnectivityReceiver.showSnackbar(SearchActivity.this);
            }
        }
    }

    private void makeGetProduct(String search) {

        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new NameValuePair("search", search));

        // CommonAsyTask class for load data from api and manage response and api
        CommonAsyTask task = new CommonAsyTask(params,
                BaseURL.GET_PRODUCT_URL, new CommonAsyTask.VJsonResponce() {
            @Override
            public void VResponce(String response) {
                Log.e(TAG, response);

                Gson gson = new Gson();
                Type listType = new TypeToken<List<Medical_category_list_model>>() {
                }.getType();

                // store data in list using gson values
                medical_category_list_modelList = gson.fromJson(response, listType);

                Medical_product_list_adapter adapter = new Medical_product_list_adapter(medical_category_list_modelList, SearchActivity.this);
                rv_search.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                CommonAppCompatActivity.showListToast(SearchActivity.this, medical_category_list_modelList.isEmpty());

            }

            @Override
            public void VError(String responce) {
                Log.e(TAG, responce);
            }
        }, true, this);
        task.execute();

    }

}
