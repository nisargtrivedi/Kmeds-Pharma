package codecanyon.jagatpharma;

import android.os.Bundle;
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

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Config.BaseURL;
import adapter.ItemAdapter;
import model.My_order_detail_model;
import model.model_enquiry;
import util.CommonAsyTask;
import util.ConnectivityReceiver;
import util.NameValuePair;
import util.Session_management;

public class EnquiryList extends AppCompatActivity {


    RecyclerView rvEnquiries;
    TextView tvMsg;
    private Session_management sessionManagement;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManagement = new Session_management(this);
        setContentView(R.layout.activity_enquiry_list);

        rvEnquiries=findViewById(R.id.rvEnquiries);
        tvMsg=findViewById(R.id.tvMsg);
        rvEnquiries.setLayoutManager(new LinearLayoutManager(EnquiryList.this));
        if(ConnectivityReceiver.isConnected()){
            listEnquiry();
        }else{
            CommonAppCompatActivity.showToast(EnquiryList.this,"Please check internet connection");
            tvMsg.setVisibility(View.VISIBLE);
            rvEnquiries.setVisibility(View.GONE);
        }

    }

    private void listEnquiry() {

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        params.add(new NameValuePair("userID", user_id));

        // CommonAsyTask class for load data from api and manage response and api
        CommonAsyTask task = new CommonAsyTask(params,
                BaseURL.LIST_ENQUIRY, new CommonAsyTask.VJsonResponce() {
            @Override
            public void VResponce(String response) {
              //  Log.e(TAG, response);
                //System.out.println("REORDER ----->"+response);
                List<model_enquiry> my_order_detail_modelList = new ArrayList<>();

                // gson library use for getting api response from api and store as over model
                Gson gson = new Gson();
                Type listType = new TypeToken<List<model_enquiry>>() {
                }.getType();

                // store gson values in list
                my_order_detail_modelList = gson.fromJson(response, listType);
                ItemAdapter adapter=new ItemAdapter(EnquiryList.this,my_order_detail_modelList,false);
                rvEnquiries.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if(my_order_detail_modelList!=null && my_order_detail_modelList.size()>0) {
                    tvMsg.setVisibility(View.GONE);
                    rvEnquiries.setVisibility(View.VISIBLE);
                }else{
                    tvMsg.setVisibility(View.VISIBLE);
                    rvEnquiries.setVisibility(View.GONE);
                }
            }

            @Override
            public void VError(String responce) {
               // Log.e(TAG, responce);
                // display toast message
                CommonAppCompatActivity.showToast(EnquiryList.this, responce);
            }
        }, true, this);
        task.execute();
    }
}
