package org.rmj.guanzongroup.authlibrary.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.GCircle.Account.ClientMasterSalesKit;
import org.rmj.g3appdriver.GCircle.Account.EmployeeMaster;
import org.rmj.g3appdriver.GCircle.room.Entities.EClientInfoSalesKit;
import org.rmj.g3appdriver.GCircle.room.Entities.EEmployeeInfo;

public class VMAccountSettings extends AndroidViewModel {
    private final EmployeeMaster poEmpInfo;
    private final ClientMasterSalesKit poSalesKit;
    public VMAccountSettings(@NonNull Application application) {
        super(application);

        this.poEmpInfo = new EmployeeMaster(application);
        this.poSalesKit = new ClientMasterSalesKit(application);
    }
    public LiveData<EEmployeeInfo> GetPoEmpInfo(){
        return poEmpInfo.getUserInfo();
    }

    public LiveData<EClientInfoSalesKit> GetCompleteProfile(){
        return poSalesKit.GetProfileAccount();
    }
}
