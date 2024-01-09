package org.rmj.guanzongroup.agent.Activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

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
        mViewModel = new ViewModelProvider(Activity_SelectUpLine.this).get(VMSelectUpLine.class);
        setContentView(R.layout.activity_select_up_line);
        initView();
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
                    poMessage.initDialog();
                    poMessage.setTitle("Agent Upline");
                    poMessage.setMessage("Agent Upline user id successfully submitted.");
                    poMessage.setPositiveButton("Okay", (view, dialog) -> {
                        dialog.dismiss();
                        finish();
                    });
                    poMessage.show();
                }

                @Override
                public void OnFailed(String message) {
                    poLoading.dismiss();

                    poMessage = new MessageBox(Activity_SelectUpLine.this);
                    poMessage.initDialog();
                    poMessage.setTitle("Agent Upline");
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
        });


    }
    private void initView(){
        txtUpline = findViewById(R.id.txt_upLine);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);
        poMessage = new MessageBox(Activity_SelectUpLine.this);
        poLoading = new LoadDialog(Activity_SelectUpLine.this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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