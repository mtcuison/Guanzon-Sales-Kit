/*
 * Created by Android Team MIS-SEG Year 2021
 * Copyright (c) 2021. Guanzon Central Office
 * Guanzon Bldg., Perez Blvd., Dagupan City, Pangasinan 2400
 * Project name : GhostRider_Android
 * Module : GhostRider_Android.g3appdriver
 * Electronic Personnel Access Control Security System
 * project file created : 4/24/21 3:19 PM
 * project file last modified : 4/24/21 3:18 PM
 */

package org.rmj.g3appdriver.GCircle.room.Repositories;

import static org.rmj.g3appdriver.dev.Api.ApiResult.SERVER_NO_RESPONSE;
import static org.rmj.g3appdriver.dev.Api.ApiResult.getErrorMessage;
import static org.rmj.g3appdriver.etc.AppConstants.getLocalMessage;

import android.app.Application;

import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rmj.g3appdriver.GCircle.Api.GCircleApi;
import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DClientInfo;
import org.rmj.g3appdriver.GCircle.room.Entities.EClientInfo;
import org.rmj.g3appdriver.GCircle.room.GGC_GCircleDB;
import org.rmj.g3appdriver.dev.Api.HttpHeaders;
import org.rmj.g3appdriver.dev.Api.WebClient;

public class RClient {
    private DClientInfo clientDao;
    private LiveData<EClientInfo> clientInfo;

    private final GCircleApi poApi;
    private final HttpHeaders poHeaders;

    private String message;

    public RClient(Application application){
        GGC_GCircleDB database = GGC_GCircleDB.getInstance(application);
        clientDao = database.ClientDao();
        clientInfo = clientDao.getClientInfo();
        this.poApi = new GCircleApi(application);
        this.poHeaders = HttpHeaders.getInstance(application);
    }


    public void insertClient(EClientInfo clientInfo){
        //TODO: Create asyncktask class that will insert data to local database on background thread.
    }


    public void updateClient(EClientInfo clientInfo){
        //TODO: Create asyncktask class that will update data to local database on background thread.
    }


    public void deleteClient(EClientInfo clientInfo){
        //TODO: Create asyncktask class that will delete data to local database on background thread.
    }

    public LiveData<EClientInfo> getClientInfo(){
        return clientInfo;
    }

    public String getUserID(){
        return clientDao.getUserID();
    }

    public boolean ImportClientInfo(){
        try{
            JSONObject params = new JSONObject();
            params.put("bsearch", true);
            params.put("descript", "All");

            EClientInfo loObj = clientDao.getClientInfo().getValue();

            String lsResponse = WebClient.sendRequest(
                    poApi.getUrlImportTermCategory(),
                    params.toString(),
                    poHeaders.getHeaders());

            if(lsResponse == null){
                message = SERVER_NO_RESPONSE;
                return false;
            }

            JSONObject loResponse = new JSONObject(lsResponse);

            String lsResult = loResponse.getString("result");
            if(lsResult.equalsIgnoreCase("error")){
                JSONObject loError = loResponse.getJSONObject("error");
                message = getErrorMessage(loError);
                return false;
            }

            JSONArray laJson = loResponse.getJSONArray("detail");
            for(int x = 0; x < laJson.length(); x++){
                JSONObject loJson = laJson.getJSONObject(x);
            }

            return true;
        } catch (Exception e){
            e.printStackTrace();
            message = getLocalMessage(e);
            return false;
        }
    }
}
