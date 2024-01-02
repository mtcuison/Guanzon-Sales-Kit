package org.rmj.g3appdriver.lib.Promotions;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rmj.g3appdriver.GCircle.room.GGC_GCircleDB;
import org.rmj.g3appdriver.GConnect.Api.GConnectApi;
import org.rmj.g3appdriver.GConnect.room.DataAccessObject.DEvents;
import org.rmj.g3appdriver.GConnect.room.DataAccessObject.DPromo;
import org.rmj.g3appdriver.GConnect.room.Entities.EEvents;
import org.rmj.g3appdriver.GConnect.room.Entities.EPromo;
import org.rmj.g3appdriver.dev.Api.HttpHeaders;
import org.rmj.g3appdriver.dev.Api.WebClient;
import org.rmj.g3appdriver.etc.AppConfigPreference;
import org.rmj.g3appdriver.etc.AppConstants;

import java.util.List;


public class GPromos {
    private static final String TAG = GPromos.class.getSimpleName();

    private final Application instance;

    private final DPromo poPromo;
    private final DEvents poEvents;

    private final HttpHeaders poHeaders;
    private final AppConfigPreference poConfig;
    private final GConnectApi poAPI;

    private String message;

    public GPromos(Application context) {
        this.instance = context;
        this.poPromo = GGC_GCircleDB.getInstance(instance).PromoDao();
        this.poEvents = GGC_GCircleDB.getInstance(instance).EventsDao();
        this.poHeaders = HttpHeaders.getInstance(context);
        this.poConfig = AppConfigPreference.getInstance(context);
        this.poAPI = new GConnectApi(context);
    }

    public boolean ImportPromosLinks(){
        try{
            JSONObject params = new JSONObject();
            String lsResponse = WebClient.sendRequest(poAPI.getImportPromosAPI(), params.toString(), poHeaders.getHeaders());
            if(lsResponse == null){
                message = "Server no response.";
                Log.d(TAG, "Unable to retrieve data from server. Server no response.");
                return false;
            }

            JSONObject loResponse = new JSONObject(lsResponse);
            String lsResult = loResponse.getString("result");
            if(lsResult.equalsIgnoreCase("error")){
                JSONObject loError = loResponse.getJSONObject("error");
                message = loError.getString("message");
                Log.d(TAG, "Unable to retrieve records from server. Message : " + message);
                return false;
            }

            JSONArray laDetail = loResponse.getJSONArray("detail");
            for(int x = 0; x < laDetail.length(); x++){
                JSONObject loJson = laDetail.getJSONObject(x);
                EPromo loPromo = poPromo.getPromoInfoIfExist(loJson.getString("sTransNox"));
                if(loPromo == null) {
                    //check the records from API, if record status is not equal to 1, record is inactive, do not insert
//                if(!"1".equalsIgnoreCase(loJson.getString("cRecdStat"))){
                    // insert saving method inside...
                    EPromo info = new EPromo();
                    info.setTransNox(loJson.getString("sTransNox"));
                    info.setDivision(loJson.getInt("cDivision"));
                    info.setTransact(loJson.getString("dTransact"));
                    info.setImageUrl(loJson.getString("sImageURL"));
                    info.setImageSld(loJson.getString("sImageNme"));
                    info.setPromoUrl(loJson.getString("sPromoURL"));
                    info.setCaptionx(loJson.getString("sCaptionx"));
                    info.setDateFrom(loJson.getString("dDateFrom"));
                    info.setDateThru(loJson.getString("dDateThru"));
//                    info.setRecdStat(loJson.getString("cRecdStat"));
//                    info.setTimeStmp(loJson.getString("dTimeStmp"));
                    poPromo.insert(info);
                    Log.d(TAG, "New record save!");
//                }
//            } else {
//                Date ldDate1 = SQLUtil.toDate(loPromo.getTimeStmp(), SQLUtil.FORMAT_TIMESTAMP);
//                Date ldDate2 = SQLUtil.toDate((String) loJson.get("dTimeStmp"), SQLUtil.FORMAT_TIMESTAMP);
//
//                if(!ldDate1.equals(ldDate2)){
//                    poPromo.UpdatePromoInfo(loJson.getString("dTransact"),
//                            loJson.getString("dDateFrom"),
//                            loJson.getString("dDateThru"),
//                            loJson.getString("sCaptionx"),
//                            loJson.getString("sImageURL"),
//                            loJson.getString("cRecdStat"),
//                            loJson.getString("sImageNme"),
//                            loJson.getString("dTimeStmp"),
//                            loJson.getString("sPromoUrl"),
//                            loJson.getString("cDivision"),
//                            loJson.getString("sTransNox"));
//                    Log.d(TAG, "A record has been updated!");
//                }
                }
            }

            return true;
        } catch (Exception e){
            e.printStackTrace();
            message = e.getMessage();
            return false;
        }
    }

