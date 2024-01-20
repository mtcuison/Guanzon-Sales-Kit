package org.rmj.guanzongroup.agent.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.GCircle.Account.ClientMasterSalesKit;
import org.rmj.g3appdriver.GCircle.room.Entities.EClientInfoSalesKit;
import org.rmj.g3appdriver.SalesKit.Obj.SalesKit;
import org.rmj.g3appdriver.utils.ConnectionUtil;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

public class VMSelectUpLine extends AndroidViewModel {
    private static final String TAG = VMSelectUpLine.class.getSimpleName();

    private final SalesKit poSys;
    private final ConnectionUtil poConn;
    private final ClientMasterSalesKit poSalesKit;

    private String message;

    public interface OnTaskExecute{
        void OnExecute();
        void OnSuccess();
        void OnFailed(String message);
    }

    public VMSelectUpLine(@NonNull Application application) {
        super(application);
        poSys = new SalesKit(application);
        poConn = new ConnectionUtil(application);
        this.poSalesKit = new ClientMasterSalesKit(application);
    }

    public LiveData<EClientInfoSalesKit> GetCompleteProfile(){
        return poSalesKit.GetProfileAccount();
    }

    public void SubmitUpLine(String UserID, VMAgentList.OnTaskExecute listener){
        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                listener.OnExecute();
            }

            @Override
            public Object DoInBackground(Object args) {
                if (!poConn.isDeviceConnected()){
                    message = poConn.getMessage();
                    return false;
                }

                if(!poSys.SubmitUpLine(UserID)){
                    message = poSys.getMessage();
                    return false;
                }

                return true;
            }

            @Override
            public void OnPostExecute(Object object) {
                boolean isSuccess = (boolean) object;
                if(!isSuccess){
                    listener.OnFailed(message);
                    return;
                }

                listener.OnSuccess();
            }
        });
    }
}
