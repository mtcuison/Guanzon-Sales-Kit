package org.rmj.guanzongroup.ganado.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DTownInfo;
import org.rmj.g3appdriver.etc.LoadDialog;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.guanzongroup.ganado.R;
import org.rmj.guanzongroup.ganado.ViewModel.VMPersonalInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Activity_ClientInfo extends AppCompatActivity {

    private VMPersonalInfo mViewModel;
    private MessageBox poMessage;
    private LoadDialog poDialogx;

    private TextInputEditText txtLastNm, txtFrstNm, txtMiddNm,txtMaidNm, txtSuffixx,  txtBirthDt,
            txtEmailAdd, txtMobileNo,  txtHouseNox, txtAddress;
    private TextInputLayout tilMaidNm;
    private MaterialAutoCompleteTextView txtMunicipl,txtBPlace;
    private RadioGroup rgGender;
    private MaterialAutoCompleteTextView spinner_relation;
    private MaterialButton btnContinue, btnPrev;
    private MaterialCheckBox txtMobileType1, txtMobileType2, txtMobileType3;

    private MaterialToolbar toolbar;
    private String sTansNox = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        poMessage = new MessageBox(Activity_ClientInfo.this);
        poDialogx = new LoadDialog(Activity_ClientInfo.this);
        setContentView(R.layout.activity_client_info);
        initWidgets();
        mViewModel.InitializeApplication(getIntent());
        mViewModel.InitGeoLocation(Activity_ClientInfo.this);

        mViewModel.getRelation().observe(Activity_ClientInfo.this, eRelations->{
            try {
                ArrayList<String> string = new ArrayList<>();
                for (int x = 0; x < eRelations.size(); x++) {
                    String lsColor = eRelations.get(x).getRelatnDs();
//                        String lsTown =  loList.get(x).sProvName ;
                    string.add(lsColor);

                }
                ArrayAdapter<String> adapters = new ArrayAdapter<>(Activity_ClientInfo.this, android.R.layout.simple_spinner_dropdown_item, string.toArray(new String[0]));
                spinner_relation.setText(eRelations.get(0).getRelatnDs());
                spinner_relation.setText(eRelations.get(0).getRelatnDs());
                mViewModel.getModel().setsReltionx(eRelations.get(0).getRelatnID());
                spinner_relation.setAdapter(adapters);
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        spinner_relation.setOnItemClickListener(new Activity_ClientInfo.OnItemClickListener(spinner_relation));
        mViewModel.GetTownProvinceList().observe(Activity_ClientInfo.this, new Observer<List<DTownInfo.TownProvinceInfo>>() {
            @Override
            public void onChanged(List<DTownInfo.TownProvinceInfo> loList) {
                try {
                    ArrayList<String> string = new ArrayList<>();
                    for (int x = 0; x < loList.size(); x++) {
                        String lsTown = loList.get(x).sTownName + ", " + loList.get(x).sProvName;
//                        String lsTown =  loList.get(x).sProvName ;
                        string.add(lsTown);

                    }
                    ArrayAdapter<String> adapters = new ArrayAdapter<>(Activity_ClientInfo.this, android.R.layout.simple_spinner_dropdown_item, string.toArray(new String[0]));
                    txtBPlace.setAdapter(adapters);
                    txtBPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            for (int x = 0; x < loList.size(); x++) {
                                String lsLabel = loList.get(x).sTownName + ", " + loList.get(x).sProvName;
                                String lsSlctd = txtBPlace.getText().toString().trim();
                                if (lsSlctd.equalsIgnoreCase(lsLabel)) {
                                    mViewModel.getModel().setBirthPlc(loList.get(x).sTownIDxx);
                                    mViewModel.getModel().setsBirthIDx(lsLabel);
                                    break;
                                }

                            }
                        }
                    });
                    ArrayAdapter<String> adapters1 = new ArrayAdapter<>(Activity_ClientInfo.this, android.R.layout.simple_spinner_dropdown_item, string.toArray(new String[0]));
                    txtMunicipl.setAdapter(adapters1);
                    txtMunicipl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            for (int x = 0; x < loList.size(); x++) {
                                String lsLabel = loList.get(x).sTownName + ", " + loList.get(x).sProvName;
                                String lsSlctd = txtMunicipl.getText().toString().trim();
                                if (lsSlctd.equalsIgnoreCase(lsLabel)) {
                                    mViewModel.getModel().setTownIDxx(loList.get(x).sTownIDxx);
                                    mViewModel.getModel().setsMncplNme(lsLabel);
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
        txtBirthDt.setOnClickListener(v -> {
            final Calendar newCalendar = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") final SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy");
            final DatePickerDialog StartTime = new DatePickerDialog(Activity_ClientInfo.this,
                    android.R.style.Theme_Holo_Dialog, (view131, year, monthOfYear, dayOfMonth) -> {
                try {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    String lsDate = dateFormatter.format(newDate.getTime());
                    txtBirthDt.setText(lsDate);
                    Date loDate = new SimpleDateFormat("MMMM dd, yyyy").parse(lsDate);
                    lsDate = new SimpleDateFormat("yyyy-MM-dd").format(loDate);
//                    Log.d(TAG, "Save formatted time: " + lsDate);
                    mViewModel.getModel().setBirthDte(lsDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            StartTime.getDatePicker().setMaxDate(new Date().getTime());
            StartTime.show();
        });

        rgGender.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rb_male) {
                mViewModel.getModel().setGenderCd("0");
//                tilMaidNm.setVisibility(View.GONE);
//                txtMaidNm.setText("");
            }
            if (i == R.id.rb_female) {
                mViewModel.getModel().setGenderCd("1");
//                tilMaidNm.setVisibility(View.VISIBLE);
            }
        });

        btnContinue.setOnClickListener(v ->{

            mViewModel.InitGeoLocation(Activity_ClientInfo.this);
            mViewModel.getModel().setFrstName(txtFrstNm.getText().toString());
            mViewModel.getModel().setMiddName(txtMiddNm.getText().toString());
            mViewModel.getModel().setLastName(txtLastNm.getText().toString());
            mViewModel.getModel().setSuffixNm(txtSuffixx.getText().toString());
            mViewModel.getModel().setHouseNox(txtHouseNox.getText().toString());
            mViewModel.getModel().setAddressx(txtAddress.getText().toString());
            mViewModel.getModel().setEmailAdd(txtEmailAdd.getText().toString());
            mViewModel.getModel().setMobileNo(txtMobileNo.getText().toString());
            mViewModel.getModel().setMaidenNm(txtMaidNm.getText().toString());
            mViewModel.SaveData(new VMPersonalInfo.OnSaveInquiry() {
                @Override
                public void OnSave() {
                    poDialogx.initDialog("Ganado", "Saving Client Info. Please wait...", false);
                    poDialogx.show();
                }

                @Override
                public void OnSuccess(String args) {
                    poDialogx.dismiss();

                    poMessage.initDialog();
                    poMessage.setTitle("Ganado");
                    poMessage.setMessage(args);
                    poMessage.setPositiveButton("Okay", (view1, dialog) -> {
                        dialog.dismiss();
                        mViewModel.initStopLocation();
                        Intent loIntent = new Intent(Activity_ClientInfo.this, Activity_BrandSelection.class);
                        loIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(loIntent);
                        overridePendingTransition(org.rmj.g3appdriver.R.anim.anim_intent_slide_in_right, org.rmj.g3appdriver.R.anim.anim_intent_slide_out_left);

                        finish();
                    });
                    poMessage.show();
                }


                @Override
                public void OnFailed(String message) {
                    poDialogx.dismiss();

                    mViewModel.InitGeoLocation(Activity_ClientInfo.this);
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
        mViewModel = new ViewModelProvider(Activity_ClientInfo.this).get(VMPersonalInfo.class);
        poMessage = new MessageBox(Activity_ClientInfo.this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        txtLastNm = findViewById(R.id.txt_lastname);
        txtFrstNm = findViewById(R.id.txt_firstname);
        txtMiddNm = findViewById(R.id.txt_middname);
        txtSuffixx = findViewById(R.id.txt_suffix);
        txtBirthDt = findViewById(R.id.txt_birthdate);
        txtBPlace = findViewById(R.id.txt_bpTown);
        rgGender = findViewById(R.id.rg_gender);
        txtMobileNo = findViewById(R.id.txt_mobile);
        txtEmailAdd = findViewById(R.id.txt_emailAdd);
        txtHouseNox = findViewById(R.id.txt_houseNox);
        txtAddress = findViewById(R.id.txt_address);
        txtMunicipl = findViewById(R.id.txt_town);
        spinner_relation = findViewById(R.id.spinner_relation);
        txtMaidNm = findViewById(R.id.txt_maiden);
        tilMaidNm = findViewById(R.id.til_maiden);
        btnContinue = findViewById(R.id.btnContinue);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showConfirmationDialog();
//                finish();
            }
        });
    }

//    @Override
//    public void finish() {
//        super.finish();
//
//
//        poMessage.initDialog();
//        poMessage.setPositiveButton("Yes", (view, dialog) -> {
//            dialog.dismiss();
//            mViewModel.initStopLocation();
//            overridePendingTransition(org.rmj.g3appdriver.R.anim.anim_intent_slide_in_left, org.rmj.g3appdriver.R.anim.anim_intent_slide_out_right);
//
//            finish();
//        });
//        poMessage.setNegativeButton("No", (view, dialog) -> dialog.dismiss());
//        poMessage.setTitle("Ganado");
//        poMessage.setMessage("Do you really want to close the client information module? Every detail entered will be removed.");
//        poMessage.show();
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            showConfirmationDialog();
//            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mViewModel.RemoveInquiry();
        getViewModelStore().clear();
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
                mViewModel.getRelation().observe(Activity_ClientInfo.this, relations->{
                    mViewModel.getModel().setsReltionx(relations.get(i).getRelatnID());
                });
            }
        }
    }

    private void showConfirmationDialog(){
        poMessage.initDialog();
        poMessage.setPositiveButton("Yes", (view, dialog) -> {
            dialog.dismiss();
            mViewModel.initStopLocation();
            overridePendingTransition(org.rmj.g3appdriver.R.anim.anim_intent_slide_in_left, org.rmj.g3appdriver.R.anim.anim_intent_slide_out_right);

            finish();
        });
        poMessage.setNegativeButton("No", (view, dialog) -> dialog.dismiss());
        poMessage.setTitle("Ganado");
        poMessage.setMessage("Do you really want to close the client information module? Every detail entered will be removed.");
        poMessage.show();
    }
}