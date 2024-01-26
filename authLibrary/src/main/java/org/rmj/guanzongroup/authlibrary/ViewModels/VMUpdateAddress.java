package org.rmj.guanzongroup.authlibrary.ViewModels;

import static org.rmj.g3appdriver.etc.AppConstants.getLocalMessage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DTownInfo;
import org.rmj.g3appdriver.GCircle.room.Entities.EBarangayInfo;
import org.rmj.g3appdriver.lib.Account.AccountMaster;
import org.rmj.g3appdriver.lib.Account.Model.Auth;
import org.rmj.g3appdriver.lib.Account.Model.iAuth;
import org.rmj.g3appdriver.lib.Account.pojo.ChangeUserAddress;
import org.rmj.g3appdriver.lib.Etc.Barangay;
import org.rmj.g3appdriver.lib.Etc.Town;
import org.rmj.g3appdriver.utils.ConnectionUtil;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

import java.util.List;

public class VMUpdateAddress extends AndroidViewModel {
    public static final String TAG =  VMLogin.class.getSimpleName();
    private final iAuth poSys;
    private final ConnectionUtil poConn;
    private String message;
    private final Town poTown;
    private final Barangay poBrgy;
    public VMUpdateAddress(@NonNull Application application) {
        super(application);

        this.poSys = new AccountMaster(application).initGuanzonApp().getInstance(Auth.CHANGE_ADDRESS);
        this.poConn = new ConnectionUtil(application);
        this.poTown = new Town(application);
        this.poBrgy = new Barangay(application);
    }
    public LiveData<List<DTownInfo.TownProvinceInfo>> GetTownProvinceList() {
        return poTown.getTownProvinceInfo();
    }
    public LiveData<List<EBarangayInfo>> GetBarangayList(String args) {
        return poBrgy.getAllBarangayFromTown(args);
    }
    public void ChangeAccountAddress(ChangeUserAddress loAddress, SubmitChanges callback){
        TaskExecutor.Execute(loAddress, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.onChange("Guanzon Sales Kit", "Changing user address");
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

}
