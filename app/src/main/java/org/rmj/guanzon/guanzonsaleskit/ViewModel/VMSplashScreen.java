/*
 * Created by Android Team MIS-SEG Year 2021
 * Copyright (c) 2021. Guanzon Central Office
 * Guanzon Bldg., Perez Blvd., Dagupan City, Pangasinan 2400
 * Project name : GhostRider_Android
 * Module : GhostRider_Android.app
 * Electronic Personnel Access Control Security System
 * project file created : 4/24/21 3:19 PM
 * project file last modified : 4/24/21 3:17 PM
 */

package org.rmj.guanzon.guanzonsaleskit.ViewModel;


import static org.rmj.g3appdriver.etc.AppConstants.getLocalMessage;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.squareup.picasso.BuildConfig;

import org.rmj.g3appdriver.GCircle.Account.ClientMasterSalesKit;
import org.rmj.g3appdriver.GCircle.room.Entities.ETokenInfo;
import org.rmj.g3appdriver.GCircle.room.Repositories.AppTokenManager;
import org.rmj.g3appdriver.GCircle.room.Repositories.RMcBrand;
import org.rmj.g3appdriver.GCircle.room.Repositories.RMcCategory;
import org.rmj.g3appdriver.GCircle.room.Repositories.RMcModel;
import org.rmj.g3appdriver.GCircle.room.Repositories.RMcModelPrice;
import org.rmj.g3appdriver.GCircle.room.Repositories.RMcTermCategory;
import org.rmj.g3appdriver.GConnect.Account.ClientMaster;
import org.rmj.g3appdriver.GConnect.Account.ClientSession;
import org.rmj.g3appdriver.SalesKit.Obj.SalesKit;
import org.rmj.g3appdriver.etc.AppConfigPreference;
import org.rmj.g3appdriver.lib.Etc.Barangay;
import org.rmj.g3appdriver.lib.Etc.Branch;
import org.rmj.g3appdriver.lib.Etc.Country;
import org.rmj.g3appdriver.lib.Etc.Province;
import org.rmj.g3appdriver.lib.Etc.Relation;
import org.rmj.g3appdriver.lib.Etc.Town;
import org.rmj.g3appdriver.lib.Ganado.Obj.Ganado;
import org.rmj.g3appdriver.lib.Promotions.GPromos;
import org.rmj.g3appdriver.utils.ConnectionUtil;
import org.rmj.g3appdriver.utils.Task.OnDoBackgroundTaskListener;
import org.rmj.g3appdriver.utils.Task.OnLoadApplicationListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;


public class VMSplashScreen extends AndroidViewModel {
    private static final String TAG = VMSplashScreen.class.getSimpleName();

    private final Application instance;

    private final ConnectionUtil poConn;
    private final AppConfigPreference poConfig;
    private final ClientMaster poUser;
    private final ClientSession poSession;
    private final GPromos poPromoEvents;

    private String message;

    public VMSplashScreen(@NonNull Application application) {
        super(application); 
        this.instance = application;
        this.poConn = new ConnectionUtil(instance);
        this.poConfig = AppConfigPreference.getInstance(instance);
        this.poUser = new ClientMaster(instance);
        this.poPromoEvents = new GPromos(instance);
        this.poSession = ClientSession.getInstance(instance);
        this.poConfig.setPackageName(BuildConfig.APPLICATION_ID);
        this.poConfig.setProductID("GuanzonApp");
        this.poConfig.setUpdateLocally(true);
        this.poConfig.setTestCase(true);
        this.poConfig.setupAppVersionInfo(BuildConfig.VERSION_CODE, BuildConfig.VERSION_NAME, "");
        ETokenInfo loToken = new ETokenInfo();
        loToken.setTokenInf("temp_token");
        CheckConnection();
        Log.e("userid",poSession.getUserID());
    }

    public void SaveFirebaseToken(String fsVal){
        TaskExecutor.Execute(fsVal, new OnDoBackgroundTaskListener() {
            @Override
            public Object DoInBackground(Object args) {
                return new AppTokenManager(instance).SaveFirebaseToken((String) args);
            }

            @Override
            public double OnPostExecute(Object object) {
                return 0;
            }
        });
    }

