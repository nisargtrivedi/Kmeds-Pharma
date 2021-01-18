package codecanyon.jagatpharma;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Config.BaseURL;
import adapter.ItemAdapter;
import model.WalletModel;
import model.model_enquiry;
import util.CommonAsyTask;
import util.NameValuePair;
import util.Session_management;

public class MyWallet extends CommonAppCompatActivity {

    private Session_management sessionManagement;
    TextView tvAmount;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywallet);
        tvAmount=findViewById(R.id.tvAmount);
        sessionManagement = new Session_management(this);
        wallet();
    }

    private void wallet() {

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        params.add(new NameValuePair("userID", user_id));

        // CommonAsyTask class for load data from api and manage response and api
        CommonAsyTask task = new CommonAsyTask(params,
                BaseURL.WALLET_AMOUNT, new CommonAsyTask.VJsonResponce() {
            @Override
            public void VResponce(String response) {
                Log.e("WALLET RESPONSE-->", response);
                //System.out.println("REORDER ----->"+response);

                try {
                    List<WalletModel> my_order_detail_modelList = new ArrayList<>();

                    // gson library use for getting api response from api and store as over model
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<WalletModel>>() {
                    }.getType();
                    //System.out.println("TYPE--->"+listType.getTypeName().toString()+response);
                    // store gson values in list

                    if (response.equalsIgnoreCase("No Wallet Amount Found")) {
                    } else {
                        my_order_detail_modelList = gson.fromJson(response, listType);
                        tvAmount.setText("₹ " + my_order_detail_modelList.get(0).amount);
                    }
                }catch (Exception ex){
                    tvAmount.setText(ex.toString());
                }
            }

            @Override
            public void VError(String responce) {
                // Log.e(TAG, responce);
                // display toast message
                CommonAppCompatActivity.showToast(MyWallet.this, responce);
            }
        }, true, this);
        task.execute();
    }
}
