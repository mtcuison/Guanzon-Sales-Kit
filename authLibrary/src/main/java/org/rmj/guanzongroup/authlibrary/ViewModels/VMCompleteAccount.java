package org.rmj.guanzongroup.authlibrary.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.GCircle.Account.ClientMasterSalesKit;
import org.rmj.g3appdriver.GCircle.Account.EmployeeSession;
import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DTownInfo;
import org.rmj.g3appdriver.GCircle.room.Entities.EBarangayInfo;
import org.rmj.g3appdriver.GCircle.room.Entities.EClientInfoSalesKit;
import org.rmj.g3appdriver.lib.Etc.Barangay;
import org.rmj.g3appdriver.lib.Etc.Country;
import org.rmj.g3appdriver.lib.Etc.Town;
import org.rmj.g3appdriver.utils.ConnectionUtil;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

import java.util.List;

public class VMCompleteAccount extends AndroidViewModel {
    private String message;
    private final ConnectionUtil poConn;
    private final ClientMasterSalesKit poClMaster;
    private final Town poTown;
    private final Barangay poBrgy;
    private final Country poCountry;
    private final EmployeeSession poSession;
    public VMCompleteAccount(@NonNull Application application) {
        super(application);

        this.poConn = new ConnectionUtil(application);
        this.poClMaster = new ClientMasterSalesKit(application);
        this.poTown = new Town(application);
        this.poBrgy = new Barangay(application);
        this.poCountry = new Country(application);
        this.poSession = EmployeeSession.getInstance(application);
    }
    public LiveData<EClientInfoSalesKit> GetCompleteProfile(){
        return poClMaster.GetProfileAccount();
    }
    public LiveData<List<DTownInfo.TownProvinceInfo>> GetTownProvinceList() {
        return poTown.getTownProvinceInfo();
    }
    public LiveData<List<EBarangayInfo>> GetBarangayList(String args) {
        return poBrgy.getAllBarangayFromTown(args);
    }
    public String getMessage(){
        return message;
    }
    public void CompleteAccount(EClientInfoSalesKit foClient, SubmitCallback callback){
        foClient.setUserIDxx(poSession.getUserID());
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

                if (!poClMaster.CompleteClientInfo((EClientInfoSalesKit) args)){
                    message = poClMaster.getMessage();
                    return false;
                }

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
