package adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import Config.BaseURL;
import codecanyon.jagatpharma.My_prescription_viewActivity;
import codecanyon.jagatpharma.R;
import model.My_prescription_model;

/**
 * Created by Rajesh on 2017-09-21.
 */

public class My_prescription_adapter extends RecyclerView.Adapter<My_prescription_adapter.MyViewHolder> {

    private List<My_prescription_model> modelList;
    private Context context;
    public String pres_id = "";

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView order_no, date, status, view_detail;
        private ImageView iv_img1, iv_img2, iv_img3;

        public MyViewHolder(View view) {
            super(view);
            iv_img1 = (ImageView) view.findViewById(R.id.iv_my_prescription_img_1);
            iv_img2 = (ImageView) view.findViewById(R.id.iv_my_prescription_img_2);
            iv_img3 = (ImageView) view.findViewById(R.id.iv_my_prescription_img_3);
            order_no = (TextView) view.findViewById(R.id.tv_my_prescription_number);
            date = (TextView) view.findViewById(R.id.tv_my_prescription_date);
            status = (TextView) view.findViewById(R.id.tv_my_prescription_status);
            view_detail = (TextView) view.findViewById(R.id.tv_my_prescription_view);

            iv_img1.setOnClickListener(this);
            iv_img2.setOnClickListener(this);
            iv_img3.setOnClickListener(this);
            view_detail.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();

            if (id == R.id.iv_my_prescription_img_1) {
                showImages(modelList.get(position).getPrescription_img1());
            } else if (id == R.id.iv_my_prescription_img_2) {
                showImages(modelList.get(position).getPrescription_img2());
            } else if (id == R.id.iv_my_prescription_img_3) {
                showImages(modelList.get(position).getPrescription_img3());
            } else if (id == R.id.tv_my_prescription_view) {
                Intent viewIntent = new Intent(context, My_prescription_viewActivity.class);
                viewIntent.putExtra("pres_id", modelList.get(position).getPres_id());
                viewIntent.putExtra("prescriptionData", (Serializable) modelList.get(position));
                context.startActivity(viewIntent);
            }
        }
    }

    public My_prescription_adapter(Context context, List<My_prescription_model> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @Override
    public My_prescription_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_my_prescription, parent, false);
        return new My_prescription_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(My_prescription_adapter.MyViewHolder holder, int position) {
        My_prescription_model mList = modelList.get(position);

        if (mList.getPrescription_img1().isEmpty()) {
            holder.iv_img1.setVisibility(View.GONE);
        } else {
            holder.iv_img1.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(BaseURL.IMG_PRESCRIPTION_URL + mList.getPrescription_img1())
                    .placeholder(R.drawable.ic_loading)
                    .into(holder.iv_img1);
        }

        if (mList.getPrescription_img2().isEmpty()) {
            holder.iv_img2.setVisibility(View.GONE);
        } else {
            holder.iv_img2.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(BaseURL.IMG_PRESCRIPTION_URL + mList.getPrescription_img2())
                    .placeholder(R.drawable.ic_loading)
                    .into(holder.iv_img2);
        }

        if (mList.getPrescription_img3().isEmpty()) {
            holder.iv_img3.setVisibility(View.GONE);
        } else {
            holder.iv_img3.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(BaseURL.IMG_PRESCRIPTION_URL + mList.getPrescription_img3())
                    .placeholder(R.drawable.ic_loading)
                    .into(holder.iv_img3);
        }

        holder.order_no.setText(mList.getPres_id());
        holder.date.setText(mList.getOn_date());

        if (mList.getStatus().equals("0")) {
            holder.status.setText(context.getResources().getString(R.string.pending));
        } else if (mList.getStatus().equals("1")) {
            holder.status.setText(context.getResources().getString(R.string.confirm));
            holder.status.setTextColor(context.getResources().getColor(R.color.color_1));
        } else if (mList.getStatus().equals("2")) {
            holder.status.setText(context.getResources().getString(R.string.delivered));
            holder.status.setTextColor(context.getResources().getColor(R.color.color_2));
        } else if (mList.getStatus().equals("3")) {
            holder.status.setText(context.getResources().getString(R.string.cancel));
            holder.status.setTextColor(context.getResources().getColor(R.color.color_3));
        }

        if (mList.getPres_id().equals(pres_id)) {
            pres_id = "";

            Intent viewIntent = new Intent(context, My_prescription_viewActivity.class);
            viewIntent.putExtra("pres_id", mList.getPres_id());
            viewIntent.putExtra("prescriptionData", (Serializable) mList);
            context.startActivity(viewIntent);
        }

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    // showing custom alertdialog with viewpager images
    private void showImages(String imagepath) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image_zoom);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        ImageView iv_dialoge_cancle = (ImageView) dialog.findViewById(R.id.iv_dialog_cancle);
        ImageView iv_dialoge_img = (ImageView) dialog.findViewById(R.id.iv_dialog_img);

        Picasso.with(context)
                .load(BaseURL.IMG_PRESCRIPTION_URL + imagepath)
                .placeholder(R.drawable.ic_loading)
                .skipMemoryCache()
                .resize(1024, 1024)
                .into(iv_dialoge_img);

        iv_dialoge_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

}