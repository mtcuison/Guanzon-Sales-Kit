package org.rmj.guanzongroup.ganado.Activities;

import android.app.AlertDialog;
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
import org.rmj.g3appdriver.lib.Ganado.Obj.InquiryListAdapter;
import org.rmj.guanzongroup.ganado.Dialog.DialogInquiryHistory;
import org.rmj.guanzongroup.ganado.R;
import org.rmj.guanzongroup.ganado.ViewModel.VMInquiry;

import java.util.Objects;

public class Activity_Inquiries extends AppCompatActivity {
    private VMInquiry mViewModel;
    private MaterialToolbar toolbar;
    private RecyclerView rvInquiries;
    private TextView lblNoData;
    private LoadDialog poLoad;
    private MessageBox poMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiries);

        poLoad = new LoadDialog(Activity_Inquiries.this);
        poMessage = new MessageBox(Activity_Inquiries.this);


        toolbar = findViewById(R.id.toolbar);
        rvInquiries = findViewById(R.id.rvInquiries);
        lblNoData = findViewById(R.id.lbl_InquiryNoData);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mViewModel = new ViewModelProvider(this).get(VMInquiry.class);
        mViewModel.ImportCriteria(new VMInquiry.OnTaskExecute() {
            @Override
            public void OnExecute() {
                poLoad.initDialog("Product Inquiry", "Checking data. Please wait...", false);
                poLoad.show();
            }

            @Override
            public void OnSuccess() {
                poLoad.dismiss();
                LoadInquiries();
            }

            @Override
            public void OnFailed(String message) {
                poLoad.dismiss();
                poMessage.initDialog();
                poMessage.setTitle("Guanzon Sales Kit");
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

        LoadInquiries();

    }
    private void LoadInquiries(){
        mViewModel.GetByAgentInquiries().observe(Activity_Inquiries.this, inquiries ->{
            if (inquiries.size() > 0){
                lblNoData.setVisibility(View.GONE);
                InquiryListAdapter adapter= new InquiryListAdapter(getApplication(), inquiries, new InquiryListAdapter.OnModelClickListener() {
                    @Override
                    public void OnClick(String TransNox) {
                        DialogInquiryHistory dHistory = new DialogInquiryHistory(Activity_Inquiries.this);
                        dHistory.initDialog(getApplication(), mViewModel.GetInquiry(TransNox));
                        dHistory.setPositiveButton("Close", new DialogInquiryHistory.DialogButton() {
                            @Override
                            public void OnButtonClick(View view, AlertDialog dialog) {
                                dialog.dismiss();
                            }
                        });
                        dHistory.show();
//                        DialogInquiryHistory dHistory = new DialogInquiryHistory(getApplication());
//                        dHistory.initInquiryHistory(TransNox, new DialogInquiryHistory.DialogButtonClickListener() {
//                            @Override
//                            public void OnClick(Dialog dialog, String remarksCode) {
//                                dialog.dismiss();
//                            }
//                        });
//
//                        dHistory.show();
                    }

                });

                rvInquiries.setAdapter(adapter);
                rvInquiries.setLayoutManager(new LinearLayoutManager(Activity_Inquiries.this,RecyclerView.VERTICAL,false));

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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}