package codecanyon.jagatpharma;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;

import util.Session_management;

public class SplashActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_WRITE_FIELS = 102;
    private AlertDialog dialog;

    private Session_management sessionManagement;
    ImageView imageViewScooter,imageView6;
    TextView txt;
    String currentVersion = "0";
    String newVersion = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // title remove
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        imageViewScooter=findViewById(R.id.imageViewScooter);
        imageView6=findViewById(R.id.imageView6);
        txt=findViewById(R.id.txt);

        sessionManagement = new Session_management(SplashActivity.this);
//        TranslateAnimation animation = new TranslateAnimation(0.0f, 600.0f,
//                0.0f, 0.0f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
//        animation.setDuration(2000);  // animation duration
//        animation.setRepeatCount(1);  // animation repeat count
//        animation.setRepeatMode(1);   // repeat animation (left to right, right to left )
//        animation.setFillAfter(true);
//        Animation aniSlide = AnimationUtils.loadAnimation(this,R.anim.zoom_out);
//        Animation aniSlide2 = AnimationUtils.loadAnimation(this,R.anim.zoom_in);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                imageView6.startAnimation(aniSlide2);
//
//                //txt.startAnimation(aniSlide2);
//            }
//        },1000);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                txt.setVisibility(View.VISIBLE);
//            }
//        },2000);
//        imageViewScooter.startAnimation(animation);
        checkAppPermissions();

    }

    // checking app permissions is granted or not
    public void checkAppPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.INTERNET) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_NETWORK_STATE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CAMERA) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {
                go_next();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.INTERNET,
                                android.Manifest.permission.ACCESS_NETWORK_STATE,
                                android.Manifest.permission.CAMERA,
                                Manifest.permission.CALL_PHONE
                        },
                        MY_PERMISSIONS_REQUEST_WRITE_FIELS);
            }
        } else {
            //go_next();
            new FetchAppVersionFromGooglePlayStore().execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_FIELS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                go_next();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setMessage("App required some permission please enable it")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                openPermissionScreen();
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.dismiss();
                            }
                        });
                dialog = builder.show();
            }
            return;
        }
    }

    public void go_next() {
        if (sessionManagement.isLoggedIn()) {
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            if (getIntent().getStringExtra("isorder") != null) {
                mainIntent.putExtra("isorder", "true");
            }
            if (getIntent().getStringExtra("isPrescription") != null) {
                mainIntent.putExtra("isPrescription", "true");
                mainIntent.putExtra("pres_id", getIntent().getStringExtra("pres_id"));
            }
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainIntent);
        }
        else {
            Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginIntent);
        }


    }

    public void openPermissionScreen() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", SplashActivity.this.getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    class FetchAppVersionFromGooglePlayStore extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
//            super.onPreExecute();
            try {

                currentVersion = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

                Log.e("Current Version", "::" + currentVersion);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        protected String doInBackground(String... urls) {
            String newVersion = null;

            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;

        }

        protected void onPostExecute(String onlineVersion) {
            newVersion = onlineVersion;
            System.out.println("NEW VERSION ===>" + onlineVersion);
            Log.d("new Version", newVersion);

            if (onlineVersion != null && !onlineVersion.isEmpty()) {

                if (onlineVersion.equals(currentVersion)) {
                   // Log.i("HASH_KEY", Utility.printKeyHash(Splash.this));
                    //appPreferences = new AppPreferences(Splash.this);
                    //System.out.println("USERID=========>" + appPreferences.getString("USERID"));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                go_next();
                            } catch (Exception ex) {
                            }
                        }
                    }, 3000);

                } else {
                    String message = String.format("%s recommends that to update the latest version for more products and offers.", getString(R.string.app_name));
                    AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this).create();
                    alertDialog.setTitle("Force Update");
                    alertDialog.setIcon(R.mipmap.ic_launcher);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage(message);

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Update", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                            }
                        }
                    });

                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            onBackPressed();
                        }
                    });

                    alertDialog.show();
                }

            }
        }
    }
}
