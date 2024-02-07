package org.rmj.guanzongroup.agent.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DGanadoOnline;
import org.rmj.g3appdriver.GConnect.Account.ClientSession;
import org.rmj.g3appdriver.SalesKit.Obj.SalesKit;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.guanzongroup.agent.R;
import org.rmj.guanzongroup.agent.ViewModel.VMAgentInfo;

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

    private VMAgentInfo mViewModel;
    private ClientSession poSession;
    private MessageBox poMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(Activity_UserPerformance.this).get(VMAgentInfo.class);
        setContentView(R.layout.activity_user_performance);

        poMessage = new MessageBox(this);
        poMessage.initDialog();
        poMessage.setTitle("Guanzon Sales Kit");
//        poMessage.setPositiveButton("Close", new MessageBox.DialogButton() {
//            @Override
//            public void OnButtonClick(View view, AlertDialog dialog) {
//                dialog.dismiss();
//
//                Intent loIntent = new Intent(Activity_UserPerformance.this, Activity_Settings.class);
//                startActivity(loIntent);
//                finish();
//            }
//        });
//
//        Boolean isComplete = getIntent().getBooleanExtra("isComplete", false);
//        if (isComplete == false){
//            poMessage.setMessage("Must complete account to use this feature");
//            poMessage.show();
//        }

        poSalesKit = new SalesKit(getApplication());
        poSession = ClientSession.getInstance(this);

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
        mViewModel.GetUserCountEntries().observe(Activity_UserPerformance.this, new Observer<DGanadoOnline.CountEntries>() {
            @Override
            public void onChanged(DGanadoOnline.CountEntries countEntries) {
                Log.e("nOpen", String.valueOf(countEntries.nOpen));
                Log.e("nExtracted", String.valueOf(countEntries.nExtracted));
                Log.e("nSold", String.valueOf(countEntries.nSold));
                Log.e("nEngaged", String.valueOf(countEntries.nEngaged));
                Log.e("nLost", String.valueOf(countEntries.nLost));
                Log.e("nEntries", String.valueOf(countEntries.nEntries));

                totalopen.setText(String.valueOf(countEntries.nOpen));
                totalextr.setText(String.valueOf(countEntries.nExtracted));
                totalsold.setText(String.valueOf(countEntries.nSold));
                totaleng.setText(String.valueOf(countEntries.nEngaged));
                totallost.setText(String.valueOf(countEntries.nLost));
                totalsales.setText(String.valueOf(countEntries.nEntries));

                username.setText(poSession.getFullName());
            }
        });

    }
}