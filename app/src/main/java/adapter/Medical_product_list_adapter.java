package adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import Config.BaseURL;
import codecanyon.jagatpharma.CommonAppCompatActivity;
import codecanyon.jagatpharma.Producat_detailActivity;
import codecanyon.jagatpharma.R;
import model.Medical_category_list_model;
import util.DatabaseHandler;

/**
 * Created by Rajesh on 2017-09-11.
 */

public class Medical_product_list_adapter extends RecyclerView.Adapter<Medical_product_list_adapter.MyViewHolder> {

    private List<Medical_category_list_model> modelList;
    private Activity context;
    private DatabaseHandler dbcart;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title, price, currency1, currency2, tv_qty, tv_add, tv_discount;
        private ImageView image, iv_plus, iv_minus, iv_sale, iv_add;

        private MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.iv_product_img);
            iv_sale = (ImageView) view.findViewById(R.id.iv_product_sale);
            title = (TextView) view.findViewById(R.id.tv_product_title);
            price = (TextView) view.findViewById(R.id.tv_product_price);
            tv_discount = (TextView) view.findViewById(R.id.tv_product_discount_price);
            currency2 = (TextView) view.findViewById(R.id.tv_product_currency);
            currency1 = (TextView) view.findViewById(R.id.tv_product_currency1);
            iv_plus = (ImageView) view.findViewById(R.id.iv_product_plus);
            iv_minus = (ImageView) view.findViewById(R.id.iv_product_minus);
            tv_qty = (TextView) view.findViewById(R.id.tv_product_qty);
            //tv_add = (TextView) view.findViewById(R.id.tv_product_add);
            iv_add = (ImageView) view.findViewById(R.id.iv_product_add);

            image.setOnClickListener(this);
            title.setOnClickListener(this);
            price.setOnClickListener(this);
            currency1.setOnClickListener(this);
            iv_plus.setOnClickListener(this);
            iv_minus.setOnClickListener(this);
            iv_add.setOnClickListener(this);

            iv_sale.setVisibility(View.GONE);

            iv_add.setBackgroundResource(R.drawable.ic_menu_cart);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();

            Medical_category_list_model model = modelList.get(position);

            if (id == R.id.iv_product_minus) {
                int qty = 0;

                if (!tv_qty.getText().toString().equalsIgnoreCase(""))
                    qty = Integer.valueOf(tv_qty.getText().toString());

                if (qty > 0) {
                    qty = qty - 1;
                    tv_qty.setText(String.valueOf(qty));

                    Double items = Double.parseDouble(tv_qty.getText().toString());
                    Double get_price = Double.parseDouble(model.getPrice());

                    price.setText("" + get_price * items);
                    tv_discount.setText("" + get_price * items);

                    if (!model.getDiscount().isEmpty() && !model.getDiscount().equals("0")) {
                        iv_sale.setVisibility(View.VISIBLE);
                        price.setVisibility(View.VISIBLE);
                        currency2.setVisibility(View.VISIBLE);

                        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        tv_discount.setText(getDiscountPrice(model.getDiscount(), "" + get_price * items, true));
                    } else {
                        price.setVisibility(View.GONE);
                        currency2.setVisibility(View.GONE);
                    }
                }

            } else if (id == R.id.iv_product_plus) {
                int qty = Integer.valueOf(tv_qty.getText().toString());
                qty = qty + 1;

                tv_qty.setText(String.valueOf(qty));

                Double items = Double.parseDouble(tv_qty.getText().toString());
                Double get_price = Double.parseDouble(model.getPrice());

                price.setText("" + get_price * items);
                tv_discount.setText("" + get_price * items);

                if (!model.getDiscount().isEmpty() && !model.getDiscount().equals("0")) {
                    iv_sale.setVisibility(View.VISIBLE);
                     price.setVisibility(View.VISIBLE);
                    currency2.setVisibility(View.VISIBLE);

                    price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    tv_discount.setText(getDiscountPrice(model.getDiscount(), "" + get_price * items, true));
                } else {
                    price.setVisibility(View.GONE);
                    currency2.setVisibility(View.GONE);
                }

            } else if (id == R.id.iv_product_add) {

                HashMap<String, String> map = new HashMap<>();
                map.put("product_id", model.getProduct_id());
                map.put("product_name", model.getProduct_name());
                map.put("product_image", model.getProduct_image());
                map.put("category_id", model.getCategory_id());
                map.put("in_stock", model.getIn_stock());
                map.put("price", model.getPrice());
                map.put("unit_value", model.getUnit_value());
                map.put("unit", model.getUnit());
                map.put("mfg_id", model.getMfg_id());
                map.put("isgeneric", model.getIsgeneric());
                map.put("group_name", model.getGroup_name());
                map.put("discount", model.getDiscount());
                map.put("stock", model.getStock());
                map.put("title", model.getTitle());
                map.put("mfg_name", model.getMfg_name());

                if (!tv_qty.getText().toString().equalsIgnoreCase("0")) {

                    Double items = Double.parseDouble(tv_qty.getText().toString());
                    Double price = Double.parseDouble(model.getPrice());

                    if (!model.getDiscount().isEmpty() && !model.getDiscount().equals("0")) {

                        dbcart.setCart(map, Float.valueOf(tv_qty.getText().toString()),
                                Double.parseDouble(getDiscountPrice(map.get("discount"), "" + price * items, false)),
                                Double.parseDouble(getDiscountPrice(map.get("discount"), "" + price * items, true)));
                    } else {
                        dbcart.setCart(map, Float.valueOf(tv_qty.getText().toString()),
                                price * items,
                                price * items);
                    }

                    //dbcart.setCart(map, Float.valueOf(tv_qty.getText().toString()));
                    //tv_add.setText(context.getResources().getString(R.string.update));
                    iv_add.setBackgroundResource(R.drawable.ic_cart_update);
                } else {
                    dbcart.removeItemFromCart(map.get("product_id"));
                    //tv_add.setText(context.getResources().getString(R.string.add));
                    iv_add.setBackgroundResource(R.drawable.ic_menu_cart);
                }

                CommonAppCompatActivity commonAppCompatActivity = new CommonAppCompatActivity();
                commonAppCompatActivity.updateCounter(context);

            } else {

                Intent i = new Intent(context, Producat_detailActivity.class);
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
                context.startActivity(i);

            }
        }
    }

    public Medical_product_list_adapter(List<Medical_category_list_model> modelList, Activity context) {
        this.modelList = modelList;
        this.context = context;
        dbcart = new DatabaseHandler(context);
    }

    @Override
    public Medical_product_list_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_product_list, parent, false);

        return new Medical_product_list_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Medical_product_list_adapter.MyViewHolder holder, int position) {
        Medical_category_list_model mList = modelList.get(position);

        Picasso.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + mList.getProduct_image())
                .placeholder(R.drawable.ic_loading)
                .into(holder.image);

        holder.title.setText(mList.getProduct_name());
        holder.price.setText(mList.getPrice());
        holder.currency1.setText(context.getResources().getString(R.string.rs));
        holder.currency2.setText(context.getResources().getString(R.string.rs));

        if (dbcart.isInCart(mList.getProduct_id())) {
            holder.tv_qty.setText(dbcart.getCartItemQty(mList.getProduct_id()));
            holder.iv_add.setBackgroundResource(R.drawable.ic_cart_update);
        }else{
            holder.tv_qty.setText("1");
            holder.iv_add.setBackgroundResource(R.drawable.ic_menu_cart);
        }

        /*if (!mList.getDiscount().isEmpty() && !mList.getDiscount().equals("0")) {
            holder.iv_sale.setVisibility(View.VISIBLE);
            holder.price.setVisibility(View.VISIBLE);
            holder.currency2.setVisibility(View.VISIBLE);

            Double discount1 = Double.parseDouble(mList.getDiscount());
            Double price = Double.parseDouble(mList.getPrice());
            Double discount_amount = discount1 * price / 100;

            Double effected_price = price - discount_amount;
            holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv_discount.setText("" + effected_price);
        } else {
            holder.price.setVisibility(View.GONE);
            holder.currency2.setVisibility(View.GONE);
            holder.tv_discount.setText(mList.getPrice());
        }*/

        Double items = Double.parseDouble(holder.tv_qty.getText().toString());
        Double get_price = Double.parseDouble(mList.getPrice());

        holder.price.setText("" + get_price * items);
        holder.tv_discount.setText("" + get_price * items);

        if (!mList.getDiscount().isEmpty() && !mList.getDiscount().equalsIgnoreCase("0")) {
            holder.iv_sale.setVisibility(View.VISIBLE);
            holder. price.setVisibility(View.VISIBLE);
            holder.currency2.setVisibility(View.VISIBLE);

            holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv_discount.setText(getDiscountPrice(mList.getDiscount(), "" + get_price * items, true));
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
            return String.format("%.2f",effected_price);
        } else {
            return String.format("%.2f",discount_amount);
        }
    }

}