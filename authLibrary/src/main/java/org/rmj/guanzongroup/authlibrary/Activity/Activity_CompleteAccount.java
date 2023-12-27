package org.rmj.guanzongroup.authlibrary.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.GConnect.room.Entities.EClientInfo;
import org.rmj.guanzongroup.authlibrary.R;
import org.rmj.guanzongroup.authlibrary.ViewModels.VMCompleteAccount;

public class Activity_CompleteAccount extends AppCompatActivity {
    private TextInputEditText tie_ca_fname;
    private TextInputEditText tie_ca_lname;
    private TextInputEditText tie_ca_mname;
    private TextInputEditText tie_ca_suffix;
    private TextInputEditText tie_ca_bdate;
    private TextInputEditText tie_ca_bplace;
    private TextInputEditText tie_ca_gender;
    private TextInputEditText tie_ca_status;
    private TextInputEditText til_ca_maiden;
    private TextInputEditText tie_ca_houseno;
    private TextInputEditText tie_ca_street;
    private TextInputEditText tie_ca_province;
    private TextInputEditText tie_ca_town;
    private TextInputEditText tie_ca_brgy;
    private VMCompleteAccount poAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_account);

        poAccount = new ViewModelProvider(this).get(VMCompleteAccount.class);

        tie_ca_fname = findViewById(R.id.tie_ca_fname);
        tie_ca_lname = findViewById(R.id.tie_ca_lname);
        tie_ca_mname = findViewById(R.id.tie_ca_mname);
        tie_ca_suffix = findViewById(R.id.tie_ca_suffix);
        tie_ca_bdate = findViewById(R.id.tie_ca_bdate);
        tie_ca_bplace = findViewById(R.id.tie_ca_bplace);
        tie_ca_gender = findViewById(R.id.tie_ca_gender);
        tie_ca_status = findViewById(R.id.tie_ca_status);
        til_ca_maiden = findViewById(R.id.tie_ca_maiden);
        tie_ca_houseno = findViewById(R.id.tie_ca_houseno);
        tie_ca_street = findViewById(R.id.tie_ca_street);
        tie_ca_province = findViewById(R.id.tie_ca_province);
        tie_ca_town = findViewById(R.id.tie_ca_town);
        tie_ca_brgy = findViewById(R.id.tie_ca_brgy);

        EClientInfo eClientInfo = new EClientInfo();
        eClientInfo.setFrstName(tie_ca_fname.getText().toString());
        eClientInfo.setLastName(tie_ca_lname.getText().toString());
        eClientInfo.setMiddName(tie_ca_mname.getText().toString());
        eClientInfo.setSuffixNm(tie_ca_suffix.getText().toString());
        eClientInfo.setBirthDte(tie_ca_bdate.getText().toString());
        eClientInfo.setBirthPlc(tie_ca_bplace.getText().toString());
        eClientInfo.setGenderCd(tie_ca_gender.getText().toString());
        eClientInfo.setCvilStat(tie_ca_status.getText().toString());
        eClientInfo.setMaidenNm(til_ca_maiden.getText().toString());
        eClientInfo.setHouseNo1(tie_ca_houseno.getText().toString());
        eClientInfo.setHouseNo2(tie_ca_street.getText().toString());
        eClientInfo.setTownIDx1(tie_ca_town.getText().toString());
        eClientInfo.setBrgyIDx1(tie_ca_brgy.getText().toString());
        eClientInfo.setFrstName(tie_ca_fname.getText().toString());
        eClientInfo.setFrstName(tie_ca_fname.getText().toString());
        eClientInfo.setFrstName(tie_ca_fname.getText().toString());
    }
}