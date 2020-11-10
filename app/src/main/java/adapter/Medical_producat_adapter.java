package adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import Config.BaseURL;
import codecanyon.jagatpharma.R;
import model.Medical_category_model;

/**
 * Created by Rajesh on 2017-09-11.
 */

public class Medical_producat_adapter extends RecyclerView.Adapter<Medical_producat_adapter.MyViewHolder> {

    private List<Medical_category_model> modelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,total;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.iv_medical_product_img);
            title = (TextView) view.findViewById(R.id.tv_medical_product_title);
            //total = (TextView) view.findViewById(R.id.tv_medical_product_total);
        }
    }

    public Medical_producat_adapter(List<Medical_category_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Medical_producat_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_medical_product, parent, false);

        context = parent.getContext();

        return new Medical_producat_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Medical_producat_adapter.MyViewHolder holder, int position) {
        Medical_category_model mList = modelList.get(position);

        Picasso.with(context)
                .load(BaseURL.IMG_CATEGORY_URL + mList.getImage())
                .placeholder(R.drawable.ic_loading)
                .into(holder.image);

        holder.title.setText(mList.getTitle());
        //holder.total.setText(mList.getParent());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}