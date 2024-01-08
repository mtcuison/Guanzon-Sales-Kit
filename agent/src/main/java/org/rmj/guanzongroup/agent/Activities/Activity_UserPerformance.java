package org.rmj.guanzongroup.agent.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import org.rmj.g3appdriver.GCircle.Account.EmployeeSession;
import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DGanadoOnline;
import org.rmj.g3appdriver.SalesKit.Obj.SalesKit;
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
    private SalesKit poSalesKit;
    private EmployeeSession poSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_performance);

        poSalesKit = new SalesKit(getApplication());
        poSession = EmployeeSession.getInstance(this);

        toolbar = findViewById(R.id.toolbar);

        username = findViewById(R.id.username);
        totalsales = findViewById(R.id.totalsales);
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

        poSalesKit.GetCountEntries().observe(this, new Observer<DGanadoOnline.CountEntries>() {
            @Override
            public void onChanged(DGanadoOnline.CountEntries countEntries) {
                Log.d("User Performance", String.valueOf(countEntries.nOpen));
                /*totalopen.setText(countEntries.nOpen);
                totalextr.setText(countEntries.nExtracted);
                totalsold.setText(countEntries.nSold);
                totaleng.setText(countEntries.nEngaged);
                totallost.setText(countEntries.nLost);

                username.setText(poSession.getUserName());*/
            }
        });
    }
}