package org.rmj.g3appdriver.SalesKit.Obj;

import static org.rmj.g3appdriver.dev.Api.ApiResult.SERVER_NO_RESPONSE;
import static org.rmj.g3appdriver.dev.Api.ApiResult.getErrorMessage;
import static org.rmj.g3appdriver.etc.AppConstants.getLocalMessage;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rmj.apprdiver.util.SQLUtil;
import org.rmj.g3appdriver.GCircle.Account.EmployeeSession;
import org.rmj.g3appdriver.GCircle.Api.GCircleApi;
import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DGanadoOnline;
import org.rmj.g3appdriver.GCircle.room.Entities.ECountryInfo;
import org.rmj.g3appdriver.GCircle.room.Entities.ERelation;
import org.rmj.g3appdriver.GCircle.room.GGC_GCircleDB;
import org.rmj.g3appdriver.SalesKit.DataAccessObject.DAgentRole;
import org.rmj.g3appdriver.SalesKit.DataAccessObject.DKPOPAgentRole;
import org.rmj.g3appdriver.SalesKit.Entities.EAgentRole;
import org.rmj.g3appdriver.SalesKit.Entities.EKPOPAgentRole;
import org.rmj.g3appdriver.dev.Api.HttpHeaders;
import org.rmj.g3appdriver.dev.Api.WebClient;
import org.rmj.g3appdriver.lib.Etc.Country;
import org.rmj.g3appdriver.lib.Etc.Relation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SalesKit {
    private static final String TAG = SalesKit.class.getSimpleName();

    private final Application instance;
    private final DKPOPAgentRole poDao;
    private final DGanadoOnline poDaoGanado;
    private final DAgentRole poAgentDao;
    private final EmployeeSession poSession;
    private final GCircleApi poApi;
    private final HttpHeaders poHeaders;
    private final Relation poRelate;
    private final Country poCountry;

    private String message;

    public SalesKit(Application instance) {
        this.instance = instance;
        this.poDao = GGC_GCircleDB.getInstance(instance).kpopAgentDao();
        this.poAgentDao = GGC_GCircleDB.getInstance(instance).AgentDao();
        this.poDaoGanado = GGC_GCircleDB.getInstance(instance).ganadoDao();
        this.poSession = EmployeeSession.getInstance(instance);
        this.poApi = new GCircleApi(instance);
        this.poHeaders = HttpHeaders.getInstance(instance);
        this.poRelate = new Relation(instance);
        this.poCountry = new Country(instance);
    }

    public String getMessage() {
        return message;
    }

    public LiveData<List<ERelation>> GetRelations(){
        return poRelate.GetRelations();
    }
    public LiveData<List<ECountryInfo>> GetCountry(){
        return poCountry.getAllCountryInfo();
    }
    public LiveData<List<EKPOPAgentRole>> GetKPOPAgent(String UserID){
        return poDao.GetKPOPAgent(UserID);
    }
    public LiveData<DGanadoOnline.CountEntries> GetEntries(){
        return poDaoGanado.GetEntryCounts(poSession.getUserID());
    }
    public boolean ImportKPOPAgent(){
        try{
            JSONObject params = new JSONObject();
            EKPOPAgentRole loGanado = poDao.GetLatestData();

            if(loGanado != null){
                params.put("timestamp", loGanado.getTimeStmp());
            }

            String lsResponse = WebClient.sendRequest(
                    poApi.getDownloadInquiries(),
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

                String lsTransNo = loJson.getString("sUserIDxx");

                EKPOPAgentRole loDetail = poDao.GetKPOPAget(lsTransNo);

                if(loDetail == null){
                    EKPOPAgentRole loInfo = new EKPOPAgentRole();
                    loInfo.setUserIDxx(loJson.getString("sUserIDxx"));
                    loInfo.setEnrollBy(loJson.getString("sEnrollBy"));
                    loInfo.setEnrolled(loJson.getString("dEnrolled"));
                    loInfo.setRoleIDxx(loJson.getString("nRoleIDxx"));
                    loInfo.setUpprNdID(loJson.getString("sUpprNdID"));
                    loInfo.setRecdStat(loJson.getString("cTranStat"));
                    loInfo.setTimeStmp(loJson.getString("dTimeStmp"));
                    poDao.Save(loInfo);
                    Log.d(TAG, "KPOP Agent record has been saved!");
                } else {

                    Date ldDate1 = SQLUtil.toDate(loDetail.getTimeStmp(), SQLUtil.FORMAT_TIMESTAMP);
                    Date ldDate2 = SQLUtil.toDate((String) loJson.get("dTimeStmp"), SQLUtil.FORMAT_TIMESTAMP);
                    if (!ldDate1.equals(ldDate2)) {
                        loDetail.setUserIDxx(loJson.getString("sUserIDxx"));
                        loDetail.setEnrollBy(loJson.getString("sEnrollBy"));
                        loDetail.setEnrolled(loJson.getString("dEnrolled"));
                        loDetail.setRoleIDxx(loJson.getString("nRoleIDxx"));
                        loDetail.setUpprNdID(loJson.getString("sUpprNdID"));
                        loDetail.setRecdStat(loJson.getString("cTranStat"));
                        loDetail.setTimeStmp(loJson.getString("dTimeStmp"));
                        poDao.Update(loDetail);
                        Log.d(TAG, "KPOP Agent record has been updated!");
                    }
                }
            }

            return true;
        } catch (Exception e){
            e.printStackTrace();
            message = getLocalMessage(e);
            return false;
        }
    }
    public boolean ImportAgent(){
        try{
            JSONObject params = new JSONObject();
            EAgentRole loGanado = poAgentDao.GetLatestData();

            if(loGanado != null){
                params.put("timestamp", loGanado.getTimeStmp());
            }

            String lsResponse = WebClient.sendRequest(
                    poApi.getDownloadInquiries(),
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

                String lsTransNo = loJson.getString("sUserIDxx");

                EAgentRole loDetail = poAgentDao.GetAgentRole(lsTransNo);

                if(loDetail == null){
                    EAgentRole loInfo = new EAgentRole();
                    loInfo.setRoleIDxx(loJson.getString("sUserIDxx"));
                    loInfo.setRoleDesc(loJson.getString("sRoleDesc"));
                    loInfo.setRecdStat(loJson.getString("cRecdStat"));
                    loInfo.setModifieds(loJson.getString("sModified"));
                    loInfo.setModifiedd(loJson.getString("dModified"));
                    loInfo.setTimeStmp(loJson.getString("dTimeStmp"));
                    poAgentDao.Save(loInfo);
                    Log.d(TAG, "Agent role record has been saved!");
                } else {

                    Date ldDate1 = SQLUtil.toDate(loDetail.getTimeStmp(), SQLUtil.FORMAT_TIMESTAMP);
                    Date ldDate2 = SQLUtil.toDate((String) loJson.get("dTimeStmp"), SQLUtil.FORMAT_TIMESTAMP);
                    if (!ldDate1.equals(ldDate2)) {
                        loDetail.setRoleIDxx(loJson.getString("sUserIDxx"));
                        loDetail.setRoleDesc(loJson.getString("sRoleDesc"));
                        loDetail.setRecdStat(loJson.getString("cRecdStat"));
                        loDetail.setModifieds(loJson.getString("sModified"));
                        loDetail.setModifiedd(loJson.getString("dModified"));
                        loDetail.setTimeStmp(loJson.getString("dTimeStmp"));
                        poAgentDao.Update(loDetail);
                        Log.d(TAG, "Agent role record has been updated!");
                    }
                }
            }

            return true;
        } catch (Exception e){
            e.printStackTrace();
            message = getLocalMessage(e);
            return false;
        }
    }
    public LiveData<List<EKPOPAgentRole>> GetKPOPAgentRole(){
        return poDao.getKPopAgentRole();
    }
    public LiveData<List<EAgentRole>> getAgentRole(){ return poAgentDao.getAgentRole(); }

    public boolean SubmitUpLine(String UserIDxx){
        try{

            JSONObject params = new JSONObject();
            params.put("sUserIDxx", UserIDxx);
            String lsResponse = WebClient.sendRequest(
                    poApi.getUrlSubmitSKUpline(),
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

            JSONObject loJson = loResponse.getJSONObject("detail");



            EKPOPAgentRole loInfo = new EKPOPAgentRole();
            loInfo.setUserIDxx(loJson.getString("sUserIDxx"));
            loInfo.setEnrollBy(loJson.getString("sEnrollBy"));
            loInfo.setEnrolled(loJson.getString("dEnrolled"));
            loInfo.setRoleIDxx(loJson.getString("nRoleIDxx"));
            loInfo.setUpprNdID(loJson.getString("sUpprNdID"));
            loInfo.setRecdStat(loJson.getString("cTranStat"));
            loInfo.setTimeStmp(loJson.getString("dTimeStmp"));
            poDao.Save(loInfo);
            Log.d(TAG, "KPOP Agent record has been saved!");

            return true;
        } catch (Exception e){
            e.printStackTrace();
            message = getLocalMessage(e);
            return false;
        }
    }
    private String CreateUniqueID(){
        String lsUniqIDx = "";
        try{
            String lsBranchCd = "MX01";
            String lsCrrYear = new SimpleDateFormat("yy", Locale.getDefault()).format(new Date());
            StringBuilder loBuilder = new StringBuilder(lsBranchCd);
            loBuilder.append(lsCrrYear);

            int lnLocalID = poDao.GetRowsCountForID() + 1;
            String lsPadNumx = String.format("%05d", lnLocalID);
            loBuilder.append(lsPadNumx);
            lsUniqIDx = loBuilder.toString();
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        Log.d(TAG, lsUniqIDx);
        return lsUniqIDx;
    }

}
