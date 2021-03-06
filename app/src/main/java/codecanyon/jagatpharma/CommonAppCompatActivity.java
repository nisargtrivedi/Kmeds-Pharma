package codecanyon.jagatpharma;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import util.DatabaseHandler;

/**
 * Created by Rajesh on 2017-09-06.
 */

public class CommonAppCompatActivity extends AppCompatActivity {

    private TextView totalBudgetCount,totalBellCount;
    private static DatabaseHandler dbcart;

    public CommonAppCompatActivity() {
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem cart = menu.findItem(R.id.action_cart);
        final MenuItem notification = menu.findItem(R.id.action_notification);


        View count = cart.getActionView();
        View noti=notification.getActionView();

        count.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menu.performIdentifierAction(cart.getItemId(), 0);
            }
        });

        noti.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menu.performIdentifierAction(notification.getItemId(), 0);
            }
        });

        totalBudgetCount = (TextView) count.findViewById(R.id.tv_action_cart);
        totalBellCount = (TextView) noti.findViewById(R.id.tv_action_notification);


        dbcart = new DatabaseHandler(this);

        updateCounter(this);
        updateBellCounter();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                //onBackPressed();
                this.finish();
                return true;
            case R.id.action_cart:
                if (dbcart.getCartCount() > 0) {
                    Intent cartIntent = new Intent(this, CartActivity.class);
                    startActivity(cartIntent);
                } else {
                    showToast(this, getResources().getString(R.string.cart_empty));
                }
                return true;
            case R.id.action_notification:
                if (dbcart.getNotificationCount() > 0) {
                    Intent cartIntent = new Intent(this, NotificationActivity.class);
                    startActivity(cartIntent);
                } else {
                    showToast(this, getResources().getString(R.string.cart_empty));
                }
                return true;

            case R.id.action_change_password:
                Intent changeIntent = new Intent(this, Change_passwordActivity.class);
                startActivity(changeIntent);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    // common toast for listing in app
    public static void showListToast(Context context, boolean isEmpty) {
        if (isEmpty) {
            Toast.makeText(context, context.getResources().getString(R.string.record_not_found), Toast.LENGTH_SHORT).show();
        }
    }

    // common toast for app
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public void updateCounter(Activity context) {
        if (totalBudgetCount != null) {
            totalBudgetCount.setText("" + dbcart.getCartCount());
        } else {
            context.invalidateOptionsMenu();
        }
    }
    public void updateBellCounter() {
        if (totalBellCount != null) {
            totalBellCount.setText("" + dbcart.getNotificationCount());
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateCounter(this);
        updateBellCounter();
    }
}
