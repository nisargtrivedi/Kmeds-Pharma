package adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import Config.BaseURL;
import codecanyon.jagatpharma.R;
import util.DatabaseHandler;

/**
 * Created by Rajesh on 2017-09-12.
 */

public class Cart_adapter extends RecyclerView.Adapter<Cart_adapter.ProductHolder> {

    ArrayList<HashMap<String, String>> list;
    Activity activity;
    DatabaseHandler dbHandler;
    private boolean is_onlyview;

    public Cart_adapter(Activity activity, ArrayList<HashMap<String, String>> list, boolean is_onlyview) {
        this.list = list;
        this.activity = activity;
        this.is_onlyview = is_onlyview;

        dbHandler = new DatabaseHandler(activity);
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cart, parent, false);

        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        final HashMap<String, String> map = list.get(position);

        Picasso.with(activity)
                .load(BaseURL.IMG_PRODUCT_URL + map.get("product_image"))
                .placeholder(R.drawable.ic_loading)
                .into(holder.iv_logo);

        holder.tv_title.setText(map.get("product_name"));

        holder.tv_contetiy.setText(map.get("qty"));

        Double items = Double.parseDouble(dbHandler.getInCartItemQty(map.get("product_id")));
        Double price = Double.parseDouble(map.get("price"));

        holder.tv_price.setText("" + String.format("%.2f",price * items));

