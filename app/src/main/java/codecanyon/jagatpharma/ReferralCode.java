package codecanyon.jagatpharma;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import Config.BaseURL;
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

        referallCode=findViewById(R.id.referallCode);
        referallCode.setText(mobile);
    }
}
