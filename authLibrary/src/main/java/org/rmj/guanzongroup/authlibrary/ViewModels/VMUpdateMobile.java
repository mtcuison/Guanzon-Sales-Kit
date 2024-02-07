package org.rmj.guanzongroup.authlibrary.ViewModels;

import static org.rmj.g3appdriver.etc.AppConstants.getLocalMessage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.rmj.g3appdriver.lib.Account.AccountMaster;
import org.rmj.g3appdriver.lib.Account.Model.Auth;
import org.rmj.g3appdriver.lib.Account.Model.iAuth;
import org.rmj.g3appdriver.utils.ConnectionUtil;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

public class VMUpdateMobile extends AndroidViewModel {
    public static final String TAG =  VMLogin.class.getSimpleName();
    private final iAuth poSys;
    private final ConnectionUtil poConn;
    private String message;
    public VMUpdateMobile(@NonNull Application application) {
        super(application);

        this.poSys = new AccountMaster(application).initGuanzonApp().getInstance(Auth.CHANGE_MOBILE);
        this.poConn = new ConnectionUtil(application);
    }

    public void ChangeAccountMobile(String sMobile, SubmitChanges callback){
        TaskExecutor.Execute(sMobile, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.onChange("Guanzon Sales Kit", "Updating Mobile No");
            }

            @Override
            public Object DoInBackground(Object args) {
                try{
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

}
