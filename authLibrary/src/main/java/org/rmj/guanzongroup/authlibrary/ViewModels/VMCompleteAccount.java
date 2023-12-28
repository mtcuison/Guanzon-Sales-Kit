package org.rmj.guanzongroup.authlibrary.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.rmj.g3appdriver.GConnect.Account.ClientMaster;
import org.rmj.g3appdriver.GConnect.room.Entities.EClientInfo;
import org.rmj.g3appdriver.utils.ConnectionUtil;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

public class VMCompleteAccount extends AndroidViewModel {
    private String message;
    private final ConnectionUtil poConn;
    private final ClientMaster poClMaster;
    public VMCompleteAccount(@NonNull Application application) {
        super(application);

        this.poConn = new ConnectionUtil(application);
        this.poClMaster = new ClientMaster(application);
    }
    public String getMessage(){
        return message;
    }
    public void CompleteAccount(EClientInfo foClient, SubmitCallback callback){
        TaskExecutor.Execute(foClient, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.onLoad("Guanzon Sales Kit", "Sending request to server . . .");
            }

            @Override
            public Object DoInBackground(Object args) {
                if(!poConn.isDeviceConnected()){
                    message = poConn.getMessage();
                    return false;
                }

                if (!poClMaster.CompleteClientInfo((EClientInfo) args)){
                    message = poClMaster.getMessage();
                    return false;
                }

                poClMaster.ImportAccountInfo(); //after saving details, import to local device
                return true;
            }

            @Override
            public void OnPostExecute(Object object) {
                Boolean isSuccess = (Boolean) object;
                if (isSuccess == true){
                    callback.onSuccess();
                }else {
                    callback.onFailed(getMessage());
                }
            }
        });
    }
    public interface SubmitCallback{
        void onLoad(String title, String message);
        void onSuccess();
        void onFailed(String result);
    }
}
