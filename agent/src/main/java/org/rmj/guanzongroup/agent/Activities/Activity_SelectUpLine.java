package org.rmj.guanzongroup.agent.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.etc.LoadDialog;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.guanzongroup.agent.R;
import org.rmj.guanzongroup.agent.ViewModel.VMAgentList;
import org.rmj.guanzongroup.agent.ViewModel.VMSelectUpLine;
import org.rmj.guanzongroup.authlibrary.Activity.Activity_Settings;

import java.util.Objects;

public class Activity_SelectUpLine extends AppCompatActivity {
    private VMSelectUpLine mViewModel;
    private TextInputEditText txtUpline;
    private MaterialButton btnSubmit, btnCancel;
    private LoadDialog poLoading;
    private MessageBox poMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_up_line);

        poLoading = new LoadDialog(Activity_SelectUpLine.this);
        poMessage = new MessageBox(Activity_SelectUpLine.this);


//        Boolean isComplete = getIntent().getBooleanExtra("isComplete", false);
//        if (isComplete == false){
//            poMessage.setPositiveButton("Close", (view, dialog) -> {
//                dialog.dismiss();
//
//                Intent loIntent = new Intent(Activity_SelectUpLine.this, Activity_Settings.class);
//                startActivity(loIntent);
//                finish();
//            });
//
//            poMessage.setMessage("Must complete account to use this feature");
//            poMessage.show();
//        }

        mViewModel = new ViewModelProvider(Activity_SelectUpLine.this).get(VMSelectUpLine.class);
        mViewModel.GetCompleteProfile().observe(Activity_SelectUpLine.this, eclient ->{
            if (eclient == null){


                poMessage.initDialog();
                poMessage.setTitle("Guanzon Sales Kit");
                poMessage.setMessage("Must complete account to use this feature");
                poMessage.setPositiveButton("Close", (view, dialog) -> {
                    dialog.dismiss();

                    Intent loIntent = new Intent(Activity_SelectUpLine.this, Activity_Settings.class);
                    startActivity(loIntent);
                    finish();
                });

                poMessage.show();
            }
        });
        txtUpline = findViewById(R.id.txt_upLine);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);
        poMessage = new MessageBox(Activity_SelectUpLine.this);
        poLoading = new LoadDialog(Activity_SelectUpLine.this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        btnCancel.setOnClickListener(v ->{
            finish();
        });
        btnSubmit.setOnClickListener(v ->{
            mViewModel.SubmitUpLine(txtUpline.getText().toString(), new VMAgentList.OnTaskExecute() {
                @Override
                public void OnExecute() {
                    poLoading.initDialog("Agent Upline", "Saving upline. Please wait...", false);
                    poLoading.show();

                }

                @Override
                public void OnSuccess() {
                    poLoading.dismiss();

                    poMessage.setMessage("Agent Upline user id successfully submitted.");
                    poMessage.show();
                }

                @Override
                public void OnFailed(String message) {
                    poLoading.dismiss();

                    poMessage.setMessage(message);
                    poMessage.show();

                }
            });
        });


    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(org.rmj.g3appdriver.R.anim.anim_intent_slide_in_left, org.rmj.g3appdriver.R.anim.anim_intent_slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}