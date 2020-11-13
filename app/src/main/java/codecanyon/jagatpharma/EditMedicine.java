package codecanyon.jagatpharma;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Config.BaseURL;
import adapter.Cart_adapter;
import adapter.ItemAdapter;
import model.My_order_detail_model;
import model.model_enquiry;
import util.CommonAsyTask;
import util.NameValuePair;
import util.Session_management;

public class EditMedicine extends AppCompatActivity {

    AppCompatButton btnPlaceEnquiry,btnGo;
    EditText edtName,edtQty;
    TextView tvItems;
    RecyclerView rvMedicines;
    String dataAnswer="";
    private Session_management sessionManagement;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManagement = new Session_management(this);
        setContentView(R.layout.activity_edit_prescription);
        btnPlaceEnquiry=findViewById(R.id.btnPlaceEnquiry);
        edtName=findViewById(R.id.edtName);
        tvItems=findViewById(R.id.tvItems);
        edtQty=findViewById(R.id.edtQty);
        btnGo=findViewById(R.id.btnGo);
        rvMedicines=findViewById(R.id.rvMedicines);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtName.getText().toString().trim().length()>0 && edtQty.getText().toString().trim().length()>0) {
                    dataAnswer += edtName.getText().toString() + " " + edtQty.getText().toString() + "\n";
                    tvItems.setText(dataAnswer);
                    edtName.setText("");
                    edtQty.setText("");
                    edtName.requestFocus();
                }else{
                    CommonAppCompatActivity.showToast(EditMedicine.this, "Please Enter Any Medicine For Enquiry");
                }
            }
        });
        btnPlaceEnquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tvItems.getText().length()>0){
                    placeEnquiry();
                }else{
                    Toast.makeText(EditMedicine.this,"Please Enter Any Medicine For Enquiry",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void placeEnquiry() {

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        params.add(new NameValuePair("userID", user_id));
        params.add(new NameValuePair("items", tvItems.getText().toString()));

        // CommonAsyTask class for load data from api and manage response and api
        CommonAsyTask task = new CommonAsyTask(params,
                BaseURL.PLACE_ENQUIRY, new CommonAsyTask.VJsonResponce() {
            @Override
            public void VResponce(String response) {
              //  Log.e(TAG, response);
                System.out.println("REORDER ----->"+response);
                CommonAppCompatActivity.showToast(EditMedicine.this, response);
                finish();
            }

            @Override
            public void VError(String responce) {
               // Log.e(TAG, responce);
                // display toast message
                CommonAppCompatActivity.showToast(EditMedicine.this, responce);
            }
        }, true, this);
        task.execute();
    }
}
