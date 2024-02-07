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
import org.rmj.guanzongroup.authlibrary.R;
import org.rmj.guanzongroup.authlibrary.ViewModels.VMUpdateMobile;

public class Activity_UpdateMobile extends AppCompatActivity {
    private VMUpdateMobile mViewModel;
    private LoadDialog poDialog;
    private MessageBox poMessage;
    private TextInputEditText tie_ca_mobileNumber;
    private MaterialButton btn_submit;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_mobile);

        mViewModel = new ViewModelProvider(Activity_UpdateMobile.this).get(VMUpdateMobile.class);
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
        tie_ca_mobileNumber = findViewById(R.id.tie_ca_mobileNumber);
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
                mViewModel.ChangeAccountMobile(tie_ca_mobileNumber.getText().toString(), new VMUpdateMobile.SubmitChanges() {
                    @Override
                    public void onChange(String title, String message) {
                        poDialog.initDialog(title, message, false);
                        poDialog.show();
                    }

                    @Override
                    public void onSuccess() {
                        poDialog.dismiss();

                        poMessage.setMessage("Request sent. Please verify new mobile sent to your email.");
                        poMessage.show();
                    }

                    @Override
                    public void onFailed(String result) {
                        poDialog.dismiss();

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