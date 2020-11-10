package adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import codecanyon.jagatpharma.CommonAppCompatActivity;
import codecanyon.jagatpharma.Offers_detailActivity;
import codecanyon.jagatpharma.R;
import model.Offer_model;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Rajesh on 2017-09-15.
 */

public class Offer_adapter extends RecyclerView.Adapter<Offer_adapter.MyViewHolder> {

    private List<Offer_model> modelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, from_date, to_date, code;
        private ImageView iv_copy;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_offer_title);
            from_date = (TextView) view.findViewById(R.id.tv_offer_date_from);
            to_date = (TextView) view.findViewById(R.id.tv_offer_date_to);
            code = (TextView) view.findViewById(R.id.tv_offer_code);
            iv_copy = (ImageView) view.findViewById(R.id.iv_offer_copy_code);

            iv_copy.setOnClickListener(this);
            title.setOnClickListener(this);
            from_date.setOnClickListener(this);
            to_date.setOnClickListener(this);
            code.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int id = view.getId();

            if (id == R.id.iv_offer_copy_code) {
                //place your TextView's text in clipboard
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                clipboard.setText(code.getText());

                CommonAppCompatActivity.showToast(context, context.getResources().getString(R.string.text_copied));

            } else {
                Offer_model model = modelList.get(getAdapterPosition());

                Intent detailIntent = new Intent(context, Offers_detailActivity.class);
                detailIntent.putExtra("title", model.getOffer_title());
                detailIntent.putExtra("coupon", model.getOffer_coupon());
                detailIntent.putExtra("start_date", model.getOffer_start_date());
                detailIntent.putExtra("end_date", model.getOffer_end_date());
                detailIntent.putExtra("description", model.getOffer_description());
                context.startActivity(detailIntent);
            }

        }
    }

    public Offer_adapter(List<Offer_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Offer_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_offers, parent, false);

        context = parent.getContext();

        return new Offer_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Offer_adapter.MyViewHolder holder, int position) {
        Offer_model mList = modelList.get(position);

        holder.title.setText(mList.getOffer_title());
        holder.from_date.setText(mList.getOffer_start_date());
        holder.to_date.setText(mList.getOffer_end_date());
        holder.code.setText(mList.getOffer_coupon());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}