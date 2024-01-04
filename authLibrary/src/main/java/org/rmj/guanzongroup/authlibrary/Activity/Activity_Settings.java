package org.rmj.guanzongroup.authlibrary.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import org.rmj.guanzongroup.authlibrary.R;

public class Activity_Settings extends AppCompatActivity {
    private MaterialTextView tvCompleteAcc;
    private MaterialTextView tvChangePass;
    private MaterialTextView tvUpdateMobile;
    private MaterialTextView tvUpdateAddr;
    private MaterialButton btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tvCompleteAcc = findViewById(R.id.tvCompleteAcc);
        tvChangePass = findViewById(R.id.tvChangePass);
        tvUpdateMobile = findViewById(R.id.tvUpdateMobile);
        tvUpdateAddr = findViewById(R.id.tvUpdateAddr);
        btn_back = findViewById(R.id.btn_back);
        tvCompleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentSettings(Activity_CompleteAccount.class);
            }
        });
        tvChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentSettings(Activity_ChangePassword.class);
            }
        });
        tvUpdateMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentSettings(Activity_UpdateMobile.class);
            }
        });
        tvUpdateAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentSettings(Activity_UpdateAddress.class);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void IntentSettings(Class intentClass){
        Intent intent = new Intent(Activity_Settings.this, intentClass);
        startActivity(intent);
    }
}