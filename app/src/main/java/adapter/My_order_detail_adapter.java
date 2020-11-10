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
import model.My_order_detail_model;

/**
 * Created by Rajesh on 2017-09-19.
 */

public class My_order_detail_adapter extends RecyclerView.Adapter<My_order_detail_adapter.MyViewHolder> {

    private List<My_order_detail_model> modelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, qty, currency, price;
        private ImageView iv_img;

        public MyViewHolder(View view) {
            super(view);
            iv_img = (ImageView) view.findViewById(R.id.iv_order_detail_img);
            title = (TextView) view.findViewById(R.id.tv_order_detail_title);
            qty = (TextView) view.findViewById(R.id.tv_order_detail_qty);
            currency = (TextView) view.findViewById(R.id.tv_order_detail_currency);
            price = (TextView) view.findViewById(R.id.tv_order_detail_price);

        }

    }

    public My_order_detail_adapter(List<My_order_detail_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public My_order_detail_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_my_order_detail, parent, false);

        context = parent.getContext();

        return new My_order_detail_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(My_order_detail_adapter.MyViewHolder holder, int position) {
        My_order_detail_model mList = modelList.get(position);

        Picasso.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + mList.getProduct_image())
                .placeholder(R.drawable.ic_loading)
                .into(holder.iv_img);

        holder.title.setText(mList.getProduct_name());
        holder.qty.setText(mList.getQty());
        holder.price.setText(mList.getPrice());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}