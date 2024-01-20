package org.rmj.g3appdriver.GCircle.Account;

import static org.rmj.g3appdriver.dev.Api.ApiResult.SERVER_NO_RESPONSE;
import static org.rmj.g3appdriver.dev.Api.ApiResult.getErrorMessage;
import static org.rmj.g3appdriver.etc.AppConstants.getLocalMessage;
import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import org.json.JSONObject;
import org.rmj.g3appdriver.GCircle.Api.GCircleApi;
import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DClientInfoSalesKit;
import org.rmj.g3appdriver.GCircle.room.Entities.EClientInfoSalesKit;
import org.rmj.g3appdriver.GCircle.room.GGC_GCircleDB;
import org.rmj.g3appdriver.GConnect.Account.ClientSession;
import org.rmj.g3appdriver.GConnect.Api.GConnectApi;
import org.rmj.g3appdriver.dev.Api.HttpHeaders;
import org.rmj.g3appdriver.dev.Api.WebClient;
import org.rmj.g3appdriver.etc.AppConstants;

public class ClientMasterSalesKit {
    private static final String TAG = org.rmj.g3appdriver.GConnect.Account.ClientMaster.class.getSimpleName();
    private final DClientInfoSalesKit poDao;
    private final HttpHeaders poHeaders;
    private final GCircleApi poApi;
    private final GConnectApi poApiConnect;
    private final ClientSession poSession;
    private String message;

