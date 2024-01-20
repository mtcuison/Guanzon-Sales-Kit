package org.rmj.guanzongroup.authlibrary.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.rmj.g3appdriver.etc.AppConfigPreference;
import org.rmj.g3appdriver.etc.LoadDialog;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.lib.Account.pojo.UserAuthInfo;
import org.rmj.guanzongroup.authlibrary.Callbacks.LoginCallback;
import org.rmj.guanzongroup.authlibrary.R;
import org.rmj.guanzongroup.authlibrary.ViewModels.VMLogin;

import java.util.Objects;

public class Activity_Login extends AppCompatActivity implements LoginCallback {
    private TextInputEditText tie_username;
    private TextInputEditText tie_password;
    private MaterialTextView lblVersion;
    private MaterialTextView mtv_createaccount;
    private MaterialCheckBox cbAgree;
    private MaterialButton btn_log;
    private VMLogin mViewModel;
    private LoadDialog podialog;
    private MessageBox poMessage;
    private AppConfigPreference poConfigx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mViewModel = new ViewModelProvider(this).get(VMLogin.class);
        poConfigx = AppConfigPreference.getInstance(this);
        podialog = new LoadDialog(this);
        poMessage = new MessageBox(this);

        poConfigx.setProductID("GuanzonApp");
        poConfigx.setTestCase(true);

        poMessage.initDialog();
        poMessage.setTitle("Guanzon Sales Kit");
        poMessage.setPositiveButton("Close", (view, dialog) -> dialog.dismiss());

        tie_username = findViewById(R.id.username);
        tie_password = findViewById(R.id.password);
        lblVersion = findViewById(R.id.lbl_versionInfo);
        mtv_createaccount = findViewById(R.id.mtv_createaccount);
        cbAgree = findViewById(R.id.cbAgree);
        btn_log = findViewById(R.id.btn_log);

        cbAgree.setChecked(poConfigx.isAgreedOnTermsAndConditions());
        cbAgree.addOnCheckedStateChangedListener(new MaterialCheckBox.OnCheckedStateChangedListener() {
            @Override
            public void onCheckedStateChangedListener(@NonNull MaterialCheckBox checkBox, int state) {
                if (state == 0){
                    mViewModel.setAgreedOnTerms(false);
                } else if (state == 1) {
                    mViewModel.setAgreedOnTerms(true);
                }
            }
        });

        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Objects.requireNonNull(tie_username.getText()).toString();
                String password = Objects.requireNonNull(tie_password.getText()).toString();
                String mobileno = poConfigx.getMobileNo();
                
                if (!mViewModel.isAgreed()){
                    poMessage.setMessage("Please agree to terms and conditions");
                    poMessage.show();
                    
                    return;
                }

                mViewModel.Login(new UserAuthInfo(email,password, mobileno), Activity_Login.this);
            }
        });

        lblVersion.setText(poConfigx.getVersionInfo());

        mtv_createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Login.this, Activity_CreateAccount.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void OnAuthenticationLoad(String Title, String Message) {
        podialog.initDialog(Title, Message, false);
        podialog.show();
    }

    @Override
    public void OnSuccessLoginResult() {
        podialog.dismiss();

        Intent loIntent = new Intent();
        this.setResult(Activity.RESULT_OK, loIntent);
        this.finish();
    }

    @Override
    public void OnFailedLoginResult(String message) {
        podialog.dismiss();

        poMessage.setMessage(message);
        poMessage.show();
    }
}