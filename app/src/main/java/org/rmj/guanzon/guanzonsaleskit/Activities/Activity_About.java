package org.rmj.guanzon.guanzonsaleskit.Activities;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;

import org.rmj.guanzon.guanzonsaleskit.R;
import org.rmj.guanzongroup.authlibrary.Activity.Activity_CompleteAccount;
import org.rmj.guanzongroup.authlibrary.Activity.Activity_Settings;
import org.rmj.guanzongroup.authlibrary.Activity.Activity_TermsAndConditions;

public class Activity_About extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private MaterialTextView tvTerms_and_condition, tvPrivacy_policy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbar = findViewById(R.id.toolbar);
        tvTerms_and_condition = findViewById(R.id.tvTerms_and_condition);
        tvPrivacy_policy = findViewById(R.id.tvPrivacy_policy);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        tvTerms_and_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentSettings(Activity_TermsAndConditions.class);
            }
        });

        tvPrivacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Activity_About.this, Activity_Browser.class);
                intent.putExtra("url_link", "https://www.guanzongroup.com.ph/guanzon-sales-kit/");
                intent.putExtra("args", "0");
                startActivity(intent);
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void IntentSettings(Class intentClass){
        Intent intent = new Intent(Activity_About.this, intentClass);
        startActivity(intent);
    }
}