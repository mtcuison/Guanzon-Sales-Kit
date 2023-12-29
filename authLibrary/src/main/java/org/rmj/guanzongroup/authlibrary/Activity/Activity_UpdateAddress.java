package org.rmj.guanzongroup.authlibrary.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.etc.LoadDialog;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.lib.Account.pojo.ChangeUserAddress;
import org.rmj.guanzongroup.authlibrary.R;
import org.rmj.guanzongroup.authlibrary.ViewModels.VMUpdateAddress;

public class Activity_UpdateAddress extends AppCompatActivity {
    private VMUpdateAddress mViewModel;
    private LoadDialog poDialog;
    private MessageBox poMessage;
    private TextInputEditText tie_ca_houseno;
    private TextInputEditText tie_ca_street;
    private TextInputEditText tie_ca_town;
    private TextInputEditText tie_ca_brgy;
    private TextInputEditText tie_ca_prov;
    private TextInputEditText tie_ca_otp;
    private MaterialButton btn_sendotp;
    private MaterialButton btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);

        mViewModel = new ViewModelProvider(Activity_UpdateAddress.this).get(VMUpdateAddress.class);
        poDialog = new LoadDialog(this);
        poMessage = new MessageBox(this);

        poMessage.setTitle("Guanzon Sales Kit");
        poMessage.setPositiveButton("Close", new MessageBox.DialogButton() {
            @Override
            public void OnButtonClick(View view, AlertDialog dialog) {
                dialog.dismiss();
            }
        });

        tie_ca_houseno = findViewById(R.id.tie_ca_houseno);
        tie_ca_street = findViewById(R.id.tie_ca_street);
        tie_ca_town = findViewById(R.id.tie_ca_town);
        tie_ca_brgy = findViewById(R.id.tie_ca_brgy);
        tie_ca_prov = findViewById(R.id.tie_ca_prov);
        tie_ca_otp = findViewById(R.id.tie_ca_otp);
        btn_sendotp = findViewById(R.id.btn_sendotp);
        btn_submit = findViewById(R.id.btn_submit);

        btn_sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btnText = btn_sendotp.getText().toString();
                if (btnText == "Request OTP"){
                    mViewModel.OTPRequest(new VMUpdateAddress.RequestOTP() {
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
                    mViewModel.OTPVerification(tie_ca_otp.getText().toString(), new VMUpdateAddress.VerifyOTP() {
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
                ChangeUserAddress loAddress = new ChangeUserAddress();
                loAddress.setsHouseNox(tie_ca_houseno.getText().toString());
                loAddress.setsAddressx(tie_ca_street.getText().toString());
                loAddress.setcAddrssTp(tie_ca_prov.getText().toString());
                loAddress.setsTownIDx(tie_ca_town.getText().toString());
                loAddress.setsBrgyIdx(tie_ca_brgy.getText().toString());

                mViewModel.ChangeAccountAddress(loAddress, new VMUpdateAddress.SubmitChanges() {
                    @Override
                    public void onChange(String title, String message) {
                        poDialog.initDialog(title, message, false);
                        poDialog.show();
                    }

                    @Override
                    public void onSuccess() {
                        poDialog.dismiss();

                        poMessage.setMessage("Address changed successfully");
                        poMessage.show();
                    }

                    @Override
                    public void onFailed(String result) {
                        poDialog.dismiss();

                        poMessage.setMessage("Address failed to update");
                        poMessage.show();
                    }
                });
            }
        });
    }
}