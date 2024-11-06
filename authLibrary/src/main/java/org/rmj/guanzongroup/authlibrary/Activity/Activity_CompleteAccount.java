package org.rmj.guanzongroup.authlibrary.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.GCircle.Apps.CreditApp.CreditAppConstants;
import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DTownInfo;
import org.rmj.g3appdriver.GCircle.room.Entities.EBarangayInfo;
import org.rmj.g3appdriver.GCircle.room.Entities.EClientInfoSalesKit;
import org.rmj.g3appdriver.etc.LoadDialog;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.guanzongroup.authlibrary.R;
import org.rmj.guanzongroup.authlibrary.ViewModels.VMCompleteAccount;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Activity_CompleteAccount extends AppCompatActivity {
    private TextInputEditText tie_ca_fname;
    private TextInputEditText tie_ca_lname;
    private TextInputEditText tie_ca_mname;
    private TextInputEditText tie_ca_suffix;
    private TextInputEditText tie_ca_bdate;
    private MaterialAutoCompleteTextView tie_ca_bplace;
    private MaterialAutoCompleteTextView tie_ca_status, tie_ca_citizen, tie_ca_gender;
    private TextInputEditText til_ca_maiden;
    private TextInputEditText tie_ca_houseno;
    private TextInputEditText tie_ca_street;
    private MaterialAutoCompleteTextView tie_ca_town;
    private MaterialAutoCompleteTextView tie_ca_brgy;
    private MaterialButton btn_submit;
    private MaterialToolbar toolbar;
    private VMCompleteAccount mViewModel;
    private LoadDialog poDialog;
    private MessageBox poMessage;
    private EClientInfoSalesKit eClientInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_account);

        poDialog = new LoadDialog(this);
        poMessage = new MessageBox(this);

        eClientInfo = new EClientInfoSalesKit();

        poMessage.initDialog();
        poMessage.setTitle("Guanzon Sales Kit");


        toolbar = findViewById(R.id.toolbar);
        tie_ca_fname = findViewById(R.id.tie_ca_fname);
        tie_ca_lname = findViewById(R.id.tie_ca_lname);
        tie_ca_mname = findViewById(R.id.tie_ca_mname);
        tie_ca_suffix = findViewById(R.id.tie_ca_suffix);
        tie_ca_bdate = findViewById(R.id.tie_ca_bdate);
        tie_ca_bplace = findViewById(R.id.tie_ca_bplace);
        tie_ca_gender = findViewById(R.id.tie_ca_gender);
        tie_ca_status = findViewById(R.id.tie_ca_status);
        til_ca_maiden = findViewById(R.id.tie_ca_maiden);
