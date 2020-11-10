package codecanyon.jagatpharma;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import adapter.ItemAdapter;
import model.model_enquiry;

public class EditMedicine extends AppCompatActivity {

    AppCompatButton btnPlaceEnquiry,btnGo;
    EditText edtName,edtQty;
    TextView tvItems;
    ArrayList<model_enquiry> enquiries=new ArrayList<>();
    ItemAdapter adapter;
    RecyclerView rvMedicines;
    String dataAnswer="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_prescription);
        btnPlaceEnquiry=findViewById(R.id.btnPlaceEnquiry);
        edtName=findViewById(R.id.edtName);
        tvItems=findViewById(R.id.tvItems);
        edtQty=findViewById(R.id.edtQty);
        btnGo=findViewById(R.id.btnGo);
        rvMedicines=findViewById(R.id.rvMedicines);
        adapter=new ItemAdapter(this,enquiries,true);
        rvMedicines.setLayoutManager(new LinearLayoutManager(this));
        rvMedicines.setAdapter(adapter);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model_enquiry model=new model_enquiry();
                model.NAME=edtName.getText().toString();
                model.QTY=edtQty.getText().toString();
                dataAnswer +=edtName.getText().toString() + " "+edtQty.getText().toString()+"\n";
                tvItems.setText(dataAnswer);
            }
        });
    }
}
