package codecanyon.jagatpharma;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.UUID;

import Config.BaseURL;
import util.ConnectivityReceiver;
import util.Session_management;

public class ReferralCode extends CommonAppCompatActivity {
    TextView referallCode;
    private Session_management sessionManagement;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_referalcode);
        sessionManagement = new Session_management(this);
        String mobile = sessionManagement.getUserDetails().get(BaseURL.KEY_MOBILE);
        String email = sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL);

        String code=mobile+email.substring(0,5);
        referallCode=findViewById(R.id.referallCode);
        referallCode.setText(code);
        referallCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("REFERALCODE", code);
                clipboard.setPrimaryClip(clip);
                ConnectivityReceiver.showSnackbar(ReferralCode.this,"Code Copied!");
                return true;
            }
        });
    }
}
