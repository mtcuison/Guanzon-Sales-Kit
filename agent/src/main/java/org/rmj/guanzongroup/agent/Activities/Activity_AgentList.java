package org.rmj.guanzongroup.agent.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import org.rmj.g3appdriver.etc.LoadDialog;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.guanzongroup.agent.Adapter.AgentListAdapter;
import org.rmj.guanzongroup.agent.R;
import org.rmj.guanzongroup.agent.ViewModel.VMAgentList;

import java.util.Objects;

public class Activity_AgentList extends AppCompatActivity {
    private VMAgentList mViewModel;
    private MaterialToolbar toolbar;
    private RecyclerView rvAgentList;
    private AgentListAdapter adapter;
    private LoadDialog poLoading;
    private MessageBox poMessage;
    private TextView lblNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_list);

        poMessage = new MessageBox(Activity_AgentList.this);

        rvAgentList = findViewById(R.id.rvAgentList);
        toolbar = findViewById(R.id.toolbar);
        lblNoData = findViewById(R.id.lbl_InquiryNoData);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mViewModel = new ViewModelProvider(Activity_AgentList.this).get(VMAgentList.class);
        mViewModel.ImportKPOPAgent(new VMAgentList.OnTaskExecute() {
            @Override
            public void OnExecute() {
                poLoading = new LoadDialog(Activity_AgentList.this);
                poLoading.initDialog("Agent List", "Please wait for a while.", false);
                poLoading.show();
            }

            @Override
            public void OnSuccess() {
                poLoading.dismiss();
            }

            @Override
            public void OnFailed(String message) {
                poLoading.dismiss();
                poMessage.initDialog();
                poMessage.setIcon(org.rmj.g3appdriver.R.drawable.baseline_error_24);
                poMessage.setTitle("Guanzon Sales Kit");
                poMessage.setPositiveButton("Okay", new MessageBox.DialogButton() {
                    @Override
                    public void OnButtonClick(View view, AlertDialog dialog) {
                        dialog.dismiss();
                    }
                });
                poMessage.setMessage(message);
                poMessage.show();
            }
        });
        mViewModel.GetKPOPAgentRole().observe(Activity_AgentList.this, ekpopAgentRoles -> {
            if (ekpopAgentRoles.size() > 0){
                lblNoData.setVisibility(View.GONE);
                adapter = new AgentListAdapter(ekpopAgentRoles, new AgentListAdapter.OnAgentClickListener() {
                    @Override
                    public void OnClick(String sUserIDxx) {
                        Intent intent = new Intent(Activity_AgentList.this, Activity_AgentInfo.class);
                        intent.putExtra("sUserIDxx", sUserIDxx);
                        startActivity(intent);
                        overridePendingTransition(org.rmj.g3appdriver.R.anim.anim_intent_slide_in_right, org.rmj.g3appdriver.R.anim.anim_intent_slide_out_left);
                    }

                });

                rvAgentList.setAdapter(adapter);
                rvAgentList.setLayoutManager(new LinearLayoutManager(Activity_AgentList.this,  RecyclerView.VERTICAL, false));

            }else{
                lblNoData.setVisibility(View.VISIBLE);
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
            overridePendingTransition(org.rmj.g3appdriver.R.anim.anim_intent_slide_in_left, org.rmj.g3appdriver.R.anim.anim_intent_slide_out_right);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}