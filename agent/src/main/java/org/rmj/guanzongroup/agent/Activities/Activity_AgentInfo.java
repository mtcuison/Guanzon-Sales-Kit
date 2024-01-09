package org.rmj.guanzongroup.agent.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.rmj.guanzongroup.agent.Adapter.Adapter_Fragment;
import org.rmj.guanzongroup.agent.Fragment.Fragment_SubAgentInquiry;
import org.rmj.guanzongroup.agent.Fragment.Fragment_SubAgents;
import org.rmj.guanzongroup.agent.Fragment.Fragment_AgentPerformance;
import org.rmj.guanzongroup.agent.R;
import org.rmj.guanzongroup.agent.ViewModel.VMAgentInfo;

public class Activity_AgentInfo extends AppCompatActivity {

    private VMAgentInfo mViewModel;

    private Adapter_Fragment mAdapter;
    private ViewPager mPager;
    private TabLayout tablayout_agentInfo;
    private ViewPager viewPager;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VMAgentInfo.class);
        setContentView(R.layout.activity_agent_info);
        mViewModel.setUserID(getIntent().getStringExtra("sUserIDxx"));
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


    }


    private void initViews() {
        toolbar = findViewById(R.id.toolbar_agentInfo);

        tablayout_agentInfo = findViewById(R.id.tablayout_agentInfo);
        viewPager = findViewById(R.id.vpAgentInfo);


    }

}