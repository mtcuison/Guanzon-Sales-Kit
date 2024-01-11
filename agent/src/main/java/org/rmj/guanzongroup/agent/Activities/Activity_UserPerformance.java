package org.rmj.guanzongroup.agent.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import org.rmj.g3appdriver.GCircle.Account.EmployeeSession;
import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DGanadoOnline;
import org.rmj.g3appdriver.SalesKit.Obj.SalesKit;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.guanzongroup.agent.R;
import org.rmj.guanzongroup.authlibrary.Activity.Activity_Settings;

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
    private MessageBox poMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_performance);

        poMessage = new MessageBox(this);
        poMessage.initDialog();
        poMessage.setTitle("Guanzon Sales Kit");
        poMessage.setPositiveButton("Close", new MessageBox.DialogButton() {
            @Override
            public void OnButtonClick(View view, AlertDialog dialog) {
                dialog.dismiss();

                Intent loIntent = new Intent(Activity_UserPerformance.this, Activity_Settings.class);
                startActivity(loIntent);
                finish();
            }
        });

        Boolean isComplete = getIntent().getBooleanExtra("isComplete", false);
        if (isComplete == false){
            poMessage.setMessage("Must complete account to use this feature");
            poMessage.show();
        }

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
        totalsales = findViewById(R.id.totalsales);

        mcv_totalopen = findViewById(R.id.mcv_totalopen);
        mcv_totalextr = findViewById(R.id.mcv_totalextr);
        mcv_totalsold = findViewById(R.id.mcv_totalsold);
        mcv_totaleng = findViewById(R.id.mcv_totaleng);
        mcv_totallost = findViewById(R.id.mcv_totallost);

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

        poSalesKit.GetCountEntries().observe(this, new Observer<DGanadoOnline.CountEntries>() {
            @Override
            public void onChanged(DGanadoOnline.CountEntries countEntries) {

                int nOpen = countEntries.nOpen;
                int nExtracted = countEntries.nExtracted;
                int nSold = countEntries.nSold;
                int nEngaged = countEntries.nEngaged;
                int nLost = countEntries.nLost;

                int totalEntries = nOpen + nExtracted + nSold + nEngaged + nLost;

                totalopen.setText(String.valueOf(nOpen));
                totalextr.setText(String.valueOf(nExtracted));
                totalsold.setText(String.valueOf(nSold));
                totaleng.setText(String.valueOf(nEngaged));
                totallost.setText(String.valueOf(nLost));
                totalsales.setText(String.valueOf(totalEntries));

                username.setText(poSession.getUserName());
            }
        });
    }
}