    private void CheckConnection(){
        TaskExecutor.Execute(null, new OnDoBackgroundTaskListener() {
            @Override
            public Object DoInBackground(Object args) {
                if(!poConn.isDeviceConnected()){
                    message = poConn.getMessage();
                    return false;
                }

                return true;
            }

            @Override
            public double OnPostExecute(Object object) {
                boolean isSuccess = (boolean) object;
                if(isSuccess){
                    Toast.makeText(instance, "Device connected", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(instance, "Offline mode", Toast.LENGTH_LONG).show();
                }
                return 0;
            }
        });
    }

    public void InitializeData(OnInitializeCallback mListener){
        TaskExecutor loTask = new TaskExecutor();
        loTask.setOnLoadApplicationListener(new OnLoadApplicationListener() {
            @Override
            public Object DoInBackground() {
                try{
                    if(poConn.isDeviceConnected()){
                        if (poSession.getLoginStatus()){

                            if(!new Relation(instance).ImportRelations()){
                                Log.e(TAG, "Unable to import relationship");
                            }

                            loTask.publishProgress(1);

                            Thread.sleep(500);
                            if (new ClientMasterSalesKit(instance).ImportClientProfile(poSession.getUserID())) {
                                Log.d(TAG, "Client Profile imported successfully...");
                            }
                            loTask.publishProgress(10);
                            Thread.sleep(500);

                            if (new Ganado(instance).ImportInquiries()) {
                                Log.d(TAG, "Inquiries imported successfully...");
                            }
                            loTask.publishProgress(20);
                            Thread.sleep(500);

                            if (!new SalesKit(instance).ImportKPOPAgent()) {
                                Log.d(TAG, "KPOP Agent imported successfully...");
                            }
                            loTask.publishProgress(30);
                            Thread.sleep(500);
                        }
                        if (poConfig.isAppFirstLaunch()){

                            if(!new Branch(instance).ImportBranches()){
                                Log.e(TAG, "Unable to import branches");
                            }

                            loTask.publishProgress(1);

                            Thread.sleep(1000);
                            Log.d(TAG, "Initializing province data.");
                            if(!new Province(instance).ImportProvince()){
                                Log.e(TAG, "Unable to import province");
                            }
                            loTask.publishProgress(5);

                            Thread.sleep(1000);
                            Log.d(TAG, "Initializing town data.");
                            if(!new Town(instance).ImportTown()){
                                Log.e(TAG, "Unable to import town");
                            }
                            loTask.publishProgress(15);

                            Thread.sleep(1000);
                            Log.d(TAG, "Initializing barangay data.");
                            if(!new Barangay(instance).ImportBarangay()){
                                Log.e(TAG, "Unable to import barangay");
                            }
                            loTask.publishProgress(25);
                            Thread.sleep(1000);

                            Log.d(TAG, "Initializing country data.");
                            if(!new Country(instance).ImportCountry()){
                                Log.e(TAG, "Unable to import country");
                            }
                            loTask.publishProgress(30);
                            Thread.sleep(1000);

                        }

                        Log.d(TAG, "Importing Promo Links");
                        if (!poPromoEvents.ImportPromosLinks()){
                            Log.d(TAG, "Unable to import promo links");
                        }
                        loTask.publishProgress(35);
                        Thread.sleep(1000);

                        Log.d(TAG, "Importing Event Links");
                        if (!poPromoEvents.ImportEventsLinks()){
                            Log.d(TAG, "Unable to import events link");
                        }
                        loTask.publishProgress(39);
                        Thread.sleep(1000);

                        if (new RMcModel(instance).ImportMCModel()) {
                            Log.d(TAG, "MC Model imported successfully...");
                        }
                        loTask.publishProgress(45);
                        Thread.sleep(500);
                        if (new RMcModel(instance).ImportCashPrices()) {
                            Log.d(TAG, "MC Model Cash Prices imported successfully...");
                        }
                        loTask.publishProgress(55);
                        Thread.sleep(500);
                        if (new RMcModel(instance).ImportModelColor()) {
                            Log.d(TAG, "MC Model Color imported successfully...");
                        }
                        loTask.publishProgress(65);
                        Thread.sleep(500);

                        if (new RMcBrand(instance).ImportMCBrands()) {
                            Log.d(TAG, "MC Brand imported successfully...");
                        }
                        loTask.publishProgress(75);
                        Thread.sleep(500);
                        if (new RMcModelPrice(instance).ImportMcModelPrice()) {
                            Log.d(TAG, "MC Model Cash Prices imported successfully...");
                        }

                        loTask.publishProgress(85);
                        Thread.sleep(500);


                        if (new RMcCategory(instance).ImportMcCategory()) {
                            Log.d(TAG, "MC Category imported successfully...");
                        }

                        loTask.publishProgress(92);
                        Thread.sleep(500);

                        if (new RMcTermCategory(instance).ImportMcTermCategory()) {
                            Log.d(TAG, "MC Term Category imported successfully...");
                        }
                        loTask.publishProgress(98);
                        Thread.sleep(500);


                        if(!poSession.getLoginStatus()){
                            return 2;
                        }

                        return 1;
                    } else {
                        if(!poConfig.isAppFirstLaunch()){
                            message = "Offline Mode.";
                            return 1;
                        }

                        message = "Please connect you device to internet to download data.";
                        return 0;
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    message = getLocalMessage(e);
                    return 0;
                }
            }

            @Override
            public void OnProgress(int progress) {
                String lsArgs;
                if(poConfig.isAppFirstLaunch()){
                    lsArgs = "Importing Data...";
                } else {
                    lsArgs = "Updating Data...";
                }
                if(progress < 5) {
                    mListener.OnProgress(lsArgs, progress);
                } else {
                    mListener.OnHasDCP();
                }
            }

            @Override
            public void OnPostExecute(Object object) {
                int result = (int) object;
                switch (result){
                    case 0:
                        mListener.OnFailed(message);
                        break;
                    case 1:
                        mListener.OnSuccess();
                        break;
                    default:
                        mListener.OnNoSession();
                        break;
                }
            }
        });
        loTask.Execute();
    }

    public void InitUserData(OnInitializeCallback mListener){
        TaskExecutor loTask = new TaskExecutor();
        loTask.setOnLoadApplicationListener(new OnLoadApplicationListener() {
            @Override
            public Object DoInBackground() {
                try{
                    if(poConn.isDeviceConnected()){
                        if (poSession.getLoginStatus()){

                            if(!new Relation(instance).ImportRelations()){
                                Log.e(TAG, "Unable to import relationship");
                            }

                            loTask.publishProgress(5);
                            Thread.sleep(1000);
                            Log.d(TAG, "Importing Promo Links");
                            if (!poPromoEvents.ImportPromosLinks()){
                                Log.d(TAG, "Unable to import promo links");
                            }
                            loTask.publishProgress(10);
                            Thread.sleep(1000);

                            Log.d(TAG, "Importing Event Links");
                            if (!poPromoEvents.ImportEventsLinks()){
                                Log.d(TAG, "Unable to import events link");
                            }
                            loTask.publishProgress(15);
                            Thread.sleep(1000);

                            Thread.sleep(500);
                            if (new ClientMasterSalesKit(instance).ImportClientProfile(poSession.getUserID())) {
                                Log.d(TAG, "Client Profile imported successfully...");
                            }
                            loTask.publishProgress(88);
                            Thread.sleep(500);

                            if (new Ganado(instance).ImportInquiries()) {
                                Log.d(TAG, "Inquiries imported successfully...");
                            }
                            loTask.publishProgress(90);
                            Thread.sleep(500);

                            if (!new SalesKit(instance).ImportKPOPAgent()) {
                                Log.d(TAG, "KPOP Agent imported successfully...");
                            }
                            loTask.publishProgress(99);
                            Thread.sleep(500);
                        }

                        if(!poSession.getLoginStatus()){
                            return 2;
                        }

                        return 1;
                    } else {
                        if(!poConfig.isAppFirstLaunch()){
                            message = "Offline Mode.";
                            return 1;
                        }

                        message = "Please connect you device to internet to download data.";
                        return 0;
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    message = getLocalMessage(e);
                    return 0;
                }
            }

            @Override
            public void OnProgress(int progress) {
                String lsArgs;
                if(poConfig.isAppFirstLaunch()){
                    lsArgs = "Importing Data...";
                } else {
                    lsArgs = "Updating Data...";
                }
                if(progress < 5) {
                    mListener.OnProgress(lsArgs, progress);
                } else {
                    mListener.OnHasDCP();
                }
            }

            @Override
            public void OnPostExecute(Object object) {
                int result = (int) object;
                switch (result){
                    case 0:
                        mListener.OnFailed(message);
                        break;
                    case 1:
                        mListener.OnSuccess();
                        break;
                    default:
                        mListener.OnNoSession();
                        break;
                }
            }
        });
        loTask.Execute();
    }

    public interface OnInitializeCallback {
        void OnProgress(String args, int progress);
        void OnHasDCP();
        void OnSuccess();
        void OnNoSession();
        void OnFailed(String message);
    }
}