//        tie_ca_citizen = findViewById(R.id.tie_ca_citizen);
        tie_ca_houseno = findViewById(R.id.tie_ca_houseno);
        tie_ca_street = findViewById(R.id.tie_ca_street);
        tie_ca_town = findViewById(R.id.tie_ca_town);
        tie_ca_brgy = findViewById(R.id.tie_ca_brgy);

        btn_submit = findViewById(R.id.btn_submit);

        setSupportActionBar(toolbar); //set object toolbar as default action bar for activity

        getSupportActionBar().setTitle(" "); //set default title for action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //set back button to toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true); //enable the back button set on toolbar

        mViewModel = new ViewModelProvider(this).get(VMCompleteAccount.class);
        mViewModel.GetCompleteProfile().observe(this, new Observer<EClientInfoSalesKit>() {
            @Override
            public void onChanged(EClientInfoSalesKit eClientInfoSalesKit) {
                if (eClientInfoSalesKit != null){
                    String fstname = eClientInfoSalesKit.getFrstName();
                    String midname = eClientInfoSalesKit.getMiddName();
                    String lstname = eClientInfoSalesKit.getLastName();
                    String suffix = eClientInfoSalesKit.getSuffixNm();

                    tie_ca_fname.setText(fstname);
                    tie_ca_mname.setText(midname);
                    tie_ca_lname.setText(lstname);
                    tie_ca_suffix.setText(suffix);
                }
            }
        });
        mViewModel.GetTownProvinceList().observe(Activity_CompleteAccount.this, new Observer<List<DTownInfo.TownProvinceInfo>>() {
            @Override
            public void onChanged(List<DTownInfo.TownProvinceInfo> loList) {
                try {
                    ArrayList<String> string = new ArrayList<>();
                    for (int x = 0; x < loList.size(); x++) {
                        String lsTown = loList.get(x).sTownName + ", " + loList.get(x).sProvName;
                        string.add(lsTown);
                    }
                    ArrayAdapter<String> adapters = new ArrayAdapter<>(Activity_CompleteAccount.this, android.R.layout.simple_spinner_dropdown_item, string.toArray(new String[0]));
                    tie_ca_town.setAdapter(adapters);
                    tie_ca_town.setOnItemClickListener((parent, view, position, id) -> {
                        for (int x = 0; x < loList.size(); x++) {
                            String lsLabel = loList.get(x).sTownName + ", " + loList.get(x).sProvName;
                            String lsSlctd = tie_ca_town.getText().toString().trim();
                            if (lsSlctd.equalsIgnoreCase(lsLabel)) {
                                eClientInfo.setTownIDx1(loList.get(x).sTownIDxx);
                                eClientInfo.setTownIDx2(loList.get(x).sTownIDxx);
                                break;
                            }
                        }

                        mViewModel.GetBarangayList(eClientInfo.getTownIDx1()).observe(Activity_CompleteAccount.this, new Observer<List<EBarangayInfo>>() {
                            @Override
                            public void onChanged(List<EBarangayInfo> BrgyList) {
                                ArrayList<String> string1 = new ArrayList<>();
                                for (int x = 0; x < BrgyList.size(); x++) {
                                    String lsBrgy = BrgyList.get(x).getBrgyName();
                                    string1.add(lsBrgy);
                                }
                                ArrayAdapter<String> adapters = new ArrayAdapter<>(Activity_CompleteAccount.this,
                                        android.R.layout.simple_spinner_dropdown_item, string1.toArray(new String[0]));
                                tie_ca_brgy.setAdapter(adapters);
                                tie_ca_brgy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        for (int x = 0; x < BrgyList.size(); x++) {
                                            String lsLabel = BrgyList.get(x).getBrgyName();
                                            String lsSlctd = tie_ca_brgy.getText().toString().trim();
                                            if (lsSlctd.equalsIgnoreCase(lsLabel)) {
                                                eClientInfo.setBrgyIDx1(BrgyList.get(x).getBrgyIDxx());
                                                eClientInfo.setBrgyIDx2(BrgyList.get(x).getBrgyIDxx());
                                            }
                                        }

                                    }
                                });
                            }
                        });
                    });

                    ArrayAdapter<String> loAdapter = new ArrayAdapter<>(Activity_CompleteAccount.this, android.R.layout.simple_spinner_dropdown_item, string.toArray(new String[0]));
                    tie_ca_bplace.setAdapter(loAdapter);
                    tie_ca_bplace.setOnItemClickListener((parent, view, position, id) -> {
                        for (int x = 0; x < loList.size(); x++) {
                            String lsLabel = loList.get(x).sTownName + ", " + loList.get(x).sProvName;
                            String lsSlctd = tie_ca_bplace.getText().toString().trim();
                            if (lsSlctd.equalsIgnoreCase(lsLabel)) {
                                eClientInfo.setBirthPlc(loList.get(x).sTownIDxx);
                                break;
                            }
                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        ArrayAdapter<String> loAdapter = new ArrayAdapter<>(Activity_CompleteAccount.this,
                android.R.layout.simple_spinner_dropdown_item, CreditAppConstants.CIVIL_STATUS);
        tie_ca_status.setAdapter(loAdapter);
        tie_ca_status.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eClientInfo.setCvilStat(String.valueOf(position));
            }
        });

        ArrayAdapter<String> loGender = new ArrayAdapter<>(Activity_CompleteAccount.this,
                android.R.layout.simple_spinner_dropdown_item, CreditAppConstants.GENDER);
        tie_ca_gender.setAdapter(loGender);
        tie_ca_gender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eClientInfo.setGenderCd(String.valueOf(position));
            }
        });


        tie_ca_bdate.setOnClickListener(v -> {
            final Calendar newCalendar = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") final SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy");
            final DatePickerDialog StartTime = new DatePickerDialog(Activity_CompleteAccount.this,
                    android.R.style.Theme_Holo_Dialog, (view131, year, monthOfYear, dayOfMonth) -> {
                try {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    String lsDate = dateFormatter.format(newDate.getTime());
                    tie_ca_bdate.setText(lsDate);
                    Date loDate = new SimpleDateFormat("MMMM dd, yyyy").parse(lsDate);
                    lsDate = new SimpleDateFormat("yyyy-MM-dd").format(loDate);
                    Log.d("ActivityCompleteAccount", "Save formatted time: " + lsDate);
//                    mViewModel.getModel().setBrthDate(lsDate);
                    eClientInfo.setBirthDte(lsDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            StartTime.getDatePicker().setMaxDate(new Date().getTime());
            StartTime.show();
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Personal Info
                eClientInfo.setFrstName(tie_ca_fname.getText().toString());
                eClientInfo.setLastName(tie_ca_lname.getText().toString());
                eClientInfo.setMiddName(tie_ca_mname.getText().toString());
                eClientInfo.setSuffixNm(tie_ca_suffix.getText().toString());
//                eClientInfo.setBirthDte(tie_ca_bdate.getText().toString());
//                eClientInfo.setBirthPlc(tie_ca_bplace.getText().toString());
                eClientInfo.setMaidenNm(til_ca_maiden.getText().toString());
//                eClientInfo.setCitizenx(tie_ca_citizen.getText().toString());

                //Address Info
                eClientInfo.setHouseNo1(tie_ca_houseno.getText().toString());
                eClientInfo.setAddress1(tie_ca_street.getText().toString());
                eClientInfo.setHouseNo2(tie_ca_houseno.getText().toString());
                eClientInfo.setAddress2(tie_ca_street.getText().toString());

                mViewModel.CompleteAccount(eClientInfo, new VMCompleteAccount.SubmitCallback() {
                    @Override
                    public void onLoad(String title, String message) {
                        poDialog.initDialog(title, message, false);
                        poDialog.show();
                    }

                    @Override
                    public void onSuccess() {
                        poDialog.dismiss();

                        poMessage.setIcon(org.rmj.g3appdriver.R.drawable.baseline_message_24);
                        poMessage.setMessage("Complete Account request successfully sent to server");
                        poMessage.setPositiveButton("Close", new MessageBox.DialogButton() {
                            @Override
                            public void OnButtonClick(View view, AlertDialog dialog) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        poMessage.show();
                    }

                    @Override
                    public void onFailed(String result) {
                        poDialog.dismiss();

                        poMessage.setIcon(org.rmj.g3appdriver.R.drawable.baseline_error_24);
                        poMessage.setMessage(result);
                        poMessage.setPositiveButton("Close", new MessageBox.DialogButton() {
                            @Override
                            public void OnButtonClick(View view, AlertDialog dialog) {
                                dialog.dismiss();
                            }
                        });
                        poMessage.show();
                    }
                });
            }
        });

        overridePendingTransition(org.rmj.g3appdriver.R.anim.anim_intent_slide_in_left, org.rmj.g3appdriver.R.anim.anim_intent_slide_out_right);
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