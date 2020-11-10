package adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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

    ArrayList<model_enquiry> list;
    Activity activity;
    DatabaseHandler dbHandler;
    private boolean is_onlyview;

    public ItemAdapter(Activity activity, ArrayList<model_enquiry> list, boolean is_onlyview) {
        this.list = list;
        this.activity = activity;
        this.is_onlyview = is_onlyview;

        dbHandler = new DatabaseHandler(activity);
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);

        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        final model_enquiry map = list.get(position);

        holder.tvName.setText(map.NAME);
        holder.tvQty.setText(map.QTY);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvQty;


        private ProductHolder(View view) {
            super(view);

            tvName = (TextView) view.findViewById(R.id.tvName);
            tvQty = (TextView) view.findViewById(R.id.tvQty);

        }
    }



}
