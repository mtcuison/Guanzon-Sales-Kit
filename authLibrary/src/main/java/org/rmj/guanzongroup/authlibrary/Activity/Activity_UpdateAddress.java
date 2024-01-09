package org.rmj.guanzongroup.authlibrary.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DTownInfo;
import org.rmj.g3appdriver.GCircle.room.Entities.EAddressUpdate;
import org.rmj.g3appdriver.GCircle.room.Entities.EBarangayInfo;
import org.rmj.g3appdriver.etc.LoadDialog;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.lib.Account.pojo.ChangeUserAddress;
import org.rmj.guanzongroup.authlibrary.R;
import org.rmj.guanzongroup.authlibrary.ViewModels.VMUpdateAddress;

import java.util.ArrayList;
import java.util.List;

public class Activity_UpdateAddress extends AppCompatActivity {
    private VMUpdateAddress mViewModel;
    private LoadDialog poDialog;
    private MessageBox poMessage;
    private TextInputEditText tie_ca_houseno;
    private TextInputEditText tie_ca_street;
    private MaterialAutoCompleteTextView tie_ca_town;
    private MaterialAutoCompleteTextView tie_ca_brgy;
    private MaterialAutoCompleteTextView tie_ca_prov;
    private TextInputEditText tie_ca_otp;
    private MaterialButton btn_sendotp;
    private MaterialButton btn_submit;
    private MaterialToolbar toolbar;
    private EAddressUpdate loAddrUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);

        mViewModel = new ViewModelProvider(Activity_UpdateAddress.this).get(VMUpdateAddress.class);
        poDialog = new LoadDialog(this);
        poMessage = new MessageBox(this);
        loAddrUpdate = new EAddressUpdate();

        poMessage.initDialog();
        poMessage.setTitle("Guanzon Sales Kit");
        poMessage.setPositiveButton("Close", new MessageBox.DialogButton() {
            @Override
            public void OnButtonClick(View view, AlertDialog dialog) {
                dialog.dismiss();
            }
        });

        toolbar = findViewById(R.id.toolbar);
        tie_ca_houseno = findViewById(R.id.tie_ca_houseno);
        tie_ca_street = findViewById(R.id.tie_ca_street);
        tie_ca_prov = findViewById(R.id.tie_ca_prov);
        tie_ca_town = findViewById(R.id.tie_ca_town);
        tie_ca_brgy = findViewById(R.id.tie_ca_brgy);
        tie_ca_otp = findViewById(R.id.tie_ca_otp);
        btn_sendotp = findViewById(R.id.btn_sendotp);
        btn_submit = findViewById(R.id.btn_submit);

        setSupportActionBar(toolbar); //set object toolbar as default action bar for activity

        getSupportActionBar().setTitle(" "); //set default title for action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //set back button to toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true); //enable the back button set on toolbar

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mViewModel.GetTownProvinceList().observe(Activity_UpdateAddress.this, new Observer<List<DTownInfo.TownProvinceInfo>>() {
            @Override
            public void onChanged(List<DTownInfo.TownProvinceInfo> loList) {
                try {
                    ArrayList<String> town = new ArrayList<>();
                    ArrayList<String> prov = new ArrayList<>();
                    for (int x = 0; x < loList.size(); x++) {
                        String lsTown = loList.get(x).sTownName;
                        String lsProv = loList.get(x).sProvName;

                        town.add(lsTown);
                        prov.add(lsProv);
                    }
                    ArrayAdapter<String> adaptersTown = new ArrayAdapter<>(Activity_UpdateAddress.this, android.R.layout.simple_spinner_dropdown_item, town.toArray(new String[0]));
                    tie_ca_town.setAdapter(adaptersTown);
                    tie_ca_town.setOnItemClickListener((parent, view, position, id) -> {
                        for (int x = 0; x < loList.size(); x++) {
                            String lsLabel = loList.get(x).sTownName;

                            String lsSlctd = tie_ca_town.getText().toString().trim();
                            if (lsSlctd.equalsIgnoreCase(lsLabel)) {
                                loAddrUpdate.setTownIDxx(loList.get(x).sTownIDxx);
                                break;
                            }
                        }
                    });
                    ArrayAdapter<String> adaptersProv = new ArrayAdapter<>(Activity_UpdateAddress.this, android.R.layout.simple_spinner_dropdown_item, prov.toArray(new String[0]));
                    tie_ca_prov.setAdapter(adaptersProv);
                    tie_ca_prov.setOnItemClickListener((parent, view, position, id) -> {
                        for (int x = 0; x < loList.size(); x++) {
                            String lsLabel = loList.get(x).sProvName;

                            String lsSlctd = tie_ca_prov.getText().toString().trim();
                            if (lsSlctd.equalsIgnoreCase(lsLabel)) {
                                loAddrUpdate.setAddrssTp(loList.get(x).sTownIDxx);
                                break;
                            }
                        }
                    });

                    mViewModel.GetBarangayList(loAddrUpdate.getTownIDxx()).observe(Activity_UpdateAddress.this, new Observer<List<EBarangayInfo>>() {
                        @Override
                        public void onChanged(List<EBarangayInfo> BrgyList) {
                            ArrayList<String> arrBrgy = new ArrayList<>();
                            for (int x = 0; x < BrgyList.size(); x++) {
                                String lsBrgy = BrgyList.get(x).getBrgyName();
                                arrBrgy.add(lsBrgy);
                            }
                            ArrayAdapter<String> adapters = new ArrayAdapter<>(Activity_UpdateAddress.this,
                                    android.R.layout.simple_spinner_dropdown_item, arrBrgy.toArray(new String[0]));
                            tie_ca_brgy.setAdapter(adapters);
                            tie_ca_brgy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    for (int x = 0; x < BrgyList.size(); x++) {
                                        String lsLabel = BrgyList.get(x).getBrgyName();
                                        String lsSlctd = tie_ca_brgy.getText().toString().trim();
                                        if (lsSlctd.equalsIgnoreCase(lsLabel)) {
                                            loAddrUpdate.setBrgyIDxx(BrgyList.get(x).getBrgyIDxx());
                                        }
                                    }
                                }
                            });
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btnText = btn_sendotp.getText().toString();
                if (btnText == "Request OTP"){
                    mViewModel.OTPRequest(new VMUpdateAddress.RequestOTP() {
                        @Override
                        public void onRequest(String title, String message) {
                            poDialog.initDialog(title, message, false);
                            poDialog.show();
                        }

                        @Override
                        public void onSuccess() {
                            poDialog.dismiss();

                            poMessage.setMessage("OTP was successfully sent to your mobile number.");
                            poMessage.show();

                            btn_submit.setEnabled(false); //disable until otp is verified
                            btn_sendotp.setText("Verify OTP"); //change text to verify otp after receiving
                        }

                        @Override
                        public void onFailed(String result) {
                            poDialog.dismiss();

                            poMessage.setMessage(result);
                            poMessage.show();

                            btn_submit.setEnabled(false); //disable until otp is verified
                            btn_sendotp.setText("Request OTP"); //set text to primary when request failed
                        }
                    });
                }

                if (btnText == "Verify OTP"){
                    mViewModel.OTPVerification(tie_ca_otp.getText().toString(), new VMUpdateAddress.VerifyOTP() {
                        @Override
                        public void onVerify(String title, String message) {
                            poDialog.initDialog(title, message, false);
                            poDialog.show();
                        }

                        @Override
                        public void onSuccess() {
                            poDialog.dismiss();

                            poMessage.setMessage("OTP Verified");
                            poMessage.show();

                            btn_submit.setEnabled(true); //enable when otp is verified
                            btn_sendotp.setText("Request OTP"); //change text to request otp after verifying
                        }

                        @Override
                        public void onFailed(String result) {
                            poDialog.dismiss();

                            poMessage.setMessage(result);
                            poMessage.show();

                            btn_submit.setEnabled(false); //disable when otp is not verified
                            btn_sendotp.setText("Verify OTP"); //retain text to verify otp
                        }
                    });
                }
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeUserAddress loAddress = new ChangeUserAddress();

                loAddress.setsHouseNox(tie_ca_houseno.getText().toString());
                loAddress.setsAddressx(tie_ca_street.getText().toString());
                loAddress.setcAddrssTp(tie_ca_prov.getText().toString());
                loAddress.setsTownIDx(tie_ca_town.getText().toString());
                loAddress.setsBrgyIdx(tie_ca_brgy.getText().toString());
                loAddress.setcPrimary("1");
                loAddress.setnLatitude("");
                loAddress.setnLongitude("");
                loAddress.setsRemarks("");
                loAddress.setsRqstCd("");
                loAddress.setsSourceCd("");
                loAddress.setsSourceNo("");

                mViewModel.ChangeAccountAddress(loAddress, new VMUpdateAddress.SubmitChanges() {
                    @Override
                    public void onChange(String title, String message) {
                        poDialog.initDialog(title, message, false);
                        poDialog.show();
                    }

                    @Override
                    public void onSuccess() {
                        poDialog.dismiss();

                        poMessage.setMessage("Address changed successfully");
                        poMessage.show();
                    }

                    @Override
                    public void onFailed(String result) {
                        poDialog.dismiss();

                        poMessage.setMessage("Address failed to update");
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
}