package codecanyon.jagatpharma;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import java.util.Calendar;
import java.util.List;

import adapter.DosageAdapter;
import codecanyon.jagatpharma.databinding.ActivityDosageReminderBinding;
import model.DosageModel;
import util.DatabaseHandler;
import util.DosageBroadCastReceiver;

public class Activity_DosageReminder extends CommonAppCompatActivity {

    ActivityDosageReminderBinding binding;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    Intent intent;

    int hour=0;
    int minutes=0;
    String am_pm = "";
    DosageAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHandler handler=new DatabaseHandler(Activity_DosageReminder.this);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_dosage_reminder);

        Calendar datetime = Calendar.getInstance();
        hour=datetime.get(Calendar.HOUR);
        minutes=datetime.get(Calendar.MINUTE);

        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        binding.timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        hour=hourOfDay;
                        minutes=minute;

                        Calendar datetime2 = Calendar.getInstance();
                        datetime2.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        datetime2.set(Calendar.MINUTE, minute);
                        if (datetime2.get(Calendar.AM_PM) == Calendar.AM)
                            am_pm = "AM";
                        else if (datetime2.get(Calendar.AM_PM) == Calendar.PM)
                            am_pm = "PM";

            }
        });
        List<DosageModel> models=handler.getAllDosage();
        adapter = new DosageAdapter(Activity_DosageReminder.this,models);
        binding.lvReminder.setAdapter(adapter);
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(binding.edtMedicine.getText().toString())){
                        Toast.makeText(Activity_DosageReminder.this,"Please enter medicine name",Toast.LENGTH_LONG).show();
                }else{
                    DosageModel model=new DosageModel();
                    model.MedicineName=binding.edtMedicine.getText().toString();
                    model.Time=hour+":"+minutes +" "+am_pm;
                    if(handler.insertDosage(model)){
                        if(models!=null) {
                            List<DosageModel> models=handler.getAllDosage();
                            if(models!=null)
                                adapter.insertData(models);

                        }
                    }
                    startAlertAtParticularTime();
                }
            }
        });
    }
    public void startAlertAtParticularTime() {

        // alarm first vibrate at 14 hrs and 40 min and repeat itself at ONE_HOUR interval

        intent = new Intent(this, DosageBroadCastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 280192, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(this, "Medicine Reminder will vibrate at time specified",
                Toast.LENGTH_SHORT).show();

    }
}