    public ClientMasterSalesKit(Application instance) {
        this.poDao = GGC_GCircleDB.getInstance(instance).ciSKDAO();
        this.poHeaders = HttpHeaders.getInstance(instance);
        this.poApi = new GCircleApi(instance);
        this.poApiConnect = new GConnectApi(instance);
        this.poSession = ClientSession.getInstance(instance);
    }
    public String getMessage() {
        return message;
    }
    public void RemoveProfile(){
        poDao.DeleteProfile();
    }
    public void SaveLocalProfile(EClientInfoSalesKit foClient){
        poDao.insert(foClient);
    }
    public boolean ImportClientProfile(String sUserIDxx){
        try {

            JSONObject params = new JSONObject();
            params.put("sUserIDxx",sUserIDxx);
            //REQUEST TO SERVER
            String lsResponse = WebClient.sendRequest(
                    poApiConnect.getImportAccountInfoAPI(),
                    params.toString(),
                    poHeaders.getHeaders());

            if(lsResponse == null){
                message = SERVER_NO_RESPONSE;
                return false;
            }

            Log.d(TAG, lsResponse);
            JSONObject loResponse = new JSONObject(lsResponse);
            String lsResult = loResponse.getString("result");


            if(!lsResult.equalsIgnoreCase("success")){
                JSONObject loError = loResponse.getJSONObject("error");
                message = getErrorMessage(loError);
                return false;
            }

            //SAVE TO LOCAL DEVICE
            EClientInfoSalesKit foClient = new EClientInfoSalesKit();
            foClient.setUserIDxx(loResponse.get("sUserIDxx").toString());
            foClient.setClientID(loResponse.get("sClientID").toString());
            foClient.setEmailAdd(loResponse.get("sEmailAdd").toString());
            foClient.setMobileNo(loResponse.get("sMobileNo").toString());
            foClient.setUserName(loResponse.get("sLastName").toString() + " " + loResponse.get("sSuffixNm").toString() + ", " +
                    loResponse.get("sFrstName").toString() + " " + loResponse.get("sMiddName").toString());
            foClient.setLastName(loResponse.get("sLastName").toString());
            foClient.setFrstName(loResponse.get("sFrstName").toString());
            foClient.setMiddName(loResponse.get("sMiddName").toString());
            foClient.setSuffixNm(loResponse.get("sSuffixNm").toString());
            foClient.setMaidenNm(loResponse.get("sMaidenNm").toString());
            foClient.setGenderCd(loResponse.get("cGenderCd").toString());
            foClient.setCvilStat(loResponse.get("cCvilStat").toString());
            foClient.setBirthDte(loResponse.get("dBirthDte").toString());
            foClient.setBirthPlc(loResponse.get("sBirthPlc").toString());
            foClient.setHouseNo1(loResponse.get("sHouseNo1").toString());
            foClient.setAddress1(loResponse.get("sAddress1").toString());
            foClient.setBrgyIDx1(loResponse.get("sBrgyIDx1").toString());
            foClient.setTownIDx1(loResponse.get("sTownIDx1").toString());
            foClient.setHouseNo2(loResponse.get("sHouseNo2").toString());
            foClient.setAddress2(loResponse.get("sAddress2").toString());
            foClient.setBrgyIDx2(loResponse.get("sBrgyIDx2").toString());
            foClient.setTownIDx2(loResponse.get("sTownIDx2").toString());
            foClient.setVerified(Integer.valueOf(loResponse.get("cVerified").toString()));

            poDao.insert(foClient);

            Log.d(TAG, "Client Profile record save!");

            return true;
        } catch (Exception e){
            e.printStackTrace();
            message = getLocalMessage(e);
            return false;
        }
    }
    public Boolean isDataValid(EClientInfoSalesKit foClient){

        if (foClient.getUserIDxx().isEmpty()) {
            message = "User ID is empty";
            return false;
        }else if (foClient.getFrstName().isEmpty()) {
            message = "Firstname is empty";
            return false;
        }else if (foClient.getLastName().isEmpty()) {
            message = "Lastname is empty";
            return false;
        }else if (foClient.getGenderCd().isEmpty()) {
            message = "Gender is empty";
            return false;
        }else if (foClient.getCvilStat().isEmpty()) {
            message = "Civil Status is empty";
            return false;
        }else if (foClient.getBirthDte() == null) {
            message = "Birthdate is empty";
            return false;
        }else if (foClient.getBirthPlc() == null) {
            message = "Birthplace is empty";
            return false;
        }

        return true;
    }
    public boolean CompleteClientInfo(EClientInfoSalesKit foClient){
        try {
            if (isDataValid(foClient)) {
                JSONObject param = new JSONObject();
                param.put("sUserIDxx", foClient.getUserIDxx());
                param.put("dTransact", new AppConstants().DATE_MODIFIED);
                param.put("sLastName", foClient.getLastName());
                param.put("sFrstName", foClient.getFrstName());
                param.put("sMiddName", foClient.getMiddName());
                param.put("sMaidenNm", foClient.getMaidenNm());
                param.put("sSuffixNm", foClient.getSuffixNm());
                param.put("cGenderCd", foClient.getGenderCd());
                param.put("cCvilStat", foClient.getCvilStat());
                param.put("sCitizenx", foClient.getCitizenx());
                param.put("dBirthDte", foClient.getBirthDte());
                param.put("sBirthPlc", foClient.getBirthPlc());
                param.put("sHouseNo1", foClient.getHouseNo1());
                param.put("sAddress1", foClient.getAddress1());
                param.put("sBrgyIDx1", foClient.getBrgyIDx1());
                param.put("sTownIDx1", foClient.getTownIDx1());
                param.put("sHouseNo2", foClient.getHouseNo2());
                param.put("sAddress2", foClient.getAddress2());
                param.put("sBrgyIDx2", foClient.getBrgyIDx2());
                param.put("sTownIDx2", foClient.getTownIDx2());

                //SAVE TO LOCAL DEVICE
                poDao.insert(foClient);

                //SEND TO SERVER
                String lsResponse = WebClient.sendRequest(
                        poApi.getUrlCompleteAccount(),
                        param.toString(),
                        poHeaders.getHeaders());

                if (lsResponse == null) {
                    message = SERVER_NO_RESPONSE;
                    return false;
                }

                JSONObject loResponse = new JSONObject(lsResponse);
                String lsResult = loResponse.getString("result");
                if (!lsResult.equalsIgnoreCase("success")) {
                    JSONObject loError = loResponse.getJSONObject("error");
                    message = getErrorMessage(loError);
                    return false;
                }
                return true;
            }else {
                return false;
            }
        } catch (Exception e){
            e.printStackTrace();
            message = getLocalMessage(e);
            return false;
        }
    }
    public void RemoveProfileSession(){
        poDao.DeleteProfile();
    }
    public LiveData<EClientInfoSalesKit> GetProfileAccount(){
        return poDao.getClientInfo();
    }
}
