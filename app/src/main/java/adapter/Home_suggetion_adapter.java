package adapter;

import android.app.Activity;
import android.graphics.Paint;
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
import model.Medical_category_list_model;

/**
 * Created by Rajesh on 2017-09-22.
 */

public class Home_suggetion_adapter extends RecyclerView.Adapter<Home_suggetion_adapter.MyViewHolder> {

    private List<Medical_category_list_model> modelList;
    private Activity context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, price, currency1, currency2, tv_discount;
        private ImageView image, iv_sale;

        private MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.iv_product_img);
            iv_sale = (ImageView) view.findViewById(R.id.iv_product_sale);
            title = (TextView) view.findViewById(R.id.tv_product_title);
            price = (TextView) view.findViewById(R.id.tv_product_price);
            tv_discount = (TextView) view.findViewById(R.id.tv_product_discount_price);
            currency2 = (TextView) view.findViewById(R.id.tv_product_currency);
            currency1 = (TextView) view.findViewById(R.id.tv_product_currency1);

            iv_sale.setVisibility(View.GONE);
        }

    }

    public Home_suggetion_adapter(List<Medical_category_list_model> modelList, Activity context) {
        this.modelList = modelList;
        this.context = context;
    }

    @Override
    public Home_suggetion_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_home_suggested, parent, false);

        return new Home_suggetion_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Home_suggetion_adapter.MyViewHolder holder, int position) {
        Medical_category_list_model mList = modelList.get(position);

        Picasso.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + mList.getProduct_image())
                .placeholder(R.drawable.ic_loading)
                .into(holder.image);

        holder.title.setText(mList.getProduct_name());
        holder.price.setText(mList.getPrice());
        holder.currency1.setText(context.getResources().getString(R.string.rs));
        holder.currency2.setText(context.getResources().getString(R.string.rs));

        //Double items = Double.parseDouble(holder.tv_qty.getText().toString());
        Double get_price = Double.parseDouble(mList.getPrice());

        holder.price.setText("" + get_price);
        holder.tv_discount.setText("" + get_price);

        if (!mList.getDiscount().isEmpty() && !mList.getDiscount().equalsIgnoreCase("0")) {
            holder.iv_sale.setVisibility(View.VISIBLE);
            holder.price.setVisibility(View.VISIBLE);
            holder.currency2.setVisibility(View.VISIBLE);

            holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv_discount.setText(getDiscountPrice(mList.getDiscount(), "" + get_price, true));
        } else {
            holder.price.setVisibility(View.GONE);
            holder.currency2.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    private String getDiscountPrice(String discount, String price, boolean getEffectedprice) {
        Double discount1 = Double.parseDouble(discount);
        Double price1 = Double.parseDouble(price);
        Double discount_amount = discount1 * price1 / 100;

        if (getEffectedprice) {
            Double effected_price = price1 - discount_amount;
            return effected_price.toString();
        } else {
            return discount_amount.toString();
        }
    }

}