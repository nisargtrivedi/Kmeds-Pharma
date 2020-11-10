package adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viethoa.RecyclerViewFastScroller;

import java.util.ArrayList;
import java.util.List;

import codecanyon.jagatpharma.R;
import model.Medical_category_list_model;

/**
 * Created by Rajesh on 2017-09-21.
 */

public class Prescription_list_adapter2 extends RecyclerView.Adapter<Prescription_list_adapter2.MyViewHolder>
        implements RecyclerViewFastScroller.BubbleTextGetter {

    private List<Medical_category_list_model> modelList;
    private List<Medical_category_list_model> mFilteredList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_prescription_title);
            count = (TextView) view.findViewById(R.id.tv_prescription_count);

            count.setVisibility(View.GONE);
        }
    }

    public Prescription_list_adapter2(List<Medical_category_list_model> modelList) {
        this.modelList = modelList;
        this.mFilteredList = modelList;
    }

    @Override
    public Prescription_list_adapter2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_prescription, parent, false);

        context = parent.getContext();

        return new Prescription_list_adapter2.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Prescription_list_adapter2.MyViewHolder holder, int position) {
        Medical_category_list_model mList = modelList.get(position);

        holder.title.setText(mList.getProduct_name());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @Override
    public String getTextToShowInBubble(int position) {
        if (position < 0 || position >= modelList.size())
            return null;

        String name = modelList.get(position).getTitle();
        if (name == null || name.length() < 1)
            return null;

        return modelList.get(position).getTitle().substring(0, 1);
    }

    public void filter(List<Medical_category_list_model> models, String query) {

        query = query.toLowerCase();

        final ArrayList<Medical_category_list_model> filteredModelList = new ArrayList<>();
        for (Medical_category_list_model model : models) {
            final String text = model.getProduct_name().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }

        this.mFilteredList = filteredModelList;
        this.modelList = filteredModelList;
        notifyDataSetChanged();
    }


}
