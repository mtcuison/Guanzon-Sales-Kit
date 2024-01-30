package org.rmj.guanzongroup.ganado.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.GCircle.room.Entities.EGanadoOnline;
import org.rmj.g3appdriver.GConnect.Account.ClientSession;
import org.rmj.g3appdriver.lib.Ganado.Obj.Ganado;
import org.rmj.g3appdriver.utils.ConnectionUtil;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

import java.util.List;

public class VMInquiry extends AndroidViewModel {
    private static final String TAG = VMInquiry.class.getSimpleName();

    private final Ganado poSys;
    private final ConnectionUtil poConn;
    private final ClientSession poSession;

    private String message;

    public interface OnTaskExecute{
        void OnExecute();
        void OnSuccess();
        void OnFailed(String message);
    }

    public VMInquiry(@NonNull Application application) {
        super(application);

        poSys = new Ganado(application);
        poConn = new ConnectionUtil(application);
        this.poSession = ClientSession.getInstance(application);
    }

    public LiveData<List<EGanadoOnline>> GetInquiries(String UserID){
        return poSys.GetByAgentInquiries(UserID);
    }
    public EGanadoOnline GetInquiry(String sTransNox){
        return poSys.GetInquiry(sTransNox);
    }
//    public LiveData<List<EGanadoOnline>> GetInquiries(){
//        return poSys.GetInquiries();
//    }
    public LiveData<List<EGanadoOnline>> GetByAgentInquiries(){
        return poSys.GetByAgentInquiries(poSession.getUserID());
    }

    public void ImportCriteria(OnTaskExecute listener){
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

                if(!poSys.ImportInquiries()){
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
