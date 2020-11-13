package adapter;

import android.content.Context;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import codecanyon.jagatpharma.R;
import listeners.RepeatOrder;
import model.My_order_model;

/**
 * Created by Rajesh on 2017-09-18.
 */

public class My_order_adapter extends RecyclerView.Adapter<My_order_adapter.MyViewHolder> {

    private List<My_order_model> modelList;
    private Context context;
    public RepeatOrder order;

    public void placeRepeatOrder(RepeatOrder order){
        this.order=order;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, status, date, time, price, items;
        public Button tvRepartOrder;
        public CardView card_view;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_my_order_number);
            status = (TextView) view.findViewById(R.id.tv_my_order_status);
            date = (TextView) view.findViewById(R.id.tv_my_order_date);
            time = (TextView) view.findViewById(R.id.tv_my_order_time);
            price = (TextView) view.findViewById(R.id.tv_my_order_price);
            items = (TextView) view.findViewById(R.id.tv_my_order_items);
            tvRepartOrder=view.findViewById(R.id.tvRepartOrder);
            card_view=view.findViewById(R.id.card_view);

        }

    }

    public My_order_adapter(List<My_order_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public My_order_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_my_order, parent, false);

        context = parent.getContext();

        return new My_order_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(My_order_adapter.MyViewHolder holder, int position) {
        My_order_model mList = modelList.get(position);

        holder.title.setText(mList.getSale_id());

        if (mList.getStatus().equals("0")) {
            holder.status.setText(context.getResources().getString(R.string.pending));
            holder.tvRepartOrder.setVisibility(View.GONE);
        } else if (mList.getStatus().equals("1")) {
            holder.status.setText(context.getResources().getString(R.string.confirm));
            holder.status.setTextColor(context.getResources().getColor(R.color.color_1));
            holder.tvRepartOrder.setVisibility(View.VISIBLE);
        } else if (mList.getStatus().equals("2")) {
            holder.status.setText(context.getResources().getString(R.string.delivered));
            holder.status.setTextColor(context.getResources().getColor(R.color.color_2));
            holder.tvRepartOrder.setVisibility(View.VISIBLE);
        } else if (mList.getStatus().equals("3")) {
            holder.status.setText(context.getResources().getString(R.string.cancel));
            holder.status.setTextColor(context.getResources().getColor(R.color.color_3));
            holder.tvRepartOrder.setVisibility(View.GONE);
        }

        holder.date.setText(mList.getOn_date());
        if (mList.getPayment_type() != null && mList.getPayment_type().length() > 0) {
            holder.time.setText(mList.getPayment_type());
        } else {
            holder.time.setText(context.getResources().getString(R.string.na));
        }
        double d = Double.parseDouble(mList.getTotal_amount());
        holder.price.setText(String.format("%.2f",d));
        holder.items.setText(mList.getTotal_items());

        holder.tvRepartOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.onRepeat(mList);
            }
        });
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.showOrder(mList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}