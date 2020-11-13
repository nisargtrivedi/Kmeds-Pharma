package adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Config.BaseURL;
import codecanyon.jagatpharma.R;
import model.model_enquiry;
import util.DatabaseHandler;

/**
 * Created by Rajesh on 2017-09-12.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ProductHolder> {

    List<model_enquiry> list;
    Activity activity;
    DatabaseHandler dbHandler;
    private boolean is_onlyview;
    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
    public ItemAdapter(Activity activity, List<model_enquiry> list, boolean is_onlyview) {
        this.list = list;
        this.activity = activity;
        this.is_onlyview = is_onlyview;

        dbHandler = new DatabaseHandler(activity);
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_row, parent, false);

        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        final model_enquiry map = list.get(position);

        String inputDateStr = map.enquiry_date;
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
            String outputDateStr = outputFormat.format(date);
            holder.tvName.setText("Date: "+outputDateStr);
            holder.tvQty.setText(map.item_details);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvQty;


        private ProductHolder(View view) {
            super(view);

            tvName = (TextView) view.findViewById(R.id.tvDate);
            tvQty = (TextView) view.findViewById(R.id.tvItems);

        }
    }



}
