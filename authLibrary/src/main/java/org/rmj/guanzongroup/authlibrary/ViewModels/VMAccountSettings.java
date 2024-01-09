package org.rmj.guanzongroup.authlibrary.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.GCircle.Account.EmployeeMaster;
import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DEmployeeInfo;
import org.rmj.g3appdriver.GCircle.room.Entities.EEmployeeInfo;
import org.rmj.g3appdriver.GCircle.room.GGC_GCircleDB;

public class VMAccountSettings extends AndroidViewModel {
    private final EmployeeMaster poEmpInfo;
    public VMAccountSettings(@NonNull Application application) {
        super(application);

        this.poEmpInfo = new EmployeeMaster(application);
    }
    public LiveData<EEmployeeInfo> GetPoEmpInfo(){
        return poEmpInfo.getUserInfo();
    }
}
