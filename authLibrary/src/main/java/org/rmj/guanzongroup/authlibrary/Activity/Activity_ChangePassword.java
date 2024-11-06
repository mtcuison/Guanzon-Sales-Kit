package org.rmj.guanzongroup.authlibrary.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.etc.LoadDialog;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.lib.Account.pojo.PasswordChange;
import org.rmj.guanzongroup.authlibrary.R;
import org.rmj.guanzongroup.authlibrary.ViewModels.VMChangePassword;

public class Activity_ChangePassword extends AppCompatActivity {
    private VMChangePassword mViewModel;
    private LoadDialog poDialog;
    private MessageBox poMessage;
    private TextInputEditText password;
    private TextInputEditText conf_password;
    private  MaterialButton btn_submit;
    private MaterialToolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mViewModel = new ViewModelProvider(Activity_ChangePassword.this).get(VMChangePassword.class);
        poDialog = new LoadDialog(this);
        poMessage = new MessageBox(this);

        poMessage.initDialog();
        poMessage.setTitle("Guanzon Sales Kit");
        poMessage.setPositiveButton("Close", new MessageBox.DialogButton() {
            @Override
            public void OnButtonClick(View view, AlertDialog dialog) {
                dialog.dismiss();
            }
        });

        toolbar = findViewById(R.id.toolbar);
        password = findViewById(R.id.password);
        conf_password = findViewById(R.id.conf_password);
        btn_submit = findViewById(R.id.btn_submit);

        setSupportActionBar(toolbar); //set object toolbar as default action bar for activity

        getSupportActionBar().setTitle(" "); //set default title for action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //set back button to toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true); //enable the back button set on toolbar

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordChange loPassword = new PasswordChange();
                loPassword.setsNewPswd(password.getText().toString());
                loPassword.setsConfPswd(password.getText().toString());

                mViewModel.ChangePassword(loPassword, new VMChangePassword.SubmitChanges() {
                    @Override
                    public void onChange(String title, String message) {
                        poDialog.initDialog(title, message, false);
                        poDialog.show();
                    }

                    @Override
                    public void onSuccess() {
                        poDialog.dismiss();

                        poMessage.setIcon(org.rmj.g3appdriver.R.drawable.baseline_message_24);
                        poMessage.setMessage("Request sent. Please verify new password sent to your email.");
                        poMessage.show();
                    }

                    @Override
                    public void onFailed(String result) {
                        poDialog.dismiss();

                        poMessage.setIcon(org.rmj.g3appdriver.R.drawable.baseline_error_24);
                        poMessage.setMessage(result);
                        poMessage.show();
                    }
                });
            }
        });

        overridePendingTransition(org.rmj.g3appdriver.R.anim.anim_intent_slide_in_left, org.rmj.g3appdriver.R.anim.anim_intent_slide_out_right);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(org.rmj.g3appdriver.R.anim.anim_intent_slide_in_left, org.rmj.g3appdriver.R.anim.anim_intent_slide_out_right);
    }
}