    public LiveData<List<EPromo>> GetPromotions() {
        return poPromo.getAllPromo();
    }

    public EPromo CheckPromo() {
        return poPromo.CheckPromo();
    }


    public boolean ImportEventsLinks(){
        try{
            JSONObject params = new JSONObject();
            String lsResponse = WebClient.sendRequest(poAPI.getImportEventsAPI(), params.toString(), poHeaders.getHeaders());
            if(lsResponse == null){
                message = "Server no response.";
                Log.d(TAG, "Unable to retrieve data from server. Server no response.");
                return false;
            }

            JSONObject loResponse = new JSONObject(lsResponse);
            String lsResult = loResponse.getString("result");
            if(lsResult.equalsIgnoreCase("error")){
                JSONObject loError = loResponse.getJSONObject("error");
                message = loError.getString("message");
                Log.d(TAG, "Unable to retrieve records from server. Message : " + message);
                return false;
            }

            JSONArray laDetail = loResponse.getJSONArray("detail");
            for(int x = 0; x < laDetail.length(); x++){
                JSONObject loJson = laDetail.getJSONObject(x);
                EEvents loPromo = poEvents.getEventInfoExist(loJson.getString("sTransNox"));
                if(loPromo == null) {
                    //check the records from API, if record status is not equal to 1, record is inactive, do not insert
//                if(!"1".equalsIgnoreCase(loJson.getString("cRecdStat"))){
                    // insert saving method inside...

                    EEvents info = new EEvents();
                    info.setTransNox(loJson.getString("sTransNox"));
                    info.setBranchNm(loJson.getString("sBranchNm"));
                    info.setEvntFrom(loJson.getString("dEvntFrom"));
                    info.setEvntThru(loJson.getString("dEvntThru"));
                    info.setEventTle(loJson.getString("sEventTle"));
                    info.setAddressx(loJson.getString("sAddressx"));
                    info.setEventURL(loJson.getString("sEventURL"));
                    info.setImageURL(loJson.getString("sImageURL"));
                    info.setNotified("0");
                    info.setModified(new AppConstants().DATE_MODIFIED);
                    info.setDirectoryFolder("Events");
                    poEvents.insert(info);
                    Log.d(TAG, "New record save!");
//                }
//            } else {
//                Date ldDate1 = SQLUtil.toDate(loPromo.getTimeStmp(), SQLUtil.FORMAT_TIMESTAMP);
//                Date ldDate2 = SQLUtil.toDate((String) loJson.get("dTimeStmp"), SQLUtil.FORMAT_TIMESTAMP);
//
//                if(!ldDate1.equals(ldDate2)){
//                    poPromo.UpdatePromoInfo(loJson.getString("dTransact"),
//                            loJson.getString("dDateFrom"),
//                            loJson.getString("dDateThru"),
//                            loJson.getString("sCaptionx"),
//                            loJson.getString("sImageURL"),
//                            loJson.getString("cRecdStat"),
//                            loJson.getString("sImageNme"),
//                            loJson.getString("dTimeStmp"),
//                            loJson.getString("sPromoUrl"),
//                            loJson.getString("cDivision"),
//                            loJson.getString("sTransNox"));
//                    Log.d(TAG, "A record has been updated!");
//                }
                }
            }

            return true;
        } catch (Exception e){
            e.printStackTrace();
            message = e.getMessage();
            return false;
        }
    }

    public LiveData<List<EEvents>> GetEvents() {
        return poEvents.getAllEvents();
    }

    public EEvents CheckEvent() {
        return poEvents.CheckEvent();
    }
}
