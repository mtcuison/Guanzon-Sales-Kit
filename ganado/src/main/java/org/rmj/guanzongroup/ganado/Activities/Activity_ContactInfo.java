package org.rmj.guanzongroup.ganado.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import org.rmj.guanzongroup.ganado.R;

import java.util.Objects;

public class Activity_ContactInfo extends AppCompatActivity {

    private TextInputEditText txtMobileNo, txtEmailAdd, txtHouseNox, txtAddress;
    private MaterialAutoCompleteTextView txtPMunicipl;

    private MaterialButton btnContinue;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        initWidgets();
    }

    private void initWidgets() {

        toolbar = findViewById(R.id.toolbar_ContactInfo);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Residence Info");

        txtMobileNo = findViewById(R.id.txt_mobile);
        txtEmailAdd = findViewById(R.id.txt_email);
        txtHouseNox = findViewById(R.id.txt_houseNox);
        txtAddress = findViewById(R.id.txt_address);
        txtPMunicipl = findViewById(R.id.txt_town);
        btnContinue = findViewById(R.id.btnContinue);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(org.rmj.g3appdriver.R.anim.anim_intent_slide_in_left, org.rmj.g3appdriver.R.anim.anim_intent_slide_out_right);
    }
}