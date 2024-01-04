package org.rmj.guanzongroup.agent.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.SalesKit.Entities.EAgentRole;
import org.rmj.g3appdriver.SalesKit.Entities.EKPOPAgentRole;
import org.rmj.g3appdriver.SalesKit.Obj.SalesKit;
import org.rmj.g3appdriver.utils.ConnectionUtil;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

import java.util.List;

public class VMAgentList extends AndroidViewModel {
    private static final String TAG = VMAgentList.class.getSimpleName();

    private final SalesKit poSys;
    private final ConnectionUtil poConn;

    private String message;

    public interface OnTaskExecute{
        void OnExecute();
        void OnSuccess();
        void OnFailed(String message);
    }

    public VMAgentList(@NonNull Application application) {
        super(application);

        poSys = new SalesKit(application);
        poConn = new ConnectionUtil(application);
    }

    public LiveData<List<EKPOPAgentRole>> GetKPOPAgentRole(){
        return poSys.GetKPOPAgentRole();
    }
    public LiveData<List<EAgentRole>> GetAgentRole(){
        return poSys.getAgentRole();
    }

    public void ImportAgent(OnTaskExecute listener){
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

                if(!poSys.ImportAgent()){
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
    public void ImportKPOPAgent(OnTaskExecute listener){
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

                if(!poSys.ImportKPOPAgent()){
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
