package org.rmj.guanzongroup.agent.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.smarteist.autoimageslider.SliderView;

import org.rmj.g3appdriver.lib.Ganado.Obj.InquiryListAdapter;
import org.rmj.guanzongroup.agent.Adapter.AgentListAdapter;
import org.rmj.guanzongroup.agent.R;
import org.rmj.guanzongroup.agent.ViewModel.VMAgentInfo;
import org.rmj.guanzongroup.agent.ViewModel.VMAgentList;

public class Activity_AgentInfo extends AppCompatActivity {

    private VMAgentInfo mViewModel;
    private SliderView poSliderx;
    private LinearLayout lnInquiries, lnAgents, loadingAgent,loadingInquiries;
    private CardView cvGanado;

    private TabLayout tabLayout;
    private RecyclerView rvAgents,rvInquiries;
    private AgentListAdapter agentAdapter;
    private InquiryListAdapter inquiryAdapter;
    private TextView lblInquiryNoData, lblAgentNoData;
    private String lsUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VMAgentInfo.class);
        setContentView(R.layout.activity_agent_info);


        lsUserId = getIntent().getStringExtra("sUserIDxx");
        initView();



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        importInquiries();
                        break;
                    default:
                        importAgent();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        importInquiries();
    }
    private void initView(){
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Transactions"));
        tabLayout.addTab(tabLayout.newTab().setText("Agents"));
        lnInquiries = findViewById(R.id.linear_inquiries);
        lnAgents = findViewById(R.id.linear_agents);
        rvAgents = findViewById(R.id.rvAgents);
        rvInquiries = findViewById(R.id.rvInquiries);
        lblInquiryNoData = findViewById(R.id.lbl_InquiryNoData);
        lblAgentNoData = findViewById(R.id.lbl_AgentNoData);
        loadingAgent = findViewById(R.id.linear_loadingAgent);
        loadingInquiries = findViewById(R.id.linear_loadingInquiries);
        LinearLayoutManager loManager = new LinearLayoutManager(Activity_AgentInfo.this);
        loManager.setOrientation(RecyclerView.VERTICAL);
        rvAgents.setLayoutManager(loManager);
    }

    private void importAgent(){
        mViewModel.ImportKPOPAgent(new VMAgentList.OnTaskExecute() {
            @Override
            public void OnExecute() {
                hideInquiries();
                hideAgents();
                loadingAgent.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnSuccess() {
                loadingAgent.setVisibility(View.GONE);
                LoadAgents();
            }

            @Override
            public void OnFailed(String message) {
                loadingAgent.setVisibility(View.GONE);
                lblAgentNoData.setVisibility(View.VISIBLE);

            }
        });
    }
    private void importInquiries(){
        mViewModel.ImportInquiries(new VMAgentList.OnTaskExecute() {
            @Override
            public void OnExecute() {
                hideInquiries();
                hideAgents();
                loadingInquiries.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnSuccess() {
                LoadInquiries();
                loadingInquiries.setVisibility(View.GONE);
            }

            @Override
            public void OnFailed(String message) {
                loadingInquiries.setVisibility(View.GONE);
                lblInquiryNoData.setVisibility(View.VISIBLE);

            }
        });
    }

    private void LoadAgents(){
        mViewModel.GetKPOPAgent(lsUserId).observe(Activity_AgentInfo.this, ekpopAgentRoles -> {
            if (ekpopAgentRoles.size() > 0){

                lblAgentNoData.setVisibility(View.GONE);
                agentAdapter = new AgentListAdapter(ekpopAgentRoles, new AgentListAdapter.OnAgentClickListener() {
                    @Override
                    public void OnClick(String sUserIDxx) {
//                        Intent intent = new Intent(Activity_AgentInfo.this, Activity_AgentInfo.class);
//                        intent.putExtra("sUserIDxx", sUserIDxx);
//                        startActivity(intent);
//                        overridePendingTransition(org.rmj.g3appdriver.R.anim.anim_intent_slide_in_right, org.rmj.g3appdriver.R.anim.anim_intent_slide_out_left);
                    }

                });

                rvAgents.setAdapter(agentAdapter);

            }else{
                lblAgentNoData.setVisibility(View.VISIBLE);
            }
        });
    }
    private void LoadInquiries(){
        Log.e("lsUserId",lsUserId);
        lnInquiries.setVisibility(View.VISIBLE);
        mViewModel.GetInquiries(lsUserId).observe(Activity_AgentInfo.this, inquiries -> {

            Log.e("inquiries", String.valueOf(inquiries.size()));
            if (inquiries.size() > 0){

                lblInquiryNoData.setVisibility(View.GONE);
                InquiryListAdapter adapter= new InquiryListAdapter(getApplication(), inquiries, new InquiryListAdapter.OnModelClickListener() {
                    @Override
                    public void OnClick(String TransNox) {
//                        Intent intent = new Intent(Activity_Inquiries.this, Activity_ProductSelection.class);
//                        intent.putExtra("TransNox",TransNox);
//                        startActivity(intent);
//                        overridePendingTransition(org.rmj.g3appdriver.R.anim.anim_intent_slide_in_left, org.rmj.g3appdriver.R.anim.anim_intent_slide_out_right);
//                        finish();

                    }

                });

                rvInquiries.setAdapter(adapter);
                rvInquiries.setLayoutManager(new LinearLayoutManager(Activity_AgentInfo.this,RecyclerView.VERTICAL,false));

            }else{
                lblInquiryNoData.setVisibility(View.VISIBLE);
            }
        });
    }


    private void hideInquiries(){
        lnInquiries.setVisibility(View.GONE);
        lblInquiryNoData.setVisibility(View.GONE);
        loadingInquiries.setVisibility(View.GONE);

    }
    private void hideAgents(){
        lnAgents.setVisibility(View.GONE);
        lblAgentNoData.setVisibility(View.GONE);
        loadingAgent.setVisibility(View.GONE);
    }
}