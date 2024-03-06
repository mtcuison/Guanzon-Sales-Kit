package org.rmj.guanzongroup.agent.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import org.rmj.g3appdriver.etc.LoadDialog;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.guanzongroup.agent.Adapter.AgentListAdapter;
import org.rmj.guanzongroup.agent.R;
import org.rmj.guanzongroup.agent.ViewModel.VMAgentList;
import org.rmj.guanzongroup.authlibrary.Activity.Activity_Settings;

import java.util.Objects;

public class Activity_AgentEnroll extends AppCompatActivity {
    private VMAgentList mViewModel;
    private MaterialToolbar toolbar;
    private RecyclerView rvAgentList;
    private AgentListAdapter adapter;
    private LoadDialog poLoading;
    private MessageBox poMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_enroll);

        poLoading = new LoadDialog(Activity_AgentEnroll.this);
        poMessage = new MessageBox(Activity_AgentEnroll.this);

        poMessage.initDialog();
        poMessage.setTitle("Guanzon Sales Kit");
        poMessage.setPositiveButton("Close", (view, dialog) -> {
            dialog.dismiss();

            Intent loIntent = new Intent(Activity_AgentEnroll.this, Activity_Settings.class);
            startActivity(loIntent);
            finish();
        });

        Boolean isComplete = getIntent().getBooleanExtra("isComplete", false);
        if (isComplete == false){
            poMessage.setMessage("Must complete account to use this feature");
            poMessage.show();
        }

        mViewModel = new ViewModelProvider(Activity_AgentEnroll.this).get(VMAgentList.class);

        rvAgentList = findViewById(R.id.rvAgentEnroll);
        toolbar = findViewById(R.id.toolbar_agent);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mViewModel.ImportKPOPAgent(new VMAgentList.OnTaskExecute() {
            @Override
            public void OnExecute() {
                poLoading = new LoadDialog(Activity_AgentEnroll.this);
                poLoading.initDialog("Agent Enroll", "Please wait for a while.", false);
                poLoading.show();
            }

            @Override
            public void OnSuccess() {
                poLoading.dismiss();
            }

            @Override
            public void OnFailed(String message) {
                poLoading.dismiss();

                poMessage = new MessageBox(Activity_AgentEnroll.this);
                poMessage.initDialog();
                poMessage.setMessage(message);
                poMessage.setPositiveButton("Okay", new MessageBox.DialogButton() {
                    @Override
                    public void OnButtonClick(View view, AlertDialog dialog) {
                        dialog.dismiss();
                    }
                });
                poMessage.show();
            }
        });
        mViewModel.GetKPOPAgentRole().observe(Activity_AgentEnroll.this, ekpopAgentRoles -> {
            if (ekpopAgentRoles.size() > 0){
                adapter = new AgentListAdapter(ekpopAgentRoles, new AgentListAdapter.OnAgentClickListener() {
                    @Override
                    public void OnClick(String sUserIDxx) {

                    }

                });
                rvAgentList.setAdapter(adapter);
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