package org.rmj.guanzongroup.authlibrary.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import org.rmj.guanzongroup.authlibrary.R;

import java.util.Objects;

public class Activity_Settings extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private MaterialTextView tvCompleteAcc;
    private MaterialTextView tvChangePass;
    private MaterialTextView tvUpdateMobile;
    private MaterialTextView tvUpdateAddr;
    private MaterialButton btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = findViewById(R.id.toolbar);
        tvCompleteAcc = findViewById(R.id.tvCompleteAcc);
        tvChangePass = findViewById(R.id.tvChangePass);
        tvUpdateMobile = findViewById(R.id.tvUpdateMobile);
        tvUpdateAddr = findViewById(R.id.tvUpdateAddr);
        btn_back = findViewById(R.id.btn_back);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

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
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(org.rmj.g3appdriver.R.anim.anim_intent_slide_in_left, org.rmj.g3appdriver.R.anim.anim_intent_slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void IntentSettings(Class intentClass){
        Intent intent = new Intent(Activity_Settings.this, intentClass);
        startActivity(intent);
    }
}