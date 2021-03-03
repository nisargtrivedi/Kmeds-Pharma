package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import codecanyon.jagatpharma.R;
import model.DosageModel;

public class DosageAdapter extends BaseAdapter {

    List<DosageModel> list;
    LayoutInflater inflater;
    Context context;

    public DosageAdapter(Context context,List<DosageModel> list){
        this.list=list;
        inflater= LayoutInflater.from(context);
        this.context=context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.dosage_row,parent,false);
            holder=new Holder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (Holder) convertView.getTag();
        }
        if(list!=null && list.size()>0){
            holder.tvMedicineName.setText(list.get(position).MedicineName);
            holder.tvMedicineTime.setText(list.get(position).Time);
        }
        return convertView;
    }

   public class  Holder{
        TextView tvMedicineName,tvMedicineTime;
        public Holder(View v){
            tvMedicineTime=v.findViewById(R.id.tvMedicineTime);
            tvMedicineName=v.findViewById(R.id.tvMedicineName);

        }
    }
    public void insertData(List<DosageModel> model){
        if(model.size()>0) {
            list.clear();
            list.addAll(model);
        }
        notifyDataSetChanged();
    }
}
