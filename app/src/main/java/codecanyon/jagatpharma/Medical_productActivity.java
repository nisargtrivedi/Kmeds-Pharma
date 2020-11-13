package codecanyon.jagatpharma;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Config.BaseURL;
import adapter.Medical_producat_adapter;
import model.Medical_category_model;
import util.CommonAsyTask;
import util.ConnectivityReceiver;
import util.NameValuePair;
import util.RecyclerTouchListener;

public class Medical_productActivity extends CommonAppCompatActivity {

    private static String TAG = Medical_productActivity.class.getSimpleName();

    private List<Medical_category_model> medical_category_modelList = new ArrayList<>();

    private RecyclerView rv_medical_product;
    private EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_product);

        rv_medical_product = (RecyclerView) findViewById(R.id.rv_medical_product);
        edtSearch=findViewById(R.id.edtSearch);

        rv_medical_product.setLayoutManager(new LinearLayoutManager(this));

        // check internet connection available or not
        if (ConnectivityReceiver.isConnected()) {
            makeGetMedicalCategory();
        } else {
            // diplay snackbar in activity
            ConnectivityReceiver.showSnackbar(this);
        }

        // recyclerview items click listener
        rv_medical_product.addOnItemTouchListener(new RecyclerTouchListener(this, rv_medical_product, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Medical_category_model model = medical_category_modelList.get(position);

                Intent i = new Intent(Medical_productActivity.this, Medical_product_listActivity.class);
                i.putExtra("id", model.getId());
                i.putExtra("title" , model.getTitle());
                startActivity(i);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }

    private void makeGetMedicalCategory() {

        // CommonAsyTask class for load data from api and manage response and api
        CommonAsyTask task = new CommonAsyTask(new ArrayList<NameValuePair>(),
                BaseURL.GET_MEDICAL_CATEGORIES_URL, new CommonAsyTask.VJsonResponce() {
            @Override
            public void VResponce(String response) {
                Log.e(TAG, response);

                Gson gson = new Gson();
                Type listType = new TypeToken<List<Medical_category_model>>() {
                }.getType();

                // store gson data in list
                medical_category_modelList = gson.fromJson(response, listType);

                // bind adapter using list
                Medical_producat_adapter adapter = new Medical_producat_adapter(medical_category_modelList);
                rv_medical_product.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                edtSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(edtSearch.getText().toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

            }

            @Override
            public void VError(String responce) {
                Log.e(TAG, responce);
            }
        }, true, this);
        task.execute();

    }

}
