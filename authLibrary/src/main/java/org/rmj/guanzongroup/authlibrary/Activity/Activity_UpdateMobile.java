package org.rmj.guanzongroup.authlibrary.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.etc.LoadDialog;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.guanzongroup.authlibrary.R;
import org.rmj.guanzongroup.authlibrary.ViewModels.VMUpdateMobile;

public class Activity_UpdateMobile extends AppCompatActivity {
    private VMUpdateMobile mViewModel;
    private LoadDialog poDialog;
    private MessageBox poMessage;
    private TextInputEditText tie_ca_mobileNumber;
    private TextInputEditText tie_ca_otp;
    private MaterialButton btn_sendotp;
    private MaterialButton btn_submit;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_mobile);

        mViewModel = new ViewModelProvider(Activity_UpdateMobile.this).get(VMUpdateMobile.class);
        poDialog = new LoadDialog(this);
        poMessage = new MessageBox(this);

        toolbar = findViewById(R.id.toolbar);
        tie_ca_mobileNumber = findViewById(R.id.tie_ca_mobileNumber);
        tie_ca_otp = findViewById(R.id.tie_ca_otp);
        btn_sendotp = findViewById(R.id.btn_sendotp);
        btn_submit = findViewById(R.id.btn_submit);

        setSupportActionBar(toolbar); //set object toolbar as default action bar for activity

        getSupportActionBar().setTitle("Account Mobile"); //set default title for action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //set back button to toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true); //enable the back button set on toolbar

        btn_sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btnText = btn_sendotp.getText().toString();
                if (btnText == "Request OTP"){
                    mViewModel.OTPRequest(new VMUpdateMobile.RequestOTP() {
                        @Override
                        public void onRequest(String title, String message) {
                            poDialog.initDialog(title, message, false);
                            poDialog.show();
                        }

                        @Override
                        public void onSuccess() {
                            poDialog.dismiss();

                            poMessage.setMessage("OTP was successfully sent to your mobile number.");
                            poMessage.show();

                            btn_submit.setEnabled(false); //disable until otp is verified
                            btn_sendotp.setText("Verify OTP"); //change text to verify otp after receiving
                        }

                        @Override
                        public void onFailed(String result) {
                            poDialog.dismiss();

                            poMessage.setMessage(result);
                            poMessage.show();

                            btn_submit.setEnabled(false); //disable until otp is verified
                            btn_sendotp.setText("Request OTP"); //set text to primary when request failed
                        }
                    });
                }

                if (btnText == "Verify OTP"){
                    mViewModel.OTPVerification(tie_ca_otp.getText().toString(), new VMUpdateMobile.VerifyOTP() {
                        @Override
                        public void onVerify(String title, String message) {
                            poDialog.initDialog(title, message, false);
                            poDialog.show();
                        }

                        @Override
                        public void onSuccess() {
                            poDialog.dismiss();

                            poMessage.setMessage("OTP Verified");
                            poMessage.show();

                            btn_submit.setEnabled(true); //enable when otp is verified
                            btn_sendotp.setText("Request OTP"); //change text to request otp after verifying
                        }

                        @Override
                        public void onFailed(String result) {
                            poDialog.dismiss();

                            poMessage.setMessage(result);
                            poMessage.show();

                            btn_submit.setEnabled(false); //disable when otp is not verified
                            btn_sendotp.setText("Verify OTP"); //retain text to verify otp
                        }
                    });
                }
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.ChangeAccountMobile(tie_ca_mobileNumber.getText().toString(), new VMUpdateMobile.SubmitChanges() {
                    @Override
                    public void onChange(String title, String message) {
                        poDialog.initDialog(title, message, false);
                        poDialog.show();
                    }

                    @Override
                    public void onSuccess() {
                        poDialog.dismiss();

                        poMessage.setMessage("Mobile changed successfully");
                        poMessage.show();
                    }

                    @Override
                    public void onFailed(String result) {
                        poDialog.dismiss();

                        poMessage.setMessage("Mobile failed to update");
                        poMessage.show();
                    }
                });
            }
        });
    }
}