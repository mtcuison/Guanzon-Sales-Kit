package org.rmj.guanzongroup.authlibrary.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.rmj.g3appdriver.etc.LoadDialog;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.lib.Account.pojo.PasswordChange;
import org.rmj.g3appdriver.lib.Account.pojo.PasswordUpdate;
import org.rmj.guanzongroup.authlibrary.R;
import org.rmj.guanzongroup.authlibrary.ViewModels.VMChangePassword;
import org.w3c.dom.Text;

public class Activity_ChangePassword extends AppCompatActivity {
    private VMChangePassword mViewModel;
    private LoadDialog poDialog;
    private MessageBox poMessage;
    private TextInputEditText password;
    private TextInputEditText conf_password;
    private TextInputEditText tie_ca_otp;
    private MaterialButton btn_sendotp;
    private  MaterialButton btn_submit;
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

        password = findViewById(R.id.password);
        conf_password = findViewById(R.id.conf_password);
        tie_ca_otp = findViewById(R.id.tie_ca_otp);
        btn_sendotp = findViewById(R.id.btn_sendotp);
        btn_submit = findViewById(R.id.btn_submit);
        btn_sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btnText = btn_sendotp.getText().toString();
                if (btnText == "Request OTP"){
                    mViewModel.OTPRequest(new VMChangePassword.RequestOTP() {
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
                    mViewModel.OTPVerification(tie_ca_otp.getText().toString(), new VMChangePassword.VerifyOTP() {
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

                        poMessage.setMessage("Password changed successfully");
                        poMessage.show();
                    }

                    @Override
                    public void onFailed(String result) {
                        poDialog.dismiss();

                        poMessage.setMessage("Password change failed");
                        poMessage.show();
                    }
                });
            }
        });
    }
}