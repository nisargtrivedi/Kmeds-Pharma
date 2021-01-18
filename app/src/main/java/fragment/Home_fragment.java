package fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jama.carouselview.CarouselView;
import com.jama.carouselview.CarouselViewListener;
import com.jama.carouselview.enums.IndicatorAnimationType;
import com.jama.carouselview.enums.OffsetType;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Config.BaseURL;
import adapter.Home_slider_adapter;
import adapter.Home_suggetion_adapter;
import codecanyon.jagatpharma.EditMedicine;
import codecanyon.jagatpharma.Edit_profileActivity;
import codecanyon.jagatpharma.LoginActivity;
import codecanyon.jagatpharma.MainActivity;
import codecanyon.jagatpharma.Medical_productActivity;
import codecanyon.jagatpharma.My_orderActivity;
import codecanyon.jagatpharma.Prescription_listActivity;
import codecanyon.jagatpharma.Producat_detailActivity;
import codecanyon.jagatpharma.R;
import codecanyon.jagatpharma.ReferralCode;
import codecanyon.jagatpharma.SearchActivity;
import codecanyon.jagatpharma.Upload_prescriptionActivity;
import model.Medical_category_list_model;
import model.Slider_model;
import util.CommonAsyTask;
import util.ConnectivityReceiver;
import util.NameValuePair;
import util.RecyclerTouchListener;
import util.Session_management;

/**
 * Created by Rajesh on 2017-09-04.
 */

public class Home_fragment extends Fragment implements View.OnClickListener {

    private static String TAG = Home_fragment.class.getSimpleName();

    private List<Slider_model> slider_modelList = new ArrayList<>();
    private List<Medical_category_list_model> medical_category_list_modelList = new ArrayList<>();

    private RecyclerView rv_slider, rv_suggest;
    private TextView tv_upload, tv_medical, tv_search;
    ImageView btnWhatsapp,btnCall,tv_prescription;
    BottomNavigationView nav_view;
    ImageView llProduct,tv_home_prescription,imgReferral;
    CarouselView carouselView;

    public Home_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
         carouselView= view.findViewById(R.id.cv);

        tv_prescription = (ImageView) view.findViewById(R.id.tv_home_prescription);
        tv_upload = (TextView) view.findViewById(R.id.tv_home_upload);
        imgReferral=view.findViewById(R.id.imgReferral);
        tv_search = (TextView) view.findViewById(R.id.tv_home_search);
        rv_slider = (RecyclerView) view.findViewById(R.id.rv_home_slider);
        rv_suggest = (RecyclerView) view.findViewById(R.id.rv_home_suggested);
       // btnCall=(ImageView) view.findViewById(R.id.btnCall);
        //btnWhatsapp=(ImageView) view.findViewById(R.id.btnWhatsapp);
        nav_view=view.findViewById(R.id.nav_view);
        llProduct=view.findViewById(R.id.llProduct);
        tv_home_prescription=view.findViewById(R.id.tv_home_prescription);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_slider.setLayoutManager(linearLayoutManager);
        rv_suggest.setLayoutManager(linearLayoutManager2);

        tv_search.setOnClickListener(this);
        tv_upload.setOnClickListener(this);
        tv_prescription.setOnClickListener(this);
        //btnCall.setOnClickListener(this);
        //btnWhatsapp.setOnClickListener(this);

        // check internet connection is available or not
        if (ConnectivityReceiver.isConnected()) {
            makeGetSlider();
            makeGetSuggest();
        } else {
            // display snackbar
            ConnectivityReceiver.showSnackbar(getActivity());
        }

        rv_slider.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_slider, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String url = slider_modelList.get(position).getSlider_url();

