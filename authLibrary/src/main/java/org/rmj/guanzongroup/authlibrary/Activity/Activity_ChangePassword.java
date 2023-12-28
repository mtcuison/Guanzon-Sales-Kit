package org.rmj.guanzongroup.authlibrary.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.rmj.guanzongroup.authlibrary.R;
import org.w3c.dom.Text;

public class Activity_ChangePassword extends AppCompatActivity {
    private TextInputLayout til_ca_otp;
    private TextInputEditText password;
    private TextInputEditText conf_password;
    private TextInputEditText tie_ca_otp;
    private MaterialButton btn_sendotp;
    private  MaterialButton btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        til_ca_otp = findViewById(R.id.til_ca_otp);
        password = findViewById(R.id.password);
        conf_password = findViewById(R.id.conf_password);
        tie_ca_otp = findViewById(R.id.tie_ca_otp);
        btn_sendotp = findViewById(R.id.btn_sendotp);
        btn_submit = findViewById(R.id.btn_submit);
        btn_sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                til_ca_otp.setEnabled(true);
            }
        });
    }
}