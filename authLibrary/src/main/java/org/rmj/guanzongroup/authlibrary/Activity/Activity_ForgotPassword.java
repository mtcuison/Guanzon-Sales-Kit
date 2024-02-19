package org.rmj.guanzongroup.authlibrary.Activity;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.etc.AppConfigPreference;
import org.rmj.g3appdriver.etc.LoadDialog;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.guanzongroup.authlibrary.R;
import org.rmj.guanzongroup.authlibrary.ViewModels.VMForgotPassword;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;

public class Activity_ForgotPassword extends AppCompatActivity implements VMForgotPassword.RequestPasswordCallback {
    private VMForgotPassword mViewModel;
    private TextInputEditText tie_email;
    private LoadDialog poDialog;
    private MessageBox poMsgBox;
    private MaterialToolbar toolbar;
    private MaterialButton btnSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        poDialog = new LoadDialog(this);
        poMsgBox = new MessageBox(this);

        toolbar = findViewById(R.id.toolbar);
        tie_email = findViewById(R.id.txt_email);
        btnSubmit = findViewById(R.id.btn_submit);
        mViewModel = new ViewModelProvider(this).get(VMForgotPassword.class);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Objects.requireNonNull(tie_email.getText()).toString().trim();

                mViewModel.RequestPassword(email, new VMForgotPassword.RequestPasswordCallback() {
                    @Override
                    public void OnSendRequest(String title, String message) {
                        poDialog.initDialog(title, message, false);
                        poDialog.show();

                        poMsgBox.initDialog();
                    }

                    @Override
                    public void OnSuccessRequest() {
                        poDialog.dismiss();
                        poMsgBox.setTitle("Result");
                        poMsgBox.setMessage("Successfully sent request.");
                        poMsgBox.setPositiveButton("OK", new MessageBox.DialogButton() {
                            @Override
                            public void OnButtonClick(View view, AlertDialog dialog) {
                                dialog.dismiss();
                            }
                        });

                        poMsgBox.show();
                    }

                    @Override
                    public void OnFailedRequest(String message) {
                        poDialog.dismiss();
                        poMsgBox.setTitle("Result");
                        poMsgBox.setMessage("Failed to send request: " + message);
                        poMsgBox.setPositiveButton("OK", new MessageBox.DialogButton() {
                            @Override
                            public void OnButtonClick(View view, AlertDialog dialog) {
                                dialog.dismiss();
                            }
                        });

                        poMsgBox.show();
                    }
                });
            }
        });
    }


    @Override
    public void OnSendRequest(String title, String message) {
        poDialog.initDialog(title, message, false);
        poDialog.show();
    }

    @Override
    public void OnSuccessRequest() {
        poDialog.dismiss();
        poMsgBox.initDialog();
        poMsgBox.setTitle("Forgot Password");
        poMsgBox.setMessage("You'll be receiving an email from MIS, Please check your email account");
        poMsgBox.setPositiveButton("Okay", (view, msgDialog) -> msgDialog.dismiss());
        poMsgBox.show();
    }

    @Override
    public void OnFailedRequest(String message) {
        poDialog.dismiss();
        poMsgBox.initDialog();
        poMsgBox.setTitle("Forgot Password");
        poMsgBox.setMessage(message);
        poMsgBox.setPositiveButton("Okay", (view, msgDialog) -> msgDialog.dismiss());
        poMsgBox.show();
    }
}