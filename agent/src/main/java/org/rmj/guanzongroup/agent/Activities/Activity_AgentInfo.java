package org.rmj.guanzongroup.agent.Activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textview.MaterialTextView;

import org.rmj.guanzongroup.agent.Adapter.Adapter_Fragment;
import org.rmj.guanzongroup.agent.Fragment.Fragment_AgentPerformance;
import org.rmj.guanzongroup.agent.Fragment.Fragment_SubAgentInquiry;
import org.rmj.guanzongroup.agent.Fragment.Fragment_SubAgents;
import org.rmj.guanzongroup.agent.R;
import org.rmj.guanzongroup.agent.ViewModel.VMAgentInfo;

public class Activity_AgentInfo extends AppCompatActivity {

    private VMAgentInfo mViewModel;

    private Adapter_Fragment mAdapter;
    private ViewPager mPager;
    private TabLayout tablayout_agentInfo;
    private ViewPager viewPager;
    private MaterialToolbar toolbar;
    private MaterialTextView tvUsername;
    private MaterialTextView tvEmail;
    private MaterialTextView tvMobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VMAgentInfo.class);
        setContentView(R.layout.activity_agent_info);
        mViewModel.setUserID(getIntent().getStringExtra("sUserIDxx"));
        mViewModel.setUserLvl("1");
        initViews();

        mAdapter = new Adapter_Fragment(getSupportFragmentManager());
        mAdapter.addFragment(new Fragment_AgentPerformance());
        mAdapter.addFragment(new Fragment_SubAgentInquiry());
        mAdapter.addFragment(new Fragment_SubAgents());
        mAdapter.addTitle("Performance");
        mAdapter.addTitle("Transactions");
        mAdapter.addTitle("Agents");

        viewPager.setAdapter(mAdapter);
        tablayout_agentInfo.setupWithViewPager(viewPager);

        mViewModel.GetKPOPAgentInfo().observe(Activity_AgentInfo.this,agentInfo ->{
            if(agentInfo != null){
                tvUsername.setText(agentInfo.getsUserName());
                tvEmail.setText(agentInfo.getsEmailAdd());
                tvMobile.setText(agentInfo.getsMobileNo());
            }
        });


    }


    private void initViews() {
        toolbar = findViewById(R.id.toolbar_agentInfo);

        tablayout_agentInfo = findViewById(R.id.tablayout_agentInfo);
        viewPager = findViewById(R.id.vpAgentInfo);

        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvMobile = findViewById(R.id.tvMobile);
        setSupportActionBar(toolbar); //set object toolbar as default action bar for activity

        getSupportActionBar().setTitle(" "); //set default title for action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //set back button to toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true); //enable the back button set on toolbar




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

}