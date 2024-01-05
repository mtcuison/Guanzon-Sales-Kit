package org.rmj.guanzongroup.ganado.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.GCircle.room.Entities.ECountryInfo;
import org.rmj.g3appdriver.etc.LoadDialog;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.guanzongroup.ganado.R;
import org.rmj.guanzongroup.ganado.ViewModel.VMFinancierInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Activity_FinancierInfo extends AppCompatActivity {

    private VMFinancierInfo mViewModel;
    private MessageBox poMessage;
    private LoadDialog poDialogx;

    private TextInputEditText txtLastNm, txtFrstNm, txtMiddNm, txtSuffixx,  txt_facebookAccount,
            txtEmailAdd, txtMobileNo,  txt_whatsAppAccount, txtAddress, txt_income;

    private MaterialAutoCompleteTextView txtCountry;
    private MaterialAutoCompleteTextView spinner_relation;
    private MaterialButton btnContinue, btnPrev;
    private MaterialCheckBox txtMobileType1, txtMobileType2, txtMobileType3;

    private MaterialToolbar toolbar;
    private String sTansNox = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        poMessage = new MessageBox(Activity_FinancierInfo.this);
        poDialogx = new LoadDialog(Activity_FinancierInfo.this);
        setContentView(R.layout.activity_financier_info);
        initWidgets();
        mViewModel.InitializeApplication(getIntent());

        mViewModel.getRelation().observe(Activity_FinancierInfo.this, eRelations->{
            try {
                ArrayList<String> string = new ArrayList<>();
                for (int x = 0; x < eRelations.size(); x++) {
                    String lsColor = eRelations.get(x).getRelatnDs();
//                        String lsTown =  loList.get(x).sProvName ;
                    string.add(lsColor);

                }
                ArrayAdapter<String> adapters = new ArrayAdapter<>(Activity_FinancierInfo.this, android.R.layout.simple_spinner_dropdown_item, string.toArray(new String[0]));
                spinner_relation.setText(eRelations.get(0).getRelatnDs());
                spinner_relation.setText(eRelations.get(0).getRelatnDs());
                mViewModel.getModel().setsReltionx(eRelations.get(0).getRelatnID());
                spinner_relation.setAdapter(adapters);
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        spinner_relation.setOnItemClickListener(new Activity_FinancierInfo.OnItemClickListener(spinner_relation));


        mViewModel.GetCountryList().observe(Activity_FinancierInfo.this, new Observer<List<ECountryInfo>>() {
            @Override
            public void onChanged(List<ECountryInfo> loList) {
                try {
                    ArrayList<String> strings = new ArrayList<>();
                    for (int x = 0; x < loList.size(); x++) {
                        strings.add(loList.get(x).getCntryNme());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Activity_FinancierInfo.this,
                            android.R.layout.simple_spinner_dropdown_item, strings.toArray(new String[0]));
                    txtCountry.setAdapter(adapter);

                    txtCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            for (int x = 0; x < loList.size(); x++) {
                                String lsLabel = loList.get(x).getCntryNme();
                                String lsSlctd = txtCountry.getText().toString().trim();
                                if (lsSlctd.equalsIgnoreCase(lsLabel)) {
                                    mViewModel.getModel().setCountryCode(loList.get(x).getCntryCde());
                                    mViewModel.getModel().setCountryName(lsLabel);
                                    break;
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        btnContinue.setOnClickListener(v ->{

            mViewModel.getModel().setFrstName(txtFrstNm.getText().toString());
            mViewModel.getModel().setMiddName(txtMiddNm.getText().toString());
            mViewModel.getModel().setLastName(txtLastNm.getText().toString());
            mViewModel.getModel().setSuffixNm(txtSuffixx.getText().toString());
            mViewModel.getModel().setAddressx(txtAddress.getText().toString());
            mViewModel.getModel().setEmailAdd(txtEmailAdd.getText().toString());
            mViewModel.getModel().setsFbAccntx(txt_facebookAccount.getText().toString());
            mViewModel.getModel().setsWhatsApp(txt_whatsAppAccount.getText().toString());
            if (txt_income.getText().toString().trim().isEmpty()) {
                mViewModel.getModel().setRangeOfIncome(0);
            } else {
                mViewModel.getModel().setRangeOfIncome(Long.parseLong((txt_income.getText()).toString().trim()));
            }
            mViewModel.getModel().setMobileNo(txtMobileNo.getText().toString());
            mViewModel.SaveData(new VMFinancierInfo.OnSaveInquiry() {
                @Override
                public void OnSave() {
                    poDialogx.initDialog("Ganado", "Saving inquiry. Please wait...", false);
                    poDialogx.show();
                }

                @Override
                public void OnSuccess(String args) {
                    poDialogx.dismiss();
                    poMessage.initDialog();
                    poMessage.setTitle("Ganado");
                    poMessage.setMessage(args);
                    poMessage.setPositiveButton("Okay", (view, dialog) -> {
                        dialog.dismiss();
                        finish();
                    });
                    poMessage.show();
                }


                @Override
                public void OnFailed(String message) {
                    poDialogx.dismiss();
                    poMessage.initDialog();
                    poMessage.setTitle("Ganado");
                    poMessage.setMessage(message);
                    poMessage.setPositiveButton("Okay", (view1, dialog) -> dialog.dismiss());
                    poMessage.show();
                }
            });
        });
    }

    private void initWidgets() {
        toolbar = findViewById(R.id.toolbar_PersonalInfo);
        mViewModel = new ViewModelProvider(Activity_FinancierInfo.this).get(VMFinancierInfo.class);
        poMessage = new MessageBox(Activity_FinancierInfo.this);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        txtLastNm = findViewById(R.id.txt_lastname);
        txtFrstNm = findViewById(R.id.txt_firstname);
        txtMiddNm = findViewById(R.id.txt_middname);
        txtSuffixx = findViewById(R.id.txt_suffix);
        txtCountry = findViewById(R.id.txt_financierCntry);
        txtMobileNo = findViewById(R.id.txt_mobile);
        txtEmailAdd = findViewById(R.id.txt_emailAdd);
        txt_facebookAccount = findViewById(R.id.txt_facebookAccount);
        txt_whatsAppAccount = findViewById(R.id.txt_whatsAppAccount);
        txt_income = findViewById(R.id.txt_income);
        txtAddress = findViewById(R.id.txt_address);
        spinner_relation = findViewById(R.id.spinner_relation);

        btnContinue = findViewById(R.id.btnContinue);
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

    private class OnItemClickListener implements AdapterView.OnItemClickListener {

        private final View loView;

        public OnItemClickListener(View loView) {
            this.loView = loView;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if(loView == spinner_relation){
                mViewModel.getRelation().observe(Activity_FinancierInfo.this, relations->{
                    mViewModel.getModel().setsReltionx(relations.get(i).getRelatnID());

                });
            }
        }
    }

}