                if (!url.isEmpty()) {
                    Uri webpage = Uri.parse(url);
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        webpage = Uri.parse("http://" + url);
                    }
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(webpage);
                    startActivity(i);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        imgReferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ReferralCode.class);
                startActivity(i);
            }
        });

        llProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Medical_productActivity.class);
                startActivity(i);
            }
        });

        tv_home_prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Medical_productActivity.class);
                startActivity(i);
            }
        });

        rv_suggest.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_suggest, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Medical_category_list_model model = medical_category_list_modelList.get(position);

                Intent i = new Intent(getActivity(), Producat_detailActivity.class);
                i.putExtra("id", model.getProduct_id());
                i.putExtra("product_name", model.getProduct_name());
                i.putExtra("product_image", model.getProduct_image());
                i.putExtra("description", model.getDescription());
                i.putExtra("category_id", model.getCategory_id());
                i.putExtra("in_stock", model.getIn_stock());
                i.putExtra("price", model.getPrice());
                i.putExtra("unit_value", model.getUnit_value());
                i.putExtra("unit", model.getUnit());
                i.putExtra("mfg_id", model.getMfg_id());
                i.putExtra("isgeneric", model.getIsgeneric());
                i.putExtra("group_name", model.getGroup_name());
                i.putExtra("discount", model.getDiscount());
                i.putExtra("stock", model.getStock());
                i.putExtra("title", model.getTitle());
                i.putExtra("mfg_name", model.getMfg_name());
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.btnWhatsapp:
                        String contact = "+91 7041681684"; // use country code with your phone number
                        String url = "https://api.whatsapp.com/send?phone=" + contact;
                        try {
                            PackageManager pm = getActivity().getPackageManager();
                            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                            Intent in = new Intent(Intent.ACTION_VIEW);
                            in.setData(Uri.parse(url));
                            startActivity(in);
                        } catch (PackageManager.NameNotFoundException e) {
                            Toast.makeText(getActivity(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        break;

                    case R.id.navigation_call:
                        try {
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:" + "7041681684"));
                            startActivity(callIntent);
                        }catch (Exception ex){

                        }
                        break;
                    case R.id.navigation_myorder:
                        try {
                            Intent commonIntent = new Intent(getActivity(), My_orderActivity.class);
                            startActivity(commonIntent);
                        }catch (Exception ex){

                        }
                        break;
                    case R.id.navigationprofile:
                        try {
                            Intent commonIntent = new Intent(getActivity(), Edit_profileActivity.class);
                            startActivity(commonIntent);
                        }catch (Exception ex){

                        }
                        break;

                }
                return true;
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        Intent i = null;

       if(id==R.id.btnWhatsapp){
            String contact = "+91 7041681684"; // use country code with your phone number
            String url = "https://api.whatsapp.com/send?phone=" + contact;
            try {
                PackageManager pm = getActivity().getPackageManager();
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                Intent in = new Intent(Intent.ACTION_VIEW);
                in.setData(Uri.parse(url));
                startActivity(in);
            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(getActivity(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        else if (id == R.id.tv_home_upload) {
            Session_management sessionManagement = new Session_management(getActivity());
            if (sessionManagement.isLoggedIn()) {
                i = new Intent(getActivity(), Upload_prescriptionActivity.class);
            } else {
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                loginIntent.putExtra("setfinish", "true");
                startActivity(loginIntent);
            }
        } else if (id == R.id.tv_home_prescription) {
            i = new Intent(getActivity(), EditMedicine.class);
        } else if (id == R.id.tv_home_search) {
            i = new Intent(getActivity(), SearchActivity.class);
        }
//        else if (id == R.id.tv_home_medical) {
//            i = new Intent(getActivity(), Medical_productActivity.class);
//        }

        // check intent not null then start new activity
        if (i != null) {
            startActivity(i);
        }
    }

    private void makeGetSlider() {

        // CommonAsyTask class for load data from api and manage response and api
        CommonAsyTask task = new CommonAsyTask(new ArrayList<NameValuePair>(),
                BaseURL.GET_SLIDER_URL, new CommonAsyTask.VJsonResponce() {
            @Override
            public void VResponce(String response) {
                Log.e(TAG, response);

                Gson gson = new Gson();
                Type listType = new TypeToken<List<Slider_model>>() {
                }.getType();

                slider_modelList = gson.fromJson(response, listType);

                Home_slider_adapter adapter = new Home_slider_adapter(slider_modelList);
                rv_slider.setAdapter(adapter);
                adapter.notifyDataSetChanged();


                carouselView.setSize(slider_modelList.size());
                carouselView.setResource(R.layout.row_home_slider);
                carouselView.setAutoPlay(true);
                carouselView.setIndicatorAnimationType(IndicatorAnimationType.THIN_WORM);
                carouselView.setCarouselOffset(OffsetType.CENTER);
                carouselView.setCarouselViewListener(new CarouselViewListener() {
                    @Override
                    public void onBindView(View view, int position) {
                        // Example here is setting up a full image carousel
                        util.CustomImageView imageView = view.findViewById(R.id.iv_home_slider);
                      //  imageView.setImageDrawable(getResources().getDrawable(images[position]));
                        Picasso.with(getActivity())
                                .load(BaseURL.IMG_SLIDER_URL+ slider_modelList.get(position).getSlider_image())
                                .placeholder(R.drawable.slider_loading)
                                .into(imageView);

                    }
                });
                // After you finish setting up, show the CarouselView
                carouselView.show();
            }
            @Override
            public void VError(String responce) {
                Log.e(TAG, responce);
            }
        }, true, getActivity());
        task.execute();

    }

    private void makeGetSuggest() {

        // CommonAsyTask class for load data from api and manage response and api
        CommonAsyTask task = new CommonAsyTask(new ArrayList<NameValuePair>(),
                BaseURL.GET_SUGGEST_URL, new CommonAsyTask.VJsonResponce() {
            @Override
            public void VResponce(String response) {
                Log.e(TAG, response);

                Gson gson = new Gson();
                Type listType = new TypeToken<List<Medical_category_list_model>>() {
                }.getType();

                medical_category_list_modelList = gson.fromJson(response, listType);

                Home_suggetion_adapter adapter = new Home_suggetion_adapter(medical_category_list_modelList, getActivity());
                rv_suggest.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void VError(String responce) {
                Log.e(TAG, responce);
            }
        }, true, getActivity());
        task.execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.btnWhatsapp:
                String contact = "+91 7041681684"; // use country code with your phone number
                String url = "https://api.whatsapp.com/send?phone=" + contact;
                try {
                    PackageManager pm = getActivity().getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent in = new Intent(Intent.ACTION_VIEW);
                    in.setData(Uri.parse(url));
                    startActivity(in);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getActivity(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                return true;
            }
        return super.onOptionsItemSelected(item);
    }
}
