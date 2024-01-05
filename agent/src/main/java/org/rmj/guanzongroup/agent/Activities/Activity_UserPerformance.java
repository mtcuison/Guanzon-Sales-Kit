package org.rmj.guanzongroup.agent.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import org.rmj.guanzongroup.agent.R;

public class Activity_UserPerformance extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private MaterialTextView totalsales;
    private MaterialTextView username;
    private MaterialTextView totalopen;
    private MaterialTextView totalextr;
    private MaterialTextView totalsold;
    private MaterialTextView totaleng;
    private MaterialTextView totallost;
    private MaterialCardView mcv_totalopen;
    private MaterialCardView mcv_totalextr;
    private MaterialCardView mcv_totalsold;
    private MaterialCardView mcv_totaleng;
    private MaterialCardView mcv_totallost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_performance);

        toolbar = findViewById(R.id.toolbar);

        totalsales = findViewById(R.id.totalsales);
        username = findViewById(R.id.username);
        totalopen = findViewById(R.id.totalopen);
        totalextr = findViewById(R.id.totalextr);
        totalsold = findViewById(R.id.totalsold);
        totaleng = findViewById(R.id.totaleng);
        totallost = findViewById(R.id.totallost);

        mcv_totalopen = findViewById(R.id.mcv_totalopen);
        mcv_totalextr = findViewById(R.id.mcv_totalextr);
        mcv_totalsold = findViewById(R.id.mcv_totalsold);
        mcv_totaleng = findViewById(R.id.mcv_totaleng);
        mcv_totallost = findViewById(R.id.mcv_totallost);

        setSupportActionBar(toolbar); //set object toolbar as default action bar for activity

        getSupportActionBar().setTitle("User Performance"); //set default title for action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //set back button to toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true); //enable the back button set on toolbar

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}