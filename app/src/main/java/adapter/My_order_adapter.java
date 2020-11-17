package adapter;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import codecanyon.jagatpharma.R;
import listeners.RepeatOrder;
import model.My_order_model;

import static java.util.concurrent.TimeUnit.DAYS;

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
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
       // String formattedDate = df.format(c);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Parsing the given String to Date object
        try {
            Date date = formatter.parse(mList.getOn_date());
            System.out.println("DAYS IS _________"+printDifference(date,c));
            if(printDifference(date,c)==28){
                simpleteNotification("K Meds Pharma","Your Medicines are running low. Order for a new medicine or contact K Meds",""
                );
            }
            else if(printDifference(date,c)==29){
                simpleteNotification("K Meds Pharma","Your Medicines are running low. Order for a new medicine or contact K Meds",""
                );
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

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


    private void simpleteNotification(String title, String message, String timeStamp) {

        String CHANNEL_ID = "98762";// The id of the channel.
        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.app_name);// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        }

        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(bm)
                .setContentTitle(title)
                .setContentText(Html.fromHtml(message))
                .setAutoCancel(true)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setSound(defaultSoundUri)
                .setChannelId(CHANNEL_ID);
                //.setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    public long printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        return elapsedDays;
    }
}