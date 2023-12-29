package org.rmj.guanzongroup.authlibrary.ViewModels;

import static org.rmj.g3appdriver.etc.AppConstants.getLocalMessage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.rmj.g3appdriver.lib.Account.AccountMaster;
import org.rmj.g3appdriver.lib.Account.Model.Auth;
import org.rmj.g3appdriver.lib.Account.Model.iAuth;
import org.rmj.g3appdriver.lib.Account.OTPSender;
import org.rmj.g3appdriver.lib.Account.pojo.PasswordChange;
import org.rmj.g3appdriver.lib.Account.pojo.PasswordUpdate;
import org.rmj.g3appdriver.utils.ConnectionUtil;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

public class VMChangePassword extends AndroidViewModel {
    public static final String TAG =  VMLogin.class.getSimpleName();
    private final iAuth poSys;
    private final ConnectionUtil poConn;
    private OTPSender poOTPRqst;
    private String message;
    public VMChangePassword(@NonNull Application application) {
        super(application);

        this.poSys = new AccountMaster(application).initGuanzonApp().getInstance(Auth.CHANGE_PASSWORD);
        this.poConn = new ConnectionUtil(application);
        this.poOTPRqst = new OTPSender(application);
    }
    public void ChangePassword(PasswordChange loPassword, SubmitChanges callback){
        TaskExecutor.Execute(loPassword, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.onChange("Guanzon Sales Kit", "Authenticating to GCircle App. Please wait . . .");
            }

            @Override
            public Object DoInBackground(Object args) {
                try {
                    if(!poConn.isDeviceConnected()){
                        message = poConn.getMessage();
                        return false;
                    }

                    int lnResult = poSys.DoAction(args);

                    if(lnResult == 0 || lnResult == 2){
                        message = poSys.getMessage();
                        return false;
                    }
                    return true;
                }catch (Exception e){
                    e.printStackTrace();
                    message = getLocalMessage(e);
                    return false;
                }
            }

            @Override
            public void OnPostExecute(Object object) {
                boolean isSuccess = (boolean) object;
                if(!isSuccess){
                    callback.onFailed(message);
                    return;
                }

                callback.onSuccess();
            }
        });
    }
    public interface SubmitChanges{
        void onChange(String title, String message);
        void onSuccess();
        void onFailed(String result);
    }

    public void OTPRequest(RequestOTP callback){
        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.onRequest("Guanzon Sales Kit", "Requesting OTP");
            }

            @Override
            public Object DoInBackground(Object args) {
                if(!poConn.isDeviceConnected()){
                    message = poConn.getMessage();
                    return false;
                }
                if (!poOTPRqst.SendOTP()){
                    message = poOTPRqst.getMessage();
                    return false;
                }

                return true;
            }

            @Override
            public void OnPostExecute(Object object) {
                Boolean isSuccess = (Boolean) object;
                if (!isSuccess){
                    callback.onFailed(message);
                }

                callback.onSuccess();
            }
        });
    }
    public interface RequestOTP{
        void onRequest(String title, String message);
        void onSuccess();
        void onFailed(String result);
    }

    public void OTPVerification(String OTP, VerifyOTP callback){
        TaskExecutor.Execute(OTP, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.onVerify("Guanzon Sales Kit", "Verifying OTP");
            }

            @Override
            public Object DoInBackground(Object args) {
                if(!poConn.isDeviceConnected()){
                    message = poConn.getMessage();
                    return false;
                }
                if (!poOTPRqst.VerifyOTP((String) args)){
                    message = poOTPRqst.getMessage();
                    return false;
                }
                return true;
            }

            @Override
            public void OnPostExecute(Object object) {
                Boolean isSuccess = (Boolean) object;
                if (!isSuccess){
                    callback.onFailed(message);
                }

                callback.onSuccess();
            }
        });
    }
    public interface VerifyOTP{
        void onVerify(String title, String message);
        void onSuccess();
        void onFailed(String result);
    }
}
