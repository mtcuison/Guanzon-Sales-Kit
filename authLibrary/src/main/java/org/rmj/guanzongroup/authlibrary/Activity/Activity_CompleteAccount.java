package org.rmj.guanzongroup.authlibrary.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.GConnect.room.Entities.EClientInfo;
import org.rmj.g3appdriver.etc.LoadDialog;
import org.rmj.g3appdriver.etc.MessageBox;
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
    private TextInputEditText tie_ca_citizen;
    private TextInputEditText tie_ca_houseno;
    private TextInputEditText tie_ca_street;
    private TextInputEditText tie_ca_town;
    private TextInputEditText tie_ca_brgy;
    private MaterialButton btn_submit;
    private VMCompleteAccount poAccount;
    private LoadDialog poDialog;
    private MessageBox poMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_account);

        poAccount = new ViewModelProvider(this).get(VMCompleteAccount.class);
        poDialog = new LoadDialog(this);
        poMessage = new MessageBox(this);

        poMessage.initDialog();
        poMessage.setTitle("Guanzon Sales Kit");
        poMessage.setPositiveButton("Close", new MessageBox.DialogButton() {
            @Override
            public void OnButtonClick(View view, AlertDialog dialog) {
                dialog.dismiss();
            }
        });

        tie_ca_fname = findViewById(R.id.tie_ca_fname);
        tie_ca_lname = findViewById(R.id.tie_ca_lname);
        tie_ca_mname = findViewById(R.id.tie_ca_mname);
        tie_ca_suffix = findViewById(R.id.tie_ca_suffix);
        tie_ca_bdate = findViewById(R.id.tie_ca_bdate);
        tie_ca_bplace = findViewById(R.id.tie_ca_bplace);
        tie_ca_gender = findViewById(R.id.tie_ca_gender);
        tie_ca_status = findViewById(R.id.tie_ca_status);
        til_ca_maiden = findViewById(R.id.tie_ca_maiden);
        tie_ca_citizen = findViewById(R.id.tie_ca_citizen);
        tie_ca_houseno = findViewById(R.id.tie_ca_houseno);
        tie_ca_street = findViewById(R.id.tie_ca_street);
        tie_ca_town = findViewById(R.id.tie_ca_town);
        tie_ca_brgy = findViewById(R.id.tie_ca_brgy);

        btn_submit = findViewById(R.id.btn_submit);

        EClientInfo eClientInfo = new EClientInfo();
        //Personal Info
        eClientInfo.setFrstName(tie_ca_fname.getText().toString());
        eClientInfo.setLastName(tie_ca_lname.getText().toString());
        eClientInfo.setMiddName(tie_ca_mname.getText().toString());
        eClientInfo.setSuffixNm(tie_ca_suffix.getText().toString());
        eClientInfo.setBirthDte(tie_ca_bdate.getText().toString());
        eClientInfo.setBirthPlc(tie_ca_bplace.getText().toString());
        eClientInfo.setGenderCd(tie_ca_gender.getText().toString());
        eClientInfo.setCvilStat(tie_ca_status.getText().toString());
        eClientInfo.setMaidenNm(til_ca_maiden.getText().toString());
        eClientInfo.setCitizenx(tie_ca_citizen.getText().toString());

        //Address Info
        eClientInfo.setHouseNo1(tie_ca_houseno.getText().toString());
        eClientInfo.setAddress1(tie_ca_street.getText().toString());
        eClientInfo.setTownIDx1(tie_ca_town.getText().toString());
        eClientInfo.setBrgyIDx1(tie_ca_brgy.getText().toString());

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                poAccount.CompleteAccount(eClientInfo, new VMCompleteAccount.SubmitCallback() {
                    @Override
                    public void onLoad(String title, String message) {
                        poDialog.initDialog(title, message, false);
                        poDialog.show();
                    }

                    @Override
                    public void onSuccess() {
                        poDialog.dismiss();

                        poMessage.setMessage("Account request successfully sent to server");
                        poMessage.show();
                    }

                    @Override
                    public void onFailed(String result) {
                        poDialog.dismiss();

                        poMessage.setMessage(result);
                        poMessage.show();
                    }
                });
            }
        });
    }
}