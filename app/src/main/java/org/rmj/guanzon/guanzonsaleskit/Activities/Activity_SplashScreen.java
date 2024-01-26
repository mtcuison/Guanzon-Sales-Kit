package org.rmj.guanzon.guanzonsaleskit.Activities;

import static org.rmj.g3appdriver.utils.ServiceScheduler.FIFTEEN_MINUTE_PERIODIC;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textview.MaterialTextView;
import org.rmj.g3appdriver.etc.AppConfigPreference;
import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.etc.TransparentToolbar;
import org.rmj.g3appdriver.utils.AppDirectoryCreator;
import org.rmj.g3appdriver.utils.ServiceScheduler;
import org.rmj.guanzon.guanzonsaleskit.R;
//import org.rmj.guanzon.guanzonsaleskit.BuildConfig;
import org.rmj.guanzon.guanzonsaleskit.Service.DataDownloadService;
import org.rmj.guanzon.guanzonsaleskit.Service.GMessagingService;
import org.rmj.guanzon.guanzonsaleskit.ViewModel.VMSplashScreen;
import org.rmj.guanzongroup.authlibrary.Activity.Activity_Login;

import java.util.ArrayList;
import java.util.List;

public class Activity_SplashScreen extends AppCompatActivity {
    public static final String TAG = Activity_SplashScreen.class.getSimpleName();

    private VMSplashScreen mViewModel;

    private ProgressBar prgrssBar;
    private MaterialTextView lblVrsion;

    private MessageBox poDialog;

    private ActivityResultLauncher<String[]> poRequest;

    private ActivityResultLauncher<Intent> poLogin;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VMSplashScreen.class);
        setContentView(R.layout.activity_splash_screen);
        InitActivityResultLaunchers();
        poDialog = new MessageBox(Activity_SplashScreen.this);
        new TransparentToolbar(Activity_SplashScreen.this).SetupActionbar();
        prgrssBar = findViewById(R.id.progress_splashscreen);
        lblVrsion = findViewById(R.id.lbl_versionInfo);
//        lblVrsion.setText(BuildConfig.VERSION_NAME);
        if (!isMyServiceRunning(GMessagingService.class)) {
            startService(new Intent(Activity_SplashScreen.this, GMessagingService.class));
        }
        InitializeAppContentDisclosure();

        AppDirectoryCreator loCreator = new AppDirectoryCreator();
        if(loCreator.createAppDirectory(Activity_SplashScreen.this)){
            Log.e(TAG, loCreator.getMessage());
        } else {
            Log.e(TAG, loCreator.getMessage());
        }
    }

    private void InitializeAppContentDisclosure(){
        boolean isFirstLaunch = AppConfigPreference.getInstance(Activity_SplashScreen.this).isAppFirstLaunch();
        if(isFirstLaunch) {
            MessageBox loMessage = new MessageBox(Activity_SplashScreen.this);
            loMessage.initDialog();
            loMessage.setTitle("Guanzon Sales Kit");
            loMessage.setMessage("Guanzon  Sales Kit collects data and other major features of the app" +
                    " even when the app is closed or not in use.");
            loMessage.setPositiveButton("Agree", (view, dialog) -> {
                dialog.dismiss();
                CheckPermissions();
            });
            loMessage.setNegativeButton("Disagree", (view, dialog) -> {
                dialog.dismiss();
                finish();
            });
            loMessage.show();
        } else {
            CheckPermissions();
        }
    }

    private void CheckPermissions(){
        List<String> lsPermissions = new ArrayList<>();
        if(ActivityCompat.checkSelfPermission(Activity_SplashScreen.this, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            lsPermissions.add(android.Manifest.permission.INTERNET);
        }
        if(ActivityCompat.checkSelfPermission(Activity_SplashScreen.this, android.Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED){
            lsPermissions.add(android.Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if(ActivityCompat.checkSelfPermission(Activity_SplashScreen.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            lsPermissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ActivityCompat.checkSelfPermission(Activity_SplashScreen.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            lsPermissions.add(android.Manifest.permission.READ_PHONE_STATE);
        }
        if(ActivityCompat.checkSelfPermission(Activity_SplashScreen.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            lsPermissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if(ActivityCompat.checkSelfPermission(Activity_SplashScreen.this, android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED){
            lsPermissions.add(android.Manifest.permission.GET_ACCOUNTS);
        }
        if(ActivityCompat.checkSelfPermission(Activity_SplashScreen.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            lsPermissions.add(android.Manifest.permission.CAMERA);
        }
        if(ActivityCompat.checkSelfPermission(Activity_SplashScreen.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            lsPermissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(ActivityCompat.checkSelfPermission(Activity_SplashScreen.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            lsPermissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(ActivityCompat.checkSelfPermission(Activity_SplashScreen.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                lsPermissions.add(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
        poRequest.launch(lsPermissions.toArray(new String[0]));
    }

    private void InitializeAppData(){
        mViewModel.InitializeData(new VMSplashScreen.OnInitializeCallback() {
            @Override
            public void OnProgress(String args, int progress) {
                prgrssBar.setProgress(progress);
            }

            @Override
            public void OnHasDCP() {
            }

            @Override
            public void OnSuccess() {
                startActivity(new Intent(Activity_SplashScreen.this, Activity_Home.class));
                finish();
            }

            @Override
            public void OnNoSession() {
                poLogin.launch(new Intent(Activity_SplashScreen.this, Activity_Login.class));
            }

            @Override
            public void OnFailed(String message) {
                poDialog.initDialog();
                poDialog.setTitle("Guanzon Circle");
                poDialog.setMessage(message);
                poDialog.setPositiveButton("Okay", (view, dialog) -> {
                    dialog.dismiss();
                    finish();
                });
                poDialog.show();
            }
        });
    }

    private void InitializeData(){
        mViewModel.InitUserData(new VMSplashScreen.OnInitializeCallback() {
            @Override
            public void OnProgress(String args, int progress) {
                prgrssBar.setProgress(progress);
            }

            @Override
            public void OnHasDCP() {
            }

            @Override
            public void OnSuccess() {
                startActivity(new Intent(Activity_SplashScreen.this, Activity_Home.class));
                finish();
            }

            @Override
            public void OnNoSession() {
            }

            @Override
            public void OnFailed(String message) {
                poDialog.initDialog();
                poDialog.setTitle("Guanzon Circle");
                poDialog.setMessage(message);
                poDialog.setPositiveButton("Okay", (view, dialog) -> {
                    dialog.dismiss();
                    finish();
                });
                poDialog.show();
            }
        });
    }

    private void InitActivityResultLaunchers(){
        poRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            InitializeAppData();
        });
        poLogin = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                ServiceScheduler.scheduleJob(Activity_SplashScreen.this, DataDownloadService.class, FIFTEEN_MINUTE_PERIODIC, AppConstants.DataServiceID);
//                InitializeData();
                startActivity(new Intent(Activity_SplashScreen.this, Activity_Home.class));
                finish();
            } else if (result.getResultCode() == RESULT_CANCELED) {
                finish();
            }
        });
//        poLogin = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//            if (result.getResultCode() == RESULT_OK) {
//                InitializeData();
////                startActivity(new Intent(Activity_SplashScreen.this, Activity_Home.class));
////                finish();
//            } else if (result.getResultCode() == RESULT_CANCELED) {
//                finish();
//            }
//        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}