package org.rmj.guanzongroup.ganado.ViewModel;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DTownInfo;
import org.rmj.g3appdriver.GCircle.room.Entities.EGanadoOnline;
import org.rmj.g3appdriver.GCircle.room.Entities.ERelation;
import org.rmj.g3appdriver.lib.Etc.Town;
import org.rmj.g3appdriver.lib.Ganado.Obj.Ganado;
import org.rmj.g3appdriver.lib.Ganado.pojo.ClientInfo;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

import java.util.List;

public class VMPersonalInfo extends AndroidViewModel implements GanadoUI {
    private static final String TAG = VMPersonalInfo.class.getSimpleName();
    private MutableLiveData<Location> currentLocation = new MutableLiveData<>();

    private final Application instance;
    private final Ganado poApp;
    private final ClientInfo poModel;

    private String TransNox;

    private String message;
    private final Town poTown;
    private String lsLat = "", lsLong = "";

    public interface OnSaveInquiry {
        void OnSave();

        void OnSuccess(String args);


        void OnFailed(String message);
    }
    public interface OnGetLocation {
        void onInit();
        void OnSuccess(String args);


        void OnFailed(String message);
    }

    public VMPersonalInfo(@NonNull Application application) {
        super(application);
        this.instance = application;
        this.poApp = new Ganado(application);
        this.poModel = new ClientInfo();
        this.poTown = new Town(application);
    }
    public void InitGeoLocation(Activity activity){

        this.poApp.InitGeoLocation(activity);
    }

    public ClientInfo getModel() {
        return poModel;
    }

    public void initStopLocation(){
        poApp.stopLocationUpdates();
    }

    public LiveData<List<ERelation>> getRelation() {
        return poApp.GetRelations();
    }

    @Override
    public void InitializeApplication(Intent params) {
        TransNox = params.getStringExtra("sTransNox");
        poModel.setsTransNox(TransNox);
    }

    @Override
    public LiveData<EGanadoOnline> GetApplication() {
        return null;
    }

    @Override
    public void ParseData(EGanadoOnline args, OnParseListener listener) {

    }


    @Override
    public void Validate(Object args) {

    }

    @Override
    public void SaveData(OnSaveInfoListener listener) {

    }

    public LiveData<List<DTownInfo.TownProvinceInfo>> GetTownProvinceList() {
        return poTown.getTownProvinceInfo();
    }

    public void SaveData(OnSaveInquiry listener) {
//        new SaveDetailTask(listener).execute(poModel);
        TaskExecutor.Execute(poModel, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                listener.OnSave();
            }

            @Override
            public Object DoInBackground(Object args) {
                ClientInfo lsInfo = (ClientInfo) poModel;

                TransNox = lsInfo.getsTransNox();
                String lsResult = (poApp.SaveClientInfo(lsInfo)) ? "" : null;
                if (lsResult == null) {
                    message = poApp.getMessage();
                    return null;
                }

                String lsResult1 = (poApp.SaveInquiry(lsInfo.getsTransNox())) ? "" : null;
                if (lsResult1 == null) {
                    message = poApp.getMessage();
                    return null;
                }
                return "Motorcycle inquiry saved successfully!";
            }

            @Override
            public void OnPostExecute(Object object) {
                String lsResult = (String) object;
                if (lsResult == null) {
                    listener.OnFailed(message);
                } else {
                    listener.OnSuccess(lsResult);
                }
            }
        });
    }
}
