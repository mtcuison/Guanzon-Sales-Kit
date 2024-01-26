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

package org.rmj.guanzon.guanzonsaleskit.Service;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import org.rmj.g3appdriver.GCircle.Account.ClientMasterSalesKit;
import org.rmj.g3appdriver.GCircle.ImportData.Obj.ImportBarangay;
import org.rmj.g3appdriver.GCircle.ImportData.Obj.ImportBrand;
import org.rmj.g3appdriver.GCircle.ImportData.Obj.ImportBrandModel;
import org.rmj.g3appdriver.GCircle.ImportData.Obj.ImportCategory;
import org.rmj.g3appdriver.GCircle.ImportData.Obj.ImportCountry;
import org.rmj.g3appdriver.GCircle.ImportData.Obj.ImportMcModelPrice;
import org.rmj.g3appdriver.GCircle.ImportData.Obj.ImportMcTermCategory;
import org.rmj.g3appdriver.GCircle.ImportData.Obj.ImportProvinces;
import org.rmj.g3appdriver.GCircle.ImportData.Obj.ImportTown;
import org.rmj.g3appdriver.GCircle.ImportData.Obj.Import_MCCashPrice;
import org.rmj.g3appdriver.GCircle.ImportData.Obj.Import_McColors;
import org.rmj.g3appdriver.GCircle.ImportData.Obj.Import_Occupations;
import org.rmj.g3appdriver.GCircle.ImportData.Obj.Import_Relation;
import org.rmj.g3appdriver.GCircle.ImportData.Obj.Import_SCARequest;
import org.rmj.g3appdriver.GCircle.ImportData.Obj.Import_SysConfig;
import org.rmj.g3appdriver.GCircle.ImportData.model.ImportInstance;
import org.rmj.g3appdriver.GConnect.Account.ClientSession;
import org.rmj.g3appdriver.SalesKit.Obj.SalesKit;
import org.rmj.g3appdriver.lib.Etc.Relation;
import org.rmj.g3appdriver.lib.Ganado.Obj.Ganado;
import org.rmj.g3appdriver.lib.Promotions.GPromos;

@SuppressLint("SpecifyJobSchedulerIdRange")
public class DataDownloadService extends JobService {
    public static final String TAG = DataDownloadService.class.getSimpleName();

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        try{
            doBackgroundTask(jobParameters);
        } catch (Exception e){
            e.printStackTrace();
            jobFinished(jobParameters, false);
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.e(TAG, "Data import service has stop.");
        return true;
    }

    private void doBackgroundTask(JobParameters params) {
        ImportInstance[]  importInstances = {

                new ImportBrand(getApplication()),
                new ImportBrandModel(getApplication()),
                new Import_McColors(getApplication()),
                new Import_MCCashPrice(getApplication()),
                new ImportMcModelPrice(getApplication()),
                new ImportCategory(getApplication()),
                new Import_Relation(getApplication()),
                new ImportProvinces(getApplication()),
                new ImportTown(getApplication()),
                new ImportBarangay(getApplication()),
                new ImportMcTermCategory(getApplication()),
                new ImportCountry(getApplication()),
                new Import_Occupations(getApplication()),
                new Import_SysConfig(getApplication()),
                new Import_SCARequest(getApplication())};
        new Thread(() -> {
            if (new ClientMasterSalesKit(getApplication()).ImportClientProfile(ClientSession.getInstance(getApplication()).getUserID())) {
                Log.d(TAG, "Client Profile imported successfully...");
            }
            try {
                sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(!new Relation(getApplication()).ImportRelations()){
                Log.e(TAG, "Unable to import relationship");
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Log.d(TAG, "Importing Promo Links");
            if (!new GPromos(getApplication()).ImportPromosLinks()){
                Log.d(TAG, "Unable to import promo links");
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Log.d(TAG, "Importing Event Links");
            if (!new GPromos(getApplication()).ImportEventsLinks()){
                Log.d(TAG, "Unable to import events link");
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (new Ganado(getApplication()).ImportInquiries()) {
                Log.d(TAG, "Inquiries imported successfully...");
            }
            try {
                sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (!new SalesKit(getApplication()).ImportKPOPAgent()) {
                Log.d(TAG, "KPOP Agent imported successfully...");
            }
            try {
                sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            for (ImportInstance importInstance : importInstances) {
//                importInstance.ImportData(new ImportDataCallback() {
//                    @Override
//                    public void OnSuccessImportData() {
//                        Log.e(TAG, importInstance.getClass().getSimpleName() + " import success.");
//                    }
//
//                    @Override
//                    public void OnFailedImportData(String message) {
//                        Log.e(TAG, importInstance.getClass().getSimpleName() + " import failed. " + message);
//                    }
//                });
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }

            jobFinished(params, false);
        }).start();
    }
}
