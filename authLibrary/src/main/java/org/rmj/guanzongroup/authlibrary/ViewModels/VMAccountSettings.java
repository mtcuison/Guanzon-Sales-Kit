package org.rmj.guanzongroup.authlibrary.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.GCircle.Account.ClientMasterSalesKit;
import org.rmj.g3appdriver.GCircle.room.Entities.EClientInfo;
import org.rmj.g3appdriver.GCircle.room.Entities.EClientInfoSalesKit;
import org.rmj.g3appdriver.GConnect.Account.ClientMaster;

public class VMAccountSettings extends AndroidViewModel {
    private final ClientMaster poClient;
    private final ClientMasterSalesKit poSalesKit;
    public VMAccountSettings(@NonNull Application application) {
        super(application);

        this.poClient = new ClientMaster(application);
        this.poSalesKit = new ClientMasterSalesKit(application);
    }
    public LiveData<EClientInfo> GetPoEmpInfo(){
        return poClient.GetClientInfo();
    }

    public LiveData<EClientInfoSalesKit> GetCompleteProfile(){
        return poSalesKit.GetProfileAccount();
    }
}