        if (!map.get("discount").isEmpty() && !map.get("discount").equalsIgnoreCase("0")) {
            holder.tv_discount_currency.setVisibility(View.VISIBLE);
            holder.tv_discount.setVisibility(View.VISIBLE);

            holder.tv_discount.setText(getDiscountPrice(map.get("discount"), "" + price * items, false));

            holder.tv_price.setPaintFlags(holder.tv_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv_total.setText(getDiscountPrice(map.get("discount"), "" + price * items, true));
        } else {
            holder.tv_discount_currency.setVisibility(View.GONE);
            holder.tv_total.setVisibility(View.GONE);
        }

        holder.iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = 0;
                if (!holder.tv_contetiy.getText().toString().equalsIgnoreCase(""))
                    qty = Integer.valueOf(holder.tv_contetiy.getText().toString());

                if (qty > 0) {
                    qty = qty - 1;
                    /*holder.tv_contetiy.setText(String.valueOf(qty));

                    Double items = Double.parseDouble(holder.tv_contetiy.getText().toString());
                    Double price = Double.parseDouble(map.get("price"));

                    holder.tv_price.setText("" + price * items);

                    if (!map.get("discount").isEmpty() && !map.get("discount").equalsIgnoreCase("0")) {
                        holder.tv_discount.setText(getDiscountPrice(map.get("discount"), "" + price * items, false));

                        holder.tv_price.setPaintFlags(holder.tv_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        holder.tv_total.setText(getDiscountPrice(map.get("discount"), "" + price * items, true));

                        dbHandler.setCart(map, Float.valueOf(holder.tv_contetiy.getText().toString()),
                                Double.parseDouble(getDiscountPrice(map.get("discount"), "" + price * items, false)),
                                Double.parseDouble(getDiscountPrice(map.get("discount"), "" + price * items, true)));
                    } else {
                        dbHandler.setCart(map, Float.valueOf(holder.tv_contetiy.getText().toString()),
                                price * items,
                                price * items);
                    }
*/
                    // set cart data in local database
                    setCartData(holder.tv_contetiy,holder.tv_price,holder.tv_discount,holder.tv_total,map,qty);
                    updateintent();
                }

                if (holder.tv_contetiy.getText().toString().equalsIgnoreCase("0")) {
                    dbHandler.removeItemFromCart(map.get("product_id"));
                    list.remove(position);
                    notifyDataSetChanged();

                    updateintent();
                }
            }
        });

        holder.iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int qty = Integer.valueOf(holder.tv_contetiy.getText().toString());
                qty = qty + 1;

                /*
                holder.tv_contetiy.setText(String.valueOf(qty));

                Double items = Double.parseDouble(holder.tv_contetiy.getText().toString());
                Double price = Double.parseDouble(map.get("price"));

                holder.tv_price.setText("" + price * items);

                if (!map.get("discount").isEmpty() && !map.get("discount").equalsIgnoreCase("0")) {
                    holder.tv_discount.setText(getDiscountPrice(map.get("discount"), "" + price * items, false));

                    holder.tv_price.setPaintFlags(holder.tv_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.tv_total.setText(getDiscountPrice(map.get("discount"), "" + price * items, true));

                    dbHandler.setCart(map, Float.valueOf(holder.tv_contetiy.getText().toString()),
                            Double.parseDouble(getDiscountPrice(map.get("discount"), "" + price * items, false)),
                            Double.parseDouble(getDiscountPrice(map.get("discount"), "" + price * items, true)));
                } else {
                    dbHandler.setCart(map, Float.valueOf(holder.tv_contetiy.getText().toString()),
                            price * items,
                            price * items);
                }*/

                // set cart data in local database
                setCartData(holder.tv_contetiy,holder.tv_price,holder.tv_discount,holder.tv_total,map,qty);

                updateintent();
            }
        });

        holder.tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHandler.removeItemFromCart(map.get("product_id"));
                list.remove(position);
                notifyDataSetChanged();

                updateintent();
            }
        });

    }

    private void setCartData(TextView tv_contetiy, TextView tv_price, TextView tv_discount, TextView tv_total,HashMap<String, String> map,int qty) {

        tv_contetiy.setText(String.valueOf(qty));

        Double items = Double.parseDouble(tv_contetiy.getText().toString());
        Double price = Double.parseDouble(map.get("price"));

        tv_price.setText("" + price * items);

        if (!map.get("discount").isEmpty() && !map.get("discount").equalsIgnoreCase("0")) {
            tv_discount.setText(getDiscountPrice(map.get("discount"), "" + price * items, false));

            tv_price.setPaintFlags(tv_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tv_total.setText(getDiscountPrice(map.get("discount"), "" + price * items, true));

            dbHandler.setCart(map, Float.valueOf(tv_contetiy.getText().toString()),
                    Double.parseDouble(getDiscountPrice(map.get("discount"), "" + price * items, false)),
                    Double.parseDouble(getDiscountPrice(map.get("discount"), "" + price * items, true)));
        } else {
            dbHandler.setCart(map, Float.valueOf(tv_contetiy.getText().toString()),
                    price * items,
                    price * items);
        }
    }

    // get discount price by discount amount and price
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

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        private TextView tv_title, tv_price, tv_total, tv_contetiy,
                tv_unit, tv_unit_value, tv_remove, tv_discount, tv_discount_currency;
        private ImageView iv_logo, iv_plus, iv_minus;

        private ProductHolder(View view) {
            super(view);

            tv_title = (TextView) view.findViewById(R.id.tv_cart_title);
            tv_price = (TextView) view.findViewById(R.id.tv_cart_price);
            tv_total = (TextView) view.findViewById(R.id.tv_cart_discount_price);
            tv_contetiy = (TextView) view.findViewById(R.id.tv_cart_qty);
            iv_logo = (ImageView) view.findViewById(R.id.iv_cart_img);
            iv_plus = (ImageView) view.findViewById(R.id.iv_cart_plus);
            iv_minus = (ImageView) view.findViewById(R.id.iv_cart_minus);
            tv_remove = (TextView) view.findViewById(R.id.tv_cart_remove);
            tv_discount = (TextView) view.findViewById(R.id.tv_cart_discount);
            tv_discount_currency = (TextView) view.findViewById(R.id.tv_cart_discount_currency);

            if (is_onlyview) {
                iv_plus.setVisibility(View.GONE);
                iv_minus.setVisibility(View.GONE);
                ConstraintLayout cl = (ConstraintLayout) view.findViewById(R.id.cl_cart);
                cl.setVisibility(View.GONE);
                View view1 = (View) view.findViewById(R.id.view);
                view1.setVisibility(View.GONE);
            }

        }
    }

    // update intent for update current activity using broadcast intent
    private void updateintent() {
        Intent updates = new Intent("GetPills_cart");
        updates.putExtra("type", "update");
        activity.sendBroadcast(updates);
    }